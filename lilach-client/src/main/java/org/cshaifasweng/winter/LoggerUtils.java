package org.cshaifasweng.winter;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerUtils {
    public static void setupLogger(Logger log) {
        log.setLevel(Level.ALL);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter());
        handler.setLevel(Level.ALL);
        log.addHandler(handler);
    }
}
