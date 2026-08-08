package com.gratis.pages;

import com.gratis.base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

/**
 * gratis.com has no email/password form and no separate registration page. "Üye olun
 * ya da Giriş Yapın" opens a single phone-number + OTP flow ("Giriş Yap / Üye Ol -
 * Telefon numaranızla giriş yapabilir ya da yeni bir hesap oluşturabilirsiniz") that
 * creates the account on first use and logs an existing number straight in - confirmed
 * by opening the flow on the live site. RegisterPage/ForgotPasswordPage were removed
 * for the same reason: there's no password to forget. See README "Auth Flow".
 */
public class LoginPage extends BasePage {

    public LoginPage(Page page) {
        super(page);
    }

    private Locator phoneInput() {
        return page.getByPlaceholder("0(5 )"); // TODO verify exact placeholder/name attribute against live DOM
    }

    private Locator continueButton() {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("DEVAM ET"));
    }

    private Locator otpInput() {
        return page.locator("input[inputmode='numeric'], input[name*='otp']").first();
    }

    private Locator otpSubmitButton() {
        return page.locator("button[type='submit']");
    }

    private Locator inlineError() {
        return page.locator("[class*='error'], [role='alert']").first();
    }

    public void enterPhoneNumber(String phone) {
        phoneInput().fill(phone);
    }

    public void submitPhoneNumber() {
        continueButton().click();
    }

    public boolean isContinueButtonEnabled() {
        return continueButton().isEnabled();
    }

    public void enterOtp(String otp) {
        otpInput().fill(otp);
    }

    public HomePage submitOtp() {
        otpSubmitButton().click();
        return new HomePage(page);
    }

    public void submitOtpExpectingFailure() {
        otpSubmitButton().click();
    }

    /** Convenience for tests/fixtures that only care about ending up logged in. */
    public HomePage loginOrRegisterWithPhone(String phone, String otp) {
        enterPhoneNumber(phone);
        submitPhoneNumber();
        enterOtp(otp);
        return submitOtp();
    }

    public boolean isErrorVisible() {
        return inlineError().isVisible();
    }

    public String errorText() {
        return inlineError().innerText().trim();
    }
}
