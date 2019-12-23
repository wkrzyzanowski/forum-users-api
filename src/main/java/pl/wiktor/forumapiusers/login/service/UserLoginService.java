package pl.wiktor.forumapiusers.login.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.wiktor.forumapiusers.login.model.UserSecurity;
import pl.wiktor.forumapiusers.persistance.model.UserEntity;
import pl.wiktor.forumapiusers.persistance.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserLoginService implements UserDetailsService {

    public static final String ROLE_PREFIX = "ROLE_";

    private UserRepository userRepository;

    public UserLoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserSecurity loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return getUserSecFromEntity(userEmail);
    }

    public UserSecurity getUserSecFromEntity(String email) {
        UserEntity user = userRepository.getByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User doesn't exist."));

        List<SimpleGrantedAuthority> roles = new ArrayList<>();

        user.getRoles()
                .forEach(role -> {
                    roles.add(new SimpleGrantedAuthority(ROLE_PREFIX + role.getName()));
                });

        user.setLastLogin(LocalDateTime.now());

        userRepository.save(user);

        return UserSecurity
                .builder()
                .login(user.getEmail())
                .password(user.getPassword())
                .authorities(roles)
                .build();
    }
}
