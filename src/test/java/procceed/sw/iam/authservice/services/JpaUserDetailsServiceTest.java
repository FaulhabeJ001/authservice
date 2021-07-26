package procceed.sw.iam.authservice.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import procceed.sw.iam.authservice.entities.MyUser;
import procceed.sw.iam.authservice.repositories.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class JpaUserDetailsServiceTest {

    @Autowired
    UserDetailsService userDetailsService;

    @MockBean
    UserRepository userRepository;

    @Test
    @DisplayName("Considering that the user already exists in the database," +
            "test that the method returns a valid UserDetails object.")
    public void loadUserByUsernameWhenUserExistsTest() {
        MyUser user = MyUser.builder().username("user").build();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        assertEquals(user.getUsername(), userDetails.getUsername());

    }

    @Test
    @DisplayName("Considering that the user doesn't exist in the database," +
            "test that the method throws UsernameNotFoundException.")
    public void loadUserByUsernameWhenUserDoesntExistTest() {
        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("user"));
    }
}