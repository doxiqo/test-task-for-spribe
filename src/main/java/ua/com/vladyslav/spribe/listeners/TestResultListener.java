package ua.com.vladyslav.spribe.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import ua.com.vladyslav.spribe.logging.Log;

public class TestResultListener implements ITestListener {

    private static final Log LOG = Log.get(TestResultListener.class);

    @Override
    public void onStart(ITestContext context) {
        LOG.info("============================================================");
        LOG.info("Starting test suite: {}", context.getName());
        LOG.info("============================================================");
    }

    @Override
    public void onTestStart(ITestResult result) {
        LOG.info(">>> STARTED: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        LOG.info("<<< PASSED: {} ({} sec)", result.getMethod().getMethodName(), duration / 1000.0);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Throwable cause = result.getThrowable();
        LOG.error("<<< FAILED: {}", result.getMethod().getMethodName());
        LOG.error("Reason: {}", cause != null ? cause.toString() : "Unknown error");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOG.warn(">>> SKIPPED: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onFinish(ITestContext context) {
        LOG.info("============================================================");
        LOG.info("Finished test suite: {}", context.getName());
        LOG.info("Summary: Passed={} | Failed={} | Skipped={} | Total={} sec",
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size(),
                (context.getEndDate().getTime() - context.getStartDate().getTime()) / 1000.0);
        LOG.info("============================================================");
    }
}