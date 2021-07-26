package procceed.sw.iam.authservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Profile("test")
@RequiredArgsConstructor
@Configuration
@EnableAuthorizationServer
public class AuthServerTestConfiguration extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;

    private final TokenStore tokenStore;

    private final JwtAccessTokenConverter converter;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("client1")
                .secret("secret1")
                .authorizedGrantTypes("authorization_code", "refresh_token")
                .scopes("read")
                .redirectUris("http://localhost:9090/home")
                .and()
                .withClient("client2")
                .secret("secret2")
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("read")
                .and()
                .withClient("client3")
                .secret("secret3")
                .authorizedGrantTypes("client_credentials")
                .scopes("read");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .authenticationManager(authenticationManager)
                .tokenStore(tokenStore)
                .accessTokenConverter(converter);
    }
}