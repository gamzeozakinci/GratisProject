package com.gratis.tests;

import com.gratis.base.BaseTest;
import com.gratis.config.ConfigReader;
import com.gratis.pages.CheckoutPage;
import com.gratis.utils.Constants;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CheckoutTests extends BaseTest {

    @Test(description = "TC_026 - Store Pickup Delivery (Gel-Al) Selection Flow", groups = Constants.GROUP_CHECKOUT)
    public void storePickupSelectionFlow() {
        page.navigate(ConfigReader.baseUrl() + "checkout/shipping");
        CheckoutPage checkout = new CheckoutPage(page);

        checkout.chooseStorePickup();
        checkout.selectStore("İstanbul", "Kadıköy", "Kadıköy Bahariye Mağazası");

        Assert.assertTrue(checkout.shippingFeeText().contains("Ücretsiz") || checkout.shippingFeeText().contains("0,00"),
                "Shipping fee should become free when a store is selected for pickup");

        checkout.continueToPayment();
        Assert.assertTrue(checkout.currentUrl().contains("/checkout/payment"), "Should proceed to the payment step");
    }

    @Test(description = "TC_027 - Address Creation and Home Delivery Selection", groups = Constants.GROUP_CHECKOUT)
    public void addressCreationAndHomeDelivery() {
        page.navigate(ConfigReader.baseUrl() + "checkout/shipping");
        CheckoutPage checkout = new CheckoutPage(page);

        checkout.chooseHomeDelivery();
        checkout.addNewAddress("Ofis", "Ankara", "Çankaya", "Kavaklıdere",
                "Atatürk Bulvarı No: 15, Kat: 3", "06680");
        checkout.selectSavedAddress("Ofis");

        Assert.assertFalse(checkout.shippingFeeText().isEmpty(), "A calculated shipping fee should be displayed");

        checkout.continueToPayment();
        Assert.assertTrue(checkout.currentUrl().contains("/checkout/payment"), "Should proceed to the payment step");
    }

    @Test(description = "TC_028 - Complete Payment using Valid Credit Card via 3D Secure (E2E)",
            groups = {Constants.GROUP_CHECKOUT, "critical"})
    public void completePaymentWithValidCardVia3DSecure() {
        // Requires the sandbox/mock payment mode to be active in the target test environment.
        page.navigate(ConfigReader.baseUrl() + "checkout/payment");
        CheckoutPage checkout = new CheckoutPage(page);

        checkout.choosePayWithCard();
        checkout.fillCardDetails("Ayşe Yılmaz", "4355123456789012", "12/28", "123");
        checkout.acceptLegalAgreement();
        checkout.pay();
        checkout.completeThreeDSecure(ConfigReader.get("mock.otp"));

        Assert.assertTrue(checkout.isOrderSuccessVisible(), "Order success page should be shown");
        Assert.assertTrue(checkout.orderId().matches("GR\\d+"), "A unique order ID should be displayed");
        // DB-level assertion (order status = PAID / PENDING_PREPARATION) needs a backend
        // hook (DB query or internal API) - see README "API/DB Assertions".
    }

    @Test(description = "TC_029 - Fail Payment using Card with Insufficient Funds (Negative)",
            groups = Constants.GROUP_CHECKOUT)
    public void paymentFailsWithInsufficientFundsCard() {
        page.navigate(ConfigReader.baseUrl() + "checkout/payment");
        CheckoutPage checkout = new CheckoutPage(page);

        checkout.choosePayWithCard();
        checkout.fillCardDetails("Ayşe Yılmaz", "4355123456789999", "10/28", "999");
        checkout.acceptLegalAgreement();
        checkout.pay();

        Assert.assertTrue(checkout.isPaymentFailureBannerVisible(), "A payment failure banner should be shown");
        Assert.assertTrue(checkout.paymentFailureText().contains("Yetersiz Bakiye"),
                "Error should reference insufficient funds (error code 51)");
        Assert.assertTrue(checkout.isStillOnPaymentPage(), "User should remain on the payment page, no redirect");
    }
}
