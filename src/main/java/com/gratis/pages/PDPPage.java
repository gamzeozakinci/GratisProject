package com.gratis.pages;

import com.gratis.base.BasePage;
import com.microsoft.playwright.Page;

// TODO: Product Detail Page
//  - title text
//  - is "Add to Cart" enabled - and is the out-of-stock label showing
//  - quantity selector + add to cart (waits for an "added" confirmation)
//  - image thumbnails: click one, tell which one is currently highlighted
//  - wishlist heart: toggle it, tell if it's filled, tell if a success toast showed
//  - guest wishlist click -> login prompt visible + its message text
//  - accordion tabs (description / usage / ingredients) - open one by label
public class PDPPage extends BasePage {

    public PDPPage(Page page) {
        super(page);
    }
}
