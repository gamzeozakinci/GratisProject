package com.gratis.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class CheckoutPage {

    private final Page page;

    public CheckoutPage(Page page) {
        this.page = page;
    }

    public void fromStore() {
        page.getByRole(AriaRole.RADIOGROUP, new Page.GetByRoleOptions().setName("Mağazadan Teslim Alacağım (ÜCRETSİZ)")).click();
    }

    public void toAddress() {
        page.getByRole(AriaRole.RADIOGROUP, new Page.GetByRoleOptions().setName("Kargo (Adrese Teslim)")).click();
    }

    public void toCheckout() {
        page.getByText("ÖDEME ADIMINA GEÇ").click();
    }

    // - sehir secimi, ilçe, adres kaydetme(bos hesap lazım),







}
