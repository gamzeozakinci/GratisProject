package com.gratis.driver;

import com.gratis.config.ConfigReader;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.ViewportSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Owns Playwright / Browser / BrowserContext / Page per test thread.
 *
 * Everything is ThreadLocal so `mvn test -DforkCount / parallel="methods"` in testng.xml
 * is safe out of the box. This is the Playwright equivalent of the
 * ThreadLocal&lt;WebDriverWait&gt; pattern used in the Selenium/Cucumber project -
 * one browser context per thread, never shared, always torn down in tearDown().
 */
public final class PlaywrightFactory {

    private static final Logger log = LoggerFactory.getLogger(PlaywrightFactory.class);

    private static final ThreadLocal<Playwright> PLAYWRIGHT = new ThreadLocal<>();
    private static final ThreadLocal<Browser> BROWSER = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> CONTEXT = new ThreadLocal<>();
    private static final ThreadLocal<Page> PAGE = new ThreadLocal<>();

    private PlaywrightFactory() {
    }

    public static Page initDesktopPage() {
        return initPage(ConfigReader.getInt("desktop.viewport.width"),
                ConfigReader.getInt("desktop.viewport.height"));
    }

    public static Page initMobilePage() {
        return initPage(ConfigReader.getInt("mobile.viewport.width"),
                ConfigReader.getInt("mobile.viewport.height"));
    }

    private static Page initPage(int width, int height) {
        Playwright playwright = Playwright.create();
        PLAYWRIGHT.set(playwright);

        Browser browser = launchBrowser(playwright);
        BROWSER.set(browser);

        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(new ViewportSize(width, height))
                .setLocale("tr-TR"));
        context.setDefaultTimeout(ConfigReader.getInt("default.timeout"));
        CONTEXT.set(context);

        Page page = context.newPage();
        PAGE.set(page);

        log.info("Initialized {}x{} page on thread {}", width, height, Thread.currentThread().getId());
        return page;
    }

    private static Browser launchBrowser(Playwright playwright) {
        String browserName = ConfigReader.get("browser").toLowerCase();
        boolean headless = ConfigReader.getBoolean("headless");
        double slowMo = Double.parseDouble(ConfigReader.get("slow.mo", "0"));

        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(headless)
                .setSlowMo(slowMo);

        return switch (browserName) {
            case "firefox" -> playwright.firefox().launch(options);
            case "webkit" -> playwright.webkit().launch(options);
            default -> playwright.chromium().launch(options);
        };
    }

    public static Page getPage() {
        Page page = PAGE.get();
        if (page == null) {
            throw new IllegalStateException("Page not initialized on this thread. Call initDesktopPage()/initMobilePage() first.");
        }
        return page;
    }

    public static void tearDown() {
        try {
            if (CONTEXT.get() != null) CONTEXT.get().close();
            if (BROWSER.get() != null) BROWSER.get().close();
            if (PLAYWRIGHT.get() != null) PLAYWRIGHT.get().close();
        } finally {
            PAGE.remove();
            CONTEXT.remove();
            BROWSER.remove();
            PLAYWRIGHT.remove();
            log.info("Tore down browser resources on thread {}", Thread.currentThread().getId());
        }
    }
}
