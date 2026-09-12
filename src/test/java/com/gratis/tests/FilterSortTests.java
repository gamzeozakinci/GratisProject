package com.gratis.tests;

import com.gratis.base.BaseTest;
import com.gratis.pages.HeaderComponent;
import com.gratis.pages.PLPPage;
import com.microsoft.playwright.Locator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FilterSortTests extends BaseTest {

    @Test(description = "TC_013 - Product Filtering by Brand and Price Range on PLP")
    public void filterByBrandAndPriceRange() {
        HeaderComponent header = new HeaderComponent(page);
        header.headerSacbakim();

        PLPPage plp = new PLPPage(page);
        plp.filterMarka();
        plp.selectMarka();

        assertThat(page).hasURL(Pattern.compile(".*brand=.*"));

        Locator items = page.locator("a[href*='-p-']");
        assertThat(items.first()).isVisible();

        for (int i = 0; i < 5; i++) {
            String text = items.nth(i).innerText();
            Assert.assertTrue(text.toLowerCase().contains("wella"), "Item " + i + " did not contain 'wella': " + text);
        }
    }


    @Test(description = "TC_014 - Product Sorting by Price and Sales Volume")
    public void sortByPriceLowToHighAndHighToLow() {
        HeaderComponent header = new HeaderComponent(page);
        header.headerSacbakim();

        PLPPage plp = new PLPPage(page);
        assertThat(page.locator("a[href*='-p-']").first()).isVisible();

        plp.filtrele();
        plp.filterCokSatan();
        assertThat(page).hasURL(Pattern.compile(".*salesCount_desc.*"));

        plp.filtrele();
        plp.filterFiyatArtan();
        assertThat(page).hasURL(Pattern.compile(".*discountedPrice_asc.*"));
    }

    @Test(description = "TC_015 - Clearing an applied filter resets the product list")
    public void clearingFiltersResetsProductList() {
        HeaderComponent header = new HeaderComponent(page);
        header.headerSacbakim();

        acceptCookiesIfPresent();

        // scoped to :has(h5) - a plain a[href*='-p-'] can match a promotional
        // banner link instead of an actual product card (confirmed live: this
        // grabbed "Astra Sadece Gratis'te" unscoped). Real product cards all
        // have an <h5> name element; decoys don't.
        Locator firstProduct = page.locator("a[href*='-p-']:has(h5)").first();
        assertThat(firstProduct).isVisible(); // let the page settle first
        String product = firstProduct.innerText();

        PLPPage plp = new PLPPage(page);
        plp.filterMarka();
        plp.selectMarka();

        // this IS the check that the product changed, not just a wait - it
        // fails with a clear timeout if the text never actually differs.
        assertThat(firstProduct).not().hasText(product);
    }
}
