package ua.com.vladyslav.spribe.players;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import ua.com.vladyslav.spribe.BaseTest;
import ua.com.vladyslav.spribe.api.facades.PlayerApiFacade;

public abstract class BasePlayerTest extends BaseTest {
    protected PlayerApiFacade playerApiService;

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        playerApiService = new PlayerApiFacade();
    }

    @AfterClass(alwaysRun = true)
    public void cleanUp() {
        playerApiService.cleanUp();
    }

}
