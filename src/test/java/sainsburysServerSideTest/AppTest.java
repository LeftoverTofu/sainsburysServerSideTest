/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package sainsburysServerSideTest;

import org.junit.Test;

import sainsburys.scrape.app.App;

import static org.junit.Assert.*;

public class AppTest {
    @Test public void testAppHasAGreeting() {
        App classUnderTest = new App();
        assertNotNull("app should have a greeting", classUnderTest.getGreeting());
    }
}
