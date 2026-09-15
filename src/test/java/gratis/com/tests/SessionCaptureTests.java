package gratis.com.tests;

import com.gratis.base.BaseTest;
import com.gratis.driver.PlaywrightFactory;
import com.gratis.pages.HeaderComponent;
import com.gratis.pages.LoginPage;
import org.testng.annotations.Test;

/**
 * Not part of the normal suite - run this manually whenever
 * src/test/resources/auth-state.json is missing, or tests using
 * PlaywrightFactory.initLoggedInPage() start getting redirected to /login
 * (the saved session has expired). Needs a real OTP typed in by hand, same
 * as AuthTests.
 *
 * The saved session is short-lived - roughly 10-15 minutes. gratis.com's
 * backend (Retter.io) issues a real JWT "token" cookie with that lifetime
 * baked into its own exp claim; decoding it after a capture confirmed an
 * exact 15-minute iat-to-exp window. The site's own client-side SDK would
 * normally refresh it silently during a real browsing session, but that
 * refresh never gets a chance to run here - the server-side redirect to
 * /login happens before any client JS loads. Practical takeaway: run this
 * right before you run whatever login-dependent test(s) you actually need,
 * not once at the start of a long session.
 */
public class SessionCaptureTests extends BaseTest {

    @Test(description = "One-time: log in manually and save the session for reuse")
    public void captureLoggedInSession() {
        HeaderComponent hp = new HeaderComponent(page);
        LoginPage lp = new LoginPage(page);

        hp.openLoginOrRegister();
        lp.enterRegisteredPhoneNumber();
        lp.clickDevamEt();

        System.out.println("Check your phone, type the OTP directly into the browser, then click Resume (▶) " +
                "in the Playwright Inspector.");
        page.pause();

        page.waitForURL("https://www.gratis.com/");

        PlaywrightFactory.saveLoginState();
    }
}
