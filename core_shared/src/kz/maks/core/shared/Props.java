package kz.maks.core.shared;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Props {

    private static final String FILE_PATH = "./app.properties";

    private static Properties properties;

    private static void loadProperties() {
        try {
            File file = new File(FILE_PATH);
            properties = new Properties();
            properties.load(new FileInputStream(file));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String key) {
        if (properties == null)
            loadProperties();

        Object val = properties.get(key);

        if (val == null)
            throw new RuntimeException("Property " + key + " is not set");

        return val.toString();
    }

}
