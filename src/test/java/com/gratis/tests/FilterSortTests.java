package com.gratis.tests;

import com.gratis.base.BaseTest;
import com.gratis.pages.HeaderComponent;
import org.testng.annotations.Test;

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
    }
}
