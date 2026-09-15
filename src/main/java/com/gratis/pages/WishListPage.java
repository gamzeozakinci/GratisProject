package com.gratis.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.AriaRole;

public class WishListPage {

    private final Page page;

    public WishListPage(Page page) {
        this.page = page;
    }

    public void wishFirstItem() {
        page.locator(".absolute.top-2.right-2::nth2").click();
    }




}
