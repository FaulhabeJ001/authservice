package procceed.sw.iam.authservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import procceed.sw.iam.authservice.config.WebSecurityTestConfiguration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@Import(WebSecurityTestConfiguration.class)
@AutoConfigureMockMvc
class AuthserviceApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser
    @DisplayName("Considering the user is authenticated," +
            "assert that HTTP status is 200 OK (login-screen)")
    public void givenMockUserAndClient_whenGetAuthorize_thenOk() throws Exception {
        mvc.perform(
                get("/oauth/authorize?response_type=code&client_id=client1&scope=read")
        )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Considering the user is not authenticated yet," +
            "assert that HTTP status is 3xx Redirect to /login")
    public void givenClient_whenAuthorize_thenRedirect() throws Exception {
        mvc.perform(
                get("/oauth/authorize?response_type=code&client_id=client1&scope=read")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }


    @Test
    @DisplayName("Considering the client and the user exist in the database and the request " +
            "is a valid one, assert that the HTTP status is 200 OK and the authorization server " +
            "generates the access token.")
    public void givenPasswordGrantAndValidUser_whenRetrieveToken_thenOk() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("scope", "read");
        params.add("username", "john");
        params.add("password", "12345");

        mvc.perform(
                post("/oauth/token")
                        .with(httpBasic("client2","secret2"))
                        .params(params)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").exists())
                .andExpect(jsonPath("$.refresh_token").exists());
    }

    @Test
    @DisplayName("Considering the client authenticating the request does not exist " +
            "assert that the response status is HTTP 401 (unauthorized) and " +
            "the authorization server doesn't generate the access token.")
    void givenPasswordGrantAndInvalidClient_whenRetrieveToken_thenUnauthorized() throws Exception {

        mvc.perform(
                post("/oauth/token")
                        .with(httpBasic("other_client", "secret"))
                        .queryParam("grant_type", "password")
                        .queryParam("username", "john")
                        .queryParam("password", "12345")
                        .queryParam("scope", "read")
        )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.access_token").doesNotExist());

    }

    @Test
    @DisplayName("Considering the user authenticating the request does not exist " +
            "assert that the response status is HTTP 400 (bad request) and " +
            "the authorization server doesn't generate the access token.")
    public void givenPasswordGrantAndInvalidUser_whenRetrieveToken_thenBadRequest() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", "client2");
        params.add("username", "notValid");
        params.add("password", "notValid");

        mvc.perform(post("/oauth/token")
                .with(httpBasic("client2","secret2"))
                .params(params)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.access_token").doesNotExist())
                .andExpect(jsonPath("$.refresh_token").doesNotExist());
    }

    @Test
    @DisplayName("Considering the client exist in the database and the request " +
            "is a valid one, assert that the HTTP status is 200 OK and the authorization server " +
            "generates the access token.")
    public void givenClientCredentialsGrantAndValidUser_whenRetrieveToken_thenOk() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "client_credentials");
        params.add("scope", "read");

        mvc.perform(post("/oauth/token")
                .with(httpBasic("client3","secret3"))
                .params(params)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").exists());
    }

    @Test
    @DisplayName("Considering the client authenticating the request does not exist " +
            "assert that the response status is HTTP 401 (unauthorized) and " +
            "the authorization server doesn't generate the access token.")
    public void givenClientCredentialsGrantAndInvalidUser_whenRetrieveToken_thenUnauthorized() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "client_credentials");
        params.add("scope", "read");

        mvc.perform(post("/oauth/token")
                .with(httpBasic("invalid","invalid"))
                .params(params)
        )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.access_token").doesNotExist());

    }

}
