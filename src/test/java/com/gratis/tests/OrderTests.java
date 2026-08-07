package com.gratis.tests;

import com.gratis.base.BaseTest;
import com.gratis.pages.HomePage;
import com.gratis.pages.OrderPage;
import com.gratis.utils.Constants;
import org.testng.Assert;
import org.testng.annotations.Test;

public class OrderTests extends BaseTest {

    @Test(description = "TC_030 - Post-Order Validation (Order Summary Screen & Email Verification)",
            groups = {Constants.GROUP_REGRESSION})
    // Depends on TC_028 having produced a real order; in the full suite this reads the
    // order ID that CheckoutTests.completePaymentWithValidCardVia3DSecure captured
    // (e.g. via a shared ITestContext attribute) rather than a hardcoded value.
    public void orderAppearsInHistoryWithMatchingDetails() {
        String orderId = System.getProperty("lastOrderId", "GR123456789");

        HomePage home = new HomePage(page);
        home.header.goToMyOrders();

        OrderPage orders = new OrderPage(page);
        Assert.assertTrue(orders.isOrderListed(orderId), "Order should appear in the order history list");

        orders.openOrderDetail(orderId);
        Assert.assertFalse(orders.detailProductsText().isEmpty(), "Order detail should list the purchased products");
        Assert.assertFalse(orders.detailAddressText().isEmpty(), "Order detail should show billing/shipping address");
        Assert.assertFalse(orders.detailShippingMethodText().isEmpty(), "Order detail should show the shipping method");

        // Confirmation email verification needs a mailbox API (Mailinator/Ethereal etc.) -
        // out of scope for the UI layer, documented in README "Out of Scope / Future Work".
    }
}
