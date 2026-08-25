package com.gratis.driver;

import com.gratis.config.ConfigReader;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.ViewportSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Owns the single Playwright / Browser / BrowserContext / Page used by whichever test
 * is currently running. Plain static fields, not ThreadLocal - simple, but only safe
 * for tests running one at a time (no parallel="..." in testng.xml). If you ever want
 * parallel execution, each thread would need its own browser instead of sharing these
 * fields, which is what ThreadLocal is for.
 */
public final class PlaywrightFactory {

    private static final Logger log = LoggerFactory.getLogger(PlaywrightFactory.class);

    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext context;
    private static Page page;

    private PlaywrightFactory() {
    }

    public static Page initPage() {
        playwright = Playwright.create();

        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(ConfigReader.getBoolean("headless"))
                .setArgs(List.of("--start-maximized")));

        context = browser.newContext(new Browser.NewContextOptions()
                // null viewport = "use whatever size the actual window ends up at"
                // instead of a fixed size Playwright would otherwise resize the
                // (maximized) window back down to. Only affects headed mode - headless
                // has no real window, so it falls back to Playwright's default viewport.
                .setViewportSize(null)
                .setLocale("tr-TR"));
        context.setDefaultTimeout(ConfigReader.getInt("default.timeout"));

        page = context.newPage();
        log.info("Browser launched");
        return page;
    }

    /**
     * For tests that need a real mobile-sized page (e.g. TC_006's hamburger menu),
     * not the maximized desktop one initPage() gives every other test. Callers must
     * tearDown() the desktop page BaseTest already opened before calling this, then
     * reassign BaseTest.page to what this returns - see NavigationTests for the pattern.
     */
    public static Page initMobilePage() {
        playwright = Playwright.create();

        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(ConfigReader.getBoolean("headless")));

        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(new ViewportSize(390, 844)) // iPhone 12-ish size
                .setIsMobile(true)
                .setHasTouch(true)
                .setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) " +
                        "AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1")
                .setLocale("tr-TR"));
        context.setDefaultTimeout(ConfigReader.getInt("default.timeout"));

        page = context.newPage();
        log.info("Mobile browser launched");
        return page;
    }

    public static void tearDown() {
        if (context != null) {
            context.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
        log.info("Browser closed");
    }
}
