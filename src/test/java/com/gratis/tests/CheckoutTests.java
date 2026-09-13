package com.gratis.tests;

import com.gratis.base.LoggedInBaseTest;
import com.gratis.pages.CartPage;
import com.gratis.pages.CheckoutPage;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CheckoutTests extends LoggedInBaseTest {


    @Test(description = "TC_028 - Store Pickup Delivery (Gel-Al) Selection Flow, " +
            "a billing address is required for store pickup and its creation " +
            "modal is shown - no address is entered, no payment is attempted")
    public void storePickupSelectionFlow() {
        CartPage cart = new CartPage(page);
        cart.cartButton();
        cart.continueToDelivery();

        CheckoutPage checkout= new CheckoutPage(page);
        checkout.chooseIl();
        checkout.chooseIlce();
        checkout.chooseMagaza();
        checkout.accAgreement();

        checkout.openBillingAddressModal();
        assertThat(checkout.addAddressModal()).isVisible();

    }

    @Test(description = "TC_029 - Address Creation and Home Delivery Selection, " +
            "reaching the payment step opens the Ödeme payment modal - " +
            "no card details are entered, no payment is attempted")
    public void addressCreationAndHomeDelivery() {
        CartPage cart = new CartPage(page);
        cart.cartButton();
        cart.continueToDelivery();
        CheckoutPage checkout= new CheckoutPage(page);
        checkout.chooseOnline();

        if (page.getByText("Sistemimizde kayıtlı adresiniz bulunmamaktadır.").isVisible()) {
            checkout.addAddress();

            checkout.addName();
            checkout.addSurname();
            checkout.addressName();
            checkout.adresIL();
            checkout.adresILCE();
            checkout.adresStreet();
            checkout.addressDetail();
            acceptCookiesIfPresent();
            checkout.saveAddress();
        }

        checkout.accAgreement();
        checkout.continueToPayment();

        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Ödeme"))).isVisible();

    }

    @Test(description = "TC_030 - Navigating directly to checkout with an empty cart blocks access")
    public void emptyCartBlocksCheckoutAccess() {
        CartPage cart = new CartPage(page);
        cart.cartButton();
        cart.clearCart();
        cart.tamam();

        assertThat(page.getByText("Sepetinizde Ürün Bulunmuyor")).isVisible();
        assertThat(page.getByText("TESLİMAT ADIMINA GEÇ")).not().isVisible();

    }
}
