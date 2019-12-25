package pl.wiktor.forumapiusers.login.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
public class AuthRequest {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

}
