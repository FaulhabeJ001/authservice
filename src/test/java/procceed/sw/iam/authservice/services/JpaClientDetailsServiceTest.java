package procceed.sw.iam.authservice.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import procceed.sw.iam.authservice.entities.Client;
import procceed.sw.iam.authservice.repositories.ClientRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@SpringBootTest
class JpaClientDetailsServiceTest {

    @MockBean
    private ClientRepository clientRepository;

    private JpaClientDetailsService clientDetailsService;

    @BeforeEach
    void setup() {
        clientDetailsService = new JpaClientDetailsService(clientRepository);
    }

    @Test
    @DisplayName("Considering that the client exists in the database," +
            "test that the method returns a valid ClientDetails instance.")
    public void givenClient_whenLoadClient_thenClientReturned() {
        String clientName = "client";

        Client client = Client.builder().client(clientName).build();

        when(clientRepository.findByClient(clientName)).thenReturn(Optional.of(client));

        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientName);

        assertEquals(client.getClient(), clientDetails.getClientId());
    }

    @Test
    @DisplayName("Considering that the client doesn't exist in the database," +
            "test that the method throws ClientRegistrationException.")
    public void givenNoClient_whenLoadClient_thenException() {

        when(clientRepository.findByClient("client")).thenReturn(Optional.empty());

        assertThrows(ClientRegistrationException.class,
                () -> clientDetailsService.loadClientByClientId("client"));
    }
}