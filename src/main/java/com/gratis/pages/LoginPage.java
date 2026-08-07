package com.gratis.pages;

import com.gratis.base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class LoginPage extends BasePage {

    public LoginPage(Page page) {
        super(page);
    }

    private Locator emailInput() { return page.locator("input[name='email']"); }
    private Locator passwordInput() { return page.locator("input[name='password']"); }
    private Locator rememberMeCheckbox() { return page.locator("input#rememberMe"); }
    private Locator loginButton() { return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Giriş Yap")); }
    private Locator forgotPasswordLink() { return page.getByText("Şifremi Unuttum"); }
    private Locator globalErrorBanner() { return page.locator(".form-error-banner, .alert-error"); }

    public void enterEmail(String email) {
        type(emailInput(), email);
    }

    public void enterPassword(String password) {
        type(passwordInput(), password);
    }

    public void checkRememberMe() {
        click(rememberMeCheckbox());
    }

    public HomePage submitLogin() {
        click(loginButton());
        return new HomePage(page);
    }

    public void submitLoginExpectingFailure() {
        click(loginButton());
    }

    public ForgotPasswordPage goToForgotPassword() {
        click(forgotPasswordLink());
        return new ForgotPasswordPage(page);
    }

    public boolean isGlobalErrorVisible() {
        return isVisible(globalErrorBanner());
    }

    public String globalErrorText() {
        return textOf(globalErrorBanner());
    }

    public boolean isPasswordFieldCleared() {
        return passwordInput().inputValue().isEmpty();
    }

    public boolean isEmailFieldRetained(String expectedEmail) {
        return expectedEmail.equals(emailInput().inputValue());
    }
}
