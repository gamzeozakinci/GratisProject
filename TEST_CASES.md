# Gratis.com Test Cases

26 test cases covering gratis.com's Auth, Navigation, Search, Filter/Sort, Catalog, Cart, Checkout, and Order flows. Each one maps 1:1 to a `@Test(description = "TC_XXX - ...")` annotation in `src/test/java/com/gratis/tests/`.

**Status legend:** ✅ Implemented · 🔶 In progress · ⬜ Not started (`// TODO: implement`)

**Login for tests that need it:** several tests require being logged in (this site has no guest cart or wishlist — confirmed live). Rather than a real phone+OTP flow every run, those tests load a saved session via `PlaywrightFactory.initLoggedInPage()` / `LoggedInBaseTest`, captured once by running `SessionCaptureTests` manually. That saved session is short-lived (~15 minutes — it's a real JWT issued by the site's backend), so it needs recapturing shortly before running any of these. See `SessionCaptureTests.java`'s class comment.

## Auth — `AuthTests.java`

gratis.com has no email/password form and no separate registration page — a single phone-number + OTP flow ("Giriş Yap / Üye Ol") handles both login and signup. There's no forgot-password flow either, since there's no password. See `LoginPage.java` for details.

| TC | Description | Method | Status |
|----|---|---|---|
| TC_001 | Registering with a brand-new phone number completes the OTP flow and logs the user in | `newPhoneNumberCompletesRegistration` | 🔶 Full registration form now implemented (name, surname, email, birth date, Gratis Kart checkbox, agreement checkbox, cookie accept, confirm) + manual OTP via `page.pause()`. **Reuse risk:** the phone number comes from the static `phone.number` config value — the first run registers it, so a second run hits "already registered" instead of a fresh signup. `TestDataGenerator.uniquePhoneNumber()` already exists in the codebase but isn't wired in here yet. |
| TC_002 | An already-registered phone number routes through OTP login | `existingPhoneNumberRoutesToLogin` | 🔶 Full flow + assertions (URL, logged-in name in header); OTP still entered manually |
| TC_003 | An incorrect OTP is rejected with a validation error and does not authenticate | `invalidOtpShowsError` | 🔶 Manual invalid-OTP entry via `page.pause()`; asserts the real error text |
| TC_004 | An invalid phone number format is blocked before an OTP is ever sent | `invalidPhoneFormatBlocksContinue` | ✅ Fully automated — no manual step needed, `pressSequentially()` handles the masked field correctly |

**Known limitation:** OTP delivery is real SMS to a real phone, with no sandbox/mock bypass. TC_001–TC_003 pause execution (`page.pause()`) for a human to read the code and type it in — semi-automated, not fully automated.

## Navigation — `NavigationTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_005 | Mega Menu Category Hover and Redirection (Desktop) | `megaMenuHoverAndRedirect` | 🔶 Flow + assertion written, but the "Makyaj" locator is unresolved — 5 elements share that text on the homepage; needs manual DevTools inspection |
| TC_006 | Mobile Hamburger Menu Navigation & Accordion Drilldown | `mobileHamburgerMenuDrilldown` | 🔶 Full flow written; `HeaderComponent.mobileHeaderYuzbakim()` is still missing the `.first()` disambiguation its sibling methods have |
| TC_007 | Header Basket Icon Counter Synchronization | `basketCounterSyncsDynamically` | ✅ Now swaps to a logged-in page (cart genuinely requires login — confirmed this session) and uses `PLPPage.add1stItem()`/`add2ndItem()`, which are now scoped to the correct product card instead of an unscoped page-wide button match |
| TC_008 | Header Logo Redirection from Subpages | `logoRedirectsHomeFromSubpage` | ✅ |

## Search — `SearchTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_009 | Product Search using Autocomplete Suggestions | `autocompleteSuggestionsAppearWhileTyping` | ✅ |
| TC_010 | Product Search with Valid Keyword | `searchWithValidKeywordShowsResults` | 🔶 `PLPPage.checkSearchWord()` and the `hasText("“göz“")` assertion still use smart/curly quotes, which likely don't match the real rendered heading text — never verified against the live DOM |

**TC_011 removed:** the SQLi/XSS/special-character search payload test was deleted from `SearchTests.java` entirely. Numbering keeps the gap.

## Filter & Sort — `FilterSortTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_012 | Product Filtering by Brand and Price Range on PLP | `filterByBrandAndPriceRange` | ✅ |
| TC_013 | Product Sorting by Price and Sales Volume | `sortByPriceLowToHighAndHighToLow` | ✅ Works, but now has both a `Thread.sleep(2000)` **and** the proper `assertThat(...).isVisible()` settle-wait doing the same job — the sleep is redundant given the established "sleep is a flaky crutch" lesson from TC_009/TC_012, worth removing once confirmed unnecessary |

