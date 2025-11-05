package ua.com.vladyslav.spribe.listeners;

import org.testng.IAlterSuiteListener;
import org.testng.xml.XmlSuite;
import ua.com.vladyslav.spribe.config.ConfigurationManager;
import ua.com.vladyslav.spribe.logging.Log;

import java.util.List;

public class ThreadConfigAlterListener implements IAlterSuiteListener {
    private static final Log LOG = Log.get(ThreadConfigAlterListener.class);

    @Override
    public void alter(List<XmlSuite> suites) {
        String value = System.getProperty("thread.count");
        if (value == null || value.isBlank()) {
            value = ConfigurationManager.getInstance().getProperty("thread.count");
        }

        if (value != null && !value.isBlank()) {
            try {
                int threads = Integer.parseInt(value.trim());
                for (XmlSuite suite : suites) {
                    suite.setThreadCount(threads);
                    suite.setDataProviderThreadCount(threads);
                    suite.getTests().forEach(test -> test.setThreadCount(threads));
                }
                LOG.info("ThreadConfigAlterListener applied thread.count= {}", threads);
            } catch (NumberFormatException e) {
                LOG.error("Invalid thread.count: {}", value);
            }
        }
    }
}