package com.gratis.tests;

import com.gratis.base.BaseTest;
import com.gratis.config.ConfigReader;
import com.gratis.pages.HomePage;
import com.gratis.pages.LoginPage;
import com.gratis.utils.Constants;
import com.gratis.utils.TestDataGenerator;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * gratis.com has no separate registration form: "Giriş Yap / Üye Ol" is a single
 * phone-number + OTP flow that creates the account on first use. TC_001/TC_002 exercise
 * that flow through LoginPage rather than a dedicated RegisterPage - see README "Auth Flow".
 */
public class RegistrationTests extends BaseTest {

    @Test(description = "TC_001 - Registering with a brand-new phone number completes the OTP flow and logs the user in",
            groups = {Constants.GROUP_SMOKE, Constants.GROUP_LOGIN})
    public void newPhoneNumberCompletesRegistration() {
        HomePage home = new HomePage(page);
        LoginPage loginPage = home.goToAuth();

        loginPage.enterPhoneNumber(TestDataGenerator.uniquePhoneNumber());
        loginPage.submitPhoneNumber();
        loginPage.enterOtp(ConfigReader.get("mock.otp"));
        HomePage postAuth = loginPage.submitOtp();

        Assert.assertFalse(loginPage.isErrorVisible(),
                "No error should be shown after submitting a valid OTP for a brand-new number");
        Assert.assertTrue(postAuth.header.isUserLoggedIn("Hesabım"),
                "Header should show the account entry point after a successful sign-up");
    }

    @Test(description = "TC_002 - An already-registered phone number routes through the same OTP login, not a duplicate signup",
            groups = {Constants.GROUP_SMOKE, Constants.GROUP_LOGIN})
    public void existingPhoneNumberRoutesToLogin() {
        HomePage home = new HomePage(page);
        LoginPage loginPage = home.goToAuth();

        loginPage.enterPhoneNumber(ConfigReader.get("registered.phone.number"));
        loginPage.submitPhoneNumber();

        Assert.assertFalse(loginPage.isErrorVisible(),
                "Gratis merges login/registration behind one phone+OTP form, so an existing " +
                        "number should also reach the OTP step, not a 'duplicate account' error");
    }
}
