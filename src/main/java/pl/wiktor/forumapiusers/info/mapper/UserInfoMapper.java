package pl.wiktor.forumapiusers.info.mapper;

import pl.wiktor.forumapiusers.info.model.UserInfoDTO;
import pl.wiktor.forumapiusers.persistance.model.UserEntity;

public class UserInfoMapper {

    public static UserInfoDTO fromEntityToDto(UserEntity userEntity) {
        return UserInfoDTO.builder()
                .uuid(userEntity.getUuid())
                .email(userEntity.getEmail())
                .nick(userEntity.getNick())
                .lastLogin(userEntity.getLastLogin())
                .helpCount(userEntity.getHelpCount())
                .build();

    }

}
