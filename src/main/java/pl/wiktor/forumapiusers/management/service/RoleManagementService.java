package pl.wiktor.forumapiusers.management.service;

import org.springframework.stereotype.Service;
import pl.wiktor.forumapiusers.management.mapper.UserManagementMapper;
import pl.wiktor.forumapiusers.management.model.UserManagementDTO;
import pl.wiktor.forumapiusers.persistance.model.RoleEntity;
import pl.wiktor.forumapiusers.persistance.model.UserEntity;
import pl.wiktor.forumapiusers.exception.RoleException;
import pl.wiktor.forumapiusers.persistance.repository.RoleRepository;

import java.text.MessageFormat;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleManagementService {

    private UserManagementService userManagementService;

    private RoleRepository roleRepository;

    public RoleManagementService(UserManagementService userManagementService, RoleRepository roleRepository) {
        this.userManagementService = userManagementService;
        this.roleRepository = roleRepository;
    }


    public UserManagementDTO manageUserRoles(String uuid, Set<String> roles) {
        UserEntity userEntity = userManagementService.getUserByUuid(uuid);
        Set<RoleEntity> newRoles = resolveRolesFromStrings(roles);

        userEntity.setRoles(newRoles);

        userManagementService.persistUserEntityWithErrorHandling(userEntity);

        return UserManagementMapper.fromEntityToDto(userEntity);
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
