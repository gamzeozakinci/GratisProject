# Gratis.com Test Cases

29 test cases covering gratis.com's Auth, Navigation, Search, Filter/Sort, Catalog, Cart, Checkout, and Order flows. Each one maps 1:1 to a `@Test(description = "TC_XXX - ...")` annotation in `src/test/java/com/gratis/tests/`.

**Status legend:** ✅ Implemented · 🔶 In progress · ⬜ Not started (`// TODO: implement`)

## Auth — `AuthTests.java`

gratis.com has no email/password form and no separate registration page — a single phone-number + OTP flow ("Giriş Yap / Üye Ol") handles both login and signup. There's no forgot-password flow either, since there's no password. See `LoginPage.java` for details.

| TC | Description | Method | Status |
|----|---|---|---|
| TC_001 | Registering with a brand-new phone number completes the OTP flow and logs the user in | `newPhoneNumberCompletesRegistration` | 🔶 Phone entry + DEVAM ET done; OTP entered manually via `page.pause()`; no assertion yet |
| TC_002 | An already-registered phone number routes through the same OTP login, not a duplicate signup | `existingPhoneNumberRoutesToLogin` | 🔶 Full flow + assertions (URL, logged-in name in header); OTP still entered manually |
| TC_003 | An incorrect OTP is rejected with a validation error and does not authenticate | `invalidOtpShowsError` | ⬜ |
| TC_004 | An invalid phone number format is blocked before an OTP is ever sent | `invalidPhoneFormatBlocksContinue` | ⬜ Repurposed from a "forgot password" case — no such feature exists on this site |

**Known limitation:** OTP delivery is real SMS to a real phone, with no sandbox/mock bypass available on the live site. TC_001/TC_002 pause execution (`page.pause()`) so a human can read the code off their phone and type it into the browser directly — this makes those two tests semi-automated, not fully automated. TC_003 will need the same treatment once implemented.

## Navigation — `NavigationTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_005 | Mega Menu Category Hover and Redirection (Desktop) | `megaMenuHoverAndRedirect` | 🔶 Flow + assertion written, but the "Makyaj" locator is unresolved — 5 elements share that text on the homepage and none of them was confirmed to be the real header link via automated checks; needs manual DevTools inspection |
| TC_006 | Mobile Hamburger Menu Navigation & Accordion Drilldown | `mobileHamburgerMenuDrilldown` | ⬜ Mobile viewport now available (`PlaywrightFactory.initMobilePage()`) — test body itself (hamburger icon, drilldown, assertions) still needs to be written |
| TC_007 | Header Basket Icon Counter Synchronization | `basketCounterSyncsDynamically` | ⬜ |
| TC_008 | Header Logo Redirection from Subpages | `logoRedirectsHomeFromSubpage` | ⬜ |

## Search — `SearchTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_009 | Product Search using Autocomplete Suggestions | `autocompleteSuggestionsAppearWhileTyping` | ⬜ |
| TC_010 | Product Search with Valid Keyword | `searchWithValidKeywordShowsResults` | ⬜ |
| TC_011 | Product Search with Special Characters (SQLi / XSS Check) | `searchHandlesMaliciousPayloadsSafely` | ⬜ Data-driven — 3 payloads (SQLi, XSS, special symbols) via `@DataProvider` |

## Filter & Sort — `FilterSortTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_012 | Product Filtering by Brand and Price Range on PLP | `filterByBrandAndPriceRange` | ⬜ |
| TC_013 | Product Sorting by Price and Sales Volume | `sortByPriceLowToHighAndHighToLow` | ⬜ |

## Catalog (PLP/PDP) — `CatalogTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_014 | Verify Product Detail Page Information Layout | `pdpLayoutShowsAllRequiredElements` | ⬜ |
| TC_015 | Verify Product Stock Status (In-Stock vs Out-of-Stock) | `stockStatusReflectsAvailability` | ⬜ Needs known in-stock/out-of-stock product slugs |
| TC_016 | Multi-Image Carousel Zoom and Navigation on PDP | `thumbnailCarouselUpdatesMainImage` | ⬜ |
| TC_017 | Add/Remove Product to Wishlist (Logged-In User) | `addAndRemoveProductFromWishlistWhenLoggedIn` | ⬜ Needs a logged-in session |
| TC_018 | Wishlist Access and Redirection for Guest User | `guestWishlistClickPromptsLogin` | ⬜ |

## Cart — `CartTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_019 | Add Product to Cart from PLP and PDP | `addProductsFromPlpAndPdp` | ⬜ |
| TC_020 | Update Product Quantity in Cart (Boundary & Limit Checks) | `quantityBoundaryChecksInCart` | ⬜ |
| TC_021 | Remove Product from Shopping Cart | `removeProductFromCart` | ⬜ |
| TC_022 | Apply Valid Discount Promo Code to Order | `applyValidPromoCode` | ⬜ |
| TC_023 | Apply Invalid or Expired Promo Code | `invalidAndExpiredPromoCodesAreRejected` | ⬜ |
| TC_024 | Shopping Cart Session Persistence | `cartPersistsAcrossReloadAndReLogin` | ⬜ |

## Checkout — `CheckoutTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_025 | Store Pickup Delivery (Gel-Al) Selection Flow | `storePickupSelectionFlow` | ⬜ |
| TC_026 | Address Creation and Home Delivery Selection | `addressCreationAndHomeDelivery` | ⬜ |
| TC_027 | Complete Payment using Valid Credit Card via 3D Secure (E2E) | `completePaymentWithValidCardVia3DSecure` | ⬜ Same real-OTP constraint as Auth — 3D Secure also needs a human to enter a code |
| TC_028 | Fail Payment using Card with Insufficient Funds (Negative) | `paymentFailsWithInsufficientFundsCard` | ⬜ |

## Order — `OrderTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_029 | Post-Order Validation (Order Summary Screen & Email Verification) | `orderAppearsInHistoryWithMatchingDetails` | ⬜ Depends on a real order already existing |

---

**Progress: 3 / 29 in progress, 0 fully complete, 26 not started.**
