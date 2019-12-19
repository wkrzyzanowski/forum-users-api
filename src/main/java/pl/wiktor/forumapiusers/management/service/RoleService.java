package pl.wiktor.forumapiusers.management.service;

import org.springframework.stereotype.Service;
import pl.wiktor.forumapiusers.management.mapper.UserMapper;
import pl.wiktor.forumapiusers.management.model.dto.UserDTO;
import pl.wiktor.forumapiusers.management.model.entity.RoleEntity;
import pl.wiktor.forumapiusers.management.model.entity.UserEntity;
import pl.wiktor.forumapiusers.management.model.exceptions.RoleException;
import pl.wiktor.forumapiusers.management.repository.RoleRepository;

import java.text.MessageFormat;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private UserService userService;

    private RoleRepository roleRepository;

    public RoleService(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }


    public UserDTO manageUserRoles(String uuid, Set<String> roles) {
        UserEntity userEntity = userService.getUserByUuid(uuid);
        Set<RoleEntity> newRoles = resolveRolesFromStrings(roles);

        userEntity.setRoles(newRoles);

        userService.persistUserEntityWithErrorHandling(userEntity);

        return UserMapper.fromEntityToDto(userEntity);
    }

    private Set<RoleEntity> resolveRolesFromStrings(Set<String> roles) {
        Set<RoleEntity> roleEntitySet = roles
                .stream()
                .map(roleString -> roleRepository.getByName(roleString)
                        .orElseThrow(() -> {
                            throw new RoleException(
                                    MessageFormat.format(RoleException.NAME_NOT_FOUND,
                                            roleString));
                        })).collect(Collectors.toSet());

        addUserRoleAlways(roleEntitySet);

        return roleEntitySet;
    }

     private void addUserRoleAlways(Set<RoleEntity> newRoles) {
        RoleEntity userRole = roleRepository.getOne(RoleEntity.USER_ROLE_ID);
        newRoles.add(userRole);
    }
}
