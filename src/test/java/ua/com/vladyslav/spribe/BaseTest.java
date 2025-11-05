package ua.com.vladyslav.spribe;

import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.xml.XmlSuite;
import ua.com.vladyslav.spribe.api.ApiClientFactory;
import ua.com.vladyslav.spribe.config.ConfigurationManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public abstract class BaseTest {

    @BeforeSuite(alwaysRun = true)
    public void globalSetup() {
        ConfigurationManager config = ConfigurationManager.getInstance();
        config.restAssuredSetup();
        writeAllureEnvironmentFile(config);
        System.out.println("--- Test Suite Setup: Configuration loaded ---");
    }

    @BeforeTest(alwaysRun = true)
    public void setupSuite(ITestContext context) {
        ConfigurationManager config = ConfigurationManager.getInstance();
        int threadCount = Integer.parseInt(config.getProperty("thread.count"));

        context.getSuite().getXmlSuite().setParallel(XmlSuite.ParallelMode.METHODS);
        context.getSuite().getXmlSuite().setThreadCount(threadCount);

        System.out.printf("--- Running tests in parallel with %d threads%n ---", threadCount);
    }

    @AfterSuite(alwaysRun = true)
    public void globalTeardown() {
        ApiClientFactory.cleanup();
        System.out.println("--- ApiClientFactory cleaned up after suite ---");
    }

    // thread-safe way to add config to Allure
    private void writeAllureEnvironmentFile(ConfigurationManager config) {
        Properties envProps = new Properties();
        envProps.put("Environment", config.getProperty("env"));
        envProps.put("Base URL", config.getProperty("api.base.url"));
        envProps.put("Thread Count", config.getProperty("thread.count"));
        envProps.put("Run ID", System.getProperty("log.session.id"));

        File allureEnvFile = new File("build/allure-results/environment.properties");

        try (FileWriter writer = new FileWriter(allureEnvFile)) {
            envProps.store(writer, "Allure environment information");
            System.out.printf("Environment info written to %s%n", allureEnvFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to write Allure environment.properties: " + e.getMessage());
        }
    }
}
