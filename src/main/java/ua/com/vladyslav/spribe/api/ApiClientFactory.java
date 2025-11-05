package ua.com.vladyslav.spribe.api;

import ua.com.vladyslav.spribe.api.services.PlayerApiService;

public final class ApiClientFactory {
    private static final ThreadLocal<PlayerApiService> player = ThreadLocal.withInitial(PlayerApiService::new);

    private ApiClientFactory() {}

    public static PlayerApiService playerApi() {
        return player.get();
    }

    public static void cleanup() {
        player.remove();
    }
}
