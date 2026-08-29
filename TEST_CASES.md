# Gratis.com Test Cases

27 test cases covering gratis.com's Auth, Navigation, Search, Filter/Sort, Catalog, Cart, Checkout, and Order flows. Each one maps 1:1 to a `@Test(description = "TC_XXX - ...")` annotation in `src/test/java/com/gratis/tests/`.

**Status legend:** ✅ Implemented · 🔶 In progress · ⬜ Not started (`// TODO: implement`)

## Auth — `AuthTests.java`

gratis.com has no email/password form and no separate registration page — a single phone-number + OTP flow ("Giriş Yap / Üye Ol") handles both login and signup. There's no forgot-password flow either, since there's no password. See `LoginPage.java` for details.

| TC | Description | Method | Status |
|----|---|---|---|
| TC_001 | Registering with a brand-new phone number completes the OTP flow and logs the user in | `newPhoneNumberCompletesRegistration` | 🔶 Phone entry + DEVAM ET + manual OTP entry via `page.pause()`; no post-login assertion yet |
| TC_002 | An already-registered phone number routes through the same OTP login, not a duplicate signup | `existingPhoneNumberRoutesToLogin` | 🔶 Full flow + assertions (URL, logged-in name in header); OTP still entered manually |
| TC_003 | An incorrect OTP is rejected with a validation error and does not authenticate | `invalidOtpShowsError` | 🔶 Manual invalid-OTP entry via `page.pause()`; asserts the real error text ("Girdiğiniz kod hatalıdır...") |
| TC_004 | An invalid phone number format is blocked before an OTP is ever sent | `invalidPhoneFormatBlocksContinue` | ✅ Fully automated — `LoginPage.invalidPhoneNumber()` uses `pressSequentially()` (this masked phone field breaks with `.fill()`), asserts "Son 7 hane aynı olamaz." |

**Known limitation:** OTP delivery is real SMS to a real phone, with no sandbox/mock bypass available on the live site. TC_001–TC_003 pause execution (`page.pause()`) so a human can read the code off their phone and type it into the browser directly — semi-automated, not fully automated.

## Navigation — `NavigationTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_005 | Mega Menu Category Hover and Redirection (Desktop) | `megaMenuHoverAndRedirect` | 🔶 Flow + assertion written, but the "Makyaj" locator is unresolved — 5 elements share that text on the homepage and none was confirmed as the real header link via automated checks; needs manual DevTools inspection |
| TC_006 | Mobile Hamburger Menu Navigation & Accordion Drilldown | `mobileHamburgerMenuDrilldown` | 🔶 Full flow written; `HeaderComponent.mobileHeaderYuzbakim()` is missing the `.first()` disambiguation its sibling methods have, so it may hit the same multi-match issue `mobileHeaderCiltbakim()` had before being fixed |
| TC_007 | Header Basket Icon Counter Synchronization | `basketCounterSyncsDynamically` | 🔶 Flow + assertions written (add two items, check counter reads "1" then "2"); leading `//giriş gerekiyor` comment suggests an unresolved question about guest vs. logged-in cart behavior |
| TC_008 | Header Logo Redirection from Subpages | `logoRedirectsHomeFromSubpage` | ✅ `PLPPage.logo()` uses `.first()` to disambiguate the 5 `<a href="/">` matches on a subpage |

## Search — `SearchTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_009 | Product Search using Autocomplete Suggestions | `autocompleteSuggestionsAppearWhileTyping` | ✅ Waits on `suggestions.first()` before reading `.count()`, since `.count()`/`.innerText()` don't auto-wait like actions do |
| TC_010 | Product Search with Valid Keyword | `searchWithValidKeywordShowsResults` | 🔶 `PLPPage.checkSearchWord()` and the `hasText("“göz“")` assertion still use smart/curly quotes, which likely don't match the real rendered heading text — worth verifying against the live DOM |

**TC_011 removed:** the SQLi/XSS/special-character search payload test (previously TC_011, data-driven via `@DataProvider`) has been deleted from `SearchTests.java` entirely — no method, no data provider. Numbering below still reflects the original TC_012+ scheme rather than closing the gap.

## Filter & Sort — `FilterSortTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_012 | Product Filtering by Brand and Price Range on PLP | `filterByBrandAndPriceRange` | ✅ Expands MARKA (exact text match), checks the Wella checkbox, waits for `brand=` in the URL (checkbox filters apply instantly — no UYGULA needed), verifies the first 5 results all mention "wella" |
| TC_013 | Product Sorting by Price and Sales Volume | `sortByPriceLowToHighAndHighToLow` | ✅ Sorts by Çok Satanlar then Fiyat Artan, asserting the `sort=` URL param each time; relies on `HeaderComponent.headerSacbakim()`'s `mouse().move(0,0)` fix so the mega menu opened on the way in doesn't linger and block the sort control |

## Catalog (PLP/PDP) — `CatalogTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_014 | Verify Product Detail Page Information Layout | `pdpLayoutShowsAllRequiredElements` | ✅ Fixed — `PLPPage.clickFirstItemName()` (which returned `Locator.toString()`) was removed; now asserts the PDP's real `<h1>` product title is visible |
| TC_015 | Verify Product Stock Status (In-Stock vs Out-of-Stock) | `stockStatusReflectsAvailability` | ⬜ Empty body, just a `//giriş gerekiyor` comment |
| TC_016 | Product Page Checks *(renamed from "Multi-Image Carousel Zoom and Navigation on PDP")* | `thumbnailCarouselUpdatesMainImage` | ✅ Fixed both weak assertions — the lightbox close now checks the overlay (`.fixed.inset-0.bg-white/90`) is no longer visible instead of an unrelated generic button, and "Tüm Yorumları Görüntüle" is confirmed (via live DOM check) to switch tabs rather than scroll, so it now asserts the "DEĞERLENDİR" review button is visible instead of the unconfirmed `#comments-list` / `isInViewport()`. Still broader than its original single-purpose scope, and method name (`thumbnailCarouselUpdatesMainImage`) doesn't match the description — cosmetic, not a correctness issue. |
| TC_017 | Add/Remove Product to Wishlist (Logged-In User) | `addAndRemoveProductFromWishlistWhenLoggedIn` | ⬜ Empty body, just a `//giriş gerekiyor` comment |
| TC_018 | Wishlist Access and Redirection for Guest User | `guestWishlistClickPromptsLogin` | ✅ Checks all three guest entry points (header link, PLP card heart, PDP heart) all redirect to `/login` |

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
| TC_027 | Reaching the payment step lands on the correct payment URL | `paymentStepReachesPaymentUrl` | 🔶 Assertion written; the cart→shipping→payment navigation to get there is still `// TODO` |

**Deliberately reduced scope:** the two original payment cases (complete payment via 3D Secure, fail payment on insufficient funds) were removed, not just left as TODOs. This is a personal project against the live production site with no test card credentials, and actually submitting a real payment isn't something to automate here. TC_027 now only confirms the checkout flow reaches the payment URL — nothing past that point is exercised.

## Order — `OrderTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_028 | Post-Order Validation (Order Summary Screen & Email Verification) | `orderAppearsInHistoryWithMatchingDetails` | ⬜ Depends on a real order already existing |

---

**Progress: 8 / 27 implemented, 8 in progress, 11 not started.**
