package cz.muni.fi.userservice.security;

import cz.muni.fi.userservice.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
    private final Utils utils = new Utils();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx) throws IOException{
        utils.response401(response);
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("type email and password");
        super.afterPropertiesSet();
    }
}
