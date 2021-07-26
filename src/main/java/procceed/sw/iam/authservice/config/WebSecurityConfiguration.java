package procceed.sw.iam.authservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import procceed.sw.iam.authservice.repositories.UserRepository;
import procceed.sw.iam.authservice.services.JpaUserDetailsService;

@Profile("!test")
@RequiredArgsConstructor
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepository;

    @Bean
    public UserDetailsService jpaUserDetailsService() {
        return new JpaUserDetailsService(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin();
        http.csrf().disable();
        http.csrf(c -> c.ignoringAntMatchers(
                "/users/**", "/clients/**"
        ));

        http.authorizeRequests()
                .mvcMatchers("/users/**").permitAll()
                .mvcMatchers("/clients/**").permitAll()
                .anyRequest().authenticated();

    }

}
