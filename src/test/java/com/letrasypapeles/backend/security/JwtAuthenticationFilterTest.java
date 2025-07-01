package com.letrasypapeles.backend.security;

import com.letrasypapeles.backend.service.UsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class JwtAuthenticationFilterTest {

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil, usuarioService);
        SecurityContextHolder.clearContext();
    }

    @Test
    void testDoFilterInternal_ValidToken() throws ServletException, IOException {
        String token = "valid.jwt.token";
        String bearerToken = "Bearer " + token;
        String username = "testuser";
        UserDetails userDetails = new User(username, "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        doReturn(bearerToken).when(request).getHeader("Authorization");
        doReturn("/api/test").when(request).getRequestURI();
        doReturn(true).when(jwtUtil).validateJwtToken(token);
        doReturn(username).when(jwtUtil).getUsernameFromToken(token);
        doReturn(userDetails).when(usuarioService).loadUserByUsername(username);

        SecurityContextHolder.clearContext();

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals(username, ((UserDetails) auth.getPrincipal()).getUsername());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
        String invalidToken = "invalid.token";

        SecurityContextHolder.clearContext();

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_NoToken() throws ServletException, IOException {
        doReturn(null).when(request).getHeader("Authorization");
        doReturn("/api/test").when(request).getRequestURI();

        SecurityContextHolder.clearContext();

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_BearerToken() throws ServletException, IOException {
        String token = "valid.jwt.token";
        String bearerToken = "Bearer " + token;
        String username = "testuser";
        UserDetails userDetails = new User(username, "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        doReturn(bearerToken).when(request).getHeader("Authorization");
        doReturn("/api/test").when(request).getRequestURI();
        doReturn(true).when(jwtUtil).validateJwtToken(token);
        doReturn(username).when(jwtUtil).getUsernameFromToken(token);
        doReturn(userDetails).when(usuarioService).loadUserByUsername(username);

        SecurityContextHolder.clearContext();

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_ExceptionHandling() throws ServletException, IOException {
        String token = "valid.jwt.token";
        String bearerToken = "Bearer " + token;

        doReturn(bearerToken).when(request).getHeader("Authorization");
        doReturn("/api/test").when(request).getRequestURI();
        doThrow(new RuntimeException("Test exception")).when(jwtUtil).validateJwtToken(token);

        SecurityContextHolder.clearContext();

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_EmptyAuthHeader() throws ServletException, IOException {
        doReturn("").when(request).getHeader("Authorization");
        doReturn("/api/test").when(request).getRequestURI();

        SecurityContextHolder.clearContext();

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_AuthHeaderWithoutBearer() throws ServletException, IOException {
        doReturn("Basic sometoken").when(request).getHeader("Authorization");
        doReturn("/api/test").when(request).getRequestURI();

        SecurityContextHolder.clearContext();

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_ValidTokenWithNullUsername() throws ServletException, IOException {
        String token = "valid.jwt.token";
        String bearerToken = "Bearer " + token;

        doReturn(bearerToken).when(request).getHeader("Authorization");
        doReturn("/api/test").when(request).getRequestURI();
        doReturn(true).when(jwtUtil).validateJwtToken(token);
        doReturn(null).when(jwtUtil).getUsernameFromToken(token);

        SecurityContextHolder.clearContext();

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth);
        verify(filterChain).doFilter(request, response);
    }
}