package pl.wiktor.forumapiusers.management.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.wiktor.forumapiusers.management.mapper.UserMapper;
import pl.wiktor.forumapiusers.management.model.dto.UserDTO;
import pl.wiktor.forumapiusers.management.model.entity.UserEntity;
import pl.wiktor.forumapiusers.management.model.exceptions.UserNotFoundException;
import pl.wiktor.forumapiusers.management.repository.UserRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::fromEntityToDto)
                .collect(Collectors.toList());
    }

    public UserDTO getSingleUser(String uuid) {
        return UserMapper
                .fromEntityToDto(
                        userRepository.getByUuid(uuid)
                                .orElseThrow(() -> new UserNotFoundException(uuid)));
    }

    public UserDTO addNewUser(UserDTO userDTO) {
        UserEntity userEntity = UserMapper.fromDtoToEntity(userDTO);
        userRepository.save(userEntity);
        return userDTO;
    }

    public UserDTO editUser(String uuid, UserDTO userDTO) {
        UserEntity userEntity = userRepository.getByUuid(uuid)
                .orElseThrow(() -> new UserNotFoundException(uuid));

        if (userDTO.getNick() != null && !userDTO.getNick().isEmpty()) {
            userEntity.setNick(userDTO.getNick());
        }

        if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()) {
            userEntity.setEmail(userDTO.getEmail());
        }

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            userEntity.setPassword(userDTO.getPassword());
        }

        if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()) {
            userEntity.setEmail(userDTO.getEmail());
        }

        if (userDTO.getLastLogin() != null) {
            userEntity.setLastLogin(userDTO.getLastLogin());
        }

        userRepository.save(userEntity);

        return UserMapper.fromEntityToDto(userEntity);
    }

    public UserDTO editUserHelpCounter(String uuid, String sign) {

        UserEntity userEntity = userRepository.getByUuid(uuid)
                .orElseThrow(() -> new UserNotFoundException(uuid));

        if (sign.equals("plus")) {
            int current = userEntity.getHelpCount();
            ++current;
            userEntity.setHelpCount(current);
            log.debug("Help counter increased for user: " + userEntity.getNick() + " to: " + userEntity.getHelpCount());
        }

        if (sign.equals("minus")) {
            int current = userEntity.getHelpCount();
            --current;
            if (current < 0) {
                current = 0;
            }
            userEntity.setHelpCount(current);
            log.debug("Help counter decreased for user: " + userEntity.getNick() + " to: " + userEntity.getHelpCount());
        }

        userRepository.save(userEntity);

        return UserMapper.fromEntityToDto(userEntity);
    }

    public UserDTO createNewUser(UserDTO userDTO) {

        UserEntity userEntity = UserEntity.builder()
                .nick(userDTO.getNick())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .uuid(UUID.randomUUID().toString())
                .lastLogin(null)
                .helpCount(0)
                .build();


        userRepository.save(userEntity);
        log.debug("New user created: " + userEntity.toString());

        return UserMapper.fromEntityToDto(userEntity);
    }
}
