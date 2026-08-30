package com.gratis.tests;

import com.gratis.base.BaseTest;
import com.gratis.pages.CartPage;
import com.gratis.pages.HeaderComponent;
import com.gratis.pages.PDPPage;
import com.gratis.pages.PLPPage;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CartTests extends BaseTest {

    @Test(description = "TC_019 - Add Product to Cart from PLP and PDP")
    public void addProductsFromPlpAndPdp() {
        HeaderComponent header = new HeaderComponent(page);
        header.headerSacbakim();

        PLPPage plp = new PLPPage(page);
        plp.add1stItem();
        String firstItem = page.locator("a[href*='-p-']").first().toString();

        CartPage cart = new CartPage(page);
        cart.cartButton();

        assertThat(page.locator(".flex.flex-1.flex-col").getByText(firstItem).first()).isVisible();

        cart.clearCart();
        cart.tamam();
        cart.logoCart();

        header.headerSacbakim();
        plp.clickFirstItem();

        PDPPage pdp = new PDPPage(page);
        pdp.addToCart();
        cart.cartButton();
        assertThat(page.locator(".flex.flex-1.flex-col").getByText(firstItem).first()).isVisible();


    }

    @Test(description = "TC_020 - Update Product Quantity in Cart (Boundary & Limit Checks)")
    public void quantityBoundaryChecksInCart() {
        // TODO: implement
    }

    @Test(description = "TC_021 - Remove Product from Shopping Cart")
    public void removeProductFromCart() {
        // TODO: implement
    }


    @Test(description = "TC_023 - Apply Invalid or Expired Promo Code")
    public void invalidAndExpiredPromoCodesAreRejected() {
        // TODO: implement
    }

    @Test(description = "TC_024 - Shopping Cart Session Persistence")
    public void cartPersistsAcrossReloadAndReLogin() {
        // TODO: implement
    }
}
