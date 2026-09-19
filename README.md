# Gratis.com – Playwright + TestNG UI Automation

End-to-end UI test automation for **[gratis.com](https://www.gratis.com)**, a large Turkish
cosmetics e-commerce site. 30 test cases across authentication, navigation, search,
filtering, product pages, wishlist, cart and checkout, written in **Java** with
**Playwright** and **TestNG** using the **Page Object Model**.

It runs against the real, live site, not a mock or a staging copy, so much of the work
went into making tests reliable against a production front end: OTP-only login,
look-alike elements, flaky clicks and a third-party payment widget. The choices made
for those are described below.

> This is the Playwright + TestNG entry in a four-part QA automation portfolio, alongside
> a Selenium + Cucumber project, an Appium project and a REST Assured + JMeter project.

## Highlights

- **30 test cases in 7 areas** (Auth, Navigation, Search, Filter & Sort, Catalog, Cart,
  Checkout). Each has a documented purpose in [`TEST_CASES.md`](TEST_CASES.md).
- **Page Object Model.** Locators and page actions live in `src/main/java/com/gratis/pages/`;
  tests only describe the flow and the assertions.
- **Web-first assertions and auto-waiting** (`assertThat(locator).isVisible()`, `hasText`,
  `hasURL`) instead of fixed sleeps.
- **Logged-in tests without automating the OTP**, by reusing saved session cookies.
- **Three suites**: full regression, a tagged smoke suite, and a manual auth suite.
- **Desktop and mobile viewports**, failure screenshots, and TestNG's HTML report.

## What Is Covered

| Area | Test class | Test cases | What it checks |
|---|---|---|---|
| Auth | `AuthTests` | TC_001–005 | Phone + OTP registration and login, wrong OTP, invalid phone format, logout |
| Navigation | `NavigationTests` | TC_006–009 | Mega menu, mobile hamburger menu, basket counter, logo redirect |
| Search | `SearchTests` | TC_010–012 | Autocomplete, valid keyword, empty-results state |
| Filter & Sort | `FilterSortTests` | TC_013–015 | Brand filter, sorting, list re-render |
| Catalog | `CatalogTests` | TC_016–021 | Product page, gallery/zoom, add to cart, wishlist add/remove, guest redirect |
| Cart | `CartTests` | TC_022–027 | Add from listing and product page, quantity, remove, promo code, persistence, clear |
| Checkout | `CheckoutTests` | TC_028–030 | Store pickup, home delivery, empty-cart guard (stops before payment) |

## Tech Stack

| Layer | Tool |
|---|---|
| Language | Java 17 |
| Browser automation | Playwright for Java 1.47 |
| Test runner | TestNG 7.10 |
| Build | Maven |
| Logging | SLF4J + Log4j2 |

## Browser Support

The tests currently run on **Chromium**, the Chrome-based browser bundled with Playwright
(desktop viewport, plus a mobile viewport emulated in Chromium for the mobile navigation
test). Support for more browsers (Firefox and WebKit) is planned for a future version.

## Engineering Decisions

**Login without automating the OTP.** gratis.com has no password: login and registration
are one phone-number + SMS OTP flow, with no sandbox or test code. Instead of repeating
it on every run, `SessionCaptureTests` is run once by hand (a human types the real code)
and saves the session cookies with Playwright's `storageState()`. Logged-in tests then
load them into a fresh browser context and start already authenticated. The session
token lasts about 15 minutes, so it is re-captured shortly before a run. The OTP tests
themselves pause for a human to type the code.

**Finding the right element.** A test finds things on the page with locators. On gratis.com
the same element often appears several times in the page: the wishlist link exists four
times and only one copy is visible, and every product card has its own heart and "+"
button. A test that just says "click the wishlist link" could hit a hidden copy or the
wrong product, so locators are narrowed down to one product card, one cart row or one
dialog, or to the visible copy only.

**Retry with verification for flaky actions.** A few interactions are genuinely
non-deterministic, such as the "Saç Bakım" header link and opening the payment step. Those
page-object methods retry the click and only continue once its result (the URL change or
the modal) is confirmed.

**Page objects created inside each test method.** Fields initialised at class level run
before `@BeforeMethod` has created the browser page, which caused null-pointer errors. Each
test builds the page objects it needs after setup, so every test also gets a fresh browser.

**Tests stop before payment.** This is a volunteer project on the live production site with
no test environment, test card or test data, so no payment or order can be tested. The
checkout tests stop when the payment step opens (an in-page "Ödeme" modal; the page stays
on `/checkout`). No card details are entered and no order is placed.

## Project Structure

```
src/main/java/com/gratis/
├── base/        BaseTest (fresh guest page per test, failure screenshots) and
│                LoggedInBaseTest (same, but starts logged in from the saved session)
├── config/      ConfigReader (loads config.properties)
├── driver/      PlaywrightFactory (desktop, mobile and logged-in browser setup)
└── pages/       Page objects: HeaderComponent, LoginPage, PLPPage, PDPPage,
                 WishListPage, CartPage, CheckoutPage

src/test/java/gratis/com/tests/
├── AuthTests.java            TC_001–005
├── NavigationTests.java      TC_006–009
├── SearchTests.java          TC_010–012
├── FilterSortTests.java      TC_013–015
├── CatalogTests.java         TC_016–021
├── CartTests.java            TC_022–027
├── CheckoutTests.java        TC_028–030
└── SessionCaptureTests.java  Manual helper that saves the login session (not part of the suites)

src/XML_files/
├── testng.xml                Full unattended regression suite, one <test> block per area
├── testng-smoke.xml          One representative test per area, selected by groups="smoke"
└── auth.xml                  AuthTests only, run manually (needs a real OTP)

src/test/resources/
├── config.properties.example Template for your local config
└── config.properties         Your local config (gitignored)
```

## Getting Started

### Prerequisites

- JDK 17 or newer and Maven
- A phone number you can receive a real SMS on (gratis.com only supports OTP login)

Playwright downloads its browsers automatically the first time it runs.

### Setup

```bash
git clone https://github.com/gamzeozakinci/GratisProject.git
cd GratisProject
cp src/test/resources/config.properties.example src/test/resources/config.properties
```

`config.properties` is gitignored because it holds a real phone number. Keep
`config.properties.example` (placeholders only) in sync when you add a key.

Save `config.properties` as **UTF-8**: it contains Turkish characters (for example the
search word `göz`) and is read as UTF-8, so another encoding turns them into `�` and the
search tests fail. In IntelliJ, set this under Settings → Editor → File Encodings →
"Default encoding for properties files" → UTF-8.

### Before running the suites

1. **Set your phone number and register it.** Put the same phone number in both
   `phone.number` and `registered.phone.number` in `src/test/resources/config.properties`.
   If that number isn't already a gratis.com account, run
   `AuthTests.newPhoneNumberCompletesRegistration` (TC_001) once to register it: type the
   SMS code into the browser when it pauses, then click Resume (▶) in the Playwright
   Inspector.
2. **Set `registered.account.name`** to the name gratis.com shows in the header once you're
   logged in with that account (TC_002 checks for it).
3. **Choose headless or visible.** `headless=false` shows the browser while tests run; set
   `headless=true` to run without a window. The OTP tests (`AuthTests`,
   `SessionCaptureTests`) need a visible browser to type the code into, so keep
   `headless=false` for those.
4. **Save the login session right before running the suites.** Run
   `SessionCaptureTests.captureLoggedInSession`, enter the SMS code when it pauses and click
   Resume. This saves your login cookies to `src/test/resources/auth-state.json`
   (gitignored), which almost every test uses to start logged in without the OTP. The saved
   session only lasts about 15 minutes; when logged-in tests start redirecting to the login
   page, run it again.

### Running

```bash
mvn test -DsuiteXmlFile=src/XML_files/testng.xml         # full unattended regression
mvn test -DsuiteXmlFile=src/XML_files/testng-smoke.xml   # smoke subset (6 tests, no OTP)
mvn test -DsuiteXmlFile=src/XML_files/auth.xml            # Auth only, needs a real OTP
```

You can also run a single class or method from your IDE. Any config key can be overridden
on the command line, for example `mvn test -Dheadless=false`.

### Reports

After a run, open `test-output/index.html` for TestNG's HTML report. Failure screenshots are
saved to `test-output/screenshots/`.

## Configuration

All environment and test-data values live in `src/test/resources/config.properties`:

| Key | Purpose |
|---|---|
| `base.url` | Site under test |
| `headless` | Run the browser without a visible window |
| `default.timeout` | Default Playwright timeout in milliseconds |
| `registered.phone.number` | Phone number of your account (used for login and session capture) |
| `phone.number` | Number used by TC_001 to register (use the same number) |
| `registered.account.name` | Name shown in the header once logged in |
| `register.first.name`, `register.last.name`, `register.email`, `register.birth.date` | Details typed into the registration form (TC_001) |
| `search.keyword` | Word searched by the search tests (TC_010, TC_011); must return products |
| `invalid.phone.number` | Invalid phone number typed by TC_004 |
| `address.first.name`, `address.last.name`, `address.title`, `address.city`, `address.district`, `address.neighborhood`, `address.detail` | Address typed at checkout (TC_029); city and district also pick the store in TC_028. Write them as the site's dropdowns show them (upper case, Turkish characters) |
| `invalid.promo.code` | Fake promo code typed by the invalid-promo-code test (TC_025) |
| `screenshot.on.failure`, `screenshot.dir` | Failure screenshot behaviour and location |
