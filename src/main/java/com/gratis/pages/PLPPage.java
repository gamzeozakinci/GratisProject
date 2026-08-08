package com.gratis.pages;

import com.gratis.base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.List;

/**
 * Product Listing Page: category pages, search results pages and filtered/sorted
 * views all share this template on gratis.com, so one page object covers
 * TC_006 (mega menu redirect target), TC_009 (deep-linked category), TC_010-012
 * (search + autocomplete), TC_013-014 (filter + sort).
 */
public class PLPPage extends BasePage {

    public PLPPage(Page page) {
        super(page);
    }

    private Locator plpHeader() { return page.locator("h1.plp-title"); }
    private Locator breadcrumbs() { return page.locator(".breadcrumbs"); }
    private Locator resultsCount() { return page.locator(".results-count"); }
    private Locator noResultsMessage() { return page.getByText("Aramanıza uygun sonuç bulunamadı."); }
    private Locator productCards() { return page.locator(".product-card"); }
    private Locator productPrices() { return page.locator(".product-card .price"); }

    // Filters
    private Locator brandFilterAccordion() { return page.getByText("Marka"); }
    private Locator brandCheckbox(String brand) { return page.locator("label").filter(new Locator.FilterOptions().setHasText(brand)); }
    private Locator priceFilterAccordion() { return page.getByText("Fiyat"); }
    private Locator priceMinInput() { return page.locator("input[name='priceMin']"); }
    private Locator priceMaxInput() { return page.locator("input[name='priceMax']"); }
    private Locator applyPriceButton() { return page.getByText("Uygula"); }
    private Locator activeFilterBadges() { return page.locator(".filter-badge"); }

    // Sort
    private Locator sortDropdown() { return page.locator(".sort-dropdown, select[name='sort']"); }

    public String headerText() {
        return plpHeader().innerText().trim();
    }

    public String breadcrumbText() {
        return breadcrumbs().innerText().trim();
    }

    public boolean isNoResultsMessageVisible() {
        return noResultsMessage().isVisible();
    }

    public String resultsCountText() {
        return resultsCount().isVisible() ? resultsCount().innerText().trim() : "";
    }

    public int productCount() {
        return productCards().count();
    }

    public List<Double> firstNPricesInOrder(int n) {
        return productPrices().all().stream()
                .limit(n)
                .map(l -> parsePrice(l.innerText()))
                .toList();
    }

    private double parsePrice(String raw) {
        // e.g. "129,90 TL" -> 129.90
        String cleaned = raw.replace("TL", "").trim().replace(".", "").replace(",", ".");
        return Double.parseDouble(cleaned);
    }

    public void filterByBrand(String brand) {
        brandFilterAccordion().click();
        brandCheckbox(brand).click();
    }

    public void filterByPriceRange(String min, String max) {
        priceFilterAccordion().click();
        priceMinInput().fill(min);
        priceMaxInput().fill(max);
        applyPriceButton().click();
    }

    public List<String> activeFilterBadgeTexts() {
        return activeFilterBadges().allInnerTexts();
    }

    public void sortBy(String optionLabel) {
        sortDropdown().selectOption(new com.microsoft.playwright.options.SelectOption().setLabel(optionLabel));
        page.waitForLoadState();
    }

    public void openProductCard(int index) {
        productCards().nth(index).click();
    }

    public void openProductCard(String titleContains) {
        page.locator(".product-card").filter(new Locator.FilterOptions().setHasText(titleContains)).first().click();
    }

    public void addFirstProductToCart() {
        productCards().first().getByRole(AriaRole.BUTTON,
                new Locator.GetByRoleOptions().setName("Sepete Ekle")).click();
    }
}
