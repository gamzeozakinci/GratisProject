package com.gratis.pages;

import com.gratis.base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class PDPPage extends BasePage {

    public PDPPage(Page page) {
        super(page);
    }

    private Locator productTitle() {
        return page.locator("h1.product-title");
    }

    private Locator productBrand() {
        return page.locator(".product-brand");
    }

    private Locator standardPrice() {
        return page.locator(".price-standard");
    }

    private Locator cardPrice() {
        return page.locator(".price-gratis-card");
    }

    private Locator mainImage() {
        return page.locator(".pdp-main-image img");
    }

    private Locator thumbnails() {
        return page.locator(".pdp-thumbnail");
    }

    private Locator quantityIncrease() {
        return page.locator("button.qty-increase");
    }

    private Locator addToCartButton() {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sepete Ekle"));
    }

    private Locator addedToCartLabel() {
        return page.getByText("Sepete Eklendi");
    }

    private Locator outOfStockLabel() {
        return page.getByText("Stokta Yok");
    }

    private Locator wishlistHeartButton() {
        return page.locator("button.wishlist-toggle, [aria-label*='Favori']");
    }

    private Locator wishlistToast() {
        return page.getByText("Ürün favorilerinize eklenmiştir.");
    }

    private Locator loginPromptModal() {
        return page.locator(".auth-modal, .login-modal");
    }

    private Locator loginPromptMessage() {
        return page.getByText("Favorilerinize ürün eklemek için giriş yapmalısınız.");
    }

    private Locator accordionTab(String label) {
        return page.getByText(label, new Page.GetByTextOptions().setExact(true));
    }

    public String title() {
        return productTitle().innerText().trim();
    }

    public boolean isAddToCartEnabled() {
        return addToCartButton().isEnabled();
    }

    public boolean isOutOfStockLabelVisible() {
        return outOfStockLabel().isVisible();
    }

    public void selectQuantity(int qty) {
        for (int i = 1; i < qty; i++) {
            quantityIncrease().click();
        }
    }

    public void addToCart() {
        addToCartButton().click();
        addedToCartLabel().waitFor();
    }

    public void clickThumbnail(int index) {
        thumbnails().nth(index).click();
    }

    public boolean isThumbnailHighlighted(int index) {
        String cls = thumbnails().nth(index).getAttribute("class");
        return cls != null && (cls.contains("active") || cls.contains("selected"));
    }

    public void toggleWishlist() {
        wishlistHeartButton().click();
    }

    public boolean isWishlistHeartFilled() {
        String cls = wishlistHeartButton().getAttribute("class");
        return cls != null && (cls.contains("filled") || cls.contains("active"));
    }

    public boolean isWishlistToastVisible() {
        return wishlistToast().isVisible();
    }

    public boolean isLoginPromptVisible() {
        return loginPromptModal().isVisible() || currentUrl().contains("/login");
    }

    public String loginPromptMessageText() {
        return loginPromptMessage().innerText().trim();
    }

    public void openAccordionTab(String label) {
        accordionTab(label).click();
    }
}
