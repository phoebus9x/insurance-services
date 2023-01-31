package dbWork;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import serverSettings.ConfigRead;

import static org.junit.jupiter.api.Assertions.*;

class DbInsuranceTest {
    private DbInsurance dbInsurance;

    @BeforeEach
    void setUp() {
        ConfigRead.initialize();
        dbInsurance = DbInsurance.getInstance();
    }

    @Test
    void select() {
        assertEquals(2, dbInsurance.select(0).size());
    }
}