package com.gratis.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

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

    public void headerMakyaj() {
        page.locator("//a[text()=\"Makyaj\"]").first().hover();

    }

    public void hoverTORuj() {
        page.locator("//span[text()=\"Ruj\"]").click();
    }

    public void mobileAcceptCookies() {
        page.locator("#banner-accept-button").click();
    }

    public void mobileHeader() {

        page.locator("//img[@alt=\"mobile-header-icon\"]").click();
    }


    public void mobileHeaderCiltbakim() {

        page.locator("//span[text()=\"Cilt Bakım\"]").first().click();
    }

    public void mobileHeaderYuzbakim() {

        page.locator("//span[text()=\"Yüz Bakım\"]").click();
    }

    public void mobileHeaderTonikler() {
        page.locator("//span[text()=\"Tonikler\"]").first().click();
    }

    public void headerSacbakim(){
        page.locator("//a[text()=\"Saç Bakım\"]").first().click();
    }

}
