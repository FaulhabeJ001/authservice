package procceed.sw.iam.authservice.controller;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import procceed.sw.iam.authservice.config.WebSecurityTestConfiguration;
import procceed.sw.iam.authservice.entities.Authority;
import procceed.sw.iam.authservice.entities.MyUser;
import procceed.sw.iam.authservice.services.UserService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(UserController.class)
@Import(WebSecurityTestConfiguration.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void createUser() throws Exception {
        mockMvc.perform(post("/users")
                .content(new ObjectMapper().writeValueAsString(toCreateUser()))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION));

        verify(userService, times(1)).createUser(toCreateUser());
    }

    @Test
    void getAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(twoUsers());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].authorities", hasSize(1)));
    }

    private List<MyUser> twoUsers() {
        return List.of( MyUser.builder().username("john1").password("12345").authorities(List.of(Authority.builder().authority("read").build())).build(),
                MyUser.builder().username("john2").password("6789").authorities(List.of(Authority.builder().authority("write").build())).build());
    }

    private MyUser toCreateUser() {
        return MyUser.builder().username("john").password("12345").authorities(List.of(
                Authority.builder().authority("read").build())).build();
    }
}