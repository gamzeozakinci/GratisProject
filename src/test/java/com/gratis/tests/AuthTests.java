package com.gratis.tests;

import com.gratis.base.BaseTest;
import com.gratis.config.ConfigReader;
import com.gratis.pages.HeaderComponent;
import com.gratis.pages.LoginPage;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * gratis.com has no separate registration form or login form - "Giriş Yap / Üye Ol"
 * is a single phone-number + OTP flow that creates the account on first use and logs
 * an existing number straight in. Login and registration cases live in one class here
 * for the same reason: the site itself doesn't separate them, so the tests shouldn't
 * pretend to either. See README "Auth Flow".
 */
public class AuthTests extends BaseTest {

    @Test(description = "TC_001 - Registering with a brand-new phone number completes the OTP flow and logs the user in")
    public void newPhoneNumberCompletesRegistration() {
        HeaderComponent header = new HeaderComponent(page);
        LoginPage loginPage = new LoginPage(page);

        header.openLoginOrRegister();

        loginPage.enterPhoneNumber();
        loginPage.clickDevamEt();

        System.out.println("Check your phone, type the OTP directly into the browser, then click Resume (▶) in the Playwright Inspector.");
        page.pause();
    }

    @Test(description = "TC_002 - An already-registered phone number routes through the same OTP login, not a duplicate signup")
    public void existingPhoneNumberRoutesToLogin() {
        HeaderComponent header = new HeaderComponent(page);
        LoginPage loginPage = new LoginPage(page);

        header.openLoginOrRegister();

        loginPage.enterRegisteredPhoneNumber();
        loginPage.clickDevamEt();

        System.out.println("Check your phone, type the OTP directly into the browser, then click Resume (▶) in the Playwright Inspector.");
        page.pause();

        assertThat(page).hasURL("https://www.gratis.com/");
        assertThat(page.getByText(ConfigReader.get("registered.account.name"))).isVisible();
    }

    @Test(description = "TC_003 - An incorrect OTP is rejected with a validation error and does not authenticate.")
    public void invalidOtpShowsError() {
        HeaderComponent header = new HeaderComponent(page);
        LoginPage loginPage = new LoginPage(page);

        header.openLoginOrRegister();

        loginPage.enterRegisteredPhoneNumber();
        loginPage.clickDevamEt();

        //sonra bak buna
        // page durdurma


    }

    @Test(description = "TC_004 - An invalid phone number format is blocked before an OTP is ever sent.")
    public void invalidPhoneFormatBlocksContinue() {
        HeaderComponent header = new HeaderComponent(page);
        LoginPage loginPage = new LoginPage(page);

        header.openLoginOrRegister();

        loginPage.invalidPhoneNumber();
        loginPage.clickDevamEt();

        assertThat(page.getByText(ConfigReader.get("Son 7 hane aynı olamaz."))).isVisible();
    }
}
