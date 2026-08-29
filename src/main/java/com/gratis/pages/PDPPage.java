package com.gratis.pages;

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
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("button")).first().click();

    }


}
