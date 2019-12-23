package pl.wiktor.forumapiusers.info.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wiktor.forumapiusers.info.service.UserInfoService;

import java.util.List;

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
        return ResponseEntity.ok(userInfoService.getUserListByUuidList(uuidList));
    }

}
