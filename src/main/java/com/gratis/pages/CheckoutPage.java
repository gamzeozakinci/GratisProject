package com.gratis.pages;

import com.gratis.base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.SelectOption;

public class CheckoutPage extends BasePage {

    public CheckoutPage(Page page) {
        super(page);
    }

    // --- Shipping step ---
    private Locator storePickupOption() {
        return page.getByText("Mağazadan Teslimat (Gel-Al)");
    }

    private Locator homeDeliveryOption() {
        return page.getByText("Adrese Teslimat");
    }

    private Locator citySelect() {
        return page.locator("select[name='city']");
    }

    private Locator districtSelect() {
        return page.locator("select[name='district']");
    }

    private Locator storeListItem(String storeName) {
        return page.getByText(storeName);
    }

    private Locator addNewAddressButton() {
        return page.getByText("Yeni Adres Ekle");
    }

    private Locator addressTitleInput() {
        return page.locator("input[name='addressTitle']");
    }

    private Locator neighborhoodInput() {
        return page.locator("input[name='neighborhood']");
    }

    private Locator fullAddressInput() {
        return page.locator("textarea[name='fullAddress']");
    }

    private Locator zipCodeInput() {
        return page.locator("input[name='zipCode']");
    }

    private Locator saveAddressButton() {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Kaydet"));
    }

    private Locator addressCard(String title) {
        return page.locator(".address-card").filter(new Locator.FilterOptions().setHasText(title));
    }

    private Locator continueToPaymentButton() {
        return page.getByText("Ödemeye Geç");
    }

    private Locator shippingFeeSummaryRow() {
        return page.locator(".summary-shipping");
    }

    // --- Payment step ---
    private Locator payWithCardOption() {
        return page.getByText("Kredi Kartı ile Ödeme");
    }

    private Locator cardholderName() {
        return page.locator("input[name='cardholderName']");
    }

    private Locator cardNumber() {
        return page.locator("input[name='cardNumber']");
    }

    private Locator expiryDate() {
        return page.locator("input[name='expiryDate']");
    }

    private Locator cvc() {
        return page.locator("input[name='cvc']");
    }

    private Locator legalAgreementCheckbox() {
        return page.locator("input#distanceSellingAgreement");
    }

    private Locator payButton() {
        return page.getByText("Ödeme Yap");
    }

    private Locator otpInput() {
        return page.locator("input.otp-input");
    }

    private Locator otpConfirmButton() {
        return page.getByText("Onayla");
    }

    private Locator orderReceivedHeading() {
        return page.getByText("Siparişiniz Alındı!");
    }

    private Locator orderIdText() {
        return page.locator(".order-id");
    }

    private Locator paymentFailureBanner() {
        return page.locator(".payment-error-banner");
    }

    // Shipping actions
    public void chooseStorePickup() {
        storePickupOption().click();
    }

    public void chooseHomeDelivery() {
        homeDeliveryOption().click();
    }

    public void selectStore(String city, String district, String storeName) {
        citySelect().selectOption(new SelectOption().setLabel(city));
        districtSelect().selectOption(new SelectOption().setLabel(district));
        storeListItem(storeName).click();
    }

    public String shippingFeeText() {
        return shippingFeeSummaryRow().innerText().trim();
    }

    public void addNewAddress(String title, String city, String district, String neighborhood,
                              String fullAddress, String zip) {
        addNewAddressButton().click();
        addressTitleInput().fill(title);
        citySelect().selectOption(new SelectOption().setLabel(city));
        districtSelect().selectOption(new SelectOption().setLabel(district));
        neighborhoodInput().fill(neighborhood);
        fullAddressInput().fill(fullAddress);
        zipCodeInput().fill(zip);
        saveAddressButton().click();
    }

    public void selectSavedAddress(String title) {
        addressCard(title).click();
    }

    public void continueToPayment() {
        continueToPaymentButton().click();
    }

    // Payment actions
    public void choosePayWithCard() {
        payWithCardOption().click();
    }

    public void fillCardDetails(String name, String number, String expiry, String cvcValue) {
        cardholderName().fill(name);
        cardNumber().fill(number);
        expiryDate().fill(expiry);
        cvc().fill(cvcValue);
    }

    public void acceptLegalAgreement() {
        legalAgreementCheckbox().click();
    }

    public void pay() {
        payButton().click();
    }

    public void completeThreeDSecure(String otpCode) {
        otpInput().waitFor();
        otpInput().fill(otpCode);
        otpConfirmButton().click();
    }

    public boolean isOrderSuccessVisible() {
        return orderReceivedHeading().isVisible();
    }

    public String orderId() {
        return orderIdText().innerText().trim();
    }

    public boolean isPaymentFailureBannerVisible() {
        return paymentFailureBanner().isVisible();
    }

    public String paymentFailureText() {
        return paymentFailureBanner().innerText().trim();
    }

    public boolean isStillOnPaymentPage() {
        return currentUrl().contains("/checkout/payment");
    }
}
