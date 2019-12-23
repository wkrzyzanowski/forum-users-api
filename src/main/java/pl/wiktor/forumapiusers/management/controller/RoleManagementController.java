package pl.wiktor.forumapiusers.management.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.wiktor.forumapiusers.management.model.UserManagementDTO;
import pl.wiktor.forumapiusers.exception.RoleException;
import pl.wiktor.forumapiusers.exception.UserException;
import pl.wiktor.forumapiusers.management.service.RoleManagementService;

import java.text.MessageFormat;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/mgmt/roles")
public class RoleManagementController {

    @Autowired
    private RoleManagementService roleManagementService;

    @Secured({"ROLE_ADMIN"})
    @PutMapping("/{uuid}")
    public ResponseEntity<UserManagementDTO> manageUserRole(@PathVariable("uuid") String userUuid, @RequestBody Set<String> roles) {

        if (userUuid == null || userUuid.isEmpty()) {
            throw new UserException(MessageFormat.format(UserException.UUID_NOT_FOUND, userUuid));
        }

        if (roles == null) {
            throw new RoleException(MessageFormat.format(RoleException.NAME_NOT_FOUND, roles.toString()));
        }

        return ResponseEntity.ok().body(roleManagementService.manageUserRoles(userUuid, roles));
    }

}
