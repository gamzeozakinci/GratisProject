# Gratis.com Test Cases

30 automated UI test cases for gratis.com covering Auth, Navigation, Search, Filter/Sort, Catalog, Cart and Checkout. Each one maps to a `@Test(description = "TC_XXX - ...")` in `src/test/java/gratis/com/tests/`.

## Techniques Used

- **Playwright for Java + TestNG**, written with the Page Object Model: locators and page actions live in `src/main/java/com/gratis/pages/`, tests only describe the flow and the assertions.
- **Web-first assertions and auto-waiting** (`assertThat(locator).isVisible()`, `hasText`, `hasURL`) instead of fixed sleeps, so tests wait for the real page state.
- **Scoped locators** (product card, cart row, modal) so a test never clicks the wrong one of several look-alike elements on the page.
- **Retry with verification** for the few genuinely flaky UI actions (the "Saç Bakım" nav link, the payment step), so a click is only trusted once its result is confirmed.
- **Fresh browser per test** with screenshot-on-failure saved to `test-output/screenshots/`.
- **Suites**: `testng.xml` (full unattended regression), `testng-smoke.xml` (one representative test per area, tagged `groups = "smoke"`), and `auth.xml` (OTP tests, run manually).

## Login and OTP (cookies)

gratis.com logs users in with a phone number and an SMS OTP only, and there is no sandbox or test OTP. Repeating that on every run is not practical, so the tests that need a logged-in user use **saved session cookies**: `SessionCaptureTests` is run manually once (a human types the real OTP) and stores the cookies/localStorage in `auth-state.json`; every logged-in test then loads that file and goes straight in without touching the OTP system. The session token lasts about 15 minutes, so the capture must be re-run shortly before a logged-in run. The tests that exercise the OTP flow itself (TC_001–TC_003) pause for a human to type the code.

## Payment Limitation

No payment can be tested. This is a volunteer project against the live production site, with no test environment and no test data (no test card, no sandbox). The checkout tests therefore stop at the moment the payment step opens: on gratis.com this is an in-page "Ödeme" modal (the page stays on `/checkout`), and that is all we can verify. No card details are entered and no order is ever placed.

## Auth — `AuthTests.java`

Login and registration are the same phone-number + OTP flow on this site; there is no password and no forgot-password flow.

| TC | Purpose |
|----|---|
| TC_001 | A brand-new phone number can register: enter the number, type the OTP by hand, fill in name, surname, e-mail and birth date, accept the Gratis Kart and agreement checkboxes, confirm, and end up on the homepage logged in. |
| TC_002 | An already-registered number goes through OTP login and lands on the homepage with the account's name shown in the header. |
| TC_003 | A wrong OTP is rejected with the site's validation error and the user is not logged in. |
| TC_004 | An invalid phone number format (repeated digits) is blocked with "Son 7 hane aynı olamaz." before any OTP is sent. |
| TC_005 | A logged-in user can log out from the account menu and return to a guest state. |

## Navigation — `NavigationTests.java`

| TC | Purpose |
|----|---|
| TC_006 | Hovering the "Makyaj" header menu and clicking "Ruj" opens the correct category page (`/makyaj/ruj-c-5010101`). |
| TC_007 | On a mobile viewport, the hamburger menu opens and can be drilled down through the accordion (Cilt Bakım → Yüz Bakım → Tonikler). |
| TC_008 | The basket counter in the header updates to 1 and then 2 as two products are added from a listing page. |
| TC_009 | Clicking the header logo from a category page returns to the homepage. |

## Search — `SearchTests.java`

| TC | Purpose |
|----|---|
| TC_010 | Typing a keyword in the search bar shows autocomplete suggestions, and every suggestion contains that keyword. |
| TC_011 | Searching a valid keyword and pressing Enter shows a results page for that keyword, and a product opened from it relates to the keyword. |
| TC_012 | Searching a nonsense keyword shows the "Sonuç Bulunamadı" empty-results state. |

## Filter & Sort — `FilterSortTests.java`

| TC | Purpose |
|----|---|
| TC_013 | Filtering a category by brand (Wella) updates the URL with the brand parameter and the listed products belong to that brand. |
| TC_014 | Sorting by "Çok Satanlar" and by "Fiyat Artan" is reflected in the URL sort parameter each time. |
| TC_015 | Applying a brand filter genuinely re-renders the product list (the first product changes). |

## Catalog (PLP/PDP) — `CatalogTests.java`

| TC | Purpose |
|----|---|
| TC_016 | A product detail page opens from a listing and shows the product title heading. |
| TC_017 | A logged-in user can add a product to the cart from its detail page and sees the success message. |
| TC_018 | The product image gallery works: next image changes the main image, the zoom lightbox opens, zooms in/out and closes, and the reviews tab opens. |
| TC_019 | A logged-in user can add products to the "Favorilerim" list through the "Listeye Ekle" dialog (success toast), remove one again (removal toast), and the saved product shows up on the wishlist page. |
| TC_020 | A guest is redirected to `/login` when using the wishlist from the header, from a listing, and from a product page. |
| TC_021 | A logged-in user can remove a product from the wishlist page and sees the removal message. |

## Cart — `CartTests.java`

Runs with a saved logged-in session. TC_023, TC_024 and TC_027 depend on TC_022 (and TC_027 on TC_024) because they need the item that TC_022 puts in the cart.

| TC | Purpose |
|----|---|
| TC_022 | A product can be added to the cart both from the listing page and from its detail page, and it appears in the cart each time. |
| TC_023 | The quantity of a cart item can be increased and decreased, and the displayed quantity changes accordingly. |
| TC_024 | A cart item can be reduced to quantity 1 and then removed, after which it is no longer in the cart. |
| TC_025 | An invalid or expired promo code is rejected with the "Kupon Kodu geçersizdir." message (only invalid codes are covered, no valid test codes exist). |
| TC_026 | An item added to the cart is still there after the page is reloaded. |
| TC_027 | "Tümünü Sil" removes every item and the empty-cart message is shown. |

## Checkout — `CheckoutTests.java`

Runs with a saved logged-in session and stops before any payment (see Payment Limitation above).

| TC | Purpose |
|----|---|
| TC_028 | Store pickup (Gel-Al): choosing city, district and store and accepting the terms shows that a billing address is required, by opening the "Yeni Adres Ekle" dialog. Nothing is filled in. |
| TC_029 | Home delivery: choosing "Kargo", creating a delivery address if none exists, accepting the terms and pressing "ÖDEME ADIMINA GEÇ" opens the payment step. The test stops there. |
| TC_030 | With an empty cart the checkout cannot be reached: after clearing the cart the empty-cart message is shown and the "TESLİMAT ADIMINA GEÇ" button is not. |
