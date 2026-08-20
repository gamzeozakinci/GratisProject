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
                .setHeadless(ConfigReader.getBoolean("headless")));

        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(new ViewportSize(
                        ConfigReader.getInt("desktop.viewport.width"),
                        ConfigReader.getInt("desktop.viewport.height")))
                .setLocale("tr-TR"));
        context.setDefaultTimeout(ConfigReader.getInt("default.timeout"));

        page = context.newPage();
        log.info("Browser launched");
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
