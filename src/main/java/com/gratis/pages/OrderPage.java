package com.gratis.pages;

import com.gratis.base.BasePage;
import com.microsoft.playwright.Page;

// TODO: Order history page
//  - is a given order id listed
//  - open an order's detail view (by order id)
//  - detail view: products text, address text, shipping method text
public class OrderPage extends BasePage {

    public OrderPage(Page page) {
        super(page);
    }
}
