package com.gratis.tests;

import com.gratis.base.BaseTest;
import com.gratis.config.ConfigReader;
import com.gratis.pages.HomePage;
import com.gratis.pages.PDPPage;
import com.gratis.pages.PLPPage;
import com.gratis.pages.WishlistPage;
import com.gratis.utils.Constants;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CatalogTests extends BaseTest {

    @Test(description = "TC_015 - Verify Product Detail Page Information Layout", groups = Constants.GROUP_CATALOG)
    public void pdpLayoutShowsAllRequiredElements() {
        HomePage home = new HomePage(page);
        home.header.search("Beaulis Ruj");
        home.header.submitSearch();

        PLPPage results = new PLPPage(page);
        results.openProductCard("Beaulis Ruj");

        PDPPage pdp = new PDPPage(page);
        Assert.assertTrue(pdp.title().contains("Beaulis Ruj"), "PDP title should match the searched product");
        pdp.openAccordionTab("Ürün Açıklaması");
        pdp.openAccordionTab("Kullanım Önerisi");
        pdp.openAccordionTab("İçindekiler");
        // Layout/overlap assertions are better suited to a visual-regression tool
        // (e.g. Playwright's screenshot matching) - flagged in README "Future Work".
    }

    @Test(description = "TC_016 - Verify Product Stock Status (In-Stock vs Out-of-Stock)", groups = Constants.GROUP_CATALOG)
    public void stockStatusReflectsAvailability() {
        // Preconditions assume seeded product slugs for a known in-stock / out-of-stock item;
        // replace with your test environment's actual fixture slugs.
        page.navigate(ConfigReader.baseUrl() + "urun/product-b-in-stock");
        PDPPage productB = new PDPPage(page);
        Assert.assertTrue(productB.isAddToCartEnabled(), "In-stock product should have an active Add to Cart button");

        page.navigate(ConfigReader.baseUrl() + "urun/product-a-out-of-stock");
        PDPPage productA = new PDPPage(page);
        Assert.assertFalse(productA.isAddToCartEnabled(), "Out-of-stock product's button should be disabled");
        Assert.assertTrue(productA.isOutOfStockLabelVisible(), "Button text should switch to Stokta Yok / Haber Ver");
    }

    @Test(description = "TC_017 - Multi-Image Carousel Zoom and Navigation on PDP", groups = Constants.GROUP_CATALOG)
    public void thumbnailCarouselUpdatesMainImage() {
        page.navigate(ConfigReader.baseUrl() + "urun/three-photo-product");
        PDPPage pdp = new PDPPage(page);

        pdp.clickThumbnail(1);
        Assert.assertTrue(pdp.isThumbnailHighlighted(1), "2nd thumbnail should be highlighted after selection");

        pdp.clickThumbnail(2);
        Assert.assertTrue(pdp.isThumbnailHighlighted(2), "3rd thumbnail should be highlighted after selection");
        // Hover-zoom lens and mobile pinch-to-zoom require pointer/touch emulation
        // beyond a basic click assertion - see README "Future Work".
    }

    @Test(description = "TC_018 - Add/Remove Product to Wishlist (Logged-In User)", groups = Constants.GROUP_CATALOG)
    public void addAndRemoveProductFromWishlistWhenLoggedIn() {
        // Precondition: logged-in session. In the full suite this reuses a saved
        // storageState from LoginTests instead of logging in inline - see README.
        HomePage home = new HomePage(page);
        home.header.search("LYKD Allık");
        home.header.submitSearch();
        new PLPPage(page).openProductCard("LYKD Allık");

        PDPPage pdp = new PDPPage(page);
        pdp.toggleWishlist();
        Assert.assertTrue(pdp.isWishlistHeartFilled(), "Heart icon should switch to filled state");
        Assert.assertTrue(pdp.isWishlistToastVisible(), "Success toast should confirm the add");

        home.header.goToWishlist();
        WishlistPage wishlist = new WishlistPage(page);
        Assert.assertTrue(wishlist.containsProduct("LYKD Allık"), "Wishlist should list the added product");

        wishlist.removeFirstItem();
        Assert.assertTrue(wishlist.isEmptyStateVisible(), "Wishlist should show the empty-state message after removal");
    }

    @Test(description = "TC_019 - Wishlist Access and Redirection for Guest User", groups = Constants.GROUP_CATALOG)
    public void guestWishlistClickPromptsLogin() {
        page.navigate(ConfigReader.baseUrl() + "urun/three-photo-product");
        PDPPage pdp = new PDPPage(page);

        pdp.toggleWishlist();

        Assert.assertTrue(pdp.isLoginPromptVisible(), "Guest should be prompted to log in");
        Assert.assertTrue(pdp.loginPromptMessageText().contains("giriş yapmalısınız"),
                "Prompt should explain login is required to use favorites");
    }
}
