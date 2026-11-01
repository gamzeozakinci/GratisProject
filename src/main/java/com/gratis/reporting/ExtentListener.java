package com.gratis.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

/**
 * Writes the ExtentReports HTML report (target/extent-report/index.html).
 * Registered once on BaseTest via @Listeners, so every test class that extends it
 * is reported, from Maven or from the IDE.
 */
public class ExtentListener implements ITestListener {

    private static final Logger log = LoggerFactory.getLogger(ExtentListener.class);
    private static final Path REPORT_PATH = Path.of("target/extent-report/index.html");

    private static ExtentReports extent;
    private static ExtentTest current;
    private static ITestResult currentResult;

    private static synchronized ExtentReports extent() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter(REPORT_PATH.toString());
            spark.config().setDocumentTitle("Gratis.com Test Report");
            spark.config().setReportName("Gratis.com Playwright + TestNG");
            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Browser", "Chromium (Playwright)");
        }
        return extent;
    }

    // The failure screenshot is taken by BaseTest.tearDown(), which runs after the
    // test method, so BaseTest hands the file over to the test that is currently
    // being reported. Embedded as base64 so the report stays one portable file.
    public static synchronized void attachScreenshot(Path file) {
        if (current == null) {
            return;
        }
        try {
            String base64 = Base64.getEncoder().encodeToString(Files.readAllBytes(file));
            current.info("Screenshot at the moment of failure",
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
        } catch (IOException e) {
            log.warn("Could not attach screenshot {} to the report", file, e);
        }
    }

    @Override
    public synchronized void onTestStart(ITestResult result) {
        String description = result.getMethod().getDescription();
        String name = (description == null || description.isBlank())
                ? result.getMethod().getMethodName() : description;
        currentResult = result;
        current = extent().createTest(name)
                .assignCategory(result.getTestClass().getRealClass().getSimpleName());
    }

    @Override
    public synchronized void onTestSuccess(ITestResult result) {
        current.pass("Passed");
    }

    @Override
    public synchronized void onTestFailure(ITestResult result) {
        current.fail(result.getThrowable());
    }

    @Override
    public synchronized void onTestSkipped(ITestResult result) {
        // Tests skipped because a test they depend on failed never "start", so they
        // have no report entry yet - open one instead of writing into the previous test's.
        if (currentResult != result) {
            onTestStart(result);
        }
        current.skip(result.getThrowable() != null ? result.getThrowable().toString() : "Skipped");
    }

    @Override
    public synchronized void onFinish(ITestContext context) {
        extent().flush();
    }
}
