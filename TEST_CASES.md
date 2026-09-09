# Gratis.com Test Cases

30 test cases covering gratis.com's Auth, Navigation, Search, Filter/Sort, Catalog, Cart, and Checkout flows. Each one maps 1:1 to a `@Test(description = "TC_XXX - ...")` annotation in `src/test/java/com/gratis/tests/`.

**Numbering note:** these were renumbered sequentially (TC_001–TC_030, no gaps) after several earlier tests were removed or merged. If you're looking at an older commit/screenshot with different TC numbers for the same test, this file is the current mapping.

**Status legend:** ✅ Implemented · 🔶 In progress · ⬜ Not started (`// TODO: implement`)

**⚠️ `src/XML_files/testng.xml` is currently broken** — its Checkout `<test>` block still lists `com.gratis.tests.OrderTests`, a class that was deleted entirely (see the Checkout section below). Running the full suite via this XML will fail to even start until that line is removed (or the class is restored).

**Login for tests that need it:** several tests require being logged in (this site has no guest cart or wishlist — confirmed live). Rather than a real phone+OTP flow every run, those tests load a saved session via `PlaywrightFactory.initLoggedInPage()` / `LoggedInBaseTest`, captured once by running `SessionCaptureTests` manually. That saved session is short-lived (~10–15 minutes — it's a real JWT issued by the site's backend), so it needs recapturing shortly before running any of these. See `SessionCaptureTests.java`'s class comment.

## Auth — `AuthTests.java`

gratis.com has no email/password form and no separate registration page — a single phone-number + OTP flow ("Giriş Yap / Üye Ol") handles both login and signup. There's no forgot-password flow either, since there's no password. See `LoginPage.java` for details.

| TC | Description | Method | Status |
|----|---|---|---|
| TC_001 | Registering with a brand-new phone number completes the OTP flow and logs the user in | `newPhoneNumberCompletesRegistration` | 🔶 Full registration form implemented (name, surname, email, birth date, Gratis Kart checkbox, agreement checkbox, cookie accept, confirm) + manual OTP via `page.pause()`. **Reuse risk still open:** the phone number comes from the static `phone.number` config value — the first run registers it, so a second run hits "already registered" instead of a fresh signup. |
| TC_002 | An already-registered phone number routes through OTP login | `existingPhoneNumberRoutesToLogin` | 🔶 Full flow + assertions (URL, logged-in name in header); OTP still entered manually |
| TC_003 | An incorrect OTP is rejected with a validation error and does not authenticate | `invalidOtpShowsError` | 🔶 Manual invalid-OTP entry via `page.pause()`; asserts the real error text |
| TC_004 | An invalid phone number format is blocked before an OTP is ever sent | `invalidPhoneFormatBlocksContinue` | ✅ Fully automated — no manual step needed |
| TC_005 | Logging out returns the user to a guest state | `logoutReturnsToGuestState` | ⬜ Not started (`// TODO: implement`) |

**Known limitation:** OTP delivery is real SMS to a real phone, with no sandbox/mock bypass. TC_001–TC_003 pause execution (`page.pause()`) for a human to read the code and type it in — semi-automated, not fully automated.

## Navigation — `NavigationTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_006 | Mega Menu Category Hover and Redirection (Desktop) | `megaMenuHoverAndRedirect` | 🔶 `HeaderComponent.headerMakyaj()` now uses `.first()` to disambiguate the "Makyaj" text (5 elements share it on the homepage), but this hasn't been confirmed against a real run since |
| TC_007 | Mobile Hamburger Menu Navigation & Accordion Drilldown | `mobileHamburgerMenuDrilldown` | 🔶 Full flow written; `HeaderComponent.mobileHeaderYuzbakim()` is still missing the `.first()` disambiguation its sibling methods (`mobileHeaderCiltbakim`, `mobileHeaderTonikler`) already have |
| TC_008 | Header Basket Icon Counter Synchronization | `basketCounterSyncsDynamically` | ✅ Confirmed passing by an actual test run. Swaps to a logged-in page inline (cart requires login); `PLPPage.add1stItem()`/`add2ndItem()` are scoped to the correct product card |
| TC_009 | Header Logo Redirection from Subpages | `logoRedirectsHomeFromSubpage` | ✅ |

