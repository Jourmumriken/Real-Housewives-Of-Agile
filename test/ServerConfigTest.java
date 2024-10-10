import junit.framework.TestCase;
import config.ServerConfig;
public class ServerConfigTest extends TestCase {
    public ServerConfig config = ServerConfig.getInstance();

    public void testSetPort() {
        int test = 9000;
        config.setPort(test);
        assertEquals(test, config.getPort());
    }
    public void testGetPort() {
        int test = 8080;
        assertEquals(test, config.getPort());
    }
}
