# Gratis.com – Playwright + TestNG Automation Framework

UI test automation framework for **gratis.com** (cosmetics e-commerce), built from a
28-case test suite spanning Auth, Navigation, Search/Filtering, Catalog, Cart and
Checkout. Java + Playwright + TestNG, Page Object Model, no external reporting layer —
TestNG's own HTML/XML report is generated under `test-output/` after every run.

This is the **Playwright + TestNG** entry in a 4-part QA automation portfolio
(alongside a Selenium + Cucumber project, an Appium project, and a REST Assured +
JMeter project).

## Tech Stack

| Layer | Tool |
|---|---|
| Browser automation | Playwright for Java 1.47 |
| Test runner | TestNG 7.10 |
| Build | Maven |
| Logging | SLF4J + Log4j2 |
| Language | Java 17 |

## Project Structure

```
src/main/java/com/gratis/
├── base/        BasePage (just the shared Page + logger - Page Objects call
│                Playwright's own Locator/Page methods directly), BaseTest (TestNG lifecycle)
├── config/      ConfigReader (loads config.properties)
├── driver/      PlaywrightFactory (ThreadLocal browser/context/page per test thread)
├── pages/       Page Objects: HeaderComponent, HomePage, LoginPage (handles both
│                login AND registration - see "Auth Flow" below), PLPPage, PDPPage,
│                WishlistPage, CartPage, CheckoutPage, OrderPage
└── utils/       Constants (payloads, group names), TestDataGenerator (unique phone numbers)

src/test/java/com/gratis/tests/
├── AuthTests.java           TC_001–TC_004 (login and registration are one flow on this site)
├── NavigationTests.java     TC_005, TC_006, TC_007, TC_008
├── SearchTests.java         TC_009, TC_010, TC_011
├── FilterSortTests.java     TC_012, TC_013
├── CatalogTests.java        TC_014, TC_015, TC_016, TC_017, TC_018
├── CartTests.java           TC_019–TC_024
├── CheckoutTests.java       TC_025–TC_027 (payment intentionally stops at URL check)
└── OrderTests.java          TC_028

src/XML_files/testng.xml          Full suite, grouped by module, sequential (no parallel="...")
src/XML_files/testng-smoke.xml    Smoke-only subset (TC_001–004)
```

## Running

```bash
mvn test                                                          # full suite
mvn test -DsuiteXmlFile=src/XML_files/testng-smoke.xml            # smoke subset only
```
There's no `-Dgroups=...` filtering anymore - `@Test` group tags were dropped along with
`Constants.java` when the framework was simplified. Run a single module by pointing
`mvn test` at a one-off suite XML listing just that module's class(es), or run a class
directly from your IDE.
Report: open `test-output/index.html` after a run. Failure screenshots land in
`test-output/screenshots/`.

## Configuration

All environment/test-data values live in `src/test/resources/config.properties`
(base URL, browser, headless flag, viewport sizes, known test accounts, promo codes,
mock OTP). Override any key at the CLI, e.g. `mvn test -Dbrowser=firefox -Dheadless=false`.

`config.properties` is gitignored because `registered.phone.number` ends up holding a
real phone number once you're testing against your own account. First-time setup:

```bash
cp src/test/resources/config.properties.example src/test/resources/config.properties
# then fill in registered.phone.number (and anything else) with your real test values
```

`config.properties.example` (committed, placeholders only) stays in sync with whatever
keys the framework actually reads — update both files together when you add a key.

## Auth Flow (verified against the live site)

