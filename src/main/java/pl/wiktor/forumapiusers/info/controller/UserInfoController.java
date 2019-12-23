package pl.wiktor.forumapiusers.info.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.wiktor.forumapiusers.info.service.UserInfoService;

import java.text.MessageFormat;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/info")
public class UserInfoController {

    private UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Secured({"ROLE_USER"})
    @PostMapping("/users")
    public ResponseEntity<Object> getUserListByUuidList(@RequestBody List<String> uuidList) {
        log.debug(MessageFormat.format("Get user info list for: {0}", uuidList.toString()));
        return ResponseEntity.ok(userInfoService.getUserListByUuidList(uuidList));
    }

    @Secured({"ROLE_USER"})
    @GetMapping("/users/{uuid}")
    public ResponseEntity<Object> getUserListByUuid(@PathVariable("uuid") String userUuid) {
        log.debug(MessageFormat.format("Get user info for: {0}", userUuid));
        return ResponseEntity.ok(userInfoService.getUserUuid(userUuid));
    }

}
