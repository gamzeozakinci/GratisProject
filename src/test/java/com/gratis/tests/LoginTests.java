package com.gratis.tests;

import com.gratis.base.BaseTest;
import org.testng.annotations.Test;

public class LoginTests extends BaseTest {

    @Test(description = "TC_003 - Login with a valid phone number and valid OTP authenticates the user")
    public void validPhoneAndOtpLogsIn() {
        // TODO: implement
    }

    @Test(description = "TC_004 - An incorrect OTP is rejected with a validation error and does not authenticate")
    public void invalidOtpShowsError() {
        // TODO: implement
    }

    @Test(description = "TC_005 - An invalid phone number format is blocked before an OTP is ever sent " +
            "(no password/forgot-password flow exists on gratis.com to test here - see README \"Auth Flow\")")
    public void invalidPhoneFormatBlocksContinue() {
        // TODO: implement
    }
}
