package com.epam.esm.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class CustomRestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final String APPLICATION_JSON = "application/json";

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(APPLICATION_JSON);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println("{ \"errorMessage\": \"Jwt token is expired or invalid\", \"errorCode\": \"40103\"  }");
    }

    @ExceptionHandler(value = JwtAuthenticationException.class)
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         JwtAuthenticationException e) throws IOException, ServletException {
        response.setContentType(APPLICATION_JSON);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println("{ \"errorMessage\": \"Jwt token is expired or invalid\", \"errorCode\": \"40103\"  }");
    }


    @ExceptionHandler(value = AccessDeniedException.class)
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AccessDeniedException e) throws IOException {
        response.setContentType(APPLICATION_JSON);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getOutputStream().println("{ \"errorMessage\": \"Access denied\", \"errorCode\": \"40306\"  }");
    }

    @ExceptionHandler(value = Exception.class)
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         Exception e) throws IOException {
        response.setContentType(APPLICATION_JSON);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getOutputStream().println("{ \"errorMessage\": \" Internal server error\", \"errorCode\": \"50003\"  }");
    }
}
