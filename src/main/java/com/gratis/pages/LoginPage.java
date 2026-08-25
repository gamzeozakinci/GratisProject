package com.gratis.pages;

import com.gratis.config.ConfigReader;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.Scanner;

/*
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

    public void enterPhoneNumber() {
        page.locator("input[name='phoneNumber']").fill(ConfigReader.get("registered.phone.number"));
    }

    public void clickDevamEt() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("button")).click();
    }

}
