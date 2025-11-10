package ua.com.vladyslav.spribe.players;

import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Test;
import ua.com.vladyslav.spribe.data.factories.PlayerDataProviders;
import ua.com.vladyslav.spribe.models.PlayerResponse;

import static ua.com.vladyslav.spribe.api.services.PlayerApiService.SUPERVISOR_LOGIN;
import static ua.com.vladyslav.spribe.data.factories.PlayerBuilder.getBuilderFromPlayerResponse;
import static ua.com.vladyslav.spribe.data.factories.TestDataFactory.createValidRandomUserBuilder;

public class AgeTests extends BasePlayerTest {

    // ------------------ CREATE TESTS ------------------

    @Test(dataProvider = "validBoundaryAges", dataProviderClass = PlayerDataProviders.class, groups = {"positive"})
    @Description("Verify players at valid boundary ages can be created successfully")
    public void createPlayerWithValidBoundaryAges(int age) {
        var request = createValidRandomUserBuilder().age(age).buildRequest();
        var created = playerApiService.createPlayer(request, SUPERVISOR_LOGIN);

        PlayerResponse actual = playerApiService.getPlayer(created.id());
        Assert.assertEquals(actual.age(), age, "Player age should match the boundary value");
    }

    @Test(dataProvider = "invalidBoundaryAges", dataProviderClass = PlayerDataProviders.class, groups = {"negative"})
    @Description("Verify players with invalid boundary ages cannot be created")
    public void createPlayerWithInvalidBoundaryAges(int age) {
        var request = createValidRandomUserBuilder().age(age).buildRequest();
        playerApiService.invalidCreation(request, SUPERVISOR_LOGIN);
    }

    // ------------------ UPDATE TESTS ------------------

    @Test(dataProvider = "validBoundaryAges", dataProviderClass = PlayerDataProviders.class, groups = {"positive"})
    @Description("Verify supervisor can update player to valid boundary ages")
    public void updatePlayerWithValidBoundaryAges(int age) {
        var created = playerApiService.createPlayer(createValidRandomUserBuilder().buildRequest(), SUPERVISOR_LOGIN);

        playerApiService.updatePlayer(
                getBuilderFromPlayerResponse(created).age(age).buildRequest(),
                SUPERVISOR_LOGIN,
                created.id()
        );

        PlayerResponse actual = playerApiService.getPlayer(created.id());
        Assert.assertEquals(actual.age(), age, "Updated player should have correct age");
    }

    @Test(dataProvider = "invalidBoundaryAges", dataProviderClass = PlayerDataProviders.class, groups = {"negative"})
    @Description("Verify supervisor cannot update player to invalid boundary ages")
    public void updatePlayerWithInvalidBoundaryAges(int age) {
        var created = playerApiService.createPlayer(createValidRandomUserBuilder().buildRequest(), SUPERVISOR_LOGIN);

        playerApiService.invalidUpdate(
                getBuilderFromPlayerResponse(created).age(age).buildRequest(),
                SUPERVISOR_LOGIN,
                created.id()
        );
    }
}
