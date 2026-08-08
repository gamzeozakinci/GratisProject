package com.gratis.tests;

import com.gratis.base.BaseTest;
import com.gratis.config.ConfigReader;
import com.gratis.pages.HomePage;
import com.gratis.pages.LoginPage;
import com.gratis.utils.Constants;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTests extends BaseTest {

    @Test(description = "TC_003 - Login with a valid phone number and valid OTP authenticates the user",
            groups = {Constants.GROUP_SMOKE, Constants.GROUP_LOGIN})
    public void validPhoneAndOtpLogsIn() {
        HomePage home = new HomePage(page);
        LoginPage loginPage = home.goToAuth();

        loginPage.enterPhoneNumber(ConfigReader.get("registered.phone.number"));
        loginPage.submitPhoneNumber();
        loginPage.enterOtp(ConfigReader.get("mock.otp"));
        HomePage postLogin = loginPage.submitOtp();

        Assert.assertTrue(postLogin.header.isUserLoggedIn("Hesabım"),
                "Header should display the account entry point after a successful login");
    }

    @Test(description = "TC_004 - An incorrect OTP is rejected with a validation error and does not authenticate",
            groups = {Constants.GROUP_SMOKE, Constants.GROUP_LOGIN})
    public void invalidOtpShowsError() {
        HomePage home = new HomePage(page);
        LoginPage loginPage = home.goToAuth();

        loginPage.enterPhoneNumber(ConfigReader.get("registered.phone.number"));
        loginPage.submitPhoneNumber();
        loginPage.enterOtp("000000");
        loginPage.submitOtpExpectingFailure();

        Assert.assertTrue(loginPage.isErrorVisible(), "An incorrect OTP should surface an inline error");
    }

    @Test(description = "TC_005 - An invalid phone number format is blocked before an OTP is ever sent " +
            "(no password/forgot-password flow exists on gratis.com to test here - see README \"Auth Flow\")",
            groups = {Constants.GROUP_SMOKE, Constants.GROUP_LOGIN})
    public void invalidPhoneFormatBlocksContinue() {
        HomePage home = new HomePage(page);
        LoginPage loginPage = home.goToAuth();

        loginPage.enterPhoneNumber("123");

        Assert.assertFalse(loginPage.isContinueButtonEnabled(),
                "'DEVAM ET' should stay disabled (or reject on click) for a malformed phone number");
    }
}
