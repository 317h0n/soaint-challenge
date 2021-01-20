package com.soaint.shoppingcar.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException) throws IOException, ServletException {        
        Map<String, Object> body = new HashMap<String, Object>();
		body.put("error", true);
		body.put("message", "Login first");
		res.getWriter().write(new ObjectMapper().writeValueAsString(body));
		res.setStatus(HttpStatus.FORBIDDEN.value());
		res.setContentType("application/json");
    }

}
