package com.gratis.tests;

import com.gratis.base.BaseTest;
import com.gratis.pages.HeaderComponent;
import com.gratis.pages.PLPPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FilterSortTests extends BaseTest {

    @Test(description = "TC_012 - Product Filtering by Brand and Price Range on PLP")
    public void filterByBrandAndPriceRange() {
        HeaderComponent header = new HeaderComponent(page);

        header.headerSacbakim();



    }

    @Test(description = "TC_013 - Product Sorting by Price and Sales Volume")
    public void sortByPriceLowToHighAndHighToLow() {
        HeaderComponent header = new HeaderComponent(page);
        header.headerSacbakim();

        PLPPage plp = new PLPPage(page);
        plp.filtrele();
        plp.filterCokSatan();

        assertThat(page).hasURL("salesCount_desc");

        plp.filtrele();
        plp.filterFiyatArtan();

        assertThat(page).hasURL("discountedPrice_asc");


    }
}
