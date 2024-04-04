package org.example.db;

import java.io.InputStream;
import java.util.Properties;

public final class PropertiesManager{
    private static final Properties PROPERTIES = new Properties();
    private static final String PROPERTIES_FILE = "db.properties";

    static {
        loadProperties();
    }

    private PropertiesManager() {
        throw new UnsupportedOperationException();
    }

    public static String getProperties(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() {
        try (InputStream inFile = PropertiesManager.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            PROPERTIES.load(inFile);
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

}
