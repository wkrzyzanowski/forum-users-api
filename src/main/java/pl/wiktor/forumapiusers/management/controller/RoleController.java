package pl.wiktor.forumapiusers.management.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.wiktor.forumapiusers.management.model.dto.UserDTO;
import pl.wiktor.forumapiusers.management.model.exceptions.RoleException;
import pl.wiktor.forumapiusers.management.model.exceptions.UserException;
import pl.wiktor.forumapiusers.management.service.RoleService;

import java.text.MessageFormat;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/mgmt/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Secured({"ROLE_ADMIN"})
    @PutMapping("/{uuid}")
    public ResponseEntity<UserDTO> manageUserRole(@PathVariable("uuid") String userUuid, @RequestBody Set<String> roles) {

        if (userUuid == null || userUuid.isEmpty()) {
            throw new UserException(MessageFormat.format(UserException.UUID_NOT_FOUND, userUuid));
        }

        if (roles == null) {
            throw new RoleException(MessageFormat.format(RoleException.NAME_NOT_FOUND, roles.toString()));
        }

        return ResponseEntity.ok().body(roleService.manageUserRoles(userUuid, roles));
    }

}
