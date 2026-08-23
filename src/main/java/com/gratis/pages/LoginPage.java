package com.gratis.pages;

import com.gratis.config.ConfigReader;
import com.microsoft.playwright.Page;

/**
 * gratis.com has no email/password form and no separate registration page: "Üye olun
 * ya da Giriş Yapın" opens a single phone-number + OTP flow that creates the account
 * on first use and logs an existing number straight in - confirmed on the live site.
 * There's no forgot-password flow either, since there's no password.
 *
 * TODO:
 *  - phone number input + submit ("DEVAM ET")
 *  - is the continue button enabled/disabled for an invalid phone format
 *  - OTP input + submit, and a way to submit expecting failure (wrong OTP)
 *  - inline error visibility + text (wrong OTP / invalid phone)
 */
public class LoginPage {

    private final Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    public void enterPhoneNumber(){
        page.locator("input[name='phoneNumber']").fill(ConfigReader.get("registered.phone.number"));
    }
}
