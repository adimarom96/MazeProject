package Server;

import java.io.*;
import java.util.Properties;

public class Configurations {
    private static Configurations instance = null;
    private static Properties prop;
    private OutputStream out;
    private InputStream in;

    /**
     * Singleton class.
     * private constructor and a "getInstance" function.
     */
    private Configurations() {
        try {
            in = new FileInputStream("./resources/config.properties");
            prop = new Properties();
            prop.load(in);
            out = new FileOutputStream("./resources/config.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Configurations getInstance() {
        // if the instance not exist create it.
        if (instance == null) {
            instance = new Configurations();
        }
        return instance;
    }

    /**
     * @param str one of the properties names.
     * @param val the new value to put in the property.
     */
    public static synchronized void setP(String str, String val) {
        try {
            prop.setProperty(str, val);
            prop.store(instance.out, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param str - one of the properties names.
     * @return the value in the property.
     */
    public synchronized String getP(String str) {
        return prop.getProperty(str);
    }
}