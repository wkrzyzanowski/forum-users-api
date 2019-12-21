package pl.wiktor.forumapiusers.management.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.wiktor.forumapiusers.management.model.dto.UserDTO;
import pl.wiktor.forumapiusers.management.model.dto.validation.CreateUserValidation;
import pl.wiktor.forumapiusers.management.model.exceptions.InsufficientUserDataException;
import pl.wiktor.forumapiusers.management.model.exceptions.UserException;
import pl.wiktor.forumapiusers.management.model.exceptions.WrongCounterParameterException;
import pl.wiktor.forumapiusers.management.service.UserService;

import javax.websocket.server.PathParam;
import java.text.MessageFormat;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mgmt")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MOD"})
    @GetMapping(path = "/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MOD"})
    @GetMapping(path = "/users/{uuid}")
    public ResponseEntity<UserDTO> getSingleUser(@PathVariable(name = "uuid") String uuid) {
        return ResponseEntity.ok(userService.getSingleUser(uuid));
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MOD"})
    @PutMapping(path = "/users/{uuid}")
    public ResponseEntity<UserDTO> editSingleUser(@PathVariable(name = "uuid") String uuid,
                                                  @RequestBody @Validated(CreateUserValidation.class) UserDTO userDTO) {
        if (uuid == null || uuid.isEmpty() || userDTO == null) {
            throw new UserException(MessageFormat.format(UserException.UUID_NOT_FOUND, uuid));
        }
        log.debug(userDTO.toString());
        return ResponseEntity.ok(userService.updateUser(uuid, userDTO));
    }

    @Secured({"ROLE_ADMIN", "ROLE_MOD"})
    @DeleteMapping(path = "/users/{uuid}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable(name = "uuid") String uuid) {
        if (uuid == null || uuid.isEmpty()) {
            throw new UserException(MessageFormat.format(UserException.UUID_NOT_FOUND, uuid));
        }
        return ResponseEntity.ok(userService.deleteUser(uuid));
    }

    @PutMapping(path = "/users/{uuid}/counter")
    public ResponseEntity<UserDTO> editHelpCounter(@PathVariable(name = "uuid") String uuid,
                                                   @PathParam("sign") String sign) {
        if (uuid == null || uuid.isEmpty()) {
            throw new UserException(MessageFormat.format(UserException.UUID_NOT_FOUND, uuid));
        }
        if (sign != null && !sign.equals("plus") && !sign.equals("minus")) {
            throw new WrongCounterParameterException("Only two values are possible: 'plus' or 'minus'.");
        }
        return ResponseEntity.ok(userService.updateUserHelpCounter(uuid, sign));
    }


    @PostMapping(path = "/users")
    public ResponseEntity<UserDTO> createNewUser(@RequestBody @Validated(CreateUserValidation.class) UserDTO userDTO) {
        if (userDTO == null) {
            throw new InsufficientUserDataException("Full user data should be passed to create new user.");
        }
        return ResponseEntity.ok(userService.createNewUser(userDTO));
    }

}
