package pl.wiktor.forumapiusers.management.mapper;

import pl.wiktor.forumapiusers.management.model.UserManagementDTO;
import pl.wiktor.forumapiusers.persistance.model.RoleEntity;
import pl.wiktor.forumapiusers.persistance.model.UserEntity;

import java.util.Set;
import java.util.stream.Collectors;

public class UserManagementMapper {


    public static UserManagementDTO fromEntityToDto(UserEntity userEntity) {

        Set<String> roles = userEntity.getRoles()
                .stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toSet());

        return UserManagementDTO.builder()
                .uuid(userEntity.getUuid())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .nick(userEntity.getNick())
                .lastLogin(userEntity.getLastLogin())
                .helpCount(userEntity.getHelpCount())
                .roles(roles)
                .build();
    }

    public static UserEntity fromDtoToEntity(UserManagementDTO userManagementDto) {
        return UserEntity.builder()
                .uuid(userManagementDto.getUuid())
                .email(userManagementDto.getEmail())
                .password(userManagementDto.getPassword())
                .nick(userManagementDto.getNick())
                .lastLogin(userManagementDto.getLastLogin())
                .helpCount(userManagementDto.getHelpCount())
                .build();
    }


}
