package com.gratis.pages;

import com.gratis.base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class CartPage extends BasePage {

    public CartPage(Page page) {
        super(page);
    }

    private Locator cartRows() {
        return page.locator(".cart-item-row");
    }

    private Locator quantityInput(int rowIndex) {
        return cartRows().nth(rowIndex).locator("input.qty-input");
    }

    private Locator quantityIncrease(int rowIndex) {
        return cartRows().nth(rowIndex).locator("button.qty-increase");
    }

    private Locator deleteIcon(int rowIndex) {
        return cartRows().nth(rowIndex).locator(".delete-icon");
    }

    private Locator confirmDeleteButton() {
        return page.getByText("Sil");
    }

    private Locator stockLimitToast() {
        return page.getByText("Stok limiti aşıldı");
    }

    private Locator deleteConfirmPopup() {
        return page.locator(".confirm-delete-modal");
    }

    private Locator subtotal() {
        return page.locator(".summary-subtotal");
    }

    private Locator shippingFee() {
        return page.locator(".summary-shipping");
    }

    private Locator grandTotal() {
        return page.locator(".summary-grand-total");
    }

    private Locator promoCodeInput() {
        return page.locator("input[name='promoCode']");
    }

    private Locator promoApplyButton() {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Uygula"));
    }

    private Locator promoSuccessIndicator() {
        return page.getByText("Kupon başarıyla uygulandı.");
    }

    private Locator promoErrorMessage() {
        return page.getByText("Girdiğiniz kod geçersizdir veya süresi dolmuştur.");
    }

    private Locator couponDiscountLine() {
        return page.locator(".summary-coupon-discount");
    }

    public int itemCount() {
        return cartRows().count();
    }

    public int quantityOf(int rowIndex) {
        return Integer.parseInt(quantityInput(rowIndex).inputValue());
    }

    public void increaseQuantity(int rowIndex, int times) {
        for (int i = 0; i < times; i++) {
            quantityIncrease(rowIndex).click();
        }
    }

    public void setQuantityDirectly(int rowIndex, String value) {
        Locator input = quantityInput(rowIndex);
        input.fill(value);
        input.press("Enter");
    }

    public boolean isStockLimitToastVisible() {
        return stockLimitToast().isVisible();
    }

    public boolean isDeleteConfirmPopupVisible() {
        return deleteConfirmPopup().isVisible();
    }

    public void removeItem(int rowIndex) {
        deleteIcon(rowIndex).click();
        if (confirmDeleteButton().isVisible()) {
            confirmDeleteButton().click();
        }
    }

    public String subtotalText() {
        return subtotal().innerText().trim();
    }

    public String shippingFeeText() {
        return shippingFee().innerText().trim();
    }

    public String grandTotalText() {
        return grandTotal().innerText().trim();
    }

    public void applyPromoCode(String code) {
        promoCodeInput().fill(code);
        promoApplyButton().click();
    }

    public boolean isPromoSuccessVisible() {
        return promoSuccessIndicator().isVisible();
    }

    public boolean isPromoErrorVisible() {
        return promoErrorMessage().isVisible();
    }

    public String couponDiscountText() {
        return couponDiscountLine().innerText().trim();
    }

    public CheckoutPage proceedToCheckout() {
        page.getByText("Ödemeye Geç").click();
        return new CheckoutPage(page);
    }
}
