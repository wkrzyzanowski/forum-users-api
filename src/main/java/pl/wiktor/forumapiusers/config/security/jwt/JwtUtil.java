package pl.wiktor.forumapiusers.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.wiktor.forumapiusers.login.model.UserSecurity;

import java.util.*;
import java.util.function.Function;

@Service
public class JwtUtil {

    final String UUID_CLAIM = "uuid";
    final String AUTHORITIES_CLAIM = "authorities";
    @Value("${jwt.config.access.expirationTime}")
    String ACCESS_TOKEN_EXPIRE_TIME;
    @Value("${jwt.config.refresh.expirationTime}")
    String REFRESH_TOKEN_EXPIRE_TIME;
    @Value("${jwt.config.secret}")
    String SECRET_KEY;

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public List<String> extractRoles(String token) {
        final Claims claims = extractAllClaims(token);
        List<String> roles = new ArrayList<>();
        try {
            roles = (List<String>) claims.get(AUTHORITIES_CLAIM);
        } catch (Exception e) {
            e.getMessage();
        }
        return roles;
    }

    public String extractUuid(String token) {
        final Claims claims = extractAllClaims(token);
        String uuid = null;
        try {
            uuid = (String) claims.get(UUID_CLAIM);
        } catch (Exception e) {
            e.getMessage();
        }
        return uuid;
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserSecurity userDetails, TokenType type) {
        Map<String, Object> claims = new HashMap<>();

        List<String> roles = new ArrayList<>();

        userDetails.getAuthorities().forEach(x -> {
            SimpleGrantedAuthority auth = (SimpleGrantedAuthority) x;
            roles.add(auth.getAuthority());
        });

        claims.put(AUTHORITIES_CLAIM, roles);
        claims.put(UUID_CLAIM, userDetails.getUuid());
        return createToken(claims, userDetails.getUsername(), type);
    }

    private String createToken(Map<String, Object> claims, String subject, TokenType type) {
        long currentTime = System.currentTimeMillis();
        long jwtExpirationTime;
        if (type == TokenType.REFRESH) {
            jwtExpirationTime = currentTime + Long.parseLong(REFRESH_TOKEN_EXPIRE_TIME);
        } else {
            jwtExpirationTime = currentTime + Long.parseLong(ACCESS_TOKEN_EXPIRE_TIME);
        }

        return Jwts.builder()
                .claim(AUTHORITIES_CLAIM, claims.get(AUTHORITIES_CLAIM))
                .claim(UUID_CLAIM, claims.get(UUID_CLAIM))
                .setSubject(subject)
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(jwtExpirationTime))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