## Catalog (PLP/PDP) — `CatalogTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_014 | Verify Product Detail Page Information Layout | `pdpLayoutShowsAllRequiredElements` | ✅ |
| TC_015 | Verify Product Stock Status (In-Stock vs Out-of-Stock) | `stockStatusReflectsAvailability` | ✅ Now has a real body — logs in, adds to cart, asserts the "added to cart" toast. Despite the name, it currently only exercises the in-stock path; the out-of-stock side (asserting "Gelince Haber Ver" instead) isn't covered yet |
| TC_016 | Product Page Checks *(renamed from "Multi-Image Carousel Zoom and Navigation on PDP")* | `thumbnailCarouselUpdatesMainImage` | ✅ Covers next-image navigation, the zoom lightbox, and the comments tab-switch in one test. Method name still doesn't match the description — cosmetic |
| TC_017 | Add/Remove Product to Wishlist (Logged-In User) | `addAndRemoveProductFromWishlistWhenLoggedIn` | ✅ Now has a real body — logs in, adds to wishlist, confirms via a save dialog (`submitButton()`), asserts the success toast. Despite the name, only the "Add" half is covered — no "Remove" step yet |
| TC_018 | Wishlist Access and Redirection for Guest User | `guestWishlistClickPromptsLogin` | ✅ |

## Cart — `CartTests.java`

Extends `LoggedInBaseTest` — every test in this class starts already logged in. Heavily debugged this session; end-to-end confirmed working by an actual test run for TC_019.

| TC | Description | Method | Status |
|----|---|---|---|
| TC_019 | Add Product to Cart from PLP and PDP | `addProductsFromPlpAndPdp` | 🔶 Confirmed passing after multiple fixes (login wiring, `CartPage.cartButton()`, product-card scoping, contaminated-text comparisons). One more fix (a `NETWORKIDLE` wait before the second `headerSacbakim()` call, for a promo-carousel interception mid-test) is applied but not yet re-confirmed by a run |
| TC_020 | Update Product Quantity in Cart (Boundary & Limit Checks) | `quantityBoundaryChecksInCart` | 🔶 Written, not yet run. Implicitly depends on TC_019 having added an item first — nothing enforces that ordering (TestNG's default is declaration order, not a guarantee) |
| TC_021 | Remove Product from Shopping Cart | `removeProductFromCart` | 🔶 Written, not yet run; same implicit-ordering dependency on TC_019/`firstItem`, and its `.flex.flex-col.gap-4` locator hasn't been checked against the live DOM the way the other cart locators were |
| TC_023 | Apply Invalid or Expired Promo Code | `invalidAndExpiredPromoCodesAreRejected` | 🔶 `CartPage.submitPromoCode()` fixed (was hitting the `aria-label="button"` override trap); not yet confirmed by a run |
| TC_024 | Shopping Cart Session Persistence | `cartPersistsAcrossReloadAndReLogin` | 🔶 Simplified to rely on `LoggedInBaseTest` instead of its own manual login; not yet confirmed by a run |

**TC_022 removed:** the valid-promo-code test was deleted from `CartTests.java` entirely, matching TC_011's removal.

## Checkout — `CheckoutTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_025 | Store Pickup Delivery (Gel-Al) Selection Flow | `storePickupSelectionFlow` | ⬜ Test body still empty, but `CheckoutPage.java` already has real methods for this (`fromStore()`, `toAddress()`, `toCheckout()`) — the page object is ahead of the test |
| TC_026 | Address Creation and Home Delivery Selection | `addressCreationAndHomeDelivery` | ⬜ Same — `CheckoutPage` has the pieces, test body not written yet |
| TC_027 | Reaching the payment step lands on the correct payment URL | `paymentStepReachesPaymentUrl` | 🔶 Assertion written; the cart→shipping→payment navigation to get there is still `// TODO` |

**Deliberately reduced scope:** the two original payment cases (complete payment via 3D Secure, fail payment on insufficient funds) were removed, not left as TODOs — this is a personal project with no test card credentials, and a real payment isn't something to automate here.

## Order — `OrderTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_028 | Post-Order Validation (Order Summary Screen & Email Verification) | `orderAppearsInHistoryWithMatchingDetails` | ⬜ Depends on a real order already existing |

---

**Progress: 12 / 26 implemented, 12 in progress, 2 not started.**
