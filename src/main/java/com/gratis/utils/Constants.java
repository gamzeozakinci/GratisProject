package com.gratis.utils;

public final class Constants {

    private Constants() {
    }

    // TC_012 - Search input injection payloads
    public static final String SQLI_PAYLOAD = "' OR 1=1 --";
    public static final String XSS_PAYLOAD = "<script>alert('xss')</script>";
    public static final String SPECIAL_SYMBOLS_PAYLOAD = "% & $ # @ ( ) _ + = ?";

    // TestNG group names - kept identical to the "Group" column in the source test case doc
    public static final String GROUP_SMOKE = "smoke";
    public static final String GROUP_LOGIN = "login";
    public static final String GROUP_NAVIGATION = "navigation";
    public static final String GROUP_SEARCH = "search";
    public static final String GROUP_CATALOG = "catalog";
    public static final String GROUP_CART = "cart";
    public static final String GROUP_CHECKOUT = "checkout";
    public static final String GROUP_REGRESSION = "regression";
}
