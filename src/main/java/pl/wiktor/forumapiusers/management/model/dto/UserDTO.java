package pl.wiktor.forumapiusers.management.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import pl.wiktor.forumapiusers.management.model.dto.validation.CreateUserValidation;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDTO {

    private String uuid;

    @NotNull(groups = {CreateUserValidation.class})
    private String email;

    @NotNull(groups = {CreateUserValidation.class})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull(groups = {CreateUserValidation.class})
    private String nick;

    private LocalDateTime lastLogin;

    private int helpCount;

}
