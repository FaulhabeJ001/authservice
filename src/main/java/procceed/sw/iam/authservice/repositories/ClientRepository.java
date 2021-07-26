package procceed.sw.iam.authservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import procceed.sw.iam.authservice.entities.Client;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByClient(String client);
}
