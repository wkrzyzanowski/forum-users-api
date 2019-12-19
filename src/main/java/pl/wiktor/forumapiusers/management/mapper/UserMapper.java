package pl.wiktor.forumapiusers.management.mapper;

import pl.wiktor.forumapiusers.management.model.dto.UserDTO;
import pl.wiktor.forumapiusers.management.model.entity.RoleEntity;
import pl.wiktor.forumapiusers.management.model.entity.UserEntity;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {


    public static UserDTO fromEntityToDto(UserEntity userEntity) {

        Set<String> roles = userEntity.getRoles()
                .stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toSet());

        return UserDTO.builder()
                .uuid(userEntity.getUuid())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .nick(userEntity.getNick())
                .lastLogin(userEntity.getLastLogin())
                .helpCount(userEntity.getHelpCount())
                .roles(roles)
                .build();
    }

    public static UserEntity fromDtoToEntity(UserDTO userDto) {
        return UserEntity.builder()
                .uuid(userDto.getUuid())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .nick(userDto.getNick())
                .lastLogin(userDto.getLastLogin())
                .helpCount(userDto.getHelpCount())
                .build();
    }


}
