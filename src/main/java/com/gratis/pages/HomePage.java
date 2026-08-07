package com.gratis.pages;

import com.gratis.base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class HomePage extends BasePage {

    public final HeaderComponent header;

    public HomePage(Page page) {
        super(page);
        this.header = new HeaderComponent(page);
    }

    public RegisterPage goToRegister() {
        header.openLoginOrRegister();
        click(page.getByText("Üye Ol", new Locator.GetByTextOptions().setExact(true)));
        return new RegisterPage(page);
    }

    public LoginPage goToLogin() {
        header.openLoginOrRegister();
        click(page.getByText("Giriş Yap", new Locator.GetByTextOptions().setExact(true)));
        return new LoginPage(page);
    }

    public boolean isOnHomepage() {
        return currentUrl().equals("https://www.gratis.com/") || currentUrl().endsWith("gratis.com/");
    }
}
