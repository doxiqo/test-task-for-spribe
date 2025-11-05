package ua.com.vladyslav.spribe.api.services;

import io.restassured.response.Response;
import ua.com.vladyslav.spribe.api.BaseApiClient;
import ua.com.vladyslav.spribe.config.ConfigurationManager;
import ua.com.vladyslav.spribe.models.PlayerRequest;
import ua.com.vladyslav.spribe.logging.Log;

import static io.restassured.RestAssured.given;

public class PlayerApiService extends BaseApiClient {
    private static final Log LOG = Log.get(PlayerApiService.class);

    public static final String SUPERVISOR_LOGIN = "supervisor";
    public static final Integer SUPERVISOR_ID = 1;

    private static final String PLAYER_PATH = "/player";
    private static final String CREATE_PLAYER_PATH = "/create/{editor}";
    private static final String DELETE_PLAYER_PATH = "/delete/{editor}";
    private static final String GET_PLAYER_PATH = "/get";
    private static final String GET_ALL_PLAYERS_PATH = "/get/all";
    private static final String UPDATE_PLAYER_PATH = "/update/{editor}/{id}";

    public PlayerApiService() {
        super(ConfigurationManager.getInstance().getProperty("api.base.url"), PLAYER_PATH);
    }

    public Response createPlayer(PlayerRequest playerRequest, String editor) {
        LOG.info("Creating player: login={}, editor={}", playerRequest.login(), editor);
        return given(this.spec)
                .pathParam("editor", editor)
                .queryParam("login", playerRequest.login())
                .queryParam("password", playerRequest.password())
                .queryParam("email", playerRequest.email())
                .queryParam("screenName", playerRequest.screenName())
                .queryParam("role", playerRequest.role())
                .queryParam("gender", playerRequest.gender())
                .queryParam("age", playerRequest.age())
                .get(CREATE_PLAYER_PATH);
    }

    public Response deletePlayer(PlayerRequest playerRequest, String editor) {
        LOG.info("Deleting player: login={}, editor={}", playerRequest.login(), editor);
        return given(this.spec)
                .pathParam("editor", editor)
                .body(playerRequest)
                .delete(DELETE_PLAYER_PATH);
    }

    public Response getPlayer(PlayerRequest playerRequest) {
        LOG.info("Getting player by ID: {}", playerRequest.playerId());
        return given(this.spec)
                .body(playerRequest)
                .post(GET_PLAYER_PATH);
    }

    public Response getALLPlayers() {
        LOG.info("Getting all players");
        return given(this.spec)
                .get(GET_ALL_PLAYERS_PATH);
    }

    public Response updatePlayer(PlayerRequest updatedPlayerRequest, String editor, Integer userId) {
        LOG.info("Updating player: login={}, id={}, editor={}",
                updatedPlayerRequest.login(), userId, editor);
        return given(this.spec)
                .pathParam("editor", editor)
                .pathParam("id", userId)
                .body(updatedPlayerRequest)
                .patch(UPDATE_PLAYER_PATH);
    }
}
