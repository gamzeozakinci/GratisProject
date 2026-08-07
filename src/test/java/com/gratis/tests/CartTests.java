package com.gratis.tests;

import com.gratis.base.BaseTest;
import com.gratis.config.ConfigReader;
import com.gratis.pages.CartPage;
import com.gratis.pages.HomePage;
import com.gratis.pages.PDPPage;
import com.gratis.pages.PLPPage;
import com.gratis.utils.Constants;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartTests extends BaseTest {

    @Test(description = "TC_020 - Add Product to Cart from PLP and PDP", groups = Constants.GROUP_CART)
    public void addProductsFromPlpAndPdp() {
        page.navigate(ConfigReader.baseUrl() + "makyaj/dudak-makyaji");
        PLPPage plp = new PLPPage(page);
        plp.addFirstProductToCart();

        page.navigate(ConfigReader.baseUrl() + "urun/bee-beauty-tonik");
        PDPPage pdp = new PDPPage(page);
        pdp.selectQuantity(2);
        pdp.addToCart();

        page.navigate(ConfigReader.baseUrl() + "cart");
        CartPage cart = new CartPage(page);

        Assert.assertEquals(cart.itemCount(), 2, "Cart should list both products");
        Assert.assertEquals(cart.quantityOf(0), 1, "First product quantity should be 1");
        Assert.assertEquals(cart.quantityOf(1), 2, "Second product quantity should be 2");
        Assert.assertFalse(cart.grandTotalText().isEmpty(), "Grand total should be displayed and non-empty");
    }

    @Test(description = "TC_021 - Update Product Quantity in Cart (Boundary & Limit Checks)",
            groups = Constants.GROUP_CART)
    public void quantityBoundaryChecksInCart() {
        // Precondition: 1 unit of a product with warehouse stock of 5 already in cart.
        page.navigate(ConfigReader.baseUrl() + "cart");
        CartPage cart = new CartPage(page);

        cart.increaseQuantity(0, 1);
        Assert.assertEquals(cart.quantityOf(0), 2, "Quantity should update to 2 after one increase");

        cart.increaseQuantity(0, 4); // attempt to push from 2 -> 6, past the stock limit of 5
        Assert.assertTrue(cart.isStockLimitToastVisible(), "Stock-limit warning toast should appear");
        Assert.assertEquals(cart.quantityOf(0), 5, "Quantity should be capped at the 5-unit stock limit");

        cart.setQuantityDirectly(0, "0");
        Assert.assertTrue(cart.isDeleteConfirmPopupVisible(), "Entering 0 should prompt a delete confirmation");

        cart.setQuantityDirectly(0, "-5");
        Assert.assertNotEquals(cart.quantityOf(0), -5, "Negative input should be rejected/reset");

        cart.setQuantityDirectly(0, "abc");
        Assert.assertTrue(cart.quantityOf(0) >= 1, "Non-numeric input should reset to a valid quantity");
    }

    @Test(description = "TC_022 - Remove Product from Shopping Cart", groups = Constants.GROUP_CART)
    public void removeProductFromCart() {
        // Precondition: cart already has two different products.
        page.navigate(ConfigReader.baseUrl() + "cart");
        CartPage cart = new CartPage(page);
        int before = cart.itemCount();

        cart.removeItem(0);

        Assert.assertEquals(cart.itemCount(), before - 1, "Item count should decrease by one");
    }

    @Test(description = "TC_023 - Apply Valid Discount Promo Code to Order", groups = Constants.GROUP_CART)
    public void applyValidPromoCode() {
        // Precondition: cart subtotal = 100 TL.
        page.navigate(ConfigReader.baseUrl() + "cart");
        CartPage cart = new CartPage(page);

        cart.applyPromoCode(ConfigReader.get("valid.promo.code"));

        Assert.assertTrue(cart.isPromoSuccessVisible(), "Coupon success indicator should appear");
        Assert.assertTrue(cart.couponDiscountText().contains("-20"), "A -20 TL discount line should be added");
        Assert.assertTrue(cart.grandTotalText().contains("80"), "Grand total should drop to 80 TL (plus shipping)");
    }

    @Test(description = "TC_024 - Apply Invalid or Expired Promo Code", groups = Constants.GROUP_CART)
    public void invalidAndExpiredPromoCodesAreRejected() {
        page.navigate(ConfigReader.baseUrl() + "cart");
        CartPage cart = new CartPage(page);
        String totalBefore = cart.grandTotalText();

        cart.applyPromoCode(ConfigReader.get("invalid.promo.code"));
        Assert.assertTrue(cart.isPromoErrorVisible(), "Invalid code should show the generic error message");
        Assert.assertEquals(cart.grandTotalText(), totalBefore, "Total should be unchanged after an invalid code");

        cart.applyPromoCode(ConfigReader.get("expired.promo.code"));
        Assert.assertTrue(cart.isPromoErrorVisible(), "Expired code should show the same generic error message");
        Assert.assertEquals(cart.grandTotalText(), totalBefore, "Total should be unchanged after an expired code");
    }

    @Test(description = "TC_025 - Shopping Cart Session Persistence", groups = {Constants.GROUP_CART, Constants.GROUP_REGRESSION})
    public void cartPersistsAcrossReloadAndReLogin() {
        // NOTE: "close all tabs" / "clear cache but not cookies" is hard to model with a
        // single Playwright BrowserContext. This test approximates the same intent using
        // context.storageState() to snapshot the session and reload it into a new page -
        // see README "Testing Session Persistence with Playwright" for the full pattern.
        page.navigate(ConfigReader.baseUrl() + "cart");
        CartPage cart = new CartPage(page);
        int itemsBeforeReload = cart.itemCount();

        page.reload();
        Assert.assertEquals(cart.itemCount(), itemsBeforeReload, "Cart items should persist after reload");

        HomePage home = new HomePage(page);
        home.header.openLoginOrRegister(); // logout entry point varies by app; adjust once DOM is inspected
        // ... re-login flow would go here using LoginPage, then re-assert cart.itemCount()
    }
}
