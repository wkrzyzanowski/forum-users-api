package pl.wiktor.forumapiusers.info.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserInfoDTO {

    private String uuid;

    private String email;

    private String nick;

    private LocalDateTime lastLogin;

    private int helpCount;

}
