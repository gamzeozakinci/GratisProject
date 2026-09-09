package com.gratis.tests;

import com.gratis.base.LoggedInBaseTest;
import com.gratis.pages.CartPage;
import org.testng.annotations.Test;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CheckoutTests extends LoggedInBaseTest {

    @Test(description = "TC_028 - Store Pickup Delivery (Gel-Al) Selection Flow, " +
            "Reaching the payment step lands on the correct payment URL " +
            "(personal project, no test card credentials - no real payment is attempted)")
    public void storePickupSelectionFlow() {

        //adres ekleme zorundu dependency ekle once
        CartPage cart = new CartPage(page);
        cart.cartButton();
        cart.continueToDelivery();

        cart.chooseIl();
        cart.chooseIlce();
        cart.chooseMagaza();
        cart.accAgreement();
        cart.continueToPayment();

        assertThat(page).hasURL(Pattern.compile(".*checkout/payment.*"));

    }

    @Test(description = "TC_029 - Address Creation and Home Delivery Selection " +
            "Reaching the payment step lands on the correct payment URL " +
            "(personal project, no test card credentials - no real payment is attempted)")
    public void addressCreationAndHomeDelivery() {
        CartPage cart = new CartPage(page);
        cart.cartButton();
        cart.continueToDelivery();
        cart.chooseOnline();
        cart.addAddress();

        cart.addName();
        cart.addSurname();
        cart.addressName();
        cart.adresIL();
        cart.adresILCE();
        cart.adresStreet();
        cart.addressDetail();
        acceptCookiesIfPresent();
        cart.saveAddress();

        page.waitForURL(Pattern.compile(".*checkout/payment.*"));
        assertThat(page).hasURL(Pattern.compile(".*checkout/payment.*"));

    }

    @Test(description = "TC_030 - Navigating directly to checkout with an empty cart blocks access")
    public void emptyCartBlocksCheckoutAccess() {
        // TODO: implement
    }
}
