package com.gratis.base;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parent for every Page Object. Wraps the small set of actions every page needs
 * so individual page classes stay focused on locators + business flows, not on
 * raw Playwright wait/assert boilerplate.
 */
public abstract class BasePage {

    protected final Page page;
    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected BasePage(Page page) {
        this.page = page;
    }

    protected void click(Locator locator) {
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        locator.click();
    }

    protected void type(Locator locator, String text) {
        locator.waitFor();
        locator.fill(text);
    }

    protected String textOf(Locator locator) {
        locator.waitFor();
        return locator.innerText().trim();
    }

    protected boolean isVisible(Locator locator) {
        try {
            locator.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(5000));
            return locator.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    protected void waitForUrlContains(String fragment) {
        page.waitForURL(url -> url.contains(fragment));
    }

    public String currentUrl() {
        return page.url();
    }
}
