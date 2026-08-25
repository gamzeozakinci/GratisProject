package com.gratis.tests;

import com.gratis.base.BaseTest;
import org.testng.annotations.Test;

public class NavigationTests extends BaseTest {

    @Test(description = "TC_005 - Mega Menu Category Hover and Redirection (Desktop)")
    // Runs with the default desktop viewport set in BaseTest/testng.xml
    public void megaMenuHoverAndRedirect() {
        // TODO: implement
    }

    @Test(description = "TC_006 - Mobile Hamburger Menu Navigation & Accordion Drilldown")
    // NOTE: this test needs the *mobile* viewport. Run it via the "MobileNavigation"
    // <test> block in testng.xml, which passes viewport=mobile, rather than as a
    // standalone @Test - see testng.xml comments.
    public void mobileHamburgerMenuDrilldown() {
        // TODO: implement
    }

    @Test(description = "TC_007 - Header Basket Icon Counter Synchronization")
    public void basketCounterSyncsDynamically() {
        // TODO: implement
    }

    @Test(description = "TC_008 - Header Logo Redirection from Subpages")
    public void logoRedirectsHomeFromSubpage() {
        // TODO: implement
    }
}
