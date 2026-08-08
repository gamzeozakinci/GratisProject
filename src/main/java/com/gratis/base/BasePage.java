package com.gratis.base;

import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parent for every Page Object. Holds only what every page needs - the shared Page
 * (one per test) and a logger. There are no click()/type()/isVisible() wrapper
 * methods here on purpose: each Page Object calls Playwright's own Locator/Page
 * methods directly (locator.click(), locator.fill(), locator.isVisible(), ...), so
 * what you read in the page classes is the real Playwright API, not a custom layer
 * on top of it.
 */
public abstract class BasePage {

    protected final Page page;
    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected BasePage(Page page) {
        this.page = page;
    }

    /**
     * page.url() itself is a one-liner, but `page` is protected, so test classes
     * (a different package) can't call it directly on a Page Object - this just
     * exposes it.
     */
    public String currentUrl() {
        return page.url();
    }
}
