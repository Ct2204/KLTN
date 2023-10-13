package kltn.userservice;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        StringBuilder errorStatus = new StringBuilder();
        response.setContentType("application/json");
        errorStatus.append("{");
        errorStatus.append("\"status\": \"SC_NOT_FOUND\",\n");
        errorStatus.append("    \"code\": 404,\n");
        errorStatus.append("    \"message\": \"Resource not found!\"\n");
        errorStatus.append("}");
        response.getWriter().write(errorStatus.toString());
    }
}



