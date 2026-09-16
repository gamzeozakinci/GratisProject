# Gratis.com Test Cases

30 test cases covering gratis.com's Auth, Navigation, Search, Filter/Sort, Catalog, Cart, and Checkout flows. Each one maps 1:1 to a `@Test(description = "TC_XXX - ...")` annotation in `src/test/java/gratis/com/tests/`.

**Numbering note:** these were renumbered sequentially (TC_001–TC_030, no gaps) after several earlier tests were removed or merged. If you're looking at an older commit/screenshot with different TC numbers for the same test, this file is the current mapping.

**Package note:** the test package is `gratis.com.tests` (i.e. `src/test/java/gratis/com/tests/`), not `com.gratis.tests` — a deliberate rename from the earlier layout. Page objects, base classes, config, and driver code stay under `com.gratis.*` (`src/main/java/com/gratis/`) — only the test classes themselves moved.

**Status legend:** ✅ Implemented · 🔶 In progress / written but not confirmed by a fresh run · ❌ Confirmed failing (reproduced via an actual automated run, root cause identified) · ⬜ Not started

**Login for tests that need it:** several tests require being logged in (this site has no guest cart or wishlist — confirmed live). Rather than a real phone+OTP flow every run, those tests load a saved session via `PlaywrightFactory.initLoggedInPage()` / `LoggedInBaseTest`, captured once by running `SessionCaptureTests` manually. That saved session is short-lived — a real JWT issued by the site's backend with an exact 15-minute `iat`-to-`exp` window (confirmed by decoding it) — so it needs recapturing shortly before running any of these. See `SessionCaptureTests.java`'s class comment.

**Cookie-banner handling lives in `BaseTest`:** `acceptCookiesIfPresent()` (a one-shot `#banner-accept-button` visibility check + click, safe to call repeatedly) is available to every test class — guest or logged-in — since the consent banner isn't login-specific. `LoggedInBaseTest.setUp()` calls it automatically; tests that swap to a logged-in page mid-method (bypassing that lifecycle method) need to call it themselves.

**Page-object variable convention:** every test method constructs the page objects it needs locally, at the top of the method (not shared class fields + `@BeforeMethod`) — this sidesteps a real NPE bug hit earlier where field-initializer page objects were constructed before `page` itself existed. Short, consistent variable names throughout: `hp` (HeaderComponent), `plp` (PLPPage), `pdp` (PDPPage), `lp` (LoginPage), `cp` (CartPage), `chp` (CheckoutPage).

**Suite XML files** (`src/XML_files/`): `testng.xml` is the full, unattended regression suite (Navigation/Search+Filter/Catalog/Cart/Checkout — no Auth, since that blocks on a real OTP). `auth.xml` runs `AuthTests` alone, manually, when you're at the keyboard to type an OTP. `testng-smoke.xml` runs one representative, OTP-free test per feature area, selected via a `groups = "smoke"` tag on the `@Test` annotation (TC_006, TC_011, TC_013, TC_016, TC_022, TC_029).

**`HeaderComponent.headerSacbakim()` is genuinely non-deterministic when clicked plainly** — confirmed both live and via a real automated-run stack trace ("element is not stable" / "element is outside of the viewport" / a persistent overlay intercepting pointer events, alternating across retries). It now force-clicks and verifies the navigation actually landed on `*sac-bakim*`, retried up to 3 times (the old separate `headerSacbakimForced()` method was merged into this one and removed — every caller benefits automatically, no need to remember which variant to call).

## Auth — `AuthTests.java`

gratis.com has no email/password form and no separate registration page — a single phone-number + OTP flow ("Giriş Yap / Üye Ol") handles both login and signup. There's no forgot-password flow either, since there's no password. See `LoginPage.java` for details.

