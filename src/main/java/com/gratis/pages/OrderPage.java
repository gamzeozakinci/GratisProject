package com.gratis.pages;

import com.gratis.base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class OrderPage extends BasePage {

    public OrderPage(Page page) {
        super(page);
    }

    private Locator orderRow(String orderId) {
        return page.locator(".order-row").filter(new Locator.FilterOptions().setHasText(orderId));
    }

    private Locator orderDetailButton(String orderId) {
        return orderRow(orderId).getByText("Sipariş Detayı");
    }

    private Locator detailProductList() {
        return page.locator(".order-detail-products");
    }

    private Locator detailAddress() {
        return page.locator(".order-detail-address");
    }

    private Locator detailShippingMethod() {
        return page.locator(".order-detail-shipping-method");
    }

    public boolean isOrderListed(String orderId) {
        return orderRow(orderId).isVisible();
    }

    public void openOrderDetail(String orderId) {
        orderDetailButton(orderId).click();
    }

    public String detailProductsText() {
        return detailProductList().innerText().trim();
    }

    public String detailAddressText() {
        return detailAddress().innerText().trim();
    }

    public String detailShippingMethodText() {
        return detailShippingMethod().innerText().trim();
    }
}