gratis.com does **not** have an email/password form, and there is no separate
registration page or "forgot password" flow. Opening "Üye olun ya da Giriş Yapın"
shows a single screen ("Giriş Yap / Üye Ol - Telefon numaranızla giriş yapabilir ya da
yeni bir hesap oluşturabilirsiniz") that takes a phone number, then an OTP code, and
transparently creates the account on first use or logs an existing number straight in.

This was confirmed by opening the live flow in a browser, not inferred from a spec, so
`LoginPage` (phone + OTP), `HomePage.goToAuth()`, and `AuthTests` (TC_001-TC_004) are
built directly against it. Two deliberate scope changes from a typical email/password
suite:

- **No `RegisterPage`, and no separate `RegistrationTests`/`LoginTests` classes** -
  registration and login are the exact same form/flow on gratis.com, so both live in
  one `AuthTests` class instead of two, and both share `LoginPage`.
- **No `ForgotPasswordPage`** - there's no password to forget. TC_004 was repurposed
  from "forgot password" to "an invalid phone number format blocks 'DEVAM ET'
  before an OTP is ever sent," which is the closest equivalent negative case this
  auth mechanism actually has.

The exact input/button selectors in `LoginPage` are still best-effort (see the section
below) - only the *flow itself* (phone → OTP → logged in) is DOM-verified. There's no
mock/sandbox OTP bypass on the live site, so `AuthTests` pauses execution
(`page.pause()`) for a human to read the real SMS and type the code in manually -
see `AuthTests` and the "Real OTP/SMS delivery" note below.

## ⚠️ Important: Locators Need Verification Against the Live DOM

This framework was built directly from a **written test case specification**
(`gratis_test_cases.pdf`), not from inspecting gratis.com's actual rendered HTML.
The selectors in each Page Object (`input[name='email']`, `.product-card`,
`button.qty-increase`, etc.) are reasonable, spec-consistent guesses — they encode the
right *structure and flow* of every test case, but several will not match the real
site's markup until you:

1. Open the actual page in DevTools and note real attributes (ideally `data-testid`,
   otherwise stable class/id names).
2. Swap each locator in the relevant Page Object — the method signatures and test
   logic stay untouched, only the locator body changes.
3. Re-run `mvn test -Dgroups=smoke` first to validate Auth locators before moving on.

Treat this as the framework's skeleton and flow logic being complete; the "last mile"
of exact selectors is a deliberate follow-up step, and a good thing to mention as such
if you're presenting this project to recruiters — it shows you understand the
difference between framework architecture and environment-specific implementation
detail.

## Assumptions & Fixture Data

Several test cases assume backend state that a UI-only framework can't create on the
fly (an existing account reachable at `registered.phone.number`, seeded in/out-of-stock
products, an active promo code, a cart pre-loaded with 100 TL of goods). These are
documented per-test as comments and centralized in `config.properties` — replace the
placeholder values/slugs with your test environment's real seeded data or a
`@BeforeMethod` API/DB setup call.

## Out of Scope / Future Work

- **Real OTP/SMS delivery** (TC_001-TC_004 login/registration) needs a real SMS
  provider API (Twilio, etc.) to automate fully — not implemented here; `AuthTests`
  pauses (`page.pause()`) for a human to enter the real code instead.
- **Payment (TC_027 in an earlier draft) was deliberately removed**, not just left
  as a TODO — this is a personal project against the live production site with no
  test card credentials, and actually completing a real payment isn't something to
  automate here. TC_027 now only checks that the checkout flow reaches the correct
  payment URL; nothing past that point is exercised.
- **Email verification** (TC_028 order confirmation) needs a mailbox API (e.g.
  Mailinator's REST API) — not implemented here.
- **DB/API assertions** (e.g. TC_001 checking `/api/auth/register` returns a JWT)
  need either Playwright's `page.waitForResponse()` for the network layer, or a DB
  connector for backend checks — hooks are noted as comments where relevant.
- **Visual/hover interactions** (TC_005 mega menu fade timing, TC_016 image zoom lens,
  mobile pinch-to-zoom) would benefit from Playwright's screenshot-diffing rather than
  DOM assertions alone.
- **Session persistence** (TC_024) is approximated with `page.reload()`; a fuller
  implementation would use `context.storageState()` to snapshot/restore cookies across
  a genuinely new browser context, closer to "close all tabs, reopen the browser."
- **CI**: intentionally kept out of this version. A GitHub Actions workflow (checkout →
  Playwright browser install → `mvn test` → upload `test-output/`) is a natural next
  step if you want this added later.

## Notes on AI-Assisted Development

This framework's structure, Page Object boilerplate, and TestNG scaffolding were
drafted with AI assistance from a written test case spec, then intentionally left with
clear TODO-style comments where real DOM inspection or backend hooks are still needed
— rather than presenting placeholder locators as if they were verified. If you're
listing this project on a CV/GitHub, that's worth stating plainly (e.g. "scaffolded
with AI assistance, locators and CI verified manually") rather than glossing over it.
