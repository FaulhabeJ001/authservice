package procceed.sw.iam.authservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import procceed.sw.iam.authservice.entities.Client;
import procceed.sw.iam.authservice.exceptions.ClientAlreadyExistsException;
import procceed.sw.iam.authservice.repositories.ClientRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public Client createClient(Client client) {
        Optional<Client> c = clientRepository.findByClient(client.getClient());

        if (c.isPresent()) {
            throw new ClientAlreadyExistsException("Client already exists");
        }

        if (client.getScopes() != null) {
            client.getScopes().forEach(scope -> scope.setClient(client));
        }
        if (client.getClientGrantType() != null) {
            client.getClientGrantType().forEach(grantType -> grantType.setClient(client));
        }
        client.setSecret(passwordEncoder.encode(client.getSecret()));

        return clientRepository.save(client);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
}
