package com.gratis.pages;

import com.gratis.base.BasePage;
import com.microsoft.playwright.Page;

// TODO: Wishlist page
//  - does the list contain a product matching a given title
//  - remove the first item
//  - is the empty-state message showing
public class WishlistPage extends BasePage {

    public WishlistPage(Page page) {
        super(page);
    }
}
