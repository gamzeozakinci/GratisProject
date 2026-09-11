package com.gratis.tests;

import com.gratis.base.LoggedInBaseTest;
import com.gratis.pages.CartPage;
import com.gratis.pages.CheckoutPage;
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
        CheckoutPage checkout = new CheckoutPage(page);
        cart.cartButton();
        cart.continueToDelivery();

        checkout.chooseIl();
        checkout.chooseIlce();
        checkout.chooseMagaza();
        checkout.accAgreement();
        checkout.continueToPayment();

        assertThat(page).hasURL(Pattern.compile(".*checkout/payment.*"));

    }

    @Test(description = "TC_029 - Address Creation and Home Delivery Selection " +
            "Reaching the payment step lands on the correct payment URL " +
            "(personal project, no test card credentials - no real payment is attempted)")
    public void addressCreationAndHomeDelivery() {
        CartPage cart = new CartPage(page);
        CheckoutPage checkout = new CheckoutPage(page);
        cart.cartButton();
        cart.continueToDelivery();
        checkout.chooseOnline();
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

        page.waitForURL(Pattern.compile(".*checkout/payment.*"));
        assertThat(page).hasURL(Pattern.compile(".*checkout/payment.*"));

    }

    @Test(description = "TC_030 - Navigating directly to checkout with an empty cart blocks access")
    public void emptyCartBlocksCheckoutAccess() {
        //ürün silmeye bagla silince checkout clickable olmasın ve sepette ürün bulunmuyor yazısı
        CartPage cart = new CartPage(page);
        assertThat(page.locator("Sepetinizde Ürün Bulunmuyor")).isVisible();
        assertThat(page.locator("TESLİMAT ADIMINA GEÇ")).not().isVisible();

    }
}
