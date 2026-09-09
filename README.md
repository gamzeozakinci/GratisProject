# Gratis.com – Playwright + TestNG Automation Framework

UI test automation framework for **gratis.com** (cosmetics e-commerce), built from a
30-case test suite spanning Auth, Navigation, Search/Filtering, Catalog, Cart and
Checkout. Java + Playwright + TestNG, Page Object Model, no external reporting layer —
TestNG's own HTML/XML report is generated under `test-output/` after every run.

This is the **Playwright + TestNG** entry in a 4-part QA automation portfolio
(alongside a Selenium + Cucumber project, an Appium project, and a REST Assured +
JMeter project).

Every locator in this project was checked against gratis.com's real, live DOM —
not guessed from a spec. See `TEST_CASES.md` for the current, code-accurate status
of every test case, including known issues found along the way.

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
├── base/        BaseTest (TestNG lifecycle - fresh guest page per test),
│                LoggedInBaseTest (same, but starts every test already logged in
│                via a saved session - see "Login & Sessions" below)
├── config/      ConfigReader (loads config.properties, UTF-8 explicitly)
├── driver/      PlaywrightFactory (plain static fields, not ThreadLocal - see its
│                own class comment for why; initPage/initMobilePage/initLoggedInPage)
└── pages/       Page Objects: HeaderComponent, LoginPage (handles both login AND
                 registration - see "Auth Flow" below), PLPPage, PDPPage, CartPage,
                 CheckoutPage (in progress)

src/test/java/com/gratis/tests/
├── AuthTests.java           TC_001–TC_005 (login and registration are one flow on this site)
├── NavigationTests.java     TC_006–TC_009
├── SearchTests.java         TC_010–TC_012
├── FilterSortTests.java     TC_013–TC_015
├── CatalogTests.java        TC_016–TC_021
├── CartTests.java           TC_022–TC_027
├── CheckoutTests.java       TC_028–TC_030 (payment intentionally stops at URL check)
└── SessionCaptureTests.java Not part of the suite - run manually to (re)create a
                             saved login session; see "Login & Sessions" below

src/XML_files/testng.xml          Full suite, one <test> block per module, sequential
                                   (no parallel="..." - see PlaywrightFactory's comment)
src/XML_files/testng-smoke.xml    Smoke-only subset (TC_001–005)
```

## Running

```bash
mvn test                                                          # full suite
mvn test -DsuiteXmlFile=src/XML_files/testng-smoke.xml            # smoke subset only
```
Run a single module by pointing `mvn test` at a one-off suite XML listing just that
module's class(es), or run a class/method directly from your IDE.
Report: open `test-output/index.html` after a run. Failure screenshots land in
`test-output/screenshots/`.

## Configuration

All environment/test-data values live in `src/test/resources/config.properties`
(base URL, headless flag, timeouts, known test accounts, promo codes). Override any
key at the CLI, e.g. `mvn test -Dheadless=false`.

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
either creates a new account (asking for name/surname/email/birth date first) or logs
an existing number straight in.

Two deliberate scope decisions that follow from this, both because the flow itself
doesn't separate them:

- **No separate `RegisterPage`, no separate `RegistrationTests`/`LoginTests` classes** —
  registration and login are the same form on gratis.com, so both live in one
  `AuthTests` class, sharing one `LoginPage`.
- **No `ForgotPasswordPage`** — there's no password to forget. TC_004 covers the
  closest equivalent negative case this auth mechanism actually has: an invalid phone
  number format is blocked before DEVAM ET will even submit.

There's no mock/sandbox OTP bypass on the live site, so `AuthTests` and
`SessionCaptureTests` pause execution (`page.pause()`) for a human to read the real SMS
and type the code in manually.

## Login & Sessions

Several tests (`TC_008`, `TC_017`, `TC_019`, all of `CartTests`) need to already be
logged in — this site has no guest cart or guest wishlist at all (confirmed live: every
entry point redirects a guest straight to `/login`). Rather than a real phone+OTP flow
on every single run, those tests load a previously saved session:

```bash
# Run this manually first (or whenever a login-dependent test starts getting
# redirected to /login) - it needs a real OTP typed in by hand.
SessionCaptureTests.captureLoggedInSession
```

This uses Playwright's `BrowserContext.storageState()` to snapshot cookies +
localStorage to `src/test/resources/auth-state.json` (gitignored — it holds a real,
valid session for a real account), which `PlaywrightFactory.initLoggedInPage()` then
loads into a fresh browser context for any test that needs to start out authenticated.

**This session is short-lived — about 15 minutes.** gratis.com's backend (Retter.io)
issues a JWT access token with a real `exp` claim that short; the site's own
client-side SDK would normally refresh it silently during a real browsing session, but
that refresh logic never runs here because the server-side redirect to `/login` happens
before any client JS loads. In practice: run `SessionCaptureTests` shortly before you
run anything that depends on it, not once at the start of a long session.

`CartTests` extends `LoggedInBaseTest` (every test in the class needs login).
`NavigationTests.TC_008` and `CatalogTests.TC_017`/`TC_019` instead swap to a
logged-in page inline, mid-test — same pattern `TC_007` already uses to swap to a
mobile-sized page — since those classes also contain guest-only tests that must *not*
start out logged in (e.g. `TC_020` specifically tests the guest redirect itself).

## Assumptions & Fixture Data

A few test cases assume backend state a UI-only framework can't create on the fly: an
existing account reachable at `registered.phone.number`. This lives in
`config.properties` rather than being seeded automatically.

## Out of Scope / Future Work

- **Real OTP/SMS delivery** needs a real SMS provider API (Twilio, etc.) to automate
  fully — not implemented; tests pause (`page.pause()`) for a human to enter the code.
- **Payment** was deliberately removed, not left as a TODO — this is a personal
  project against the live production site with no test card credentials, and
  completing a real payment isn't something to automate here. `TC_028`/`TC_029` only
  check that checkout reaches the correct payment URL; nothing past that point is
  exercised.
- **Order confirmation / post-order validation** (a former `OrderTests` class) would
  need a real order to actually go through, which isn't attempted here for the same
  no-real-payment reason above — not implemented.
- **A refresh-token flow for the saved login session** would remove the ~15-minute
  window described above, but would mean reverse-engineering Retter.io's own refresh
  endpoint — more complexity than this project needs right now.
- **`TC_001`'s phone number is static**, so a second run of the "brand-new number"
  registration test hits "already registered" instead of a fresh signup. A
  unique-phone-number generator would fix this and isn't wired in yet.
- **TC_005, TC_012, TC_015, TC_021, TC_027, TC_030** (logout, no-results search, clear
  filters, wishlist removal, actual cart-item deletion, empty-cart checkout guard) are
  stubbed with `@Test` + `// TODO: implement` but have no body yet — see `TEST_CASES.md`.
- **CI**: intentionally kept out of this version. A GitHub Actions workflow (checkout →
  Playwright browser install → `mvn test` → upload `test-output/`) is a natural next
  step, though the manual-OTP and short-lived-session pieces above would need solving
  first for anything login-dependent to run unattended.
