package cz.abclinuxu.datoveschranky;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import cz.abclinuxu.datoveschranky.common.Config;
import cz.abclinuxu.datoveschranky.common.DataBoxEnvironment;
import cz.abclinuxu.datoveschranky.common.interfaces.DataBoxServices;
import cz.abclinuxu.datoveschranky.impl.Authentication;
import cz.abclinuxu.datoveschranky.impl.BasicAuthentication;
import cz.abclinuxu.datoveschranky.impl.ClientCertAuthentication;
import cz.abclinuxu.datoveschranky.impl.DataBoxManager;
import org.junit.jupiter.api.Test;

/**
 * @author xrosecky
 */
public class TestHelper {

    /*
    public static final String login = "5e7mvf";
    public static final String passwd = "Ab123456b";
    public static final File certFile = new File("/path/to/your/certificate.p12");
    public static final String certPassword = "your_client_cert_password";
    public static final Config config = new Config(DataBoxEnvironment.TEST);
     */
    private Properties properties = null;
    private final Config config = new Config(DataBoxEnvironment.TEST);
    // defined in pom.xml
    private static final String CONFIG_PATH = "src/test/resources/configuration.properties";

    public TestHelper() {
    }

    /**
     * Load properties from file, do nothing if it's already loaded.
     */
    private void loadProperties() {
        if (properties != null) {
            // load only once
            return;
        }

        properties = new Properties();
        File file = Paths.get(CONFIG_PATH).toFile();
        try (InputStream is = new FileInputStream(file)) {
            System.out.println("loading config from " + CONFIG_PATH);
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException(CONFIG_PATH, e);
        }
    }

    public DataBoxServices connectAsOVM() throws Exception {
        return connectBasicAuthAsOVM();
    }

    public DataBoxServices connectAsFO() throws Exception {
        return connectBasicAuthAsFO();
    }

    private DataBoxServices connectClientCertAsOVM() throws Exception {
        File certFile = null;
        String certPassword = null;
        Config config = new Config(DataBoxEnvironment.TEST);
        Authentication auth = new ClientCertAuthentication(config, certFile, certPassword);
        return new DataBoxManager(config, auth);
    }

    private DataBoxServices connectBasicAuthAsOVM() throws Exception {
        Config config = new Config(DataBoxEnvironment.TEST);
        String login = getProperties().getProperty("ovm.login");
        String passwd = getProperties().getProperty("ovm.password");
        Authentication auth = new BasicAuthentication(config, login, passwd);
        return new DataBoxManager(config, auth);
    }

    private DataBoxServices connectBasicAuthAsFO() throws Exception {
        Config config = new Config(DataBoxEnvironment.TEST);
        String login = getProperties().getProperty("fo.login");
        String passwd = getProperties().getProperty("fo.password");
        Authentication auth = new BasicAuthentication(config, login, passwd);
        return new DataBoxManager(config, auth);
    }

    public Properties getProperties() {
        loadProperties();
        return properties;
    }

    @Test
    public void testConnect() throws Exception {
        TestHelper helper = new TestHelper();
        helper.connectAsOVM();
    }

    public Config getConfig() {
        return config;
    }
}
