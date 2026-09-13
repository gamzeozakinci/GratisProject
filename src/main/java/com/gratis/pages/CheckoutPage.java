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
        return page.locator("div").filter(new Locator.FilterOptions().setHasText("Yeni Adres Ekle")).nth(1);
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

    // Clicking "ÖDEME ADIMINA GEÇ" opens an in-page "Ödeme" modal backed by
    // a live third-party payment gateway (Masterpass) that is measurably
    // slow and sometimes errors out outright ("Beklenmeyen bir hata
    // oluştu"), so the click is retried - same pattern as
    // HeaderComponent.headerSacbakimForced(). The modal itself is a real
    // <h5>Ödeme</h5> in the main document (confirmed live) - its own
    // "Pay with Card"/Masterpass content is rendered inside an iframe and
    // is NOT visible to a plain page-level locator even though it's on
    // screen, so that must not be used to detect success (confirmed live:
    // document.body.innerText does not contain "Pay with Card" while the
    // modal is fully open). The modal's backdrop (div.bg-white/90) covers
    // the whole page while it's open and blocks clicks on the button
    // underneath, so "Ödeme" is checked first each attempt to avoid
    // re-clicking into the modal's own backdrop once it has opened.
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
