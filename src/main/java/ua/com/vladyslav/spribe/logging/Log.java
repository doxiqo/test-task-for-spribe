package ua.com.vladyslav.spribe.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Log {
    private final Logger logger;

    private Log(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    public static Log get(Class<?> clazz) {
        return new Log(clazz);
    }

    public void info(String message, Object... args) {
        logger.info(message, args);
    }

    public void warn(String message, Object... args) {
        logger.warn(message, args);
    }

    public void error(String message, Object... args) {
        logger.error(message, args);
    }

    public void debug(String message, Object... args) {
        logger.debug(message, args);
    }
}