package com.gratis.base;

import com.gratis.config.ConfigReader;
import com.gratis.driver.PlaywrightFactory;
import com.microsoft.playwright.Locator;
import org.testng.annotations.BeforeMethod;

/**
 * For test classes where every test needs to already be logged in (e.g.
 * CartTests - this site requires a session for anything cart-related).
 * Extend this instead of BaseTest; everything else (screenshots on failure,
 * teardown) is inherited unchanged.
 */
public abstract class LoggedInBaseTest extends BaseTest {

    @Override
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        page = PlaywrightFactory.initLoggedInPage();
        page.navigate(ConfigReader.baseUrl());
        log.info("Navigated to {} (logged in)", ConfigReader.baseUrl());

        acceptCookiesIfPresent();
    }

    // Call this again anywhere later in a test where the banner is seen to
    // reappear (e.g. after navigating deeper into checkout) - it only clicks
    // if the banner is actually showing, so it's always safe to call.
    protected void acceptCookiesIfPresent() {
        Locator cookieBanner = page.locator("#banner-accept-button");
        if (cookieBanner.isVisible()) {
            cookieBanner.click();
        }
    }
}
