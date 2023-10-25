package cz.muni.fi.userservice.security;

import cz.muni.fi.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import cz.muni.fi.userservice.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class UserSecurityConfig {
    @Autowired
    UserService userService;

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        Set<UserDetails> users = new HashSet<>();
        for (User u: userService.getAllUsers()) {
            if (u.isAdmin()) {
                users.add(createAdmin(u));
            } else {
                users.add(createUser(u));
            }
        }

        return new InMemoryUserDetailsManager(users);
    }

    private UserDetails createAdmin(User user) {
        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .roles("ADMIN")
                .build();
    }

    private UserDetails createUser(User user) {
        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .roles("USER")
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests -> requests
                .requestMatchers("/user/*").hasRole("ADMIN")
                                .anyRequest().permitAll())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
