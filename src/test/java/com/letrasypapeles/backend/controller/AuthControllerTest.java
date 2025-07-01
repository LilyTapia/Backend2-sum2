package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.dto.LoginRequest;
import com.letrasypapeles.backend.dto.LoginResponse;
import com.letrasypapeles.backend.dto.RegisterRequest;
import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.service.ClienteService;
import com.letrasypapeles.backend.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private ClienteService clienteService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAutenticarUsuario() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtil.generateJwtToken(authentication)).thenReturn("jwt-token");

        ResponseEntity<?> response = authController.autenticarUsuario(loginRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof LoginResponse);
        assertEquals("jwt-token", ((LoginResponse) response.getBody()).getToken());
    }

    @Test
    void testRegistrarUsuario() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setNombre("Juan");
        registerRequest.setApellido("Pérez");
        registerRequest.setEmail("juan@test.com");
        registerRequest.setPassword("password");

        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan");
        cliente.setApellido("Pérez");
        cliente.setEmail("juan@test.com");

        when(clienteService.registrarCliente(any(Cliente.class))).thenReturn(cliente);

        ResponseEntity<?> response = authController.registrarUsuario(registerRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(cliente, response.getBody());
    }

    @Test
    void testRegistrarUsuarioError() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setNombre("Juan");
        registerRequest.setApellido("Pérez");
        registerRequest.setEmail("juan@test.com");
        registerRequest.setPassword("password");

        when(clienteService.registrarCliente(any(Cliente.class)))
                .thenThrow(new RuntimeException("Error de prueba"));

        ResponseEntity<?> response = authController.registrarUsuario(registerRequest);

        assertEquals(500, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Error al registrar usuario"));
    }
}