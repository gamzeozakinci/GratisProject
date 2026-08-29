package com.gratis.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

// TODO: Product Listing Page - shared by category pages, search results, filtered/sorted views
//  - page header text + breadcrumb text
//  - "no results" message visible, results count text
//  - product card count, and reading the first N prices in order (for sort assertions)
//  - filter by brand, filter by price range, reading the active filter badges
//  - change the sort dropdown
//  - open a product card (by index, or by matching title) -> PDP
//  - add-to-cart button directly from a product card
public class PLPPage {

    private final Page page;

    public PLPPage(Page page) {
        this.page = page;
    }


    public void add1stItem() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("button")).first().click();
    }

    public String check1stItem() {
        return page.locator("//span[text()=\"1\"]").first().innerText();
    }

    public void add2ndItem() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("button")).nth(1).click();
    }

    public String check2ndItem() {
        return page.locator("//span[text()=\"2\"]").first().innerText();
    }

    public void logo() {
        page.locator("//a[@href=\"/\"]").first().click();

    }

    public void checkSearchWord() {
        page.locator("//h1[text()=\"“göz“\"]");

    }

    public void clickFirstItem() {
        page.locator("a[href*='-p-']").first().click();

    }

    public void filtrele() {
        page.locator("[aria-label='sort-select']").click();
    }

    public void filterCokSatan() {
        page.locator("//*[text()=\"Çok Satanlar\"]").click();
    }

    public void filterFiyatArtan() {
        page.locator("//*[text()=\"Fiyat Artan\"]").click();
    }

    public void filterMarka() {
        page.getByText("MARKA", new Page.GetByTextOptions().setExact(true)).click();
    }

    public void selectMarka() {
        page.locator("div.flex.items-start.gap-1")
                .filter(new Locator.FilterOptions().setHasText("Wella"))
                .locator("div.cursor-pointer").first()
                .click();
    }

    public void submitButton() {
        page.getByText("UYGULA", new Page.GetByTextOptions().setExact(true)).click();
    }

    public String clickFirstItemName() {
        return page.locator("a[href*='-p-']").first().toString();

    }

    public void listingWishlist() {
        page.locator("span.hover\\:scale-110").first().click();

    }


}