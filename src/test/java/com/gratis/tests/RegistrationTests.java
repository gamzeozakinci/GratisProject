package com.gratis.tests;

import com.gratis.base.BaseTest;
import org.testng.annotations.Test;

/**
 * gratis.com has no separate registration form: "Giriş Yap / Üye Ol" is a single
 * phone-number + OTP flow that creates the account on first use. TC_001/TC_002 exercise
 * that flow through LoginPage rather than a dedicated RegisterPage - see README "Auth Flow".
 */
public class RegistrationTests extends BaseTest {

    @Test(description = "TC_001 - Registering with a brand-new phone number completes the OTP flow and logs the user in")
    public void newPhoneNumberCompletesRegistration() {
        // TODO: implement
    }

    @Test(description = "TC_002 - An already-registered phone number routes through the same OTP login, not a duplicate signup")
    public void existingPhoneNumberRoutesToLogin() {
        // TODO: implement
    }
}
