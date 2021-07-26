package procceed.sw.iam.authservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import procceed.sw.iam.authservice.entities.MyUser;
import procceed.sw.iam.authservice.exceptions.UserAlreadyExistsException;
import procceed.sw.iam.authservice.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public MyUser createUser(MyUser user) {
        Optional<MyUser> u = userRepository.findByUsername(user.getUsername());

        if (u.isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getAuthorities().forEach(authority -> authority.setUser(user));

        return userRepository.save(user);
    }

    public List<MyUser> getAllUsers() {
        return userRepository.findAll();
    }
}
