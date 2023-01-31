package serverSettings;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigRead {
    private static final String FNAME = "config.properties";
    public static int PORT;
    public static String DBHOST;
    public static String DBPORT;
    public static String DBUSER;
    public static String DBPASS;
    public static String DBNAME;

    public static void initialize() {
        Properties properties = new Properties();
        InputStream inputStream = ConfigRead.class.getClassLoader().getResourceAsStream(FNAME);
        if (inputStream != null) {
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("File '" + FNAME + "' not found");
        }
        PORT = Integer.parseInt(properties.getProperty("PORT"));
        DBHOST = properties.getProperty("DBHOST");
        DBPORT = properties.getProperty("DBPORT");
        DBUSER = properties.getProperty("DBUSER");
        DBPASS = properties.getProperty("DBPASS");
        DBNAME = properties.getProperty("DBNAME");
    }
}
