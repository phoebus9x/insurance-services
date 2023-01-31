package serverSettings;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class ConfigReadTest {
    @BeforeAll
    static void setUp() {
        ConfigRead.initialize();
    }

    @Test
    void initialize() {
        assertEquals(7347, ConfigRead.PORT);
        assertEquals("3306", ConfigRead.DBPORT);
        assertEquals("localhost", ConfigRead.DBHOST);
        assertEquals("root", ConfigRead.DBUSER);
        assertEquals("insurance_service", ConfigRead.DBNAME);
    }
}