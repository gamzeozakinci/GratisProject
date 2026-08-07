package com.gratis.pages;

import com.gratis.base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class ForgotPasswordPage extends BasePage {

    public ForgotPasswordPage(Page page) {
        super(page);
    }

    private Locator emailInput() { return page.locator("input[name='email']"); }
    private Locator sendButton() { return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Gönder")); }
    private Locator successMessage() { return page.locator(".success-message, .toast"); }

    public void requestReset(String email) {
        type(emailInput(), email);
        click(sendButton());
    }

    public boolean isOnForgotPasswordPage() {
        return currentUrl().contains("/forgot-password");
    }

    public boolean isSuccessMessageVisible() {
        return isVisible(successMessage());
    }

    public String successMessageText() {
        return textOf(successMessage());
    }
}
