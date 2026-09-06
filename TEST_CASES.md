# Gratis.com Test Cases

24 test cases covering gratis.com's Auth, Navigation, Search, Filter/Sort, Catalog, Cart, and Checkout flows. Each one maps 1:1 to a `@Test(description = "TC_XXX - ...")` annotation in `src/test/java/com/gratis/tests/`.

**Status legend:** ✅ Implemented · 🔶 In progress · ⬜ Not started (`// TODO: implement`)

**⚠️ `src/XML_files/testng.xml` is currently broken** — its Checkout `<test>` block still lists `com.gratis.tests.OrderTests`, but that class was deleted along with TC_028. Running the full suite via this XML will fail to even start until that line is removed (or the class is restored).

**Login for tests that need it:** several tests require being logged in (this site has no guest cart or wishlist — confirmed live). Rather than a real phone+OTP flow every run, those tests load a saved session via `PlaywrightFactory.initLoggedInPage()` / `LoggedInBaseTest`, captured once by running `SessionCaptureTests` manually. That saved session is short-lived (~10–15 minutes — it's a real JWT issued by the site's backend), so it needs recapturing shortly before running any of these. See `SessionCaptureTests.java`'s class comment.

## Auth — `AuthTests.java`

gratis.com has no email/password form and no separate registration page — a single phone-number + OTP flow ("Giriş Yap / Üye Ol") handles both login and signup. There's no forgot-password flow either, since there's no password. See `LoginPage.java` for details.

| TC | Description | Method | Status |
|----|---|---|---|
| TC_001 | Registering with a brand-new phone number completes the OTP flow and logs the user in | `newPhoneNumberCompletesRegistration` | 🔶 Full registration form implemented (name, surname, email, birth date, Gratis Kart checkbox, agreement checkbox, cookie accept, confirm) + manual OTP via `page.pause()`. **Reuse risk:** the phone number comes from the static `phone.number` config value — the first run registers it, so a second run hits "already registered" instead of a fresh signup. `TestDataGenerator.uniquePhoneNumber()` exists but isn't wired in. |
| TC_002 | An already-registered phone number routes through OTP login | `existingPhoneNumberRoutesToLogin` | 🔶 Full flow + assertions (URL, logged-in name in header); OTP still entered manually |
| TC_003 | An incorrect OTP is rejected with a validation error and does not authenticate | `invalidOtpShowsError` | 🔶 Manual invalid-OTP entry via `page.pause()`; asserts the real error text |
| TC_004 | An invalid phone number format is blocked before an OTP is ever sent | `invalidPhoneFormatBlocksContinue` | ✅ Fully automated — no manual step needed |

**Known limitation:** OTP delivery is real SMS to a real phone, with no sandbox/mock bypass. TC_001–TC_003 pause execution (`page.pause()`) for a human to read the code and type it in — semi-automated, not fully automated.

## Navigation — `NavigationTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_005 | Mega Menu Category Hover and Redirection (Desktop) | `megaMenuHoverAndRedirect` | 🔶 Flow + assertion written, but the "Makyaj" locator is unresolved — 5 elements share that text on the homepage |
| TC_006 | Mobile Hamburger Menu Navigation & Accordion Drilldown | `mobileHamburgerMenuDrilldown` | 🔶 Full flow written; `HeaderComponent.mobileHeaderYuzbakim()` is still missing the `.first()` disambiguation its sibling methods have |
| TC_007 | Header Basket Icon Counter Synchronization | `basketCounterSyncsDynamically` | ✅ Swaps to a logged-in page inline (cart requires login); `PLPPage.add1stItem()`/`add2ndItem()` are scoped to the correct product card |
| TC_008 | Header Logo Redirection from Subpages | `logoRedirectsHomeFromSubpage` | ✅ |

## Search — `SearchTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_009 | Product Search using Autocomplete Suggestions | `autocompleteSuggestionsAppearWhileTyping` | ✅ |
| TC_010 | Product Search with Valid Keyword | `searchWithValidKeywordShowsResults` | 🔶 `PLPPage.checkSearchWord()` and the `hasText("“göz“")` assertion still use smart/curly quotes, never verified against the live DOM |

**TC_011 removed:** the SQLi/XSS/special-character search payload test was deleted from `SearchTests.java` entirely. Numbering keeps the gap.

## Filter & Sort — `FilterSortTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_012 | Product Filtering by Brand and Price Range on PLP | `filterByBrandAndPriceRange` | ✅ |
| TC_013 | Product Sorting by Price and Sales Volume | `sortByPriceLowToHighAndHighToLow` | ✅ Works, but still has a redundant `Thread.sleep(2000)` alongside the proper `assertThat(...).isVisible()` settle-wait — safe to remove |