| TC | Description | Method | Status |
|----|---|---|---|
| TC_001 | Registering with a brand-new phone number completes the OTP flow and logs the user in | `newPhoneNumberCompletesRegistration` | 🔶 Full registration form implemented (name, surname, email, birth date, Gratis Kart checkbox, agreement checkbox, cookie accept, confirm) + manual OTP via `page.pause()`. **Reuse risk still open:** the phone number comes from the static `phone.number` config value — the first run registers it, so a second run hits "already registered" instead of a fresh signup. |
| TC_002 | An already-registered phone number routes through OTP login | `existingPhoneNumberRoutesToLogin` | 🔶 Full flow + assertions (URL, logged-in name in header); OTP still entered manually |
| TC_003 | An incorrect OTP is rejected with a validation error and does not authenticate | `invalidOtpShowsError` | 🔶 Manual invalid-OTP entry via `page.pause()`; asserts the real error text |
| TC_004 | An invalid phone number format is blocked before an OTP is ever sent | `invalidPhoneFormatBlocksContinue` | ✅ Fully automated — no manual step needed |
| TC_005 | Logging out returns the user to a guest state | `logoutReturnsToGuestState` | 🔶 `HeaderComponent.hesabimLogOut()`'s locator bug is fixed (now `getByText("Çıkış Yap").first().click()`, was incorrectly passing the Turkish text straight into `.locator()` as a CSS selector). The test still has **no assertion at all** — even with the click now working, nothing verifies the guest state was actually reached. |

**Known limitation:** OTP delivery is real SMS to a real phone, with no sandbox/mock bypass. TC_001–TC_003 pause execution (`page.pause()`) for a human to read the code and type it in — semi-automated, not fully automated. Because of this, `AuthTests` is deliberately **not** part of `testng.xml` (the unattended suite) — run it via `src/XML_files/auth.xml` when you're at the keyboard.

## Navigation — `NavigationTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_006 | Mega Menu Category Hover and Redirection (Desktop) | `megaMenuHoverAndRedirect` | 🔶 `HeaderComponent.headerMakyaj()` uses `.first()` to disambiguate the "Makyaj" text (5 elements share it on the homepage) — hasn't been confirmed against a real run. Tagged `groups = "smoke"`. |
| TC_007 | Mobile Hamburger Menu Navigation & Accordion Drilldown | `mobileHamburgerMenuDrilldown` | 🔶 Full flow written; `HeaderComponent.mobileHeaderYuzbakim()` is still missing the `.first()` disambiguation its sibling methods (`mobileHeaderCiltbakim`, `mobileHeaderTonikler`) already have |
| TC_008 | Header Basket Icon Counter Synchronization | `basketCounterSyncsDynamically` | ✅ Confirmed passing by an actual test run. Swaps to a logged-in page inline (cart requires login); `PLPPage.add1stItem()`/`add2ndItem()` are scoped to the correct product card |
| TC_009 | Header Logo Redirection from Subpages | `logoRedirectsHomeFromSubpage` | ✅ |

## Search — `SearchTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_010 | Product Search using Autocomplete Suggestions | `autocompleteSuggestionsAppearWhileTyping` | ✅ |
| TC_011 | Product Search with Valid Keyword | `searchWithValidKeywordShowsResults` | 🔶 `PLPPage.checkSearchWord()` builds a locator (`//h1[text()="“göz“"]"`, smart/curly quotes) but never asserts or clicks anything with it — calling it currently does nothing useful. The real assertion right after it in the test uses the same curly-quote text and has never been verified against the live DOM. Tagged `groups = "smoke"`. |
| TC_012 | Searching a nonsense keyword shows the empty-results state | `searchWithNoResultsShowsEmptyState` | 🔶 Fully written (`HeaderComponent.invalidSearch()` fills a gibberish keyword, asserts the "Sonuç Bulunamadı" empty-state text is visible) — looks correct, just not yet confirmed by an actual run |

