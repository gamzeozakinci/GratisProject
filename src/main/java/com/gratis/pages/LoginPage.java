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
        page.locator("input[name='phoneNumber']").pressSequentially(ConfigReader.get("invalid.phone.number"));
    }

    public void registerName() {
        page.getByPlaceholder("Adınızı giriniz").first().fill(ConfigReader.get("register.first.name"));
    }

    public void registerSurName() {
        page.getByPlaceholder("Soyadınızı giriniz").fill(ConfigReader.get("register.last.name"));
    }

    public void registerEposta() {
        page.getByPlaceholder("E-Posta adresinizi giriniz").fill(ConfigReader.get("register.email"));
    }

    public void registerBirth() {
        page.getByPlaceholder("GG.AA.YYYY").fill(ConfigReader.get("register.birth.date"));
    }

    public void gratisKartCheck() {
        page.locator("div.flex.items-start.gap-1",
                        new Page.LocatorOptions().setHasText("Gratis Kart sahibi olmak istiyorum"))
                .locator("div.cursor-pointer")
                .click();
    }

    public void agreementCheck() {
        page.locator("div.flex.items-start.gap-1",
                        new Page.LocatorOptions().setHasText("Üyelik Sözleşmesi"))
                .locator("div.cursor-pointer")
                .click();
    }

    public void acceptCookies() {
        page.locator("#banner-accept-button").click();

    }

    public void registerConfirm() {
        page.locator("#submit-button").click();

    }


}
