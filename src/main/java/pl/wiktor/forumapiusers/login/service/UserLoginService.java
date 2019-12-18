package pl.wiktor.forumapiusers.login.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.wiktor.forumapiusers.login.model.UserSecurity;
import pl.wiktor.forumapiusers.management.model.entity.RoleEntity;
import pl.wiktor.forumapiusers.management.model.entity.UserEntity;
import pl.wiktor.forumapiusers.management.repository.UserRepository;

@Service
@Slf4j
public class UserLoginService implements UserDetailsService {

    private UserRepository userRepository;

    public UserLoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return getUserSecFromEntity(userEmail);
    }

    public UserSecurity getUserSecFromEntity(String email) {
        UserEntity user = userRepository.getByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User doesn't exist."));



        return UserSecurity
                .builder()
                .login(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
