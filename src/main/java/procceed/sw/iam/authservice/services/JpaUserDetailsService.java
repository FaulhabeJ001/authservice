package procceed.sw.iam.authservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import procceed.sw.iam.authservice.entities.MyUser;
import procceed.sw.iam.authservice.repositories.UserRepository;

import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<MyUser> u = userRepository.findByUsername(username);
        MyUser myUser;

        if (!u.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        } else {
            myUser = u.get();
        }

        User.UserBuilder userBuilder = User.withUsername(myUser.getUsername());
        UserDetails userDetails = userBuilder
                .password(myUser.getPassword())
                .authorities(myUser.getAuthorities().stream()
                        .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                        .collect(Collectors.toList()))
                .build();

        return userDetails;
    }
}
