package com.gratis.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;

import java.util.regex.Pattern;

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

    // The plain, unforced click on this link is genuinely non-deterministic -
    // confirmed live (and again via a real automated-run stack trace showing
    // "element is not stable" / "element is outside of the viewport" /
    // a persistent overlay intercepting pointer events, alternating across
    // retries). Force-clicking and verifying the navigation actually
    // happened, retried, is the only version that's held up.
    public void headerSacbakim() {
        Locator link = page.locator("//a[text()=\"Saç Bakım\"]").first();
        for (int attempt = 1; attempt <= 3; attempt++) {
            link.click(new Locator.ClickOptions().setForce(true));
            page.mouse().move(0, 0);
            try {
                page.waitForURL(Pattern.compile(".*sac-bakim.*"), new Page.WaitForURLOptions().setTimeout(3000));
                return;
            } catch (TimeoutError e) {
                if (attempt == 3) {
                    throw e;
                }
            }
        }
    }

    public void search() {
        page.locator("#search-bar input:visible").fill("göz");
    }

    public void invalidSearch() {
        page.locator("#search-bar input:visible").fill("asdfghjl");
    }

    public Locator searchSuggestions() {
        Locator items = page.locator("//ul[@class=\"-mx-5 flex flex-col divide-y divide-gray-100 border-b border-gray-100 " +
                "lg:mx-0 lg:divide-y-0 lg:border-b-0\"]//li");

        return items;
    }

    // 4 elements share this href - a real, currently-visible header icon
    // (desktop or mobile variant, toggled by CSS breakpoint classes like
    // "hidden lg:block") plus 2 zero-size "Favorilerim" text links inside a
    // closed dropdown - confirmed live. .first() picked one of the
    // zero-size ones, causing every click to time out ("element is not
    // visible") no matter how long we waited. :visible picks whichever
    // match is actually rendered at the current viewport.
    public void headerWishist() {
        page.locator("a[href='/my-account/wishlist']:visible").first().click();

    }

    public void headerHesabim() {
        page.locator("//*[text()='Hesabım']").click();

    }

    public void hesabimLogOut() {
        page.getByText("Çıkış Yap").first().click();

    }

}
