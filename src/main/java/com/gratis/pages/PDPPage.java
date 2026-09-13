package com.gratis.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class PDPPage {

    private final Page page;

    public PDPPage(Page page) {
        this.page = page;
    }

    public void clickNextImage() {
        page.locator("div.h-9.w-9:has(svg[style*='rotate(0deg)'])").click();
    }

    public void openImageLightbox() {
        page.locator("div.cursor-zoom-in").click();
    }

    public void clickZoomIn() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("zoom-in")).click();
    }

    public void clickZoomOut() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("zoom-out")).click();
    }

    public void closeImageLightbox() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("close-modal")).click();
    }

    public void comments() {
        page.locator("//div[text()=\"Tüm Yorumları Görüntüle\"]").click();
    }

    public void productWishlist() {
        page.locator("div.absolute.right-3.top-3 div.mr-2").click();

    }

    public void addtoFavorites() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("button")).click();

    }

    public void addToCart() {
        // aria-label="button" is overridden site-wide and matches multiple
        // unrelated buttons on this page (addtoFavorites() below queries the
        // exact same thing) - .first() isn't reliably the add-to-cart button
        // (confirmed live: it was clicking a different button at index 0, not
        // the real one at index 1). Plain getByText("SEPETE EKLE") isn't
        // enough either - a decorative <p> elsewhere on the page has the
        // exact same text as a duplicate label, not the actual clickable
        // button (confirmed via a real strict-mode violation showing both).
        // Filtering BUTTON-role elements by that text excludes the <p> and
        // resolves to exactly the one real button.
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("button"))
                .filter(new Locator.FilterOptions().setHasText("SEPETE EKLE"))
                .click();

    }


}
