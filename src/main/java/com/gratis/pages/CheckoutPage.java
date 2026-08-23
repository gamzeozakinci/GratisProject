package com.gratis.pages;

import com.microsoft.playwright.Page;

// TODO: Checkout - shipping step, then payment step
//  Shipping:
//  - choose store pickup vs home delivery
//  - select a store (city/district/store name) - shipping fee should read as free
//  - add a new address (title/city/district/neighborhood/full address/zip), select a saved one
//  - shipping fee text, continue to payment
//  Payment:
//  - choose "pay with card", fill card details, accept the legal agreement, pay
//  - complete 3D Secure with an OTP
//  - order success visible + order id text
//  - payment failure banner visible + its text, confirm still on the payment page
public class CheckoutPage {

    private final Page page;

    public CheckoutPage(Page page) {
        this.page = page;
    }
}
