package com.gratis.pages;

import com.gratis.config.ConfigReader;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CartPage {

    private final Page page;

    public CartPage(Page page) {
        this.page = page;
    }

    private Locator productRow(String productName) {
        return page.locator("div.relative.flex.flex-row.border-b.border-gray-200.px-3.py-4.w-full.gap-3")
                .filter(new Locator.FilterOptions().setHasText(productName));
    }

    public String quantityOf(String productName) {
        return productRow(productName).locator(".font-semibold.text-black").innerText();
    }

    public Locator quantityLocator(String productName) {
        return productRow(productName).locator(".font-semibold.text-black");
    }

    public Locator cartItem(String productName) {
        return productRow(productName);
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

    private Locator quantityControls(String productName) {
        return productRow(productName).locator("div.flex.flex-col.gap-2.justify-center.items-center.bg-primary-50");

    }

    public void increaseQuantity(String productName) {
        quantityControls(productName).locator(".cursor-pointer").first().click();

    }

    public void decreaseQuantity(String productName) {
        quantityControls(productName).locator(".cursor-pointer").last().click();

    }

    public void deleteAllFromCart() {
        page.getByText("TÜMÜNÜ SİL").click();

    }

    public void openPromocode() {
        page.locator(".transform.transition-transform").first().click();

    }

    public void enterPromoCOde() {
        page.getByPlaceholder("Kodunuzu giriniz").fill(ConfigReader.get("invalid.promo.code"));

    }

    public void submitPromoCode() {
        page.getByText("UYGULA", new Page.GetByTextOptions().setExact(true)).first().click();

    }

    public void continueToDelivery() {
        page.getByText("TESLİMAT ADIMINA GEÇ").first().click();

    }

    public void removeProduct() {
        page.getByText("Sil", new Page.GetByTextOptions().setExact(true)).click();

    }

}
