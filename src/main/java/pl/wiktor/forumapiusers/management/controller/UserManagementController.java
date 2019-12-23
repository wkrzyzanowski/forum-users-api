package pl.wiktor.forumapiusers.management.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.wiktor.forumapiusers.management.model.UserManagementDTO;
import pl.wiktor.forumapiusers.management.model.validation.CreateUserValidation;
import pl.wiktor.forumapiusers.exception.InsufficientUserDataException;
import pl.wiktor.forumapiusers.exception.UserException;
import pl.wiktor.forumapiusers.exception.WrongCounterParameterException;
import pl.wiktor.forumapiusers.management.service.UserManagementService;

import javax.websocket.server.PathParam;
import java.text.MessageFormat;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mgmt")
public class UserManagementController {

    private UserManagementService userManagementService;

    public UserManagementController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MOD"})
    @GetMapping(path = "/users")
    public ResponseEntity<List<UserManagementDTO>> getAllUsers() {
        return ResponseEntity.ok(userManagementService.getAllUsers());
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MOD"})
    @GetMapping(path = "/users/{uuid}")
    public ResponseEntity<UserManagementDTO> getSingleUser(@PathVariable(name = "uuid") String uuid) {
        return ResponseEntity.ok(userManagementService.getSingleUser(uuid));
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MOD"})
    @PutMapping(path = "/users/{uuid}")
    public ResponseEntity<UserManagementDTO> editSingleUser(@PathVariable(name = "uuid") String uuid,
                                                            @RequestBody @Validated(CreateUserValidation.class) UserManagementDTO userManagementDTO) {
        if (uuid == null || uuid.isEmpty() || userManagementDTO == null) {
            throw new UserException(MessageFormat.format(UserException.UUID_NOT_FOUND, uuid));
        }
        log.debug(userManagementDTO.toString());
        return ResponseEntity.ok(userManagementService.updateUser(uuid, userManagementDTO));
    }

    @Secured({"ROLE_ADMIN", "ROLE_MOD"})
    @DeleteMapping(path = "/users/{uuid}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable(name = "uuid") String uuid) {
        if (uuid == null || uuid.isEmpty()) {
            throw new UserException(MessageFormat.format(UserException.UUID_NOT_FOUND, uuid));
        }
        return ResponseEntity.ok(userManagementService.deleteUser(uuid));
    }

    @Secured({"ROLE_USER"})
    @PutMapping(path = "/users/{uuid}/counter")
    public ResponseEntity<UserManagementDTO> editHelpCounter(@PathVariable(name = "uuid") String uuid,
                                                             @PathParam("sign") String sign) {
        if (uuid == null || uuid.isEmpty()) {
            throw new UserException(MessageFormat.format(UserException.UUID_NOT_FOUND, uuid));
        }
        if (sign != null && !sign.equals("plus") && !sign.equals("minus")) {
            throw new WrongCounterParameterException("Only two values are possible: 'plus' or 'minus'.");
        }
        return ResponseEntity.ok(userManagementService.updateUserHelpCounter(uuid, sign));
    }


    @PostMapping(path = "/users")
    public ResponseEntity<UserManagementDTO> createNewUser(@RequestBody @Validated(CreateUserValidation.class) UserManagementDTO userManagementDTO) {
        if (userManagementDTO == null) {
            throw new InsufficientUserDataException("Full user data should be passed to create new user.");
        }
        return ResponseEntity.ok(userManagementService.createNewUser(userManagementDTO));
    }

}
