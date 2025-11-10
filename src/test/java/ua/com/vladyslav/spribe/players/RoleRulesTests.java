package ua.com.vladyslav.spribe.players;

import io.qameta.allure.Description;
import org.testng.annotations.Test;
import ua.com.vladyslav.spribe.enums.Roles;
import ua.com.vladyslav.spribe.models.PlayerRequest;
import ua.com.vladyslav.spribe.models.PlayerResponse;

import static ua.com.vladyslav.spribe.api.services.PlayerApiService.SUPERVISOR_ID;
import static ua.com.vladyslav.spribe.api.services.PlayerApiService.SUPERVISOR_LOGIN;
import static ua.com.vladyslav.spribe.data.factories.TestDataFactory.createValidRandomUserBuilder;

public class RoleRulesTests extends BasePlayerTest {

    @Test(groups = {"negative"})
    @Description("Verify Supervisor cannot be deleted")
    public void supervisorCannotBeDeleted() {
        playerApiService.invalidDelete(SUPERVISOR_ID, SUPERVISOR_LOGIN);
    }

    @Test(groups = {"negative"})
    @Description("Reject creation with role other than admin or user")
    public void rejectCreationSupervisorRole() {
        PlayerRequest req = createValidRandomUserBuilder().role(Roles.SUPERVISOR).buildRequest();
        playerApiService.invalidCreation(req, SUPERVISOR_LOGIN);
    }

    @Test(groups = {"negative"})
    @Description("Verify rejection user creation with valid data by USER")
    public void rejectCommonUserToCreate() {
        PlayerRequest randomValidAdminRequest = createValidRandomUserBuilder().buildRequest();
        PlayerResponse createdPlayer = playerApiService.createPlayer(randomValidAdminRequest, SUPERVISOR_LOGIN);

        playerApiService.invalidRoleCreation(createValidRandomUserBuilder().buildRequest(), createdPlayer.login());
    }

    @Test(groups = {"negative"})
    @Description("Verify USER can NOT delete itself")
    public void rejectDeleteWithUserRole() {
        PlayerRequest req = createValidRandomUserBuilder().role(ua.com.vladyslav.spribe.enums.Roles.USER).buildRequest();
        PlayerResponse created = playerApiService.createPlayer(req, SUPERVISOR_LOGIN);

        playerApiService.invalidDelete(created.id(), created.login());
    }
}
