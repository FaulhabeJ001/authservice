package procceed.sw.iam.authservice.entities;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public class GrantedAuthorityWrapper implements GrantedAuthority {

    private final Authority authority;

    @Override
    public String getAuthority() {
        return authority.getAuthority();
    }
}
