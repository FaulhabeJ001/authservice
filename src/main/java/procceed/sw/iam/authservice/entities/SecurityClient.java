package procceed.sw.iam.authservice.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SecurityClient implements ClientDetails {

    private final Client client;

    public SecurityClient(Client client) {
        this.client = client;
    }

    @Override
    public String getClientId() {
        return client.getClient();
    }

    @Override
    public Set<String> getResourceIds() {
        return null;
    }

    @Override
    public boolean isSecretRequired() {
        return true;
    }

    @Override
    public String getClientSecret() {
        return client.getSecret();
    }

    @Override
    public boolean isScoped() {
        return false;
    }

    @Override
    public Set<String> getScope() {
        return null;
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return client.getClientGrantType().stream()
                .map(grant -> grant.getGrantType())
                .collect(Collectors.toSet());
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return Set.of(client.getRedirectUri());
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return null;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return null;
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return false;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return null;
    }
}
