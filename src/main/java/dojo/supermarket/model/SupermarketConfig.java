package dojo.supermarket.model;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SupermarketConfig {
    private static final Properties properties = new Properties();
    private static final Logger LOGGER = Logger.getLogger(SupermarketConfig.class.getName());

    static {
        try (InputStream input = SupermarketConfig.class.getClassLoader()
                .getResourceAsStream("supermarket.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Could not load supermarket.properties file", e);
        }
    }

    public static double getProperty(String key, double defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? Double.parseDouble(value) : defaultValue;
    }
}
