package com.gratis.tests;

import com.gratis.base.BaseTest;
import com.gratis.pages.HomePage;
import com.gratis.pages.PDPPage;
import com.gratis.pages.PLPPage;
import com.gratis.utils.Constants;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NavigationTests extends BaseTest {

    @Test(description = "TC_006 - Mega Menu Category Hover and Redirection (Desktop)",
            groups = Constants.GROUP_NAVIGATION)
    // Runs with the default desktop viewport set in BaseTest/testng.xml
    public void megaMenuHoverAndRedirect() {
        HomePage home = new HomePage(page);
        home.header.hoverMegaMenuCategory("Makyaj");
        home.header.clickMegaMenuLink("Maskara");

        PLPPage plp = new PLPPage(page);
        Assert.assertTrue(plp.currentUrl().contains("maskara"), "URL should point to the Maskara category");
        Assert.assertEquals(plp.headerText(), "Maskara", "PLP header should match the selected sub-category");
        Assert.assertTrue(plp.breadcrumbText().contains("Makyaj") && plp.breadcrumbText().contains("Maskara"),
                "Breadcrumbs should reflect the full category path");
    }

    @Test(description = "TC_007 - Mobile Hamburger Menu Navigation & Accordion Drilldown",
            groups = Constants.GROUP_NAVIGATION)
    // NOTE: this test needs the *mobile* viewport. Run it via the "MobileNavigation"
    // <test> block in testng.xml, which passes viewport=mobile, rather than as a
    // standalone @Test - see testng.xml comments.
    public void mobileHamburgerMenuDrilldown() {
        HomePage home = new HomePage(page);
        home.header.openHamburgerMenu();
        Assert.assertTrue(home.header.isMobileMenuOpen(), "Mobile side menu should open");

        home.header.tapMobileMenuItem("Cilt Bakım");
        home.header.tapMobileMenuItem("Yüz Bakım");
        home.header.tapMobileMenuItem("Yüz Temizleme Jelleri");

        PLPPage plp = new PLPPage(page);
        Assert.assertTrue(plp.productCount() > 0, "PLP should load cleansing gel products");
    }

    @Test(description = "TC_008 - Header Basket Icon Counter Synchronization",
            groups = Constants.GROUP_NAVIGATION)
    public void basketCounterSyncsDynamically() {
        HomePage home = new HomePage(page);
        Assert.assertEquals(home.header.cartBadgeCount(), "0", "Badge should start at 0");

        // Navigate to a PDP - in a fuller build this would go through search/PLP;
        // simplified here by deep-linking a known product slug.
        page.navigate(page.url() + "urun/bee-beauty-sampuan");
        PDPPage pdp = new PDPPage(page);
        pdp.addToCart();

        Assert.assertEquals(home.header.cartBadgeCount(), "1", "Badge should update to 1 without a reload");

        page.reload();
        Assert.assertEquals(home.header.cartBadgeCount(), "1", "Badge count should persist after refresh");
    }

    @Test(description = "TC_009 - Header Logo Redirection from Subpages",
            groups = Constants.GROUP_NAVIGATION)
    public void logoRedirectsHomeFromSubpage() {
        page.navigate(page.url() + "cilt-bakim/yuz-bakim/tonikler-c-5020128");
        HomePage home = new HomePage(page);
        home.header.clickLogo();

        Assert.assertTrue(home.isOnHomepage(), "Clicking the logo should return to the homepage");
        Assert.assertFalse(home.currentUrl().contains("?"), "Homepage URL should be clean, no query params");
    }
}