## Search — `SearchTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_010 | Product Search using Autocomplete Suggestions | `autocompleteSuggestionsAppearWhileTyping` | ✅ |
| TC_011 | Product Search with Valid Keyword | `searchWithValidKeywordShowsResults` | 🔶 `PLPPage.checkSearchWord()` builds a locator (`//h1[text()="“göz“"]"`, smart/curly quotes) but never asserts or clicks anything with it — calling it currently does nothing useful. The real assertion right after it in the test uses the same curly-quote text and has never been verified against the live DOM. |
| TC_012 | Searching a nonsense keyword shows the empty-results state | `searchWithNoResultsShowsEmptyState` | ⬜ Not started (`// TODO: implement`) |

## Filter & Sort — `FilterSortTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_013 | Product Filtering by Brand and Price Range on PLP | `filterByBrandAndPriceRange` | ✅ |
| TC_014 | Product Sorting by Price and Sales Volume | `sortByPriceLowToHighAndHighToLow` | ✅ The redundant `Thread.sleep(2000)` has been removed — now settles purely on `assertThat(...).isVisible()` |
| TC_015 | Clearing an applied filter resets the product list | `clearingFiltersResetsProductList` | ⬜ Not started (`// TODO: implement`) |

## Catalog (PLP/PDP) — `CatalogTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_016 | Verify Product Detail Page Information Layout | `pdpLayoutShowsAllRequiredElements` | ✅ |
| TC_017 | Verify Product Stock Status (In-Stock vs Out-of-Stock) | `stockStatusReflectsAvailability` | ✅ Only covers the in-stock path despite the name — no out-of-stock ("Gelince Haber Ver") assertion yet |
| TC_018 | Product Page Checks *(renamed from "Multi-Image Carousel Zoom and Navigation on PDP")* | `thumbnailCarouselUpdatesMainImage` | ✅ Covers image navigation, the zoom lightbox, and the comments tab-switch. Method name still doesn't match the description — cosmetic |
| TC_019 | Add/Remove Product to Wishlist (Logged-In User) | `addAndRemoveProductFromWishlistWhenLoggedIn` | ✅ Only the "Add" half is covered — no "Remove" step (see TC_021, which is meant to cover that separately) |
| TC_020 | Wishlist Access and Redirection for Guest User | `guestWishlistClickPromptsLogin` | ✅ |
| TC_021 | Remove Product from Wishlist (Logged-In User) | `removeProductFromWishlistWhenLoggedIn` | ⬜ Not started (`// TODO: implement`) |

## Cart — `CartTests.java`

Extends `LoggedInBaseTest` — every test in this class starts already logged in. `TC_023` and `TC_024` formally declare `dependsOnMethods = "addProductsFromPlpAndPdp"` — a correct use here, since what they depend on is server-side cart state tied to the account (which survives a fresh browser context), not local browser state. This also means TC_023/024 get skipped rather than failing confusingly if TC_022 fails.

