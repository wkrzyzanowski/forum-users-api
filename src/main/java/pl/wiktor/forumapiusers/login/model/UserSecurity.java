package pl.wiktor.forumapiusers.login.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Builder
public class UserSecurity implements UserDetails {

    @NotNull
    @NotEmpty
    private String login;

    @NotNull
    @NotEmpty
    private String password;

    private String uuid;

    private List<SimpleGrantedAuthority> authorities;

    public UserSecurity(@NotNull @NotEmpty String login, @NotNull @NotEmpty String password, String uuid, List<SimpleGrantedAuthority> authorities) {
        this.login = login;
        this.password = password;
        this.uuid = uuid;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getUsername() {
        return this.login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
