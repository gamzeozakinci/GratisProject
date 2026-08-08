package com.gratis.pages;

import com.gratis.base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class WishlistPage extends BasePage {

    public WishlistPage(Page page) {
        super(page);
    }

    private Locator wishlistItems() {
        return page.locator(".wishlist-item");
    }

    private Locator emptyMessage() {
        return page.getByText("Favori listenizde ürün bulunmamaktadır.");
    }

    private Locator removeIcon(int index) {
        return wishlistItems().nth(index).locator(".remove-icon, .heart-icon");
    }

    public boolean containsProduct(String titleContains) {
        return wishlistItems().filter(new Locator.FilterOptions().setHasText(titleContains)).count() > 0;
    }

    public void removeFirstItem() {
        removeIcon(0).click();
    }

    public boolean isEmptyStateVisible() {
        return emptyMessage().isVisible();
    }
}
