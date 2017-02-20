package models.food;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import play.Application;

import static play.test.Helpers.*;

public class FakeApplicationInMemoryDB {

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
