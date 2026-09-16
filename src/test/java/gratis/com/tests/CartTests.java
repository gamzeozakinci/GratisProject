package gratis.com.tests;

import com.gratis.base.LoggedInBaseTest;
import com.gratis.pages.*;
import com.microsoft.playwright.Locator;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CartTests extends LoggedInBaseTest {

    String firstItem;

    @Test(description = "TC_022 - Add Product to Cart from PLP and PDP", groups = "smoke")
    public void addProductsFromPlpAndPdp() {
        HeaderComponent hp = new HeaderComponent(page);
        PLPPage plp = new PLPPage(page);
        CartPage cp = new CartPage(page);
        PDPPage pdp = new PDPPage(page);

        hp.headerSacbakim();

        plp.add1stItem();
        firstItem = page.locator("a[href*='-p-']").first().locator("h5").innerText();
        cp.cartButton();

        assertThat(cp.cartItem(firstItem)).isVisible();

        cp.clearCart();
        cp.tamam();
        cp.logoCart();

        hp.headerSacbakim();
        plp.clickFirstItem();
        pdp.addToCart();
        cp.cartButton();

        assertThat(cp.cartItem(firstItem)).isVisible();

    }

    @Test(description = "TC_023 - Update Product Quantity in Cart (Boundary & Limit Checks)",
            dependsOnMethods = "addProductsFromPlpAndPdp")
    public void quantityBoundaryChecksInCart() {
        CartPage cp = new CartPage(page);
        cp.cartButton();

        Locator qty = cp.quantityLocator(firstItem);
        String currQ = qty.innerText();

        cp.increaseQuantity(firstItem);
        assertThat(qty).not().hasText(currQ);
        String newQ = qty.innerText();

        Assert.assertNotEquals(newQ, currQ, "Quantity of product did not change");

        cp.decreaseQuantity(firstItem);
        assertThat(qty).hasText(currQ);
        newQ = qty.innerText();

        Assert.assertEquals(newQ, currQ, "Quantity of product did not change");


    }

    @Test(description = "TC_024 - Remove Product from Shopping Cart",
            dependsOnMethods = "addProductsFromPlpAndPdp")
    public void removeProductFromCart() {
        CartPage cp = new CartPage(page);
        cp.cartButton();

        while (!cp.quantityOf(firstItem).equalsIgnoreCase("1")) {
            cp.decreaseQuantity(firstItem);
        }
        cp.decreaseQuantity(firstItem);
        cp.removeProduct();

        assertThat(cp.cartItem(firstItem)).not().isVisible();

    }


    @Test(description = "TC_025 - Apply Invalid or Expired Promo Code")
    public void invalidAndExpiredPromoCodesAreRejected() {
        //only tried with invalid code because since this is s volunteery test i dont have access to test promo codes
        CartPage cp = new CartPage(page);
        cp.cartButton();
        cp.openPromocode();
        cp.enterPromoCOde();
        cp.submitPromoCode();

        assertThat(page.locator("//*[text()=\"Kupon Kodu geçersizdir.\"]").first()).isVisible();

    }

    @Test(description = "TC_026 - Shopping Cart Session Persistence")
    public void cartPersistsAcrossReloadAndReLogin() {
        HeaderComponent hp = new HeaderComponent(page);
        PLPPage plp = new PLPPage(page);
        CartPage cp = new CartPage(page);

        hp.headerSacbakim();
        plp.add1stItem();

        page.reload();
        cp.cartButton();

        assertThat(page.locator("a[href*='-p-']").first()).isVisible();
    }

    @Test(description = "TC_027 - Deleting all items from the cart ",
            dependsOnMethods = "removeProductFromCart")
    public void deleteItemFromCart() {
        CartPage cp = new CartPage(page);
        cp.cartButton();
        cp.deleteAllFromCart();

        assertThat(page.getByText("Sepetinizde Ürün Bulunmuyor")).isVisible();
    }

}
