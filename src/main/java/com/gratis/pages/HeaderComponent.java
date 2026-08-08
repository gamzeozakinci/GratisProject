package com.gratis.pages;

import com.gratis.base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * The header/nav is present on almost every page, so it's modeled as a component
 * rather than duplicated inside HomePage/PLPPage/PDPPage/CartPage.
 *
 * NOTE ON LOCATORS: selectors below are written from the acceptance-criteria text in
 * the source test case doc (element names, Turkish labels) since this framework was
 * built against the spec rather than a live DOM inspection session. Swap the raw
 * text/CSS locators for data-testid attributes once you inspect the real site -
 * see README "Next Steps".
 */
public class HeaderComponent extends BasePage {

    public HeaderComponent(Page page) {
        super(page);
    }

    private Locator logo() {
        return page.locator("a.header-logo, a[href='/'] img").first();
    }

    private Locator loginRegisterWidget() {
        return page.getByText("Üye olun ya da Giriş Yapın");
    }

    private Locator accountMenu() {
        return page.getByText("Hesabım");
    }

    private Locator wishlistLink() {
        return page.getByText("Favorilerim");
    }

    private Locator myOrdersLink() {
        return page.getByText("Siparişlerim");
    }

    private Locator searchInput() {
        return page.locator("input[type='search'], input[placeholder*='Ara']");
    }

    private Locator searchSuggestionPanel() {
        return page.locator(".search-suggestions, .autocomplete-panel");
    }

    private Locator cartIcon() {
        return page.locator("a[href='/cart'], .header-cart-icon");
    }

    private Locator cartBadge() {
        return page.locator(".header-cart-icon .badge, .cart-count");
    }

    private Locator hamburgerIcon() {
        return page.locator("button.hamburger-menu, [aria-label='Menu']");
    }

    private Locator mobileMenuPanel() {
        return page.locator(".mobile-menu-panel, .side-drawer");
    }

    private Locator megaMenuCategory(String categoryName) {
        return page.locator("nav.primary-categories").getByText(categoryName, new Locator.GetByTextOptions().setExact(false));
    }

    // ---- Actions ----

    public void clickLogo() {
        logo().click();
    }

    public void openLoginOrRegister() {
        loginRegisterWidget().click();
    }

    public boolean isUserLoggedIn(String expectedDisplayName) {
        return page.getByText(expectedDisplayName).isVisible();
    }

    public void goToWishlist() {
        accountMenu().click();
        wishlistLink().click();
    }

    public void goToMyOrders() {
        accountMenu().click();
        myOrdersLink().click();
    }

    public void search(String term) {
        Locator input = searchInput();
        input.click();
        input.pressSequentially(term, new Locator.PressSequentiallyOptions().setDelay(120));
    }

    public void submitSearch() {
        page.keyboard().press("Enter");
    }

    public boolean isSuggestionPanelVisible() {
        return searchSuggestionPanel().isVisible();
    }

    public String cartBadgeCount() {
        return cartBadge().isVisible() ? cartBadge().innerText().trim() : "0";
    }

    public void openCart() {
        cartIcon().click();
    }

    public void openHamburgerMenu() {
        hamburgerIcon().click();
    }

    public boolean isMobileMenuOpen() {
        return mobileMenuPanel().isVisible();
    }

    public void hoverMegaMenuCategory(String categoryName) {
        megaMenuCategory(categoryName).hover();
    }

    public void clickMegaMenuLink(String linkText) {
        page.getByText(linkText, new Page.GetByTextOptions().setExact(true)).click();
    }

    public void tapMobileMenuItem(String label) {
        mobileMenuPanel().getByText(label).click();
    }
}
