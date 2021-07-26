package procceed.sw.iam.authservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import procceed.sw.iam.authservice.entities.Client;
import procceed.sw.iam.authservice.entities.ClientDetailsWrapper;
import procceed.sw.iam.authservice.repositories.ClientRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class JpaClientDetailsService implements ClientDetailsService {

    private final ClientRepository clientRepository;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

        Optional<Client> client = clientRepository.findByClient(clientId);

        return client
                .map(ClientDetailsWrapper::new)
                .orElseThrow(() -> new ClientRegistrationException("client not found"));
    }
}
