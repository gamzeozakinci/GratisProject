package com.gratis.tests;

import com.gratis.base.BaseTest;
import org.testng.annotations.Test;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CheckoutTests extends BaseTest {

    @Test(description = "TC_025 - Store Pickup Delivery (Gel-Al) Selection Flow")
    public void storePickupSelectionFlow() {
        // TODO: implement
    }

    @Test(description = "TC_026 - Address Creation and Home Delivery Selection")
    public void addressCreationAndHomeDelivery() {
        // TODO: implement
    }

    @Test(description = "TC_027 - Reaching the payment step lands on the correct payment URL " +
            "(personal project, no test card credentials - no real payment is attempted)")
    public void paymentStepReachesPaymentUrl() {
        // TODO: cart -> shipping -> continue to payment

        assertThat(page).hasURL(Pattern.compile(".*checkout/payment.*"));
    }
}
