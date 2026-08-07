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

    private Locator detailProductList() { return page.locator(".order-detail-products"); }
    private Locator detailAddress() { return page.locator(".order-detail-address"); }
    private Locator detailShippingMethod() { return page.locator(".order-detail-shipping-method"); }

    public boolean isOrderListed(String orderId) {
        return isVisible(orderRow(orderId));
    }

    public void openOrderDetail(String orderId) {
        click(orderDetailButton(orderId));
    }

    public String detailProductsText() {
        return textOf(detailProductList());
    }

    public String detailAddressText() {
        return textOf(detailAddress());
    }

    public String detailShippingMethodText() {
        return textOf(detailShippingMethod());
    }
}