## Filter & Sort — `FilterSortTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_013 | Product Filtering by Brand and Price Range on PLP | `filterByBrandAndPriceRange` | ✅ Tagged `groups = "smoke"` |
| TC_014 | Product Sorting by Price and Sales Volume | `sortByPriceLowToHighAndHighToLow` | ✅ |
| TC_015 | Clearing an applied filter resets the product list | `clearingFiltersResetsProductList` | 🔶 Scoped to `a[href*='-p-']:has(h5)` (every real product card has an `<h5>` name, decoy promo banners don't — confirmed live: 24/24 had one, 0 without). Asserts `assertThat(firstProduct).not().hasText(product)`, which auto-retries until the text actually changes. Not yet confirmed passing by a full run since this rewrite. |

## Catalog (PLP/PDP) — `CatalogTests.java`

| TC | Description | Method | Status |
|----|---|---|---|
| TC_016 | Verify Product Detail Page Information Layout | `pdpLayoutShowsAllRequiredElements` | ✅ Tagged `groups = "smoke"` |
| TC_017 | Verify Product Stock Status (In-Stock vs Out-of-Stock) | `stockStatusReflectsAvailability` | ✅ Only covers the in-stock path despite the name — no out-of-stock ("Gelince Haber Ver") assertion yet. Also swaps to a logged-in page inline but doesn't call `acceptCookiesIfPresent()` after — same latent gap TC_019 hit for real (see below), just hasn't broken this test's assertion yet. |
| TC_018 | Product Page Checks *(renamed from "Multi-Image Carousel Zoom and Navigation on PDP")* | `thumbnailCarouselUpdatesMainImage` | ✅ Covers image navigation, the zoom lightbox, and the comments tab-switch. Method name still doesn't match the description — cosmetic |
| TC_019 | Add/Remove Product to Wishlist (Logged-In User) | `addAndRemoveProductFromWishlistWhenLoggedIn` | ✅ Was confirmed failing by real automated runs, then fixed via a chain of **eight distinct real bugs**, each found by actually reproducing the failure (live and/or via automated run) rather than guessing: (1) `WishListPage.wishlistProducts()` called itself recursively with no base case — `StackOverflowError` on any call; now `page.locator("h5").allTextContents()`. (2) `product`'s text had `.replace(" ", "-")` applied, but the wishlist page's text never did — could never match. (3) `product` read the whole `<a>` card (which also picks up a trailing `\n(NN)` review-count badge) instead of just its `<h5>` — same never-matches problem from a second angle. (4) `HeaderComponent.headerSacbakim()` was genuinely non-deterministic (see the intro note above) — merged in the retry-and-verify fix. (5) `PLPPage.submitButton()` didn't wait for the "Listeye Ekle" modal's own backdrop to clear after clicking "Kaydet", so a second, unrelated heart click right after could get blocked by it mid-close — now waits up to 5s for `div[class*='bg-white/90']` to disappear. (6) The "eklendi" toast assertion wasn't scoped with `.first()` — genuinely ambiguous once two different products both fire the identical toast text back-to-back (Toastify stacks them); not a wrong-element bug, just needed `.first()`. (7) The "kaldırıldı" assertion used the wrong Turkish grammatical case — `"listesine"` (dative, "to the list", copied from the add-toast) instead of the real `"listesinden"` (ablative, "from the list") — confirmed by reading the actual live toast text. (8) `HeaderComponent.headerWishist()`'s unscoped `.first()` picked one of two zero-size "Favorilerim" dropdown links (4 elements share that `href`) instead of the one real, visible header icon — now `a[href='/my-account/wishlist']:visible`. |
| TC_020 | Wishlist Access and Redirection for Guest User | `guestWishlistClickPromptsLogin` | ✅ Still verifies guest→`/login` redirect from three entry points (header wishlist icon, a PLP listing action, PDP wishlist icon). **Note:** the PLP step now calls `plp.add1stItem()` (an add-to-cart button) rather than a wishlist-specific method — the assertion still passes (this site redirects guests to `/login` from *either* action), but this step no longer specifically exercises the "add to wishlist from listing" path the description implies. |
| TC_021 | Remove Product from Wishlist (Logged-In User) | `removeProductFromWishlistWhenLoggedIn` | 🔶 Now has a real body: swaps to a logged-in page inline, opens the wishlist, calls `WishListPage.wishFirstItem()`, asserts the same "kaldırıldı" (removed) toast text confirmed correct for TC_019. `wishFirstItem()`'s locator (`page.locator(".absolute.top-2").first()`) is no longer the invalid `::nth2` CSS it started as, but hasn't been verified live yet — not yet confirmed by a run. |

## Cart — `CartTests.java`

Extends `LoggedInBaseTest` — every test in this class starts already logged in. `TC_023` and `TC_024` formally declare `dependsOnMethods = "addProductsFromPlpAndPdp"`, and `TC_027` depends on `TC_024` — a correct use here, since what they depend on is server-side cart state tied to the account (which survives a fresh browser context), not local browser state. This also means downstream tests get skipped rather than failing confusingly if an earlier one fails.

**Cross-class ordering risk (not yet confirmed, see TC_028):** `TC_027` genuinely empties the account's entire cart. In `testng.xml`, `CartTests` runs before `CheckoutTests`, and nothing between them re-adds an item — `CheckoutTests`' own tests assume the cart already has one. `dependsOnMethods` can't express this across classes the way it does within `CartTests` itself.

| TC | Description | Method | Status |
|----|---|---|---|
| TC_022 | Add Product to Cart from PLP and PDP | `addProductsFromPlpAndPdp` | ✅ Confirmed passing by an actual test run, after fixing login wiring, `CartPage.cartButton()`, product-card scoping, and contaminated-text comparisons. Tagged `groups = "smoke"`. |
| TC_023 | Update Product Quantity in Cart (Boundary & Limit Checks) | `quantityBoundaryChecksInCart` | 🔶 Correctly depends on TC_022; uses `assertThat(qty).not().hasText(currQ)` (auto-retrying web-first assertion) rather than a one-shot `.innerText()` read, after an earlier race-condition bug. Not yet confirmed by its own fresh run since the CartPage locator overhaul (see `CartPage.java`'s pill-scoped `quantityControls()` — fixes a real wishlist-heart-vs-plus-button mixup found live). |
| TC_024 | Remove Product from Shopping Cart | `removeProductFromCart` | 🔶 Rewritten to actually remove the item: decreases quantity down to 1, then calls `removeProduct()` (previously this test only decreased quantity once and never removed anything — that gap is closed now). Not yet confirmed by a fresh run since the rewrite. |
| TC_025 | Apply Invalid or Expired Promo Code | `invalidAndExpiredPromoCodesAreRejected` | 🔶 `CartPage.submitPromoCode()` fixed (was hitting the `aria-label="button"` override trap); not yet confirmed by a run |
| TC_026 | Shopping Cart Session Persistence | `cartPersistsAcrossReloadAndReLogin` | 🔶 Relies on `LoggedInBaseTest` instead of its own manual login; not yet confirmed by a run |
| TC_027 | Deleting all items from the cart | `deleteItemFromCart` | 🔶 Now has a real body (was previously empty/not started): depends on TC_024, calls `CartPage.deleteAllFromCart()`, asserts the empty-cart state text. Not yet confirmed by a fresh run. |

## Checkout — `CheckoutTests.java`

Extends `LoggedInBaseTest`. Most checkout-flow steps (city/district/store, address form, payment step) live in `CheckoutPage.java`. `continueToDelivery()` stays on `CartPage` instead, since that "TESLİMAT ADIMINA GEÇ" button is still on the cart page itself, before you've actually moved into checkout.

**Both test cases below were substantially redesigned this session** after live investigation overturned an earlier assumption baked into both: neither test ever reaches a `/checkout/payment` URL, because that URL doesn't exist. Clicking "ÖDEME ADIMINA GEÇ" opens an **in-page "Ödeme" modal** — a real `<h5>Ödeme</h5>` heading in the main document, wrapping a live third-party Masterpass card-entry iframe — while the page stays on `/checkout` the whole time. The original `page.waitForURL(".*checkout/payment.*")`-style assertions could never have passed.

| TC | Description | Method | Status |
|----|---|---|---|
| TC_028 | Store Pickup Delivery (Gel-Al) Selection Flow | `storePickupSelectionFlow` | 🔶 **Scope deliberately changed.** Live investigation found that store pickup has its own separate "Fatura Adresi" (billing address) requirement, distinct from any delivery address, that starts unset even when the account already has a saved address — this is what was actually blocking the old version of this test. Rather than working around a genuinely confusing site UX flow, this test now verifies that behavior directly: it selects a store, agrees to the terms, and asserts the "Yeni Adres Ekle" (billing-address-creation) modal opens (`CheckoutPage.openBillingAddressModal()` / `addAddressModal()`) — **no address is filled in, no payment is attempted.** **New, not-yet-confirmed suspicion:** a fresh full-suite run failed earlier at `openBillingAddressModal()` (timeout waiting for "YENİ ADRES EKLE"), and `CartTests.TC_027` (which runs before this class in `testng.xml` and genuinely empties the account's cart via `deleteAllFromCart()`) is a plausible cause — nothing between TC_027 and TC_028 re-adds an item, and `storePickupSelectionFlow()` assumes the cart already has one. Live re-verification of this was blocked by a browser-window rendering issue (0×0 viewport) and hasn't been completed — treat as a lead, not a confirmed root cause. |
| TC_029 | Address Creation and Home Delivery Selection | `addressCreationAndHomeDelivery` | 🔶 Full flow: select Kargo (home delivery), fill and save a new address if none exists, agree to terms, then `continueToPayment()`. That method now retries the "ÖDEME ADIMINA GEÇ" click up to 3 times, checking for the "Ödeme" heading before and after each attempt — confirmed live that the underlying Masterpass gateway call is measurably slow and occasionally errors outright ("Beklenmeyen bir hata oluştu"), so a single click/wait can't be trusted. **No card details are entered, no payment is attempted** — the test stops once the modal is confirmed visible. Confirmed working end-to-end once via a live manual walkthrough; not yet confirmed by a fresh automated `mvn test` run of this exact version. Tagged `groups = "smoke"`. |
| TC_030 | Navigating directly to checkout with an empty cart blocks access | `emptyCartBlocksCheckoutAccess` | ✅ Now actually sets up its own premise: navigates to the cart, calls `clearCart()` + `tamam()` to genuinely empty it first, then asserts the empty-cart state text and that "TESLİMAT ADIMINA GEÇ" isn't shown. (Previously the `CartPage` object was declared but never used, so the cart was never actually emptied before asserting — that gap is closed.) |

**Payment scope unchanged in spirit, different in mechanics:** still deliberately stops before any real payment — no test card credentials exist for this personal project. What changed is *where* it stops: not a payment-URL check (that page never existed), but confirming the right in-page modal/flow state is reached. Post-order validation (a former `OrderTests` class) was removed for the same no-real-payment reason and hasn't been replaced.

## Dead code removed along the way

`HomePage.java`, `WishlistPage.java` *(lowercase "list" — since replaced by a new, differently-named `WishListPage.java`, see TC_021 above)*, `OrderPage.java`, and `TestDataGenerator.java` (all unused empty skeletons or never-wired-in utilities) were deleted from the project. `CheckoutPage.java`'s three original unused methods (`fromStore()`, `toAddress()`, `toCheckout()`) were also removed — they were never wired into any test and duplicated functionality the moved-in methods already cover. `com.gratis.tests.OrderTests`, once referenced by `testng.xml`'s Checkout module, no longer exists anywhere — that stale XML reference has since been removed too.

---

**Progress: 13 / 30 implemented, 17 in progress, 0 confirmed failing, 0 not started.**
