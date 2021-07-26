package procceed.sw.iam.authservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import procceed.sw.iam.authservice.entities.MyUser;
import procceed.sw.iam.authservice.entities.UserDetailsWrapper;
import procceed.sw.iam.authservice.repositories.UserRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<MyUser> user = userRepository.findByUsername(username);

        return user.map(UserDetailsWrapper::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
