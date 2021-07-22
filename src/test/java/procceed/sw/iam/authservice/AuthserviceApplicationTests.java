package procceed.sw.iam.authservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.common.util.JacksonJsonParser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthserviceApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser
    public void givenMockUserAndClient_whenGetAuthorize_thenOk() throws Exception {
        mvc.perform(get("/oauth/authorize?response_type=code&client_id=client1&scope=read"))
                .andExpect(status().isOk());
    }

    @Test
    public void givenClient_whenAuthorize_thenRedirect() throws Exception {
        mvc.perform(get("/oauth/authorize?response_type=code&client_id=client1&scope=read"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }


    @Test
    public void givenPasswordGrantAndValidUser_whenRetrieveToken_thenOk() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", "client2");
        params.add("username", "john");
        params.add("password", "12345");

        ResultActions result = mvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic("client2","secret2"))
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        String token = jsonParser.parseMap(resultString).get("access_token").toString();
        String refreshToken = jsonParser.parseMap(resultString).get("refresh_token").toString();

        assertNotNull(token);
        assertNotNull(refreshToken);
    }

    @Test
    public void givenPasswordGrantAndInvalidUser_whenRetrieveToken_thenBadRequest() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", "client2");
        params.add("username", "notValid");
        params.add("password", "notValid");

        ResultActions result = mvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic("client2","secret2"))
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    public void givenClientCredentialsGrantAndValidUser_whenRetrieveToken_thenOk() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "client_credentials");
        params.add("scope", "read");

        ResultActions result = mvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic("client3","secret3"))
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        String token = jsonParser.parseMap(resultString).get("access_token").toString();

        assertNotNull(token);
    }

    @Test
    public void givenClientCredentialsGrantAndInvalidUser_whenRetrieveToken_thenUnauthorized() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "client_credentials");
        params.add("scope", "read");

        ResultActions result = mvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic("invalid","invalid"))
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isUnauthorized());

    }

}
