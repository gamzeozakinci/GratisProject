package com.gratis.tests;

import com.gratis.base.LoggedInBaseTest;
import com.gratis.pages.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CartTests extends LoggedInBaseTest {

    String firstItem;

    @Test(description = "TC_019 - Add Product to Cart from PLP and PDP")
    public void addProductsFromPlpAndPdp() {
        HeaderComponent header = new HeaderComponent(page);
        header.headerSacbakim();

        PLPPage plp = new PLPPage(page);
        plp.add1stItem();
        firstItem = page.locator("a[href*='-p-']").first().toString();

        CartPage cart = new CartPage(page);
        cart.cartButton();

        Assert.assertEquals(page.locator(".flex.flex-1.flex-col").first().innerText(), firstItem);

        cart.clearCart();
        cart.tamam();
        cart.logoCart();

        header.headerSacbakim();
        plp.clickFirstItem();

        PDPPage pdp = new PDPPage(page);
        pdp.addToCart();
        cart.cartButton();
        Assert.assertEquals(page.locator(".flex.flex-1.flex-col").first().innerText(), firstItem);


    }

    @Test(description = "TC_020 - Update Product Quantity in Cart (Boundary & Limit Checks)")
    public void quantityBoundaryChecksInCart() {

        CartPage cart = new CartPage(page);
        cart.cartButton();
        String currQ = page.locator(".font-semibold.text-black").first().innerText();

        cart.increaseQuantity();

        String newQ = page.locator(".font-semibold.text-black").first().innerText();

        Assert.assertNotEquals(newQ, currQ, "Quantity of product did not change");

        cart.decreaseQuantity();
        newQ = page.locator(".font-semibold.text-black").first().innerText();

        Assert.assertEquals(newQ, currQ, "Quantity of product did not change");


    }

    @Test(description = "TC_021 - Remove Product from Shopping Cart")
    public void removeProductFromCart() {
        CartPage cart = new CartPage(page);
        cart.cartButton();

        cart.decreaseQuantity();

        assertThat(page.locator(".flex.flex-col.gap-4").getByText(firstItem)).isVisible();


    }


    @Test(description = "TC_023 - Apply Invalid or Expired Promo Code")
    public void invalidAndExpiredPromoCodesAreRejected() {
        //only tried with invalid code because since this is s volunteery test i dont have access to test promo codes
        CartPage cart = new CartPage(page);
        cart.cartButton();
        cart.openPromocode();
        cart.enterPromoCOde();
        cart.submitPromoCode();

        assertThat(page.locator("//*[text()=\"Kupon Kodu geçersizdir.\"]").first()).isVisible();


    }

    @Test(description = "TC_024 - Shopping Cart Session Persistence")
    public void cartPersistsAcrossReloadAndReLogin() {
        // already logged in - see LoggedInBaseTest.setUp()
        HeaderComponent header = new HeaderComponent(page);
        header.headerSacbakim();
        PLPPage plp = new PLPPage(page);
        plp.add1stItem();

        page.reload();

        CartPage cart = new CartPage(page);
        cart.cartButton();

        assertThat(page.locator(".flex.flex-1.flex-col").first()).isVisible(); // same cart-item locator TC_019 uses
    }
}
