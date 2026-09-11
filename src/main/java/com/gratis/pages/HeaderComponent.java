package com.gratis.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

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

    public void headerWishist() {
        page.locator("//a[@href=\"/my-account/wishlist\"]").first().click();

    }

    public void headerHesabim() {
        page.locator("//*[text()='Hesabım']").click();

    }

    public void hesabimLogOut() {
        page.locator("Çıkış Yap").click();

    }

}
