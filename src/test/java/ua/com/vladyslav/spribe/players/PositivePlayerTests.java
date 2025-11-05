package ua.com.vladyslav.spribe.players;

import io.qameta.allure.Description;
import org.testng.annotations.Test;
import ua.com.vladyslav.spribe.api.services.PlayerApiService;
import ua.com.vladyslav.spribe.enums.Roles;
import ua.com.vladyslav.spribe.models.PlayerRequest;
import ua.com.vladyslav.spribe.models.PlayerResponse;
import ua.com.vladyslav.spribe.models.PlayersResponse;
import ua.com.vladyslav.spribe.validators.PlayerValidator;

import static org.testng.Assert.*;
import static ua.com.vladyslav.spribe.api.services.PlayerApiService.SUPERVISOR_LOGIN;
import static ua.com.vladyslav.spribe.data.factories.PlayerBuilder.getBuilderFromPlayerResponse;
import static ua.com.vladyslav.spribe.data.factories.TestDataFactory.createValidRandomUserBuilder;

public class PositivePlayerTests extends BasePlayerTest {

    @Test(groups = {"positive"})
    @Description("Verify SUPERVISOR can create USER creation with valid data")
    public void testCreateUserSuccessfully() {
        PlayerRequest randomValidPlayerRequest = createValidRandomUserBuilder().buildRequest();
        PlayerResponse createdPlayer = playerApiService.createPlayer(randomValidPlayerRequest, SUPERVISOR_LOGIN);
        PlayerResponse samePlayer = playerApiService.getPlayer(createdPlayer.id());
        PlayerValidator.validatePlayerResponseMatchesRequest(samePlayer, randomValidPlayerRequest);
    }

    @Test(groups = {"positive"})
    @Description("Verify SUPERVISOR can create ADMIN that can create USER with valid data")
    public void testCreateAdminSuccessfully() {
        PlayerRequest randomValidAdminRequest = createValidRandomUserBuilder().role(Roles.ADMIN).buildRequest();
        PlayerResponse createdAdmin = playerApiService.createPlayer(randomValidAdminRequest, SUPERVISOR_LOGIN);
        PlayerResponse sameAdmin = playerApiService.getPlayer(createdAdmin.id());
        PlayerValidator.validatePlayerResponseMatchesRequest(sameAdmin, randomValidAdminRequest);

        PlayerRequest randomValidPlayerRequest = createValidRandomUserBuilder().buildRequest();
        PlayerResponse createdPlayer = playerApiService.createPlayer(randomValidPlayerRequest, createdAdmin.login());
        PlayerResponse samePlayer = playerApiService.getPlayer(createdPlayer.id());
        PlayerValidator.validatePlayerResponseMatchesRequest(samePlayer, randomValidPlayerRequest);

        playerApiService.invalidCreation(createValidRandomUserBuilder().buildRequest(), samePlayer.login());
    }

    @Test(groups = {"positive"})
    @Description("Verify SUPERVISOR can update any other players")
    public void supervisorUpdateTest() {
        PlayerRequest createReq = createValidRandomUserBuilder().buildRequest();
        PlayerResponse created = playerApiService.createPlayer(createReq, SUPERVISOR_LOGIN);

        PlayerRequest updateReq = getBuilderFromPlayerResponse(created)
                .screenName(created.screenName() + "_bySup")
                .age(created.age() == null ? 30 : created.age() + 1)
                .password("UpdPass123")
                .buildRequest();

        playerApiService.updatePlayer(updateReq, SUPERVISOR_LOGIN, created.id());
        // we have to do GET for checking 'password'
        PlayerResponse updated = playerApiService.getPlayer(created.id());

        assertNotNull(updated, "Updated response must not be null");
        assertEquals(updated.id(), created.id(), "ID should remain same after update");
        assertEquals(updated.screenName(), updateReq.screenName(), "ScreenName should be updated by supervisor");
        assertEquals(updated.age(), updateReq.age(), "Age should be updated by supervisor");
        assertEquals(updated.password(), updateReq.password(), "Password should be updated");

    }

    @Test(groups = {"positive"})
    @Description("Verify USER can update itself with valid data")
    public void userUpdateTest() {
        PlayerRequest createReq = createValidRandomUserBuilder().role(ua.com.vladyslav.spribe.enums.Roles.USER).buildRequest();
        PlayerResponse created = playerApiService.createPlayer(createReq, PlayerApiService.SUPERVISOR_LOGIN);

        PlayerRequest updateReq = getBuilderFromPlayerResponse(created)
                .screenName(created.screenName() + "_selfUpd")
                .password("UserUpd123") // валідний пароль
                .age(created.age() != null ? created.age() : 25)
                .buildRequest();

        PlayerResponse updated = playerApiService.updatePlayer(updateReq, created.login(), created.id());

        assertNotNull(updated, "Updated response must not be null");
        assertEquals(updated.id(), created.id(), "ID should remain the same when user updates itself");
        assertEquals(updated.screenName(), updateReq.screenName(), "User must be able to change own screenName");
        assertEquals(updated.role(), Roles.USER, "User role should remain unchanged after self-update");
    }

