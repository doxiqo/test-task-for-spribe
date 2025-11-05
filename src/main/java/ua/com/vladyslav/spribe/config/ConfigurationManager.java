package ua.com.vladyslav.spribe.config;

import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.parsing.Parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.Properties;

public class ConfigurationManager {
    private static final ConfigurationManager INSTANCE = new ConfigurationManager();
    private final Properties properties;
    private final String env;

    private ConfigurationManager() {
        properties = new Properties();
        env = System.getProperty("env", "dev");
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

    public void attachAllureEnvironmentInfo() {
        Allure.parameter("Environment", env);
        Allure.parameter("Base URL", getProperty("api.base.url"));

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            Allure.parameter(entry.getKey().toString(), entry.getValue().toString());
        }
    }
}
