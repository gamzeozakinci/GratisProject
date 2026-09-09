package com.gratis.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class CartPage {

    private final Page page;

    public CartPage(Page page) {
        this.page = page;
    }

    public void cartButton() {
        page.locator("a[href='/cart']").first().click();

    }

    public void logoCart() {
        page.locator("//a[@href=\"/\"]").first().click();

    }

    public void clearCart() {
        page.getByText("TÜMÜNÜ SİL").click();

    }

    public void tamam() {
        page.getByText("TAMAM").click();

    }

    public void increaseQuantity() {
        page.locator(".flex.flex-col.gap-2.justify-center .cursor-pointer").first().click();
        // 1 3 5 gibi tek sayılar + 2 4 6 gibi olan cop veya - anlamına geliyor

    }

    public void decreaseQuantity() {
        page.locator(".flex.flex-col.gap-2.justify-center .cursor-pointer").nth(2).click();

    }

    public void deleteFromCart() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("button")).click();

    }

    public void openPromocode() {
        page.locator(".transform.transition-transform").first().click();

    }

    public void enterPromoCOde() {
        page.getByPlaceholder("Kodunuzu giriniz").fill("Promocode!!");

    }

    public void submitPromoCode() {
        page.getByText("UYGULA", new Page.GetByTextOptions().setExact(true)).click();
    }

    public void continueToDelivery() {
        page.getByText("TESLİMAT ADIMINA GEÇ").first().click();
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
        page.getByText("ÖDEME ADIMINA GEÇ").click();
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
        page.locator("//label[text()='İl *']/following::button[1]").click();
        page.getByText("ANTALYA", new Page.GetByTextOptions().setExact(true)).click();

    }

    public void adresILCE() {
        page.locator("//label[text()='İlçe *']/following::button[1]").click();
        page.getByText("KONYAALTI", new Page.GetByTextOptions().setExact(true)).click();
    }

    public void adresStreet() {
        page.locator("//label[text()='Mahalle *']/following::button[1]").click();
        page.getByText("SİTELER", new Page.GetByTextOptions().setExact(true)).click();
    }

    public void addressDetail() {
        page.getByPlaceholder("Açık adresinizi ekleyiniz").fill("Test Sokak No:1 Daire:1");
    }

    public void saveAddress() {
        page.getByText("ADRESİMİ KAYDET").click();
    }
}
