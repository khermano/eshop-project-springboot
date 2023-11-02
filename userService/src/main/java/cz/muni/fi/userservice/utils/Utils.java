package cz.muni.fi.userservice.utils;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Utils {
    public void response401(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("WWW-Authenticate", "Basic realm=\"type email and password\"");
        response.setContentType("text/html");
        PrintWriter printWriter = response.getWriter();
        printWriter.println("<html><body><h1>401 Unauthorized</h1> Go away ...</body></html>");
    }
}
