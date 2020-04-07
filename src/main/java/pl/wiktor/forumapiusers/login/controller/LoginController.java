package pl.wiktor.forumapiusers.login.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.wiktor.forumapiusers.config.security.jwt.JwtUtil;
import pl.wiktor.forumapiusers.config.security.jwt.TokenType;
import pl.wiktor.forumapiusers.login.model.AuthRequest;
import pl.wiktor.forumapiusers.login.model.AuthResponse;
import pl.wiktor.forumapiusers.login.model.UserSecurity;
import pl.wiktor.forumapiusers.login.service.UserLoginService;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class LoginController {

    private static final String TOKEN_PREFIX = "Bearer ";

    private AuthenticationManager authManager;

    private UserLoginService userLoginService;

    private JwtUtil jwtUtil;

    public LoginController(AuthenticationManager authManager, UserLoginService userLoginService, JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.userLoginService = userLoginService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(path = "/auth/login")
    public ResponseEntity<AuthResponse> obtainJWT(@RequestBody @Valid AuthRequest authRequest) {
        log.debug("Login attempt: " + authRequest.toString());
        authManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        final UserSecurity userSecurity = userLoginService.loadUserByUsername(authRequest.getUsername());

        final String JWT = TOKEN_PREFIX + jwtUtil.generateToken(userSecurity, TokenType.ACCESS);
        final String REFRESH_JWT = TOKEN_PREFIX + jwtUtil.generateToken(userSecurity, TokenType.REFRESH);

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, JWT, HttpHeaders.SET_COOKIE, REFRESH_JWT).body(AuthResponse.builder()
                .username(userSecurity.getUsername())
                .uuid(userSecurity.getUuid())
                .roles(userSecurity.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .build());
    }


}
