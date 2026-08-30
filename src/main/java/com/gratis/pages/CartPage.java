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

    public void increaseQuantity() {
        page.locator(".flex.flex-col.gap-2.justify-center .cursor-pointer").first().click();
        // 1 3 5 gibi tek sayılar + 2 4 6 gibi olan cop veya - anlamına geliyor

    }

    public void decreaseQuantity() {
        page.locator(".flex.flex-col.gap-2.justify-center .cursor-pointer").nth(2).click();

    }

    public void deleteFromCart() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("button")).click();

    }


}
