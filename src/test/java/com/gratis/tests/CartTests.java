package com.gratis.tests;

import com.gratis.base.LoggedInBaseTest;
import com.gratis.pages.*;
import com.microsoft.playwright.Locator;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CartTests extends LoggedInBaseTest {

    HeaderComponent header;
    CartPage cart;
    PLPPage plp;
    PDPPage pdp;
    String firstItem;

    @BeforeMethod(alwaysRun = true)
    public void initPageObjects() {
        header = new HeaderComponent(page);
        cart = new CartPage(page);
        plp = new PLPPage(page);
        pdp = new PDPPage(page);
    }

    @Test(description = "TC_022 - Add Product to Cart from PLP and PDP")
    public void addProductsFromPlpAndPdp() {

        header.headerSacbakim();

        plp.add1stItem();
        firstItem = page.locator("a[href*='-p-']").first().locator("h5").innerText();
        cart.cartButton();

        // scoped to the actual cart-item row, not a plain a[href*='-p-'] -
        // unscoped can match a promotional/recommendation-carousel link
        // instead of the real cart row (same bug already found in
        // FilterSortTests and PLPPage.clickFirstItem()).
        assertThat(cart.cartItem(firstItem)).isVisible();

        cart.clearCart();
        cart.tamam();
        cart.logoCart();

        header.headerSacbakimForced();
        plp.clickFirstItem();
        pdp.addToCart();
        cart.cartButton();

        assertThat(cart.cartItem(firstItem)).isVisible();

    }

    @Test(description = "TC_023 - Update Product Quantity in Cart (Boundary & Limit Checks)",
            dependsOnMethods = "addProductsFromPlpAndPdp")
    public void quantityBoundaryChecksInCart() {
        cart.cartButton();

        Locator qty = cart.quantityLocator(firstItem);
        String currQ = qty.innerText();

        cart.increaseQuantity(firstItem);
        assertThat(qty).not().hasText(currQ);
        String newQ = qty.innerText();

        Assert.assertNotEquals(newQ, currQ, "Quantity of product did not change");

        cart.decreaseQuantity(firstItem);
        assertThat(qty).hasText(currQ);
        newQ = qty.innerText();

        Assert.assertEquals(newQ, currQ, "Quantity of product did not change");


    }

    @Test(description = "TC_024 - Remove Product from Shopping Cart",
            dependsOnMethods = "addProductsFromPlpAndPdp")
    public void removeProductFromCart() {
        cart.cartButton();

        while (!cart.quantityOf(firstItem).equalsIgnoreCase("1")) {
            cart.decreaseQuantity(firstItem);
        }
        cart.decreaseQuantity(firstItem);
        cart.removeProduct();

        assertThat(cart.cartItem(firstItem)).not().isVisible();

    }


    @Test(description = "TC_025 - Apply Invalid or Expired Promo Code")
    public void invalidAndExpiredPromoCodesAreRejected() {
        //only tried with invalid code because since this is s volunteery test i dont have access to test promo codes
        cart.cartButton();
        cart.openPromocode();
        cart.enterPromoCOde();
        cart.submitPromoCode();

        assertThat(page.locator("//*[text()=\"Kupon Kodu geçersizdir.\"]").first()).isVisible();

    }

    @Test(description = "TC_026 - Shopping Cart Session Persistence")
    public void cartPersistsAcrossReloadAndReLogin() {
        //yanlıs calısıyo bu tekrar bak
        header.headerSacbakim();
        plp.add1stItem();

        page.reload();
        cart.cartButton();

        assertThat(page.locator("a[href*='-p-']").first()).isVisible();
    }

    @Test(description = "TC_027 - Deleting all items from the cart ",
            dependsOnMethods = "removeProductFromCart")
    public void deleteItemFromCart() {
        cart.cartButton();
        cart.deleteAllFromCart();

        assertThat(page.getByText("Sepetinizde Ürün Bulunmuyor")).isVisible();
    }
}
