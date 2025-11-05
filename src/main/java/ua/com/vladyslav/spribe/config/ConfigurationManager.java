package ua.com.vladyslav.spribe.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.parsing.Parser;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;

public class ConfigurationManager {
    private static final ConfigurationManager INSTANCE = new ConfigurationManager();
    private final Properties properties;

    private ConfigurationManager() {
        properties = new Properties();
        String env = System.getProperty("env", "dev");
        String configFileName = "config/" + env + ".properties";

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configFileName)) {
            if (inputStream == null) {
                throw new RuntimeException("Cannot find configuration file: " + configFileName);
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file: " + configFileName, e);
        }
    }

    public static ConfigurationManager getInstance() {
        return INSTANCE;
    }

    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property not found: " + key);
        }
        return value;
    }

    public void restAssuredSetup() {
        PrintStream logStream = System.out;

        RestAssured.config = RestAssured.config()
                .logConfig(new LogConfig(logStream, true)
                        .enableLoggingOfRequestAndResponseIfValidationFails()
                        .blacklistHeader("Authorization"));

        RestAssured.defaultParser = Parser.JSON;
    }

    public void applyLogLevel() {
        String levelName = getLogLevel();
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        Level level = Level.toLevel(levelName, Level.INFO);
        context.getLogger("ua.com.vladyslav.spribe").setLevel(level);
        context.getLogger("io.restassured").setLevel(level);
        context.getLogger("org.testng").setLevel(Level.WARN);

        System.out.printf("[LOG CONFIG] Set log level to %s%n", level);
    }

    public String getLogLevel() {
        String level = properties.getProperty("log.level", "INFO");
        return level.trim().toUpperCase();
    }
}
