package gratis.com.tests;

import com.gratis.base.BaseTest;
import com.gratis.config.ConfigReader;
import com.gratis.driver.PlaywrightFactory;
import com.gratis.pages.HeaderComponent;
import com.gratis.pages.PDPPage;
import com.gratis.pages.PLPPage;
import com.gratis.pages.WishListPage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CatalogTests extends BaseTest {

    String product;

    @Test(description = "TC_016 - Verify Product Detail Page Information Layout", groups = "smoke")
    public void pdpLayoutShowsAllRequiredElements() {
        HeaderComponent hp = new HeaderComponent(page);
        hp.headerSacbakim();

        PLPPage plp = new PLPPage(page);
        plp.clickFirstItem();

        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setLevel(1))).isVisible();

    }

    @Test(description = "TC_017 - Verify Product Stock Status (In-Stock vs Out-of-Stock)")
    public void stockStatusReflectsAvailability() {
        PlaywrightFactory.tearDown();
        page = PlaywrightFactory.initLoggedInPage();
        page.navigate(ConfigReader.baseUrl());

        HeaderComponent hp = new HeaderComponent(page);
        hp.headerSacbakim();

        PLPPage plp = new PLPPage(page);
        plp.clickFirstItem();

        PDPPage pdp = new PDPPage(page);
        pdp.addToCart();

        assertThat(page.getByText("Ürün sepete başarılı bir şekilde eklendi.")).isVisible();

    }

    @Test(description = "TC_018 - Product Page Checks")
    public void thumbnailCarouselUpdatesMainImage() {
        HeaderComponent hp = new HeaderComponent(page);
        hp.headerSacbakim();

        PLPPage plp = new PLPPage(page);
        plp.clickFirstItem();

        PDPPage pdp = new PDPPage(page);

        Locator mainImage = page.locator("div[style*='background-image']").first();

        String before = mainImage.getAttribute("style");
        pdp.clickNextImage();
        String after = mainImage.getAttribute("style");

        Assert.assertNotEquals(after, before, "Main image did not change after clicking next arrow");

        pdp.openImageLightbox();
        pdp.clickZoomIn();
        pdp.clickZoomOut();
        pdp.closeImageLightbox();

        assertThat(page.locator(".fixed.inset-0.bg-white\\/90")).not().isVisible();

        pdp.comments();
        assertThat(page.getByText("DEĞERLENDİR", new Page.GetByTextOptions().setExact(true))).isVisible();

    }

    @Test(description = "TC_019 - Add/Remove Product to Wishlist (Logged-In User)")
    public void addAndRemoveProductFromWishlistWhenLoggedIn() throws InterruptedException {
        PlaywrightFactory.tearDown();
        page = PlaywrightFactory.initLoggedInPage();
        page.navigate(ConfigReader.baseUrl());
        acceptCookiesIfPresent();

        HeaderComponent hp = new HeaderComponent(page);
        hp.headerSacbakim();
        acceptCookiesIfPresent();

        PLPPage plp = new PLPPage(page);

        product = page.locator("a[href*='-p-']:has(h5)")
                .first().innerText().replace(" ", "-");
        System.out.println("product: " + product);

        plp.addRWishlist1();
        plp.submitButton();

        plp.addRWishlist2();
        plp.submitButton();

        assertThat(page.locator("//*[text()=\"Favori listesine başarılı bir şekilde eklendi.\"]")).isVisible();

        Thread.sleep(1000);

        plp.addRWishlist2();
        assertThat(page.locator("//*[text()=\"Favori listesine başarılı bir şekilde kaldırıldı.\"]")).isVisible();

    }

    @Test(description = "TC_020 - Wishlist Access and Redirection for Guest User")
    public void guestWishlistClickPromptsLogin() {
        HeaderComponent hp = new HeaderComponent(page);

        hp.headerWishist();
        assertThat(page).hasURL(Pattern.compile(".*/login"));

        page.navigate("https://www.gratis.com/");

        hp.headerSacbakim();
        PLPPage plp = new PLPPage(page);
        plp.add1stItem();
        assertThat(page).hasURL(Pattern.compile(".*/login"));

        page.navigate("https://www.gratis.com/");
        hp.headerSacbakim();
        plp.clickFirstItem();

        PDPPage pdp = new PDPPage(page);
        pdp.productWishlist();

        assertThat(page).hasURL(Pattern.compile(".*/login"));

    }

    @Test(description = "TC_021 - Remove Product from Wishlist (Logged-In User)")
    public void removeProductFromWishlistWhenLoggedIn() {
        HeaderComponent hp = new HeaderComponent(page);
        hp.headerWishist();

        WishListPage wp = new WishListPage(page);
        wp.wishFirstItem();







    }
}
