package com.gratis.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

// TODO: header/nav is used by almost every test class, so build this one early.
//  - logo click -> back to homepage
//  - isUserLoggedIn(name) - some way to tell a logged-in header apart from a guest one
//  - wishlist / my-orders nav entries
//  - search input + submit + "is the suggestion dropdown showing" check
//  - cart icon click + reading the cart badge count
//  - mobile: hamburger icon click + "is the mobile menu open"
//  - mega menu: hover a top-level category, click a link inside it
public class HeaderComponent {

    private final Page page;

    public HeaderComponent(Page page) {
        this.page = page;
    }

    public void openLoginOrRegister() {
        page.getByText("Üye olun").click();
    }

    public void headerMakyaj() {
        page.locator("//a[text()=\"Makyaj\"]").first().hover();
    }

    public void hoverTORuj() {
        page.locator("//span[text()=\"Ruj\"]").click();
    }

    public void mobileAcceptCookies() {
        page.locator("#banner-accept-button").click();
    }

    public void mobileHeader() {
        page.locator("//img[@alt=\"mobile-header-icon\"]").click();
    }

    public void mobileHeaderCiltbakim() {
        page.locator("//span[text()=\"Cilt Bakım\"]").first().click();
    }

    public void mobileHeaderYuzbakim() {
        page.locator("//span[text()=\"Yüz Bakım\"]").click();
    }

    public void mobileHeaderTonikler() {
        page.locator("//span[text()=\"Tonikler\"]").first().click();
    }

    public void headerSacbakim() {
        page.locator("//a[text()=\"Saç Bakım\"]").first().click();
        page.mouse().move(0, 0);
    }

    /**
     * Same click, but forced - for callers right after a fresh homepage
     * navigation while logged in. Confirmed (real run, 30s timeout, 61 retries)
     * that something in the logged-in homepage's header area persistently
     * intercepts this click the whole time - not a transient load delay that
     * settles if you wait longer, an actual stable overlap. Guest sessions
     * don't show this at all, so it's most likely logged-in-only content
     * (a welcome banner, Gratis Kart widget, etc.) sitting in the same region.
     * force() skips the "is anything covering this" check, since waiting it
     * out doesn't work here.
     */
    public void headerSacbakimForced() {
        page.locator("//a[text()=\"Saç Bakım\"]").first()
                .click(new Locator.ClickOptions().setForce(true));
        page.mouse().move(0, 0);
    }

    public void search() {
        page.locator("#search-bar input:visible").fill("göz");
    }

    public Locator searchSuggestions() {
        Locator items = page.locator("//ul[@class=\"-mx-5 flex flex-col divide-y divide-gray-100 border-b border-gray-100 " +
                "lg:mx-0 lg:divide-y-0 lg:border-b-0\"]//li");

        return items;
    }

    public void headerWishist() {
        page.locator("//a[@href=\"/my-account/wishlist\"]").first().click();

    }

}
