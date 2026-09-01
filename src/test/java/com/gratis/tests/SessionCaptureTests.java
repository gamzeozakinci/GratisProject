package com.gratis.tests;

import com.gratis.base.BaseTest;
import com.gratis.driver.PlaywrightFactory;
import com.gratis.pages.HeaderComponent;
import com.gratis.pages.LoginPage;
import org.testng.annotations.Test;

/**
 * Not part of the normal suite - run this manually whenever
 * src/test/resources/auth-state.json is missing, or tests using
 * PlaywrightFactory.initLoggedInPage() start getting redirected to /login
 * (the saved session has expired). Needs a real OTP typed in by hand, same
 * as AuthTests.
 */
public class SessionCaptureTests extends BaseTest {

    @Test(description = "One-time: log in manually and save the session for reuse")
    public void captureLoggedInSession() {
        HeaderComponent header = new HeaderComponent(page);
        LoginPage loginPage = new LoginPage(page);

        header.openLoginOrRegister();
        loginPage.enterRegisteredPhoneNumber();
        loginPage.clickDevamEt();

        System.out.println("Check your phone, type the OTP directly into the browser, then click Resume (▶) in the Playwright Inspector.");
        page.pause();

        PlaywrightFactory.saveLoginState();
    }
}
