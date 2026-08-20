package com.gratis.base;

import com.gratis.config.ConfigReader;
import com.gratis.driver.PlaywrightFactory;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.nio.file.Path;

public abstract class BaseTest {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected Page page;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        page = PlaywrightFactory.initPage();
        page.navigate(ConfigReader.baseUrl());
        log.info("Navigated to {}", ConfigReader.baseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE && ConfigReader.getBoolean("screenshot.on.failure")) {
            takeScreenshot(result.getMethod().getMethodName());
        }
        PlaywrightFactory.tearDown();
    }

    private void takeScreenshot(String testName) {
        try {
            Path dir = Path.of(ConfigReader.get("screenshot.dir"));
            File dirFile = dir.toFile();
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            Path target = dir.resolve(testName + "_" + System.currentTimeMillis() + ".png");
            page.screenshot(new Page.ScreenshotOptions().setPath(target).setFullPage(true));
            log.warn("Test {} failed - screenshot saved to {}", testName, target);
        } catch (Exception e) {
            log.error("Could not capture failure screenshot for {}", testName, e);
        }
    }
}
