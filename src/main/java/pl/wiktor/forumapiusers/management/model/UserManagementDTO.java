package pl.wiktor.forumapiusers.management.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import pl.wiktor.forumapiusers.management.model.validation.CreateUserValidation;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserManagementDTO {

    private String uuid;

    @NotNull(groups = {CreateUserValidation.class})
    @Email(groups = {CreateUserValidation.class})
    private String email;

    @NotNull(groups = {CreateUserValidation.class})
    @Size(min = 4, groups = {CreateUserValidation.class}, message = "Min length 4 letters/digits.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull(groups = {CreateUserValidation.class})
    @Size(min = 5, groups = {CreateUserValidation.class}, message = "Min length 5 letters/digits.")
    private String nick;

    private LocalDateTime lastLogin;

    private int helpCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<String> roles;

}
