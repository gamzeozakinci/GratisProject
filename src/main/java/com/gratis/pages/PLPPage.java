package com.gratis.pages;

import com.microsoft.playwright.Page;

// TODO: Product Listing Page - shared by category pages, search results, filtered/sorted views
//  - page header text + breadcrumb text
//  - "no results" message visible, results count text
//  - product card count, and reading the first N prices in order (for sort assertions)
//  - filter by brand, filter by price range, reading the active filter badges
//  - change the sort dropdown
//  - open a product card (by index, or by matching title) -> PDP
//  - add-to-cart button directly from a product card
public class PLPPage {

    private final Page page;

    public PLPPage(Page page) {
        this.page = page;
    }
}
