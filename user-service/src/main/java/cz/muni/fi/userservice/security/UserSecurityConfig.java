package cz.muni.fi.userservice.security;

//import cz.muni.fi.userservice.entity.User;
//import cz.muni.fi.userservice.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class UserSecurityConfig {
    /*@Autowired
    UserService userService;*/

    @Bean
    public PasswordEncoder encoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = org.springframework.security.core.userdetails.User.withUsername("pepa@novak.cz")
                .password(passwordEncoder.encode("heslo"))
                .roles("USER")
                .build();

      /*  Set<User> admins = new HashSet<>();
        for (User user: userService.getAllUsers()) {
            if (userService.isAdmin(user)) {
                admins.add(user);
            }
        }
        UserDetails userDetails = new UserDetails() {
        }*/


        UserDetails admin = org.springframework.security.core.userdetails.User.withUsername("admin@eshop.com")
                .password(passwordEncoder.encode("admin"))
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests -> requests
                .requestMatchers("/user/*").hasRole("ADMIN") //skontroluj si v starom projekte cesty!!!! co vsetko bolo povolene a co zakazane, pretoze ty to mas inak
                                .anyRequest().permitAll())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
