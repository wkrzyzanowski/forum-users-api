package pl.wiktor.forumapiusers.info.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.wiktor.forumapiusers.exception.UserException;
import pl.wiktor.forumapiusers.info.mapper.UserInfoMapper;
import pl.wiktor.forumapiusers.info.model.UserInfoDTO;
import pl.wiktor.forumapiusers.persistance.repository.UserRepository;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserInfoService {

    private UserRepository userRepository;

    public UserInfoService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserInfoDTO> getUserListByUuidList(List<String> usersList) {

        List<UserInfoDTO> userList = new ArrayList<>();

        for (String uuid : usersList) {
            try {
                UserInfoDTO user = UserInfoMapper.fromEntityToDto(userRepository.getByUuid(uuid)
                        .orElseThrow(() -> new UserException(
                                MessageFormat.format(UserException.UUID_NOT_FOUND, uuid))));

                userList.add(user);

            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return userList;
    }

}
