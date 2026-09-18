package gratis.com.tests;

import com.gratis.base.BaseTest;
import com.gratis.pages.HeaderComponent;
import com.gratis.pages.PLPPage;
import com.microsoft.playwright.Locator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class SearchTests extends BaseTest {

    @Test(description = "TC_010 - Product Search using Autocomplete Suggestions")
    public void autocompleteSuggestionsAppearWhileTyping() {
        HeaderComponent hp = new HeaderComponent(page);

        hp.search();

        Locator suggestions = hp.searchSuggestions();
        assertThat(suggestions.first()).isVisible();

        int count = hp.searchSuggestions().count();

        for (int i = 0; i < count; i++) {
            String text = hp.searchSuggestions().nth(i).innerText();
            Assert.assertTrue(text.toLowerCase().contains("göz"), "Item " + i + " did not contain 'Göz': " + text);
        }
    }

    @Test(description = "TC_011 - Product Search with Valid Keyword", groups = "smoke")
    public void searchWithValidKeywordShowsResults() {
        HeaderComponent hp = new HeaderComponent(page);

        hp.search();
        page.keyboard().press("Enter");

        PLPPage plp = new PLPPage(page);

        plp.checkSearchWord();

        assertThat(page.locator("//h1[text()=\"“göz“\"]")).hasText("“göz“");

        plp.clickFirstItem();

        assertThat(page.locator("div.overflow-x-auto.no-scrollbar"))
                .hasText(Pattern.compile(".*göz.*", Pattern.CASE_INSENSITIVE));

    }

    @Test(description = "TC_012 - Searching a nonsense keyword shows the empty-results state")
    public void searchWithNoResultsShowsEmptyState() {
        HeaderComponent hp = new HeaderComponent(page);

        hp.invalidSearch();
        page.keyboard().press("Enter");

        assertThat(page.getByText("Sonuç Bulunamadı")).isVisible();

    }
}
