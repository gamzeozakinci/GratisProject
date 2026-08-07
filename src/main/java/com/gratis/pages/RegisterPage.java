package com.gratis.pages;

import com.gratis.base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class RegisterPage extends BasePage {

    public RegisterPage(Page page) {
        super(page);
    }

    private Locator firstName() { return page.locator("input[name='firstName']"); }
    private Locator lastName() { return page.locator("input[name='lastName']"); }
    private Locator email() { return page.locator("input[name='email']"); }
    private Locator phone() { return page.locator("input[name='phone']"); }
    private Locator password() { return page.locator("input[name='password']"); }
    private Locator membershipTerms() { return page.locator("input#membershipTerms, input[name='membershipTerms']"); }
    private Locator kvkkTerms() { return page.locator("input#kvkkTerms, input[name='kvkkTerms']"); }
    private Locator registerButton() { return page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON,
            new Page.GetByRoleOptions().setName("Üye Ol")); }
    private Locator otpInput() { return page.locator("input[name='otp'], input.otp-input"); }
    private Locator otpConfirmButton() { return page.getByText("Onayla"); }
    private Locator emailFieldError() { return page.locator(".field-error, .error-message").last(); }
    private Locator successToast() { return page.locator(".toast, .notification-success"); }

    public void fillForm(String first, String last, String emailVal, String phoneVal, String passwordVal) {
        type(firstName(), first);
        type(lastName(), last);
        type(email(), emailVal);
        type(phone(), phoneVal);
        type(password(), passwordVal);
    }

    public void acceptAllAgreements() {
        click(membershipTerms());
        click(kvkkTerms());
    }

    public void submit() {
        click(registerButton());
    }

    public void completeOtp(String otpCode) {
        otpInput().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        type(otpInput(), otpCode);
        click(otpConfirmButton());
    }

    public boolean isSuccessToastVisible() {
        return isVisible(successToast());
    }

    public String successToastText() {
        return textOf(successToast());
    }

    public String emailErrorText() {
        return textOf(emailFieldError());
    }

    public boolean isPasswordFieldStillFilled() {
        String value = password().inputValue();
        return value != null && !value.isEmpty();
    }
}
