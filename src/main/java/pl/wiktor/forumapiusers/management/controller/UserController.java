package pl.wiktor.forumapiusers.management.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/mgmt/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping(path = "/{uuid}")
    public ResponseEntity<UserDTO> getSingleUser(@PathVariable(name = "uuid") String uuid) {
        return ResponseEntity.ok(userService.getSingleUser(uuid));
    }

    @PutMapping(path = "/{uuid}")
    public ResponseEntity<UserDTO> editSingleUser(@PathVariable(name = "uuid") String uuid, @RequestBody UserDTO userDTO) {
        if (uuid == null || uuid.isEmpty() || userDTO == null) {
            throw new UserException(MessageFormat.format(UserException.UUID_NOT_FOUND, uuid));
        }
        log.debug(userDTO.toString());
        return ResponseEntity.ok(userService.updateUser(uuid, userDTO));
    }

    @PutMapping(path = "/{uuid}/counter")
    public ResponseEntity<UserDTO> editHelpCounter(@PathVariable(name = "uuid") String uuid, @PathParam("sign") String sign) {
        if (uuid == null || uuid.isEmpty()) {
            throw new UserException(MessageFormat.format(UserException.UUID_NOT_FOUND, uuid));
        }
        if (sign != null && !sign.equals("plus") && !sign.equals("minus")) {
            throw new WrongCounterParameterException("Only two values are possible: 'plus' or 'minus'.");
        }
        return ResponseEntity.ok(userService.updateUserHelpCounter(uuid, sign));
    }


    @PostMapping(path = "/")
    public ResponseEntity<UserDTO> createNewUser(@RequestBody @Validated(CreateUserValidation.class) UserDTO userDTO) {
        if (userDTO == null) {
            throw new InsufficientUserDataException("Full user data should be passed to create new user.");
        }
        return ResponseEntity.ok(userService.createNewUser(userDTO));
    }

}
