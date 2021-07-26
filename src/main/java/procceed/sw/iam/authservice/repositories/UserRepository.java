package procceed.sw.iam.authservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import procceed.sw.iam.authservice.entities.MyUser;

import java.util.Optional;

public interface UserRepository extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findByUsername(String username);
}
