package procceed.sw.iam.authservice.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import procceed.sw.iam.authservice.config.WebSecurityTestConfiguration;
import procceed.sw.iam.authservice.entities.Authority;
import procceed.sw.iam.authservice.entities.MyUser;
import procceed.sw.iam.authservice.exceptions.UserAlreadyExistsException;
import procceed.sw.iam.authservice.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ActiveProfiles("test")
@SpringBootTest
@Import(WebSecurityTestConfiguration.class)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;


    @Test
    void givenNonExistentUser_WhenCreateUser_ThenUserCreated() {
        MyUser user = toCreateUser();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("567");

        userService.createUser(user);

        assertEquals("567", user.getPassword());
        assertEquals(user.getAuthorities().get(0).getUser(), user);
        verify(userRepository, times(1)).save(user);
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    void givenExistentUser_WhenCreateUser_ThenUserCreated() {
        MyUser user = toCreateUser();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class,
                () -> userService.createUser(user));

        verify(userRepository, never()).save(user);
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void givenListOfUsers_WhenGetAllUser_UsersReturned() {
        when(userRepository.findAll()).thenReturn(twoUsers());

        List<MyUser> returnedUsers = userService.getAllUsers();

        assertEquals(twoUsers(), returnedUsers);
    }

    private List<MyUser> twoUsers() {
        return List.of( MyUser.builder().username("john1").password("12345").authorities(List.of(Authority.builder().authority("read").build())).build(),
                        MyUser.builder().username("john2").password("6789").authorities(List.of(Authority.builder().authority("write").build())).build());
    }

    private MyUser toCreateUser() {
        return MyUser.builder().username("john").password("12345").authorities(List.of(
                Authority.builder().authority("read").build())).build();
    }
}