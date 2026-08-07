package com.gratis.tests;

import com.gratis.base.BaseTest;
import com.gratis.config.ConfigReader;
import com.gratis.pages.PLPPage;
import com.gratis.utils.Constants;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class FilterSortTests extends BaseTest {

    @Test(description = "TC_013 - Product Filtering by Brand and Price Range on PLP",
            groups = Constants.GROUP_SEARCH)
    public void filterByBrandAndPriceRange() {
        page.navigate(ConfigReader.baseUrl() + "cilt-bakim");
        PLPPage plp = new PLPPage(page);

        plp.filterByBrand("Bee Beauty");
        plp.filterByPriceRange("50", "250");

        List<Double> prices = plp.firstNPricesInOrder(plp.productCount());
        for (double price : prices) {
            Assert.assertTrue(price >= 50 && price <= 250,
                    "Every listed price should fall within 50-250 TL, found " + price);
        }

        List<String> badges = plp.activeFilterBadgeTexts();
        Assert.assertTrue(badges.stream().anyMatch(b -> b.contains("Bee Beauty")), "Brand filter badge should be shown");
        Assert.assertTrue(badges.stream().anyMatch(b -> b.contains("50") && b.contains("250")), "Price filter badge should be shown");
    }

    @Test(description = "TC_014 - Product Sorting by Price and Sales Volume", groups = Constants.GROUP_SEARCH)
    public void sortByPriceLowToHighAndHighToLow() {
        page.navigate(ConfigReader.baseUrl() + "sac-bakim");
        PLPPage plp = new PLPPage(page);

        plp.sortBy("Fiyata Göre En Düşük");
        List<Double> ascending = plp.firstNPricesInOrder(3);
        Assert.assertTrue(ascending.get(0) <= ascending.get(1) && ascending.get(1) <= ascending.get(2),
                "Prices should be non-decreasing when sorted low to high: " + ascending);

        plp.sortBy("Fiyata Göre En Yüksek");
        List<Double> descending = plp.firstNPricesInOrder(3);
        Assert.assertTrue(descending.get(0) >= descending.get(1) && descending.get(1) >= descending.get(2),
                "Prices should be non-increasing when sorted high to low: " + descending);
    }
}
