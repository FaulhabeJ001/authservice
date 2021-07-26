package procceed.sw.iam.authservice.token;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import procceed.sw.iam.authservice.entities.*;
import procceed.sw.iam.authservice.repositories.ClientRepository;
import procceed.sw.iam.authservice.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GenerateTokenTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    UserRepository userRepository;

    @MockBean
    ClientRepository clientRepository;

    @MockBean
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Considering the client and the user exist in the database and the request " +
            "is a valid one, assert that the HTTP status is 200 OK and the authorization server " +
            "generates the access token.")
    public void givenPasswordGrantAndValidUser_whenRetrieveToken_thenOk() throws Exception {
        Authority authority = Authority.builder().authority("read").build();
        MyUser myUser = MyUser.builder()
                .username("user")
                .password("password")
                .authorities(List.of(authority))
                .build();

        Set<ClientGrantType> clientGrantTypes = Set.of(ClientGrantType.builder().grantType("password").build());
        Set<Scope> scopes = Set.of(Scope.builder().scope("read").build());
        Client client = Client.builder()
                .client("client")
                .secret("secret")
                .clientGrantType(clientGrantTypes)
                .scopes(scopes)
                .build();

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(myUser));
        when(clientRepository.findByClient("client")).thenReturn(Optional.of(client));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        mvc.perform(
                post("/oauth/token")
                        .with(httpBasic("client","secret"))
                        .queryParam("grant_type", "password")
                        .queryParam("scope", "read")
                        .queryParam("username", "user")
                        .queryParam("password", "password")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").exists());
    }

    @Test
    @DisplayName("Considering the client authenticating the request does not exist " +
            "assert that the response status is HTTP 401 (unauthorized) and " +
            "the authorization server doesn't generate the access token.")
    void givenPasswordGrantAndInvalidClient_whenRetrieveToken_thenUnauthorized() throws Exception {
        Authority authority = Authority.builder().authority("read").build();
        MyUser myUser = MyUser.builder()
                .username("user")
                .password("password")
                .authorities(List.of(authority))
                .build();

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(myUser));
        when(clientRepository.findByClient("client")).thenReturn(Optional.empty());
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        mvc.perform(
                post("/oauth/token")
                        .with(httpBasic("client","secret"))
                        .queryParam("grant_type", "password")
                        .queryParam("scope", "read")
                        .queryParam("username", "user")
                        .queryParam("password", "password")
        )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.access_token").doesNotExist());
    }

    @Test
    @DisplayName("Considering the user authenticating the request does not exist " +
            "assert that the response status is HTTP 400 (bad request) and " +
            "the authorization server doesn't generate the access token.")
    public void givenPasswordGrantAndInvalidUser_whenRetrieveToken_thenBadRequest() throws Exception {
        Set<ClientGrantType> clientGrantTypes = Set.of(ClientGrantType.builder().grantType("password").build());
        Set<Scope> scopes = Set.of(Scope.builder().scope("read").build());
        Client client = Client.builder()
                .client("client")
                .secret("secret")
                .clientGrantType(clientGrantTypes)
                .scopes(scopes)
                .build();

        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());
        when(clientRepository.findByClient("client")).thenReturn(Optional.of(client));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        mvc.perform(
                post("/oauth/token")
                        .with(httpBasic("client","secret"))
                        .queryParam("grant_type", "password")
                        .queryParam("scope", "read")
                        .queryParam("username", "user")
                        .queryParam("password", "password")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.access_token").doesNotExist());
    }

    @Test
    @DisplayName("Considering invalid passwords for user and client " +
            "assert that the response status is HTTP 400 (bad request) and " +
            "the authorization server doesn't generate the access token.")
    public void givenPasswordGrantAndInvalidPasswords_whenRetrieveToken_thenBadRequest() throws Exception {
        Authority authority = Authority.builder().authority("read").build();
        MyUser myUser = MyUser.builder()
                .username("user")
                .password("password")
                .authorities(List.of(authority))
                .build();

        Set<ClientGrantType> clientGrantTypes = Set.of(ClientGrantType.builder().grantType("password").build());
        Set<Scope> scopes = Set.of(Scope.builder().scope("read").build());
        Client client = Client.builder()
                .client("client")
                .secret("secret")
                .clientGrantType(clientGrantTypes)
                .scopes(scopes)
                .build();

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(myUser));
        when(clientRepository.findByClient("client")).thenReturn(Optional.of(client));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        mvc.perform(
                post("/oauth/token")
                        .with(httpBasic("client","secret"))
                        .queryParam("grant_type", "password")
                        .queryParam("scope", "read")
                        .queryParam("username", "user")
                        .queryParam("password", "password")
        )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.access_token").doesNotExist());
    }

    @Test
    @DisplayName("Considering the request is valid and the client has the refresh_token grant type " +
            "assert that the authorization server issues both the access token and the refresh token.")
    void generateRefreshTokenTest() throws Exception {
        Authority authority = Authority.builder().authority("read").build();
        MyUser myUser = MyUser.builder()
                .username("user")
                .password("password")
                .authorities(List.of(authority))
                .build();

        Set<ClientGrantType> clientGrantTypes = Set.of(
                ClientGrantType.builder().grantType("password").build(),
                ClientGrantType.builder().grantType("refresh_token").build());
        Set<Scope> scopes = Set.of(Scope.builder().scope("read").build());
        Client client = Client.builder()
                .client("client")
                .secret("secret")
                .clientGrantType(clientGrantTypes)
                .scopes(scopes)
                .build();

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(myUser));
        when(clientRepository.findByClient("client")).thenReturn(Optional.of(client));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        mvc.perform(
                post("/oauth/token")
                        .with(httpBasic("client","secret"))
                        .queryParam("grant_type", "password")
                        .queryParam("scope", "read")
                        .queryParam("username", "user")
                        .queryParam("password", "password")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").exists())
                .andExpect(jsonPath("$.refresh_token").exists());

    }

}