## Catalog (PLP/PDP) — `CatalogTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_014 | Verify Product Detail Page Information Layout | `pdpLayoutShowsAllRequiredElements` | ✅ |
| TC_015 | Verify Product Stock Status (In-Stock vs Out-of-Stock) | `stockStatusReflectsAvailability` | ✅ Only covers the in-stock path despite the name — no out-of-stock ("Gelince Haber Ver") assertion yet |
| TC_016 | Product Page Checks *(renamed from "Multi-Image Carousel Zoom and Navigation on PDP")* | `thumbnailCarouselUpdatesMainImage` | ✅ Covers image navigation, the zoom lightbox, and the comments tab-switch. Method name still doesn't match the description — cosmetic |
| TC_017 | Add/Remove Product to Wishlist (Logged-In User) | `addAndRemoveProductFromWishlistWhenLoggedIn` | ✅ Only the "Add" half is covered — no "Remove" step. `WishlistPage.java` exists but is an empty skeleton, unused |
| TC_018 | Wishlist Access and Redirection for Guest User | `guestWishlistClickPromptsLogin` | ✅ |

## Cart — `CartTests.java`

Extends `LoggedInBaseTest` — every test in this class starts already logged in. `TC_020` and `TC_021` now formally declare `dependsOnMethods = "addProductsFromPlpAndPdp"` — a correct use here, since what they depend on is server-side cart state tied to the account (which survives a fresh browser context), not local browser state. This also means TC_020/021 get skipped rather than failing confusingly if TC_019 fails.

| TC | Description | Method | Status |
|----|---|---|---|
| TC_019 | Add Product to Cart from PLP and PDP | `addProductsFromPlpAndPdp` | ✅ Confirmed passing by an actual test run, after fixing login wiring, `CartPage.cartButton()`, product-card scoping, and contaminated-text comparisons |
| TC_020 | Update Product Quantity in Cart (Boundary & Limit Checks) | `quantityBoundaryChecksInCart` | 🔶 Now correctly depends on TC_019; not yet confirmed by a run |
| TC_021 | Remove Product from Shopping Cart | `removeProductFromCart` | 🔶 Now correctly depends on TC_019; its `.flex.flex-col.gap-4` locator hasn't been checked against the live DOM the way the other cart locators were |
| TC_023 | Apply Invalid or Expired Promo Code | `invalidAndExpiredPromoCodesAreRejected` | 🔶 `CartPage.submitPromoCode()` fixed (was hitting the `aria-label="button"` override trap); not yet confirmed by a run |
| TC_024 | Shopping Cart Session Persistence | `cartPersistsAcrossReloadAndReLogin` | 🔶 Relies on `LoggedInBaseTest` instead of its own manual login; not yet confirmed by a run |

**TC_022 removed:** the valid-promo-code test was deleted from `CartTests.java` entirely, matching TC_011's removal.

## Checkout — `CheckoutTests.java`

Rewritten since the last pass — still `extends BaseTest` (a guest page), not `LoggedInBaseTest`, even though both tests call `cart.cartButton()` and proceed through checkout, which very likely needs the same login this site requires for cart/wishlist everywhere else. Neither test logs in first, so both may hit the same guest-redirect-to-`/login` wall `CartTests` did before that was fixed — untested either way.

TC_027 ("Reaching the payment step lands on the correct payment URL") is no longer its own test — its assertion (`hasURL(".*checkout/payment.*")`) was folded into the end of both TC_025 and TC_026 instead.

| TC | Description | Method | Status |
|----|---|---|---|
| TC_025 | Store Pickup Delivery (Gel-Al) Selection Flow *(now includes the former TC_027 payment-URL assertion)* | `storePickupSelectionFlow` | 🔶 Full flow written (city/district/store selection, agreement, continue) via new `CartPage` methods, but **`continueToPayment()` and `accAgreement()` have identical bodies** (both click the same consent checkbox) — there's likely no real "continue to payment" click happening, just the checkbox toggled twice |
| TC_026 | Address Creation and Home Delivery Selection *(now includes the former TC_027 payment-URL assertion)* | `addressCreationAndHomeDelivery` | 🔶 Full flow written (name/surname/address fields, city/district/street search-and-select), but two likely bugs: `CartPage.chooseOnline()` builds a locator and never calls `.click()` on it (a no-op), and `addressDetail()` (clicks "ADRESİMİ KAYDET" / save) runs **before** `saveAddress()` (which only fills the final address-detail text field) — the save click likely fires before the field it's meant to save is even filled |

**Payment scope unchanged:** still deliberately stops at reaching the payment URL — no real payment is attempted, no test card credentials exist for this personal project.

## Order — *(removed)*

`OrderTests.java` (TC_028, "Post-Order Validation") has been deleted from the project entirely — not just left as a TODO. `testng.xml` still references this class (see the warning at the top of this file); that reference needs removing for the full suite to run.

---

**Progress: 12 / 24 implemented, 12 in progress, 0 not started.**
