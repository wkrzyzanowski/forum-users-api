package pl.wiktor.forumapiusers.management.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.wiktor.forumapiusers.management.mapper.UserMapper;
import pl.wiktor.forumapiusers.management.model.dto.UserDTO;
import pl.wiktor.forumapiusers.management.model.entity.RoleEntity;
import pl.wiktor.forumapiusers.management.model.entity.UserEntity;
import pl.wiktor.forumapiusers.management.model.exceptions.UserException;
import pl.wiktor.forumapiusers.management.repository.RoleRepository;
import pl.wiktor.forumapiusers.management.repository.UserRepository;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::fromEntityToDto)
                .collect(Collectors.toList());
    }

    public UserDTO getSingleUser(String uuid) {
        UserEntity userEntity = getUserByUuid(uuid);
        return UserMapper
                .fromEntityToDto(userEntity);
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

        setBasicUserRole(userEntity);

        persistUserEntityWithErrorHandling(userEntity);

        return UserMapper.fromEntityToDto(userEntity);
    }

    public UserDTO updateUser(String uuid, UserDTO userDTO) {
        UserEntity userEntity = getUserByUuid(uuid);
        editUserFields(userDTO, userEntity);
        persistUserEntityWithErrorHandling(userEntity);
        return UserMapper.fromEntityToDto(userEntity);
    }

    public UserDTO updateUserHelpCounter(String uuid, String sign) {

        UserEntity userEntity = getUserByUuid(uuid);

        if (sign.equals("plus")) {
            int current = userEntity.getHelpCount();
            ++current;
            userEntity.setHelpCount(current);
            log.debug("Help counter increased for user: " + userEntity.getUuid() + " to: " + userEntity.getHelpCount());
        }

        if (sign.equals("minus")) {
            int current = userEntity.getHelpCount();
            --current;
            if (current < 0) {
                current = 0;
            }
            userEntity.setHelpCount(current);
            log.debug("Help counter decreased for user: " + userEntity.getUuid() + " to: " + userEntity.getHelpCount());
        }

        try {
            userRepository.save(userEntity);
            log.debug("Successfully increased/decreased help counter.");
        } catch (Exception e) {
            log.error("Cannot increase/decrease help counter for user. " + userEntity.toString());
            throw new UserException(
                    MessageFormat.format("Cannot increase/decrease help counter for user with UUID: {0}", userEntity.getUuid()),
                    e.toString());
        }
        return UserMapper.fromEntityToDto(userEntity);
    }

    UserEntity getUserByUuid(String uuid) {
        return userRepository.getByUuid(uuid)
                .orElseThrow(() -> new UserException(
                        MessageFormat.format(UserException.UUID_NOT_FOUND, uuid)));
    }

    public void persistUserEntityWithErrorHandling(UserEntity userEntity) {
        log.debug(MessageFormat.format("Trying to save user: {0}", userEntity));
        try {
            userRepository.save(userEntity);
            log.debug(MessageFormat.format("Successfully saved user: {0}", userEntity));
        } catch (Exception e) {
            log.error(MessageFormat.format("Cannot save user: {0}", userEntity));
            throw new UserException(MessageFormat.format("Cannot save user: {0}", userEntity.getUuid()), e.getMessage());
        }
    }

    private void editUserFields(UserDTO userDTO, UserEntity userEntity) {
        if (userDTO.getNick() != null && !userDTO.getNick().isEmpty()) {
            userEntity.setNick(userDTO.getNick());
        }

        if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()) {
            userEntity.setEmail(userDTO.getEmail());
        }

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()) {
            userEntity.setEmail(userDTO.getEmail());
        }

        if (userDTO.getLastLogin() != null) {
            userEntity.setLastLogin(userDTO.getLastLogin());
        }

    }

    private void setBasicUserRole(UserEntity userEntity) {
        RoleEntity userRole = roleRepository.getOne(RoleEntity.USER_ROLE_ID);
        userEntity.setRoles(new HashSet<>());
        userEntity.getRoles().add(userRole);
    }
}
