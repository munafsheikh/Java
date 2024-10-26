package com.virtrics.experiment.config;

import com.virtrics.experiment.util.JwtUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

@Log4j2
public class BaseToSwaggerURIFilter implements Filter {
    public static final String DOCUMENTATION_URL = "/swagger-ui/index.html";
    public static final String HOME_URL = "/login";

    public static final String API_SUFFIX = "/api";


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        log.info("\n\n doFilter > URI --> " + request.getRequestURI());
        HttpServletResponse response = (HttpServletResponse) servletResponse;

//        if (request.getRequestURI().toLowerCase().contains(API_SUFFIX.toLowerCase())) {
////            if (profile.equalsIgnoreCase("uat"))
//            checkAuthentication(servletRequest, servletResponse, filterChain);
//        }

//        if (request.getRequestURI().isEmpty() || request.getRequestURI().equals("/")) {
//            response.sendRedirect(DOCUMENTATION_URL);
//            return;
//        }
//        try {
        filterChain.doFilter(servletRequest, servletResponse);
//
//        } catch (Exception e) {
//            response.sendRedirect(DOCUMENTATION_URL);
//        }

    }


    private void checkAuthentication(ServletRequest servletRequest,
                                     ServletResponse servletResponse, FilterChain filterChain)
            throws IOException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Enumeration<String> authHeaders = request.getHeaders(HttpHeaders.AUTHORIZATION);
        if (authHeaders.hasMoreElements()) {
            List<String> authHeaderList = List.of(authHeaders.nextElement());

            if (authHeaderList == null || authHeaderList.isEmpty()) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.sendRedirect(HOME_URL);
            } else {
                JwtUtil.validateToken(authHeaderList.get(0));
            }
        }
    }


}