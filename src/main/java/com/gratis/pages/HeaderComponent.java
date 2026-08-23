package com.gratis.pages;

import com.microsoft.playwright.Page;

// TODO: header/nav is used by almost every test class, so build this one early.
//  - logo click -> back to homepage
//  - isUserLoggedIn(name) - some way to tell a logged-in header apart from a guest one
//  - wishlist / my-orders nav entries
//  - search input + submit + "is the suggestion dropdown showing" check
//  - cart icon click + reading the cart badge count
//  - mobile: hamburger icon click + "is the mobile menu open"
//  - mega menu: hover a top-level category, click a link inside it
public class HeaderComponent {

    private final Page page;

    public HeaderComponent(Page page) {
        this.page = page;
    }

    public void openLoginOrRegister() {
        page.getByText("Üye olun").click();
    }
}
