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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class PlaywrightFactory {

    private static final Logger log = LoggerFactory.getLogger(PlaywrightFactory.class);

    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext context;
    private static Page page;

    // Saved login session (cookies + localStorage) for tests that need to already
    // be logged in - see SessionCaptureTests, which is what actually creates this
    // file. Gitignored: it holds a real, valid session for a real account.
    private static final Path AUTH_STATE_PATH = Path.of("src/test/resources/auth-state.json");

    private PlaywrightFactory() {
    }

    public static Page initPage() {
        playwright = Playwright.create();

        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(ConfigReader.getBoolean("headless"))
                .setArgs(List.of("--start-maximized")));

        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(null)
                .setLocale("tr-TR"));
        context.setDefaultTimeout(ConfigReader.getInt("default.timeout"));

        page = context.newPage();
        log.info("Browser launched");
        return page;
    }

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

    public static Page initLoggedInPage() {
        playwright = Playwright.create();

        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(ConfigReader.getBoolean("headless"))
                .setArgs(List.of("--start-maximized")));

        Browser.NewContextOptions options = new Browser.NewContextOptions()
                .setViewportSize(null)
                .setLocale("tr-TR");

        if (Files.exists(AUTH_STATE_PATH)) {
            options.setStorageStatePath(AUTH_STATE_PATH);
        } else {
            log.warn("No saved login at {} - run SessionCaptureTests first", AUTH_STATE_PATH);
        }

        context = browser.newContext(options);
        context.setDefaultTimeout(ConfigReader.getInt("default.timeout"));

        page = context.newPage();
        log.info("Browser launched (reusing saved login, if present)");
        return page;
    }

    public static void saveLoginState() {
        context.storageState(new BrowserContext.StorageStateOptions().setPath(AUTH_STATE_PATH));
        log.info("Saved login session to {}", AUTH_STATE_PATH);
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
