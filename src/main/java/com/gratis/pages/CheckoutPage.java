package com.gratis.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.AriaRole;

public class CheckoutPage {

    private final Page page;

    public CheckoutPage(Page page) {
        this.page = page;
    }

    public Locator addAddressModal() {
        return page.locator("div").filter(new Locator.FilterOptions().
                setHasText("Yeni Adres Ekle")).nth(1);
    }

    public void openBillingAddressModal() {
        page.getByText("YENİ ADRES EKLE").click();
    }

    public void chooseIl() {
        page.locator("#checkout-city-select button").click();
        page.getByText("ANTALYA", new Page.GetByTextOptions().setExact(true)).click();
    }

    public void chooseIlce() {
        page.locator("#checkout-district-select button").click();
        page.getByText("KONYAALTI", new Page.GetByTextOptions().setExact(true)).click();
    }

    public void chooseMagaza() {
        page.locator("div.mt-3.flex.flex-col.gap-2 input[type=\"radio\"]").first().click();
    }

    public void accAgreement() {
        page.locator("#checkout-consent div.cursor-pointer").click();
    }

    public void continueToPayment() {
        Locator button = page.getByText("ÖDEME ADIMINA GEÇ");
        Locator paymentModal = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Ödeme"));
        for (int attempt = 1; attempt <= 3; attempt++) {
            if (paymentModal.isVisible()) {
                return;
            }
            button.click();
            try {
                paymentModal.waitFor(new Locator.WaitForOptions().setTimeout(15000));
                return;
            } catch (TimeoutError e) {
                if (attempt == 3) {
                    throw e;
                }
            }
        }
    }

    public void chooseOnline() {
        page.locator("input[type='radio'][value='ONLINE']").click();
    }

    public void addAddress() {
        page.getByText("ADRES EKLEYİN").click();
    }

    public void addName() {
        page.getByPlaceholder("Adınız").first().fill("Test");
    }

    public void addSurname() {
        page.getByPlaceholder("Soyadınız").fill("User");
    }

    public void addressName() {
        page.getByPlaceholder("Adres İsmi *").fill("Ev adresi");
    }

    public void adresIL() {
        addAddressModal().locator("//label[text()='İl *']/following::button[1]").click();
        addAddressModal().getByText("ANTALYA", new Locator.GetByTextOptions().setExact(true)).click();
    }

    public void adresILCE() {
        addAddressModal().locator("//label[text()='İlçe *']/following::button[1]").click();
        addAddressModal().getByText("KONYAALTI", new Locator.GetByTextOptions().setExact(true)).click();
    }

    public void adresStreet() {
        addAddressModal().locator("//label[text()='Mahalle *']/following::button[1]").click();
        addAddressModal().getByText("SİTELER", new Locator.GetByTextOptions().setExact(true)).click();
    }

    public void addressDetail() {
        page.getByPlaceholder("Açık adresinizi ekleyiniz").fill("Test Sokak No:1 Daire:1");
    }

    public void saveAddress() {
        page.getByText("ADRESİMİ KAYDET").click();
    }

}
