package ua.com.vladyslav.spribe.players;

import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Test;
import ua.com.vladyslav.spribe.models.PlayerResponse;

import static ua.com.vladyslav.spribe.api.services.PlayerApiService.SUPERVISOR_LOGIN;
import static ua.com.vladyslav.spribe.data.factories.PlayerBuilder.getBuilderFromPlayerResponse;
import static ua.com.vladyslav.spribe.data.factories.TestDataFactory.createValidRandomUserBuilder;

public class PlayerAgeBoundaryTests extends BasePlayerTest {

    // ------------------ CREATE TESTS ------------------

    @Test(groups = {"positive"})
    @Description("Verify player with age = 17 (lower boundary valid) can be created")
    public void createPlayerAtLowerBoundary() {
        var request = createValidRandomUserBuilder().age(17).buildRequest();
        var created = playerApiService.createPlayer(request, SUPERVISOR_LOGIN);

        PlayerResponse actual = playerApiService.getPlayer(created.id());
        Assert.assertEquals(actual.age(), 17, "Player age should be exactly 17 after creation");
    }

    @Test(groups = {"positive"})
    @Description("Verify player with age = 59 (upper boundary valid) can be created")
    public void createPlayerAtUpperBoundary() {
        var request = createValidRandomUserBuilder().age(59).buildRequest();
        var created = playerApiService.createPlayer(request, SUPERVISOR_LOGIN);

        PlayerResponse actual = playerApiService.getPlayer(created.id());
        Assert.assertEquals(actual.age(), 59, "Player age should be exactly 59 after creation");
    }

    @Test(groups = {"negative"})
    @Description("Verify player with age = 16 (lower boundary invalid) cannot be created")
    public void createPlayerBelowLowerBoundary() {
        var request = createValidRandomUserBuilder().age(16).buildRequest();
        playerApiService.invalidCreation(request, SUPERVISOR_LOGIN);
    }

    @Test(groups = {"negative"})
    @Description("Verify player with age = 61 (upper boundary invalid) cannot be created")
    public void createPlayerAboveUpperBoundary() {
        var request = createValidRandomUserBuilder().age(61).buildRequest();
        playerApiService.invalidCreation(request, SUPERVISOR_LOGIN);
    }

    // ------------------ UPDATE TESTS ------------------

    @Test(groups = {"positive"})
    @Description("Verify supervisor can update player age to 17 (lower valid boundary)")
    public void updatePlayerToLowerBoundary() {
        var created = playerApiService.createPlayer(createValidRandomUserBuilder().buildRequest(), SUPERVISOR_LOGIN);

        playerApiService.updatePlayer(
                getBuilderFromPlayerResponse(created).age(17).buildRequest(),
                SUPERVISOR_LOGIN,
                created.id());

        PlayerResponse actual = playerApiService.getPlayer(created.id());
        Assert.assertEquals(actual.age(), 17, "Updated player should have age = 17");
    }

    @Test(groups = {"positive"})
    @Description("Verify supervisor can update player age to 60 (upper valid boundary)")
    public void updatePlayerToUpperBoundary() {
        var created = playerApiService.createPlayer(createValidRandomUserBuilder().buildRequest(), SUPERVISOR_LOGIN);

        playerApiService.updatePlayer(
                getBuilderFromPlayerResponse(created).age(60).buildRequest(),
                SUPERVISOR_LOGIN,
                created.id());

        PlayerResponse actual = playerApiService.getPlayer(created.id());
        Assert.assertEquals(actual.age(), 60, "Updated player should have age = 59");
    }

    @Test(groups = {"negative"})
    @Description("Verify supervisor cannot update player age to 16 (below valid boundary)")
    public void updatePlayerBelowLowerBoundary() {
        var created = playerApiService.createPlayer(createValidRandomUserBuilder().buildRequest(), SUPERVISOR_LOGIN);

        playerApiService.invalidUpdate(
                getBuilderFromPlayerResponse(created).age(16).buildRequest(),
                SUPERVISOR_LOGIN,
                created.id());
    }

    @Test(groups = {"negative"})
    @Description("Verify supervisor cannot update player age to 61 (above valid boundary)")
    public void updatePlayerAboveUpperBoundary() {
        var created = playerApiService.createPlayer(createValidRandomUserBuilder().buildRequest(), SUPERVISOR_LOGIN);

        playerApiService.invalidUpdate(
                getBuilderFromPlayerResponse(created).age(61).buildRequest(),
                SUPERVISOR_LOGIN,
                created.id());
    }
}
