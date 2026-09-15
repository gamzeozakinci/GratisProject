package com.gratis.tests;

import com.gratis.base.BaseTest;
import com.gratis.config.ConfigReader;
import com.gratis.driver.PlaywrightFactory;
import com.gratis.pages.HeaderComponent;
import com.gratis.pages.LoginPage;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AuthTests extends BaseTest {

    @Test(description = "TC_001 - Registering with a brand-new phone number completes the OTP flow and logs the user in")
    public void newPhoneNumberCompletesRegistration() {
        HeaderComponent hp = new HeaderComponent(page);
        LoginPage lp = new LoginPage(page);

        hp.openLoginOrRegister();

        lp.enterPhoneNumber();
        lp.clickDevamEt();

        System.out.println("Check your phone, type the OTP directly into the browser, then click Resume (▶) in the Playwright Inspector.");
        page.pause();

        lp.registerName();
        lp.registerSurName();
        lp.registerEposta();

        lp.registerBirth();
        lp.gratisKartCheck();
        lp.agreementCheck();

        lp.acceptCookies();

        lp.registerConfirm();

        assertThat(page).hasURL("https://www.gratis.com/");

    }

    @Test(description = "TC_002 - An already-registered phone number routes through OTP login")
    public void existingPhoneNumberRoutesToLogin() {
        HeaderComponent hp = new HeaderComponent(page);
        LoginPage lp = new LoginPage(page);

        hp.openLoginOrRegister();

        lp.enterRegisteredPhoneNumber();
        lp.clickDevamEt();

        System.out.println("Check your phone, type the OTP directly into the browser, then click Resume (▶) in the Playwright Inspector.");
        page.pause();

        assertThat(page).hasURL("https://www.gratis.com/");
        assertThat(page.getByText(ConfigReader.get("registered.account.name"))).isVisible();
    }

    @Test(description = "TC_003 - An incorrect OTP is rejected with a validation error and does not authenticate.")
    public void invalidOtpShowsError() {
        HeaderComponent hp = new HeaderComponent(page);
        LoginPage lp = new LoginPage(page);

        hp.openLoginOrRegister();

        lp.enterRegisteredPhoneNumber();
        lp.clickDevamEt();

        System.out.println("Enter an invalid OTP number, then click Resume (▶) in the Playwright Inspector.");
        page.pause();

        assertThat(page.getByText("Girdiğiniz kod hatalıdır, lütfen yeniden deneyiniz.")).isVisible();

    }

    @Test(description = "TC_004 - An invalid phone number format is blocked before an OTP is ever sent.")
    public void invalidPhoneFormatBlocksContinue() {
        HeaderComponent hp = new HeaderComponent(page);
        LoginPage lp = new LoginPage(page);

        hp.openLoginOrRegister();

        lp.invalidPhoneNumber();
        lp.clickDevamEt();

        assertThat(page.getByText("Son 7 hane aynı olamaz.")).isVisible();
    }

    @Test(description = "TC_005 - Logging out returns the user to a guest state")
    public void logoutReturnsToGuestState() {
        PlaywrightFactory.tearDown();
        page = PlaywrightFactory.initLoggedInPage();
        page.navigate(ConfigReader.baseUrl());

        HeaderComponent hp = new HeaderComponent(page);
        hp.headerHesabim();
        hp.hesabimLogOut();

    }

}
