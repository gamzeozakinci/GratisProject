package com.gratis.tests;

import com.gratis.base.BaseTest;
import org.testng.annotations.Test;

public class RegistrationTests extends BaseTest {

    @Test(description = "TC_001 - Registering with a brand-new phone number completes the OTP flow and logs the user in")
    public void newPhoneNumberCompletesRegistration() {
        page.getByText("Üye olun").click();


    }

    @Test(description = "TC_002 - An already-registered phone number routes through the same OTP login, not a duplicate signup")
    public void existingPhoneNumberRoutesToLogin() {
        // TODO: implement
    }
}
