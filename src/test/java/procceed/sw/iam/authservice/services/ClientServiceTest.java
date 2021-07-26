package procceed.sw.iam.authservice.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import procceed.sw.iam.authservice.config.WebSecurityTestConfiguration;
import procceed.sw.iam.authservice.entities.Client;
import procceed.sw.iam.authservice.entities.Scope;
import procceed.sw.iam.authservice.exceptions.ClientAlreadyExistsException;
import procceed.sw.iam.authservice.repositories.ClientRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
@Import(WebSecurityTestConfiguration.class)
class ClientServiceTest {

    @Autowired
    private ClientService clientService;

    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void givenNonExistentClient_WhenCreateClient_ThenClientCreated() {
        Client client = toCreateClient();
        when(clientRepository.findByClient(client.getClient())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("anyString");

        clientService.createClient(client);

        assertTrue(client.getScopes().stream().allMatch(scope -> scope.getClient().equals(client)));

        verify(clientRepository, times(1)).save(client);
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    void givenExistingClient_WhenCreateClient_ClientAlreadyExistsException() {
        Client client = toCreateClient();
        when(clientRepository.findByClient(client.getClient())).thenReturn(Optional.of(client));

        assertThrows(ClientAlreadyExistsException.class,
                () -> clientService.createClient(client));

        verify(clientRepository, never()).save(client);
        verify(passwordEncoder, never()).encode(client.getSecret());
    }

    @Test
    void givenListOfClients_WhenGetAllClients_ClientsReturned() {
        when(clientRepository.findAll()).thenReturn(twoClients());

        List<Client> returnedClients = clientService.getAllClients();
        assertEquals(twoClients(), returnedClients);
    }

    private List<Client> twoClients() {
        return List.of(Client.builder().client("client1").secret("secret1").clientGrantType(Set.of()).scopes(Set.of()).build(),
                Client.builder().client("client2").secret("secret2").clientGrantType(Set.of()).scopes(Set.of()).build());
    }

    private Client toCreateClient() {
        return Client.builder().client("client1").secret("secret1").scopes(Set.of(
                Scope.builder().scope("scope1").build()
        )).build();
    }
}