package pl.wiktor.forumapiusers.management.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.wiktor.forumapiusers.management.mapper.UserManagementMapper;
import pl.wiktor.forumapiusers.management.model.UserManagementDTO;
import pl.wiktor.forumapiusers.persistance.model.RoleEntity;
import pl.wiktor.forumapiusers.persistance.model.UserEntity;
import pl.wiktor.forumapiusers.exception.UserException;
import pl.wiktor.forumapiusers.persistance.repository.RoleRepository;
import pl.wiktor.forumapiusers.persistance.repository.UserRepository;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserManagementService {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    public UserManagementService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserManagementDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserManagementMapper::fromEntityToDto)
                .collect(Collectors.toList());
    }

    public UserManagementDTO getSingleUser(String uuid) {
        UserEntity userEntity = getUserByUuid(uuid);
        return UserManagementMapper
                .fromEntityToDto(userEntity);
    }


    public UserManagementDTO createNewUser(UserManagementDTO userManagementDTO) {

        UserEntity userEntity = UserEntity.builder()
                .nick(userManagementDTO.getNick())
                .email(userManagementDTO.getEmail())
                .password(passwordEncoder.encode(userManagementDTO.getPassword()))
                .uuid(UUID.randomUUID().toString())
                .lastLogin(null)
                .helpCount(0)
                .build();

        setBasicUserRole(userEntity);

        persistUserEntityWithErrorHandling(userEntity);

        return UserManagementMapper.fromEntityToDto(userEntity);
    }

    public UserManagementDTO updateUser(String uuid, UserManagementDTO userManagementDTO) {
        UserEntity userEntity = getUserByUuid(uuid);
        editUserFields(userManagementDTO, userEntity);
        persistUserEntityWithErrorHandling(userEntity);
        return UserManagementMapper.fromEntityToDto(userEntity);
    }

    public UserManagementDTO updateUserHelpCounter(String uuid, String sign) {

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
        return UserManagementMapper.fromEntityToDto(userEntity);
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

    private void editUserFields(UserManagementDTO userManagementDTO, UserEntity userEntity) {
        if (userManagementDTO.getNick() != null && !userManagementDTO.getNick().isEmpty()) {
            userEntity.setNick(userManagementDTO.getNick());
        }

        if (userManagementDTO.getEmail() != null && !userManagementDTO.getEmail().isEmpty()) {
            userEntity.setEmail(userManagementDTO.getEmail());
        }

        if (userManagementDTO.getPassword() != null && !userManagementDTO.getPassword().isEmpty()) {
            userEntity.setPassword(passwordEncoder.encode(userManagementDTO.getPassword()));
        }

        if (userManagementDTO.getEmail() != null && !userManagementDTO.getEmail().isEmpty()) {
            userEntity.setEmail(userManagementDTO.getEmail());
        }

        if (userManagementDTO.getLastLogin() != null) {
            userEntity.setLastLogin(userManagementDTO.getLastLogin());
        }

    }

    private void setBasicUserRole(UserEntity userEntity) {
        RoleEntity userRole = roleRepository.getOne(RoleEntity.USER_ROLE_ID);
        userEntity.setRoles(new HashSet<>());
        userEntity.getRoles().add(userRole);
    }

    public boolean deleteUser(String uuid) {
        UserEntity userEntity = getUserByUuid(uuid);
        userRepository.delete(userEntity);
        return true;
    }
}