| TC | Description | Method | Status |
|----|---|---|---|
| TC_022 | Add Product to Cart from PLP and PDP | `addProductsFromPlpAndPdp` | ✅ Confirmed passing by an actual test run, after fixing login wiring, `CartPage.cartButton()`, product-card scoping, and contaminated-text comparisons |
| TC_023 | Update Product Quantity in Cart (Boundary & Limit Checks) | `quantityBoundaryChecksInCart` | 🔶 Correctly depends on TC_022; not yet confirmed by its own run |
| TC_024 | Remove Product from Shopping Cart | `removeProductFromCart` | 🔶 Despite the name, this only calls `cart.decreaseQuantity()` once — it doesn't actually remove the item from the cart (`CartPage.deleteFromCart()` exists for that but is unused, and still uses a risky unscoped `getByRole(BUTTON, "button")`). Its `.flex.flex-col.gap-4` locator hasn't been checked against the live DOM either. See TC_027, which is meant to cover real deletion separately. |
| TC_025 | Apply Invalid or Expired Promo Code | `invalidAndExpiredPromoCodesAreRejected` | 🔶 `CartPage.submitPromoCode()` fixed (was hitting the `aria-label="button"` override trap); not yet confirmed by a run |
| TC_026 | Shopping Cart Session Persistence | `cartPersistsAcrossReloadAndReLogin` | 🔶 Relies on `LoggedInBaseTest` instead of its own manual login; not yet confirmed by a run |
| TC_027 | Actually deleting an item from the cart (not just decreasing quantity) | `deleteItemFromCart` | ⬜ Not started (`// TODO: implement`). Distinct from TC_024 — this one is meant to exercise the real removal action (`CartPage.deleteFromCart()`), which also needs its risky unscoped `getByRole(BUTTON, "button")` locator fixed before this can be written. |

## Checkout — `CheckoutTests.java`

Extends `LoggedInBaseTest` (both tests need a real session for cart/checkout, same as everywhere else on this site). Most checkout-flow steps (city/district/store, address form, payment step) live in `CheckoutPage.java`. `continueToDelivery()` stays on `CartPage` instead, since that "TESLİMAT ADIMINA GEÇ" button is still on the cart page itself, before you've actually moved into checkout.

| TC | Description | Method | Status |
|----|---|---|---|
| TC_028 | Store Pickup Delivery (Gel-Al) Selection Flow, including reaching and confirming the correct payment URL | `storePickupSelectionFlow` | 🔶 Full flow written (city/district/store selection, agreement, continue). `continueToPayment()` was fixed to click `"ÖDEME ADIMINA GEÇ"` instead of duplicating `accAgreement()`'s checkbox click, but that fix was applied without a live-verified run (session had ~114s left at the time) — still needs confirming. |
| TC_029 | Address Creation and Home Delivery Selection, including reaching and confirming the correct payment URL | `addressCreationAndHomeDelivery` | 🔶 Several real bugs found and fixed via a live-verified session: `chooseOnline()` was missing `.click()` entirely; the İl/İlçe/Mahalle dropdowns were assumed to have a search input (confirmed live they don't — plain clickable lists) and used the wrong XPath axis (`following-sibling::` → fixed to `following::button[1]`); a second cookie-consent banner (`#cookiespool-banner`, distinct from the one `LoggedInBaseTest` already handles) was found intercepting the `saveAddress()` click, so `acceptCookiesIfPresent()` (see `LoggedInBaseTest`) is now called again right before that click. An explicit `page.waitForURL(...)` was also added before the final assertion. Still not confirmed green end-to-end by an actual run since the last fix. |
| TC_030 | Navigating directly to checkout with an empty cart blocks access | `emptyCartBlocksCheckoutAccess` | ⬜ Not started (`// TODO: implement`) |

**Payment scope unchanged:** still deliberately stops at reaching the payment URL — no real payment is attempted, no test card credentials exist for this personal project. Post-order validation (a former `OrderTests` class) was removed for the same no-real-payment reason and hasn't been replaced.

## Dead code removed along the way

`HomePage.java`, `WishlistPage.java`, `OrderPage.java`, and `TestDataGenerator.java` (all unused empty skeletons or never-wired-in utilities) have been deleted from the project. `CheckoutPage.java`'s three original unused methods (`fromStore()`, `toAddress()`, `toCheckout()`) have also been removed — they were never wired into any test and duplicated functionality the moved-in methods already cover.

---

**Progress: 12 / 30 implemented, 12 in progress, 6 not started.**
