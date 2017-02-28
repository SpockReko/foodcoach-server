package models.food;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import play.Application;

import static play.test.Helpers.*;

/**
 * Used to startup a fake application with a fake database that the tests can use.
 */
public abstract class FakeApplicationInMemoryDB {

    private static Application fakeApplication;

    @BeforeClass
    public static void startFakeApplication() {
        fakeApplication = fakeApplication(inMemoryDatabase());
        start(fakeApplication);
    }

    @AfterClass
    public static void shutdownFakeApplication() {
        stop(fakeApplication);
    }
}
