package com.gratis.tests;

import com.gratis.base.BaseTest;
import com.gratis.pages.HomePage;
import com.gratis.pages.PLPPage;
import com.gratis.utils.Constants;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SearchTests extends BaseTest {

    @Test(description = "TC_010 - Product Search using Autocomplete Suggestions",
            groups = Constants.GROUP_SEARCH)
    public void autocompleteSuggestionsAppearWhileTyping() {
        HomePage home = new HomePage(page);
        home.header.search("Ruj");

        Assert.assertTrue(home.header.isSuggestionPanelVisible(),
                "Suggestion dropdown should appear after the 3rd character");
    }

    @Test(description = "TC_011 - Product Search with Valid Keyword", groups = Constants.GROUP_SEARCH)
    public void searchWithValidKeywordShowsResults() {
        HomePage home = new HomePage(page);
        home.header.search("Sampuan");
        home.header.submitSearch();

        PLPPage results = new PLPPage(page);
        Assert.assertTrue(results.currentUrl().contains("q=Sampuan") || results.currentUrl().contains("/search"),
                "URL should reflect the search query");
        Assert.assertTrue(results.breadcrumbText().contains("Arama Sonuçları"), "Breadcrumb should read Arama Sonuçları");
        Assert.assertTrue(results.productCount() > 0, "Results grid should show shampoo products");
        Assert.assertFalse(results.resultsCountText().isEmpty(), "A results count should be displayed");
    }

    @DataProvider(name = "injectionPayloads")
    public Object[][] injectionPayloads() {
        return new Object[][]{
                {com.gratis.utils.Constants.SQLI_PAYLOAD},
                {com.gratis.utils.Constants.XSS_PAYLOAD},
                {com.gratis.utils.Constants.SPECIAL_SYMBOLS_PAYLOAD}
        };
    }

    @Test(description = "TC_012 - Product Search with Special Characters (SQLi / XSS Check)",
            groups = Constants.GROUP_SEARCH, dataProvider = "injectionPayloads")
    public void searchHandlesMaliciousPayloadsSafely(String payload) {
        HomePage home = new HomePage(page);
        home.header.search(payload);
        home.header.submitSearch();

        PLPPage results = new PLPPage(page);

        // No app crash, no SQL error - the app should render a normal "no results" state
        Assert.assertTrue(results.isNoResultsMessageVisible() || results.productCount() == 0,
                "Malicious payload '" + payload + "' should yield a clean no-results page, not an error");

        // The literal payload text (if echoed back) must be escaped, never executed.
        // A real XSS check also asserts no dialog/alert fired during this navigation -
        // wire a page.onDialog() listener in the fixture if the app under test echoes input.
        Assert.assertFalse(page.content().contains("<script>alert"),
                "Raw script tags must never be reflected unescaped in the page HTML");
    }
}
