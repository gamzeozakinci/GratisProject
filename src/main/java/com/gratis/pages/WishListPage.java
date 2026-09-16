package com.gratis.pages;

import com.microsoft.playwright.Page;


public class WishListPage {

    private final Page page;

    public WishListPage(Page page) {
        this.page = page;
    }

    public void wishFirstItem() {
        page.locator(".absolute.top-2.right-2::nth2").click();
    }

}
