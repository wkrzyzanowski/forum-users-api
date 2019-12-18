package pl.wiktor.forumapiusers.login.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {

    private final String JWT;

    public AuthResponse(String JWT) {
        this.JWT = JWT;
    }


}
