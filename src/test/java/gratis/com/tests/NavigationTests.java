package gratis.com.tests;

import com.gratis.base.BaseTest;
import com.gratis.config.ConfigReader;
import com.gratis.driver.PlaywrightFactory;
import com.gratis.pages.HeaderComponent;
import com.gratis.pages.PLPPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class NavigationTests extends BaseTest {

    @Test(description = "TC_006 - Mega Menu Category Hover and Redirection (Desktop)", groups = "smoke")
    public void megaMenuHoverAndRedirect() {
        HeaderComponent hp = new HeaderComponent(page);

        hp.headerMakyaj();
        hp.hoverTORuj();

        assertThat(page).hasURL(ConfigReader.baseUrl() + "makyaj/ruj-c-5010101");

    }

    @Test(description = "TC_007 - Mobile Hamburger Menu Navigation & Accordion Drilldown")
    public void mobileHamburgerMenuDrilldown() {
        PlaywrightFactory.tearDown();
        page = PlaywrightFactory.initMobilePage();
        page.navigate(ConfigReader.baseUrl());

        HeaderComponent hp = new HeaderComponent(page);

        hp.mobileAcceptCookies();
        hp.mobileHeader();
        hp.mobileHeaderCiltbakim();
        hp.mobileHeaderYuzbakim();
        hp.mobileHeaderTonikler();

        assertThat(page.getByText("Tonikler")).isVisible();

    }

    @Test(description = "TC_008 - Header Basket Icon Counter Synchronization")
    public void basketCounterSyncsDynamically() {
        PlaywrightFactory.tearDown();
        page = PlaywrightFactory.initLoggedInPage();
        page.navigate(ConfigReader.baseUrl());

        HeaderComponent hp = new HeaderComponent(page);
        PLPPage plp = new PLPPage(page);

        hp.headerSacbakim();

        plp.add1stItem();
        Assert.assertEquals(plp.check1stItem(), "1");

        plp.add2ndItem();
        Assert.assertEquals(plp.check2ndItem(), "2");

    }

    @Test(description = "TC_009 - Header Logo Redirection from Subpages")
    public void logoRedirectsHomeFromSubpage() {
        HeaderComponent hp = new HeaderComponent(page);
        hp.headerSacbakim();

        PLPPage plp = new PLPPage(page);
        plp.logo();

        assertThat(page).hasURL(ConfigReader.baseUrl());

    }
}
