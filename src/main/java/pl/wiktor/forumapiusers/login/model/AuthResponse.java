package pl.wiktor.forumapiusers.login.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class AuthResponse {

    private String uuid;

    private String username;

    private List<String> roles;

    public AuthResponse(String uuid, String username, List<String> roles) {
        this.uuid = uuid;
        this.username = username;
        this.roles = roles;
    }
}
