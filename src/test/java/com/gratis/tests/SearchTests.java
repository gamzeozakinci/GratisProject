package com.gratis.tests;

import com.gratis.base.BaseTest;
import com.gratis.pages.HeaderComponent;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SearchTests extends BaseTest {

    @Test(description = "TC_009 - Product Search using Autocomplete Suggestions")
    public void autocompleteSuggestionsAppearWhileTyping() {
        HeaderComponent header = new HeaderComponent(page);

        header.search();

        int count = header.searchSuggestions().count();

        for (int i = 0; i < count; i++) {
            String text = header.searchSuggestions().nth(i).innerText();
            Assert.assertTrue(text.contains("Göz"), "Item " + i + " did not contain 'Göz': " + text);
        }
    }

    @Test(description = "TC_010 - Product Search with Valid Keyword")
    public void searchWithValidKeywordShowsResults() {

    }

    @DataProvider(name = "injectionPayloads")
    public Object[][] injectionPayloads() {
        return new Object[][]{
                {"' OR 1=1 --"},
                {"<script>alert('xss')</script>"},
                {"% & $ # @ ( ) _ + = ?"}
        };
    }

    @Test(description = "TC_011 - Product Search with Special Characters (SQLi / XSS Check)",
            dataProvider = "injectionPayloads")
    public void searchHandlesMaliciousPayloadsSafely(String payload) {
        // TODO: implement
    }
}
