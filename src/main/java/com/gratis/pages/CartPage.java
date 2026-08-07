package com.gratis.pages;

import com.gratis.base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class CartPage extends BasePage {

    public CartPage(Page page) {
        super(page);
    }

    private Locator cartRows() { return page.locator(".cart-item-row"); }
    private Locator quantityInput(int rowIndex) { return cartRows().nth(rowIndex).locator("input.qty-input"); }
    private Locator quantityIncrease(int rowIndex) { return cartRows().nth(rowIndex).locator("button.qty-increase"); }
    private Locator deleteIcon(int rowIndex) { return cartRows().nth(rowIndex).locator(".delete-icon"); }
    private Locator confirmDeleteButton() { return page.getByText("Sil"); }
    private Locator stockLimitToast() { return page.getByText("Stok limiti aşıldı"); }
    private Locator deleteConfirmPopup() { return page.locator(".confirm-delete-modal"); }

    private Locator subtotal() { return page.locator(".summary-subtotal"); }
    private Locator shippingFee() { return page.locator(".summary-shipping"); }
    private Locator grandTotal() { return page.locator(".summary-grand-total"); }

    private Locator promoCodeInput() { return page.locator("input[name='promoCode']"); }
    private Locator promoApplyButton() { return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Uygula")); }
    private Locator promoSuccessIndicator() { return page.getByText("Kupon başarıyla uygulandı."); }
    private Locator promoErrorMessage() { return page.getByText("Girdiğiniz kod geçersizdir veya süresi dolmuştur."); }
    private Locator couponDiscountLine() { return page.locator(".summary-coupon-discount"); }

    public int itemCount() {
        return cartRows().count();
    }

    public int quantityOf(int rowIndex) {
        return Integer.parseInt(quantityInput(rowIndex).inputValue());
    }

    public void increaseQuantity(int rowIndex, int times) {
        for (int i = 0; i < times; i++) {
            click(quantityIncrease(rowIndex));
        }
    }

    public void setQuantityDirectly(int rowIndex, String value) {
        Locator input = quantityInput(rowIndex);
        input.fill(value);
        input.press("Enter");
    }

    public boolean isStockLimitToastVisible() {
        return isVisible(stockLimitToast());
    }

    public boolean isDeleteConfirmPopupVisible() {
        return isVisible(deleteConfirmPopup());
    }

    public void removeItem(int rowIndex) {
        click(deleteIcon(rowIndex));
        if (isVisible(confirmDeleteButton())) {
            click(confirmDeleteButton());
        }
    }

    public String subtotalText() { return textOf(subtotal()); }
    public String shippingFeeText() { return textOf(shippingFee()); }
    public String grandTotalText() { return textOf(grandTotal()); }

    public void applyPromoCode(String code) {
        type(promoCodeInput(), code);
        click(promoApplyButton());
    }

    public boolean isPromoSuccessVisible() {
        return isVisible(promoSuccessIndicator());
    }

    public boolean isPromoErrorVisible() {
        return isVisible(promoErrorMessage());
    }

    public String couponDiscountText() {
        return textOf(couponDiscountLine());
    }

    public CheckoutPage proceedToCheckout() {
        click(page.getByText("Ödemeye Geç"));
        return new CheckoutPage(page);
    }
}
