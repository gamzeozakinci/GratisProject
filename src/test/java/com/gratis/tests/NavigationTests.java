package com.gratis.tests;

import com.gratis.base.BaseTest;
import com.gratis.config.ConfigReader;
import com.gratis.driver.PlaywrightFactory;
import com.gratis.pages.HeaderComponent;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class NavigationTests extends BaseTest {

    @Test(description = "TC_005 - Mega Menu Category Hover and Redirection (Desktop)")
    public void megaMenuHoverAndRedirect() {

        HeaderComponent header = new HeaderComponent(page);

        header.headerMakyaj();
        header.hoverTORuj();

        assertThat(page).hasURL("https://www.gratis.com/makyaj/ruj-c-5010101");

    }

    @Test(description = "TC_006 - Mobile Hamburger Menu Navigation & Accordion Drilldown")
    public void mobileHamburgerMenuDrilldown() {
        // BaseTest.setUp() already opened a desktop page - close it and swap in a
        // mobile-sized one instead, just for this test.

        PlaywrightFactory.tearDown();
        page = PlaywrightFactory.initMobilePage();
        page.navigate(ConfigReader.baseUrl());

        HeaderComponent header = new HeaderComponent(page);

        header.mobileAcceptCookies();
        header.mobileHeader();
        header.mobileHeaderCiltbakim();
        header.mobileHeaderYuzbakim();
        header.mobileHeaderTonikler();

        assertThat(page.getByText("Tonikler")).isVisible();

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
