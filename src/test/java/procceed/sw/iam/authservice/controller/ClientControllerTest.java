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
import procceed.sw.iam.authservice.entities.Client;
import procceed.sw.iam.authservice.services.ClientService;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(ClientController.class)
@Import(WebSecurityTestConfiguration.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Test
    void createClient() throws Exception {
        mockMvc.perform(post("/clients")
                .content(new ObjectMapper().writeValueAsString(toCreateClient()))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists(org.springframework.http.HttpHeaders.LOCATION));

        verify(clientService, times(1)).createClient(toCreateClient());
    }

    @Test
    void getClients() throws Exception {
        when(clientService.getAllClients()).thenReturn(twoClients());

        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].clientGrantType", hasSize(0)));
    }

    private List<Client> twoClients() {
        return List.of(Client.builder().client("client1").secret("secret1").clientGrantType(Set.of()).scopes(Set.of()).build(),
                        Client.builder().client("client2").secret("secret2").clientGrantType(Set.of()).scopes(Set.of()).build());
    }

    private Client toCreateClient() {
        return Client.builder().client("client1").secret("secret1").build();
    }
}