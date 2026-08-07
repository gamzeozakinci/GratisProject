package com.gratis.tests;

import com.gratis.base.BaseTest;
import com.gratis.config.ConfigReader;
import com.gratis.pages.HomePage;
import com.gratis.pages.RegisterPage;
import com.gratis.utils.Constants;
import com.gratis.utils.TestDataGenerator;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RegistrationTests extends BaseTest {

    @Test(description = "TC_001 - User Registration with Valid Details",
            groups = {Constants.GROUP_SMOKE, Constants.GROUP_LOGIN})
    public void registerWithValidDetails() {
        HomePage home = new HomePage(page);
        RegisterPage registerPage = home.goToRegister();

        String email = TestDataGenerator.uniqueEmail();
        String phone = TestDataGenerator.turkishMobile("555");
        String password = TestDataGenerator.validPassword();

        registerPage.fillForm("Ayşe", "Yılmaz", email, phone, password);
        registerPage.acceptAllAgreements();
        registerPage.submit();
        registerPage.completeOtp(ConfigReader.get("mock.otp"));

        Assert.assertTrue(registerPage.isSuccessToastVisible(), "Success toast should appear after registration");
        Assert.assertTrue(registerPage.successToastText().contains("başarıyla"),
                "Toast should confirm the membership was created");
        Assert.assertTrue(home.isOnHomepage(), "User should be redirected to the homepage");
        Assert.assertTrue(home.header.isUserLoggedIn("Ayşe"), "Header should show the user's first name after registration");
    }

    @Test(description = "TC_002 - User Registration with Existing Email Address (Negative)",
            groups = {Constants.GROUP_SMOKE, Constants.GROUP_LOGIN})
    public void registerWithExistingEmailIsBlocked() {
        HomePage home = new HomePage(page);
        RegisterPage registerPage = home.goToRegister();

        registerPage.fillForm("Fatma", "Kaya", ConfigReader.get("existing.user.email"),
                TestDataGenerator.turkishMobile("505"), "ValidPass123!");
        registerPage.acceptAllAgreements();
        registerPage.submit();

        Assert.assertTrue(registerPage.emailErrorText().contains("kayıtlı bir hesap bulunmaktadır"),
                "Error should state the account already exists");
        Assert.assertTrue(registerPage.isPasswordFieldStillFilled(),
                "Password field should remain filled (masked) after the failed submission");
        Assert.assertFalse(home.isOnHomepage(), "User should not be redirected on a blocked registration");
    }
}
