package pl.wiktor.forumapiusers.login.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.wiktor.forumapiusers.config.jwt.JwtUtil;
import pl.wiktor.forumapiusers.login.model.AuthRequest;
import pl.wiktor.forumapiusers.login.model.AuthResponse;
import pl.wiktor.forumapiusers.login.service.UserLoginService;

import javax.validation.Valid;

@RestController
public class LoginController {

    private AuthenticationManager authManager;

    private UserLoginService userLoginService;

    private JwtUtil jwtUtil;

    public LoginController(AuthenticationManager authManager, UserLoginService userLoginService, JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.userLoginService = userLoginService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(path = "/auth/login")
    public ResponseEntity<AuthResponse> obtainJWT(@RequestBody @Valid AuthRequest authRequest) throws Exception {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect name or password", e);
        }

        final UserDetails userDetails = userLoginService.loadUserByUsername(authRequest.getUsername());

        final String JWT = "Bearer " + jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(JWT));
    }


}