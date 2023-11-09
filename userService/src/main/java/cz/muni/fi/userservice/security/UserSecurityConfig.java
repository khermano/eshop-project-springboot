package cz.muni.fi.userservice.security;

import cz.muni.fi.userservice.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import cz.muni.fi.userservice.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class UserSecurityConfig {
    private static final String ADMIN = "ADMIN";

    private static final String USER = "USER";

    @Autowired
    private UserRepository userRepository;

    private UserDetails createUserDetails(User user, String role) {
        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .roles(role)
                .build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        Set<UserDetails> users = new HashSet<>();
        for (User u: userRepository.findAll()) {
            if (u.isAdmin()) {
                users.add(createUserDetails(u, ADMIN));
            } else {
                users.add(createUserDetails(u, USER));
            }
        }
        return new InMemoryUserDetailsManager(users);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests -> requests
                        .requestMatchers("/users").hasRole(ADMIN)
                        .requestMatchers("/users/*").hasRole(ADMIN)
                        .anyRequest().hasRole(USER))
                .exceptionHandling(x -> x.authenticationEntryPoint(new BasicAuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
                        response401(response);
                    }
                }))
                .exceptionHandling(x -> x.accessDeniedHandler((request, response, accessDeniedException) -> response401(response)))
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    private void response401(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("WWW-Authenticate", "Basic realm=\"type email and password\"");
        response.setContentType("text/html");
        PrintWriter printWriter = response.getWriter();
        printWriter.println("<html><body><h1>401 Unauthorized</h1> Go away ...</body></html>");
    }
}
