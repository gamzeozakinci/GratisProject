package com.gratis.tests;

import com.gratis.base.BaseTest;
import com.gratis.pages.HeaderComponent;
import com.gratis.pages.PDPPage;
import com.gratis.pages.PLPPage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CatalogTests extends BaseTest {

    @Test(description = "TC_014 - Verify Product Detail Page Information Layout")
    public void pdpLayoutShowsAllRequiredElements() {
        HeaderComponent header = new HeaderComponent(page);
        header.headerSacbakim();

        PLPPage plp = new PLPPage(page);
        plp.clickFirstItem();

        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName(plp.clickFirstItemName())));

    }

    @Test(description = "TC_015 - Verify Product Stock Status (In-Stock vs Out-of-Stock)")
    public void stockStatusReflectsAvailability() {

        //giriş gerekiyor

    }

    @Test(description = "TC_016 - Product Page Checks")
    public void thumbnailCarouselUpdatesMainImage() {
        HeaderComponent header = new HeaderComponent(page);
        header.headerSacbakim();

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

        assertThat(page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("button"))).isVisible();


        pdp.comments();
        assertThat(page.locator("#comments-list")).isInViewport();

    }

    @Test(description = "TC_017 - Add/Remove Product to Wishlist (Logged-In User)")
    public void addAndRemoveProductFromWishlistWhenLoggedIn() {
        //giriş gerekiyor
    }

    @Test(description = "TC_018 - Wishlist Access and Redirection for Guest User")
    public void guestWishlistClickPromptsLogin() {
        HeaderComponent header = new HeaderComponent(page);

        header.headerWishist();
        assertThat(page).hasURL(Pattern.compile(".*/login"));

        page.navigate("https://www.gratis.com/");

        header.headerSacbakim();
        PLPPage plp = new PLPPage(page);
        plp.listingWishlist();
        assertThat(page).hasURL(Pattern.compile(".*/login"));

        page.navigate("https://www.gratis.com/");
        header.headerSacbakim();
        plp.clickFirstItem();

        PDPPage pdp = new PDPPage(page);
        pdp.productWishlist();

        assertThat(page).hasURL(Pattern.compile(".*/login"));

    }
}
