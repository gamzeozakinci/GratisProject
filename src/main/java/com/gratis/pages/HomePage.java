package com.gratis.pages;

import com.gratis.base.BasePage;
import com.microsoft.playwright.Page;

public class HomePage extends BasePage {

    public final HeaderComponent header;

    public HomePage(Page page) {
        super(page);
        this.header = new HeaderComponent(page);
    }

    /** Opens the single phone+OTP flow that handles both login and registration. */
    public LoginPage goToAuth() {
        header.openLoginOrRegister();
        return new LoginPage(page);
    }

    public boolean isOnHomepage() {
        return currentUrl().equals("https://www.gratis.com/") || currentUrl().endsWith("gratis.com/");
    }
}