    @Test(groups = {"positive"})
    @Description("Verify ADMIN can update any USER or ADMIN with valid data")
    public void adminUpdateTest() {
        PlayerResponse userTarget = playerApiService.createPlayer(
                createValidRandomUserBuilder().role(ua.com.vladyslav.spribe.enums.Roles.USER).buildRequest(),
                PlayerApiService.SUPERVISOR_LOGIN);

        PlayerResponse adminTarget = playerApiService.createPlayer(
                createValidRandomUserBuilder().role(ua.com.vladyslav.spribe.enums.Roles.ADMIN).buildRequest(),
                PlayerApiService.SUPERVISOR_LOGIN);

        PlayerRequest userUpdate = getBuilderFromPlayerResponse(userTarget)
                .screenName(userTarget.screenName() + "_byAdmin")
                .password("AdminUpd123")
                .buildRequest();

        PlayerRequest adminUpdate = getBuilderFromPlayerResponse(adminTarget)
                .screenName(adminTarget.screenName() + "_byAdmin")
                .age(adminTarget.age() != null ? adminTarget.age() + 1 : 35)
                .buildRequest();

        PlayerResponse updatedUser = playerApiService.updatePlayer(userUpdate, "admin", userTarget.id());
        PlayerResponse updatedAdmin = playerApiService.updatePlayer(adminUpdate, "admin", adminTarget.id());

        assertNotNull(updatedUser);
        assertEquals(updatedUser.id(), userTarget.id());
        assertEquals(updatedUser.screenName(), userUpdate.screenName());

        assertNotNull(updatedAdmin);
        assertEquals(updatedAdmin.id(), adminTarget.id());
        assertEquals(updatedAdmin.screenName(), adminUpdate.screenName());
    }

    @Test(groups = {"positive"})
    @Description("Verify SUPERVISOR can delete any USER or ADMIN")
    public void supervisorDeleteTest() {
        PlayerResponse userTarget = playerApiService.createPlayer(
                createValidRandomUserBuilder().role(ua.com.vladyslav.spribe.enums.Roles.USER).buildRequest(),
                PlayerApiService.SUPERVISOR_LOGIN);

        PlayerResponse adminTarget = playerApiService.createPlayer(
                createValidRandomUserBuilder().role(ua.com.vladyslav.spribe.enums.Roles.ADMIN).buildRequest(),
                PlayerApiService.SUPERVISOR_LOGIN);

        assertNotNull(userTarget, "User target must be created");
        assertNotNull(adminTarget, "Admin target must be created");

        playerApiService.deletePlayerAndValidate(userTarget.id(), SUPERVISOR_LOGIN);
        playerApiService.deletePlayerAndValidate(adminTarget.id(), SUPERVISOR_LOGIN);

        PlayersResponse allAfter = playerApiService.getAllPlayers();

        boolean userStillPresent = allAfter.players().stream()
                .anyMatch(p -> p.id() != null && p.id().equals(userTarget.id()));
        boolean adminStillPresent = allAfter.players().stream()
                .anyMatch(p -> p.id() != null && p.id().equals(adminTarget.id()));

        assertFalse(userStillPresent, "Deleted USER should not be present in players list");
        assertFalse(adminStillPresent, "Deleted ADMIN should not be present in players list");
    }

    @Test(groups = {"positive"})
    @Description("Verify ADMIN can delete any USER or ADMIN")
    public void adminDeleteTest() {
        PlayerResponse userTarget = playerApiService.createPlayer(
                createValidRandomUserBuilder().role(ua.com.vladyslav.spribe.enums.Roles.USER).buildRequest(),
                PlayerApiService.SUPERVISOR_LOGIN);

        PlayerResponse adminTarget = playerApiService.createPlayer(
                createValidRandomUserBuilder().role(ua.com.vladyslav.spribe.enums.Roles.ADMIN).buildRequest(),
                PlayerApiService.SUPERVISOR_LOGIN);

        assertNotNull(userTarget, "User must be created");
        assertNotNull(adminTarget, "Admin must be created");

        playerApiService.deletePlayerAndValidate(userTarget.id(), adminTarget.login());
        playerApiService.deletePlayerAndValidate(adminTarget.id(), adminTarget.login());

        PlayersResponse allAfter = playerApiService.getAllPlayers();

        boolean userStillPresent = allAfter.players().stream()
                .anyMatch(p -> p.id() != null && p.id().equals(userTarget.id()));
        boolean adminStillPresent = allAfter.players().stream()
                .anyMatch(p -> p.id() != null && p.id().equals(adminTarget.id()));

        assertFalse(userStillPresent, "Admin should be able to delete USER");
        assertFalse(adminStillPresent, "Admin should be able to delete ADMIN");
    }
}
