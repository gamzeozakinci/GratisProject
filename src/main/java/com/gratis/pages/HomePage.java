package com.gratis.pages;

import com.gratis.base.BasePage;
import com.microsoft.playwright.Page;

// TODO:
//  - goToAuth() - open the header's login/register entry point, return a LoginPage
//  - isOnHomepage() - some way to confirm you're actually back on "/"
public class HomePage extends BasePage {

    public final HeaderComponent header;

    public HomePage(Page page) {
        super(page);
        this.header = new HeaderComponent(page);
    }
}
