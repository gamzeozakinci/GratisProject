package com.gratis.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class PLPPage {

    private final Page page;

    public PLPPage(Page page) {
        this.page = page;
    }


    public void add1stItem() {
        page.locator("a[href*='-p-']").first()
                .locator("xpath=ancestor::div[contains(@class,'relative')][1]")
                .locator("button[aria-label='button']")
                .click();

    }

    public String check1stItem() {
        return page.locator("//span[text()=\"1\"]").first().innerText();

    }

    public void add2ndItem() {
        page.locator("a[href*='-p-']").nth(1)
                .locator("xpath=ancestor::div[contains(@class,'relative')][1]")
                .locator("button[aria-label='button']")
                .click();


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
        // scoped to :has(h5) - a plain a[href*='-p-'] can match a promotional
        // banner link instead of a real product card (confirmed live and again
        // via a real test failure: this matched "Astra Sadece Gratis'te", whose
        // href happens to contain "-p-" too, sitting in a carousel wrapper that
        // permanently covers it - hence the endless "intercepts pointer events"
        // retries). Real product cards all have an <h5> name element; decoys don't.
        page.locator("a[href*='-p-']:has(h5)").first().click();

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
        page.getByText("Kaydet", new Page.GetByTextOptions().setExact(true)).click();

    }

    public void listingWishlist() {
        page.locator("span.hover\\:scale-110").first().click();

    }


}