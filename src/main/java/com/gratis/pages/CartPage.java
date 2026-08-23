package com.gratis.pages;

import com.microsoft.playwright.Page;

// TODO: Cart page
//  - item count, reading a row's quantity
//  - increase quantity (n times), set quantity directly by typing a value
//  - stock-limit toast visible, delete-confirm popup visible
//  - remove an item (with the confirm-delete step if one appears)
//  - subtotal / shipping / grand total text
//  - apply a promo code, and check success/error state + the discount line
//  - proceed to checkout -> CheckoutPage
public class CartPage {

    private final Page page;

    public CartPage(Page page) {
        this.page = page;
    }
}
