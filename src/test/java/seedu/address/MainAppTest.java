package seedu.address;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Contains the test cases for the file MainApp
 */
public class MainAppTest {
    @Test
    public void mainapp_runssuccessfully() {
    }

    @Test
    public void mainapp_returnscorrectversion() {
        MainApp main = new MainApp();
        assertEquals(main.VERSION.toString(), "V1.2.1ea");
    }

    @Test
    public void mainapp_loggerexists() {
    }
}
