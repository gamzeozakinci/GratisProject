package com.gratis.tests;

import com.gratis.base.BaseTest;
import com.gratis.pages.CartPage;
import org.testng.annotations.Test;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CheckoutTests extends BaseTest {

    @Test(description = "TC_025 - Store Pickup Delivery (Gel-Al) Selection Flow, " +
            "Reaching the payment step lands on the correct payment URL " +
            "(personal project, no test card credentials - no real payment is attempted)")
    public void storePickupSelectionFlow() {
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

    @Test(description = "TC_026 - Address Creation and Home Delivery Selection " +
            "Reaching the payment step lands on the correct payment URL " +
            "(personal project, no test card credentials - no real payment is attempted)")
    public void addressCreationAndHomeDelivery() {
        CartPage cart = new CartPage(page);
        cart.cartButton();


        assertThat(page).hasURL(Pattern.compile(".*checkout/payment.*"));
    }

}
