package com.gratis.tests;

import com.gratis.base.BaseTest;
import com.gratis.config.ConfigReader;
import com.gratis.pages.ForgotPasswordPage;
import com.gratis.pages.HomePage;
import com.gratis.pages.LoginPage;
import com.gratis.utils.Constants;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTests extends BaseTest {

    @Test(description = "TC_003 - User Login with Valid Credentials",
            groups = {Constants.GROUP_SMOKE, Constants.GROUP_LOGIN})
    public void loginWithValidCredentials() {
        HomePage home = new HomePage(page);
        LoginPage loginPage = home.goToLogin();

        loginPage.enterEmail(ConfigReader.get("active.user.email"));
        loginPage.enterPassword(ConfigReader.get("active.user.password"));
        loginPage.checkRememberMe();
        HomePage postLogin = loginPage.submitLogin();

        Assert.assertTrue(postLogin.header.isUserLoggedIn("Hesabım"),
                "Header should display the account dropdown after successful login");
    }

    @Test(description = "TC_004 - User Login with Invalid Password",
            groups = {Constants.GROUP_SMOKE, Constants.GROUP_LOGIN})
    public void loginWithInvalidPasswordFails() {
        HomePage home = new HomePage(page);
        LoginPage loginPage = home.goToLogin();

        loginPage.enterEmail(ConfigReader.get("active.user.email"));
        loginPage.enterPassword("WrongPassword999");
        loginPage.submitLoginExpectingFailure();

        Assert.assertTrue(loginPage.isGlobalErrorVisible(), "A global error banner should appear");
        Assert.assertTrue(loginPage.globalErrorText().contains("hatalı"),
                "Error should indicate incorrect email or password");
        Assert.assertTrue(loginPage.isPasswordFieldCleared(), "Password field should be cleared after failure");
        Assert.assertTrue(loginPage.isEmailFieldRetained(ConfigReader.get("active.user.email")),
                "Email field should retain the entered value");
    }

    @Test(description = "TC_005 - Forgot Password Functionality with Registered Email",
            groups = {Constants.GROUP_SMOKE, Constants.GROUP_LOGIN})
    public void forgotPasswordWithRegisteredEmail() {
        HomePage home = new HomePage(page);
        LoginPage loginPage = home.goToLogin();

        ForgotPasswordPage forgotPasswordPage = loginPage.goToForgotPassword();
        Assert.assertTrue(forgotPasswordPage.isOnForgotPasswordPage(), "Should navigate to /forgot-password");

        forgotPasswordPage.requestReset(ConfigReader.get("forgot.password.email"));

        Assert.assertTrue(forgotPasswordPage.isSuccessMessageVisible(), "Success message should be shown");
        Assert.assertTrue(forgotPasswordPage.successMessageText().contains("gönderilmiştir"),
                "Message should confirm the reset link was sent");
        // Note: verifying the actual email delivery requires a mailbox API (e.g. Mailinator REST API)
        // which is out of scope for the UI layer - see README "Out of Scope / Future Work".
    }
}
