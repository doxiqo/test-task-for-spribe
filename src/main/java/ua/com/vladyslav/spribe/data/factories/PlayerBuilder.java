package ua.com.vladyslav.spribe.data.factories;

import ua.com.vladyslav.spribe.enums.Genders;
import ua.com.vladyslav.spribe.enums.Roles;
import ua.com.vladyslav.spribe.models.PlayerRequest;
import ua.com.vladyslav.spribe.models.PlayerResponse;

public class PlayerBuilder {

    private Integer id;
    private String login;
    private String email;
    private Integer age;
    private Genders gender;
    private Roles role;
    private String screenName;
    private String password;

    public static PlayerBuilder build() {
        return new PlayerBuilder();
    }

    public PlayerBuilder id(Integer id) { this.id = id; return this; }
    public PlayerBuilder login(String username) { this.login = username; return this; }
    public PlayerBuilder email(String email) { this.email = email; return this; }
    public PlayerBuilder age(Integer age) { this.age = age; return this; }
    public PlayerBuilder gender(Genders gender) { this.gender = gender; return this; }
    public PlayerBuilder role(Roles role) { this.role = role; return this; }
    public PlayerBuilder screenName(String screenName) { this.screenName = screenName; return this; }
    public PlayerBuilder password(String password) { this.password = password; return this; }

    public PlayerRequest buildRequest() {
        return new PlayerRequest(id, login, email, age, gender, role, screenName, password);
    }

    public PlayerResponse buildResponse() {
        return new PlayerResponse(id, login, age, gender, role, screenName, password);
    }

    public static PlayerBuilder getBuilderFromPlayerResponse(PlayerResponse response) {
        return build()
                .age(response.age())
                .id(response.id())
                .role(response.role())
                .gender(response.gender())
                .login(response.login())
                .password(response.password())
                .screenName(response.screenName());
    }
}
