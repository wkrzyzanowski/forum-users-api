package pl.wiktor.forumapiusers.management.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String uuid;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nick;

    @Column(unique = true)
    private String password;

    private LocalDateTime lastLogin;

    private int helpCount;

}
