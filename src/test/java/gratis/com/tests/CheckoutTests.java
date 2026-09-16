package gratis.com.tests;

import com.gratis.base.LoggedInBaseTest;
import com.gratis.pages.CartPage;
import com.gratis.pages.CheckoutPage;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CheckoutTests extends LoggedInBaseTest {


    @Test(description = "TC_028 - Store Pickup Delivery (Gel-Al) Selection Flow, " +
            "a billing address is required for store pickup and its creation " +
            "modal is shown - no address is entered, no payment is attempted")
    public void storePickupSelectionFlow() {
        CartPage cp = new CartPage(page);
        cp.cartButton();
        cp.continueToDelivery();

        CheckoutPage chp = new CheckoutPage(page);
        chp.chooseIl();
        chp.chooseIlce();
        chp.chooseMagaza();
        chp.accAgreement();

        if (page.getByText("Sistemimizde kayıtlı adresiniz bulunmamaktadır.").isVisible()) {
            chp.addAddress();

            chp.addName();
            chp.addSurname();
            chp.addressName();
            chp.adresIL();
            chp.adresILCE();
            chp.adresStreet();
            chp.addressDetail();
            acceptCookiesIfPresent();
            chp.saveAddress();
        }

        chp.openBillingAddressModal();
        assertThat(chp.addAddressModal()).isVisible();

    }

    @Test(description = "TC_029 - Address Creation and Home Delivery Selection, " +
            "reaching the payment step opens the Ödeme payment modal - " +
            "no card details are entered, no payment is attempted", groups = "smoke")
    public void addressCreationAndHomeDelivery() {
        CartPage cp = new CartPage(page);
        cp.cartButton();
        cp.continueToDelivery();
        CheckoutPage chp = new CheckoutPage(page);
        chp.chooseOnline();

        if (page.getByText("Sistemimizde kayıtlı adresiniz bulunmamaktadır.").isVisible()) {
            chp.addAddress();

            chp.addName();
            chp.addSurname();
            chp.addressName();
            chp.adresIL();
            chp.adresILCE();
            chp.adresStreet();
            chp.addressDetail();
            acceptCookiesIfPresent();
            chp.saveAddress();
        }

        chp.accAgreement();
        chp.continueToPayment();

        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Ödeme"))).isVisible();

    }

    @Test(description = "TC_030 - Navigating directly to checkout with an empty cart blocks access")
    public void emptyCartBlocksCheckoutAccess() {
        CartPage cp = new CartPage(page);
        cp.cartButton();
        cp.clearCart();
        cp.tamam();

        assertThat(page.getByText("Sepetinizde Ürün Bulunmuyor")).isVisible();
        assertThat(page.getByText("TESLİMAT ADIMINA GEÇ")).not().isVisible();

    }
}
