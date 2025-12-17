package dojo.supermarket.model;

import java.io.InputStream;
import java.util.Properties;

public class SupermarketConfig {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = SupermarketConfig.class.getClassLoader()
                .getResourceAsStream("supermarket.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double getProperty(String key, double defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? Double.parseDouble(value) : defaultValue;
    }
}
