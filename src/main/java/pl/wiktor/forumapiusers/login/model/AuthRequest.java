package pl.wiktor.forumapiusers.login.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class AuthRequest {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

}
