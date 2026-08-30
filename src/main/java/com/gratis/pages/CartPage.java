package com.gratis.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class CartPage {

    private final Page page;

    public CartPage(Page page) {
        this.page = page;
    }

    public void cartButton() {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("\"/cart\"")).click();

    }

    public void logoCart() {
        page.locator("//a[@href=\"/\"]").first().click();

    }

    public void clearCart() {
        page.getByText("TÜMÜNÜ SİL").click();

    }

    public void tamam() {
        page.getByText("TAMAM").click();

    }


}
