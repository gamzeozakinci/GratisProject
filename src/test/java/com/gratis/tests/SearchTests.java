package com.gratis.tests;

import com.gratis.base.BaseTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SearchTests extends BaseTest {

    @Test(description = "TC_010 - Product Search using Autocomplete Suggestions")
    public void autocompleteSuggestionsAppearWhileTyping() {
        // TODO: implement
    }

    @Test(description = "TC_011 - Product Search with Valid Keyword")
    public void searchWithValidKeywordShowsResults() {
        // TODO: implement
    }

    @DataProvider(name = "injectionPayloads")
    public Object[][] injectionPayloads() {
        return new Object[][]{
                {"' OR 1=1 --"},
                {"<script>alert('xss')</script>"},
                {"% & $ # @ ( ) _ + = ?"}
        };
    }

    @Test(description = "TC_012 - Product Search with Special Characters (SQLi / XSS Check)",
            dataProvider = "injectionPayloads")
    public void searchHandlesMaliciousPayloadsSafely(String payload) {
        // TODO: implement
    }
}
