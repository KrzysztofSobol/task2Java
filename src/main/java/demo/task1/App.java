package demo.task1;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class App {
    private static final Logger logger = Logger.getLogger(App.class.getName());

    static {
        try {
            InputStream configFile = App.class.getClassLoader().getResourceAsStream("logging.properties");

            if (configFile == null) {
                throw new IOException("Could not find logging.properties in resources");
            }

            LogManager.getLogManager().readConfiguration(configFile);
            configFile.close();

            logger.info("Logging configuration loaded successfully");
        } catch (IOException e) {
            logger.severe("Could not load logging configuration:");
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println("start");
    }

    public static double max(double a, double b) {
        return a>b ? a : b;
    }
}