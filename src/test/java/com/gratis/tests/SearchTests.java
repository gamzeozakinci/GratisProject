package com.gratis.tests;

import com.gratis.base.BaseTest;
import com.gratis.pages.HeaderComponent;
import com.gratis.pages.PDPPage;
import com.gratis.pages.PLPPage;
import com.microsoft.playwright.Locator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class SearchTests extends BaseTest {

    @Test(description = "TC_009 - Product Search using Autocomplete Suggestions")
    public void autocompleteSuggestionsAppearWhileTyping() throws InterruptedException {
        HeaderComponent header = new HeaderComponent(page);

        header.search();

        Locator suggestions = header.searchSuggestions();
        assertThat(suggestions.first()).isVisible();

        int count = header.searchSuggestions().count();

        for (int i = 0; i < count; i++) {
            String text = header.searchSuggestions().nth(i).innerText();
            Assert.assertTrue(text.toLowerCase().contains("göz"), "Item " + i + " did not contain 'Göz': " + text);
        }
    }

    @Test(description = "TC_010 - Product Search with Valid Keyword")
    public void searchWithValidKeywordShowsResults() {

        HeaderComponent header = new HeaderComponent(page);

        header.search();
        page.keyboard().press("Enter");

        PLPPage plp = new PLPPage(page);

        plp.checkSearchWord();

        assertThat(page.locator("//h1[text()=\"“göz“\"]")).hasText("“göz“");

        plp.clickFirstItem();

        assertThat(page.locator("div.overflow-x-auto.no-scrollbar"))
                .hasText(Pattern.compile(".*göz.*", Pattern.CASE_INSENSITIVE));

    }

}
