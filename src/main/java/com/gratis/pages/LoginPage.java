package com.gratis.pages;

import com.gratis.config.ConfigReader;
import com.microsoft.playwright.Locator;
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
        page.locator("input[name='phoneNumber']").pressSequentially("000000000");
    }

    public void registerName() {
        page.getByPlaceholder("Adınızı giriniz").fill("Test");
    }

    public void registerSurName() {
        page.getByPlaceholder("Soyadınızı giriniz").fill("User");
    }

    public void registerEposta() {
        page.getByPlaceholder("E-Posta adresinizi giriniz").fill("test.user@example.com");
    }

    public void registerBirth() {
        page.getByPlaceholder("GG.AA.YYYY").fill("01011990");
    }

    public void gratisKartCheck() {
        page.locator("div.flex.items-start.gap-1",
                new Page.LocatorOptions().setHasText("Gratis Kart sahibi olmak istiyorum")
        ).click();

    }

    public void agreementCheck() {
        page.locator("div.flex.items-start.gap-1",
                new Page.LocatorOptions().setHasText("Üyelik Sözleşmesi")
        ).click();

    }


    public void registerConfirm() {
        page.locator("#submit-button").click();

    }

    //registerli olan son testleri bitir, config içine dosyaları eklenecek
    // 


}
