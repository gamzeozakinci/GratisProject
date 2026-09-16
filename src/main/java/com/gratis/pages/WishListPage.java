package com.gratis.pages;

import com.microsoft.playwright.Page;

import java.util.List;

public class WishListPage {

    private final Page page;

    public WishListPage(Page page) {
        this.page = page;
    }

    public void wishFirstItem() {
        page.locator(".absolute.top-2").first().click();
    }


    public List<String> wishlistProducts() {
        return page.locator("h5").allTextContents();
    }

}
