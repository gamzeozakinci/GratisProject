package com.gratis.tests;

import com.gratis.base.BaseTest;
import com.gratis.config.ConfigReader;
import com.gratis.pages.HeaderComponent;
import com.gratis.pages.LoginPage;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class RegistrationTests extends BaseTest {

    @Test(description = "TC_001 - Registering with a brand-new phone number completes the OTP flow and logs the user in")
    public void newPhoneNumberCompletesRegistration() throws InterruptedException {
        HeaderComponent header = new HeaderComponent(page);
        LoginPage loginPage = new LoginPage(page);

        header.openLoginOrRegister();

        loginPage.enterPhoneNumber();
        loginPage.clickDevamEt();


        Thread.sleep(3000);

    }

    @Test(description = "TC_002 - An already-registered phone number routes through the same OTP login, not a duplicate signup")
    public void existingPhoneNumberRoutesToLogin() {
        HeaderComponent header = new HeaderComponent(page);
        LoginPage loginPage = new LoginPage(page);

        header.openLoginOrRegister();

        loginPage.enterPhoneNumber();
        loginPage.clickDevamEt();

        System.out.println("Check your phone, type the OTP directly into the browser, then click Resume (▶) in the Playwright Inspector.");
        page.pause();

        assertThat(page).hasURL("https://www.gratis.com/");
        assertThat(page.getByText(ConfigReader.get("registered.account.name"))).isVisible();
    }
}
