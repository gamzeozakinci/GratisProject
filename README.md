# Gratis.com – Playwright + TestNG Automation Framework

UI test automation framework for **gratis.com** (cosmetics e-commerce), built from a
30-case test suite spanning Auth, Navigation, Search/Filtering, Catalog, Cart and
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
├── base/        BasePage (common Playwright actions), BaseTest (TestNG lifecycle)
├── config/      ConfigReader (loads config.properties)
├── driver/      PlaywrightFactory (ThreadLocal browser/context/page per test thread)
├── pages/       Page Objects: HeaderComponent, HomePage, LoginPage (handles both
│                login AND registration - see "Auth Flow" below), PLPPage, PDPPage,
│                WishlistPage, CartPage, CheckoutPage, OrderPage
└── utils/       Constants (payloads, group names), TestDataGenerator (unique phone numbers)

src/test/java/com/gratis/tests/
├── RegistrationTests.java   TC_001, TC_002
├── LoginTests.java          TC_003, TC_004, TC_005
├── NavigationTests.java     TC_006, TC_007, TC_008, TC_009
├── SearchTests.java         TC_010, TC_011, TC_012
├── FilterSortTests.java     TC_013, TC_014
├── CatalogTests.java        TC_015, TC_016, TC_017, TC_018, TC_019
├── CartTests.java           TC_020–TC_025
├── CheckoutTests.java       TC_026–TC_029
└── OrderTests.java          TC_030

testng.xml          Full suite, grouped by module, parallel="tests"
testng-smoke.xml     Smoke-only subset (TC_001–005)
```

## Running

```bash
mvn test                                     # full suite (testng.xml)
mvn test -DsuiteXmlFile=testng-smoke.xml     # smoke subset only
mvn test -Dgroups=cart                       # any single module by TestNG group
```
Report: open `test-output/index.html` after a run. Failure screenshots land in
`test-output/screenshots/`.

## Configuration

All environment/test-data values live in `src/test/resources/config.properties`
(base URL, browser, headless flag, viewport sizes, known test accounts, promo codes,
mock OTP). Override any key at the CLI, e.g. `mvn test -Dbrowser=firefox -Dheadless=false`.

## Auth Flow (verified against the live site)

gratis.com does **not** have an email/password form, and there is no separate
registration page or "forgot password" flow. Opening "Üye olun ya da Giriş Yapın"
shows a single screen ("Giriş Yap / Üye Ol - Telefon numaranızla giriş yapabilir ya da
yeni bir hesap oluşturabilirsiniz") that takes a phone number, then an OTP code, and
transparently creates the account on first use or logs an existing number straight in.

This was confirmed by opening the live flow in a browser, not inferred from a spec, so
`LoginPage` (phone + OTP), `HomePage.goToAuth()`, `RegistrationTests` (TC_001, TC_002)
and `LoginTests` (TC_003-TC_005) are built directly against it. Two deliberate scope
changes from a typical email/password suite:

- **No `RegisterPage`** - registration and login are the same form/flow, so both test
  classes share `LoginPage`.
- **No `ForgotPasswordPage`** - there's no password to forget. TC_005 was repurposed
  from "forgot password" to "an invalid phone number format blocks 'DEVAM ET'
  before an OTP is ever sent," which is the closest equivalent negative case this
  auth mechanism actually has.

The exact input/button selectors in `LoginPage` are still best-effort (see the section
below) - only the *flow itself* (phone → OTP → logged in) is DOM-verified. `mock.otp` in
`config.properties` assumes a test/sandbox environment where OTP codes are predictable;
a real SMS round-trip needs a provider API (Twilio, etc.) wired into the fixture instead.

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

- **Real OTP/SMS delivery** (TC_001-TC_005 login/registration, TC_028 3D Secure) and
  **email verification** (TC_030 order confirmation) need a real SMS provider API and
  a mailbox API (e.g. Mailinator's REST API) respectively — not implemented here;
  `mock.otp` in `config.properties` stands in for a sandboxed/predictable OTP.
- **DB/API assertions** (e.g. TC_001 checking `/api/auth/register` returns a JWT,
  TC_028 checking order status = `PAID` in the database) need either Playwright's
  `page.waitForResponse()` for the network layer, or a DB connector for backend
  checks — hooks are noted as comments where relevant.
- **Visual/hover interactions** (TC_006 mega menu fade timing, TC_017 image zoom lens,
  mobile pinch-to-zoom) would benefit from Playwright's screenshot-diffing rather than
  DOM assertions alone.
- **Session persistence** (TC_025) is approximated with `page.reload()`; a fuller
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
