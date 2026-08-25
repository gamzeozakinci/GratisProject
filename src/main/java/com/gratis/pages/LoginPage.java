package com.gratis.pages;

import com.gratis.config.ConfigReader;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class LoginPage {

    private final Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    public void enterRegisteredPhoneNumber() {
        page.locator("input[name='phoneNumber']").fill(ConfigReader.get("registered.phone.number"));
    }

    public void enterPhoneNumber() {
        page.locator("input[name='phoneNumber']").fill(ConfigReader.get("phone.number"));
    }

    public void clickDevamEt() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("button")).click();
    }

    public void invalidPhoneNumber() {
        page.locator("input[name='phoneNumber']").fill("000000000");
    }

}
