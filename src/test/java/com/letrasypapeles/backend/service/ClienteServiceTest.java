package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.repository.ClienteRepository;
import com.letrasypapeles.backend.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;
    private Role role;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        role = new Role();
        role.setNombre("CLIENTE");

        cliente = Cliente.builder()
                .id(1L)
                .nombre("Test")
                .apellido("Usuario")
                .email("test@example.com")
                .contraseña("password123")
                .puntosFidelidad(0)
                .build();
    }

    @Test
    void obtenerTodos() {
        // Given
        List<Cliente> clientes = Arrays.asList(cliente);
        when(clienteRepository.findAll()).thenReturn(clientes);

        // When
        List<Cliente> result = clienteService.obtenerTodos();

        // Then
        assertEquals(1, result.size());
        assertEquals("Test", result.get(0).getNombre());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId() {
        // Given
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        // When
        Optional<Cliente> result = clienteService.obtenerPorId(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerPorIdNoExistente() {
        // Given
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        Optional<Cliente> result = clienteService.obtenerPorId(99L);

        // Then
        assertFalse(result.isPresent());
        verify(clienteRepository, times(1)).findById(99L);
    }

    @Test
    void obtenerPorEmail() {
        // Given
        when(clienteRepository.findByEmail("test@example.com")).thenReturn(Optional.of(cliente));

        // When
        Optional<Cliente> result = clienteService.obtenerPorEmail("test@example.com");

        // Then
        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getNombre());
        verify(clienteRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void registrarCliente() {
        // Given
        String rawPassword = "password123";
        String encodedPassword = "encoded_password";
        when(clienteRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(roleRepository.findByNombre("CLIENTE")).thenReturn(Optional.of(role));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // When
        Cliente clienteInput = Cliente.builder()
                .nombre("Test")
                .apellido("Usuario")
                .email("test@example.com")
                .contraseña(rawPassword)
                .build();
        Cliente result = clienteService.registrarCliente(clienteInput);

        // Then
        assertNotNull(result);
        verify(passwordEncoder, times(1)).encode(rawPassword);
        verify(roleRepository, times(1)).findByNombre("CLIENTE");
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void registrarClienteEmailDuplicado() {
        // Given
        when(clienteRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        Cliente clienteInput = Cliente.builder()
                .nombre("Test")
                .apellido("Usuario")
                .email("test@example.com")
                .contraseña("password123")
                .build();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            clienteService.registrarCliente(clienteInput);
        });

        assertEquals("El correo electrónico ya está registrado.", exception.getMessage());
        verify(clienteRepository, times(1)).existsByEmail(anyString());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void actualizarCliente() {
        // Given
        Cliente clienteActualizado = Cliente.builder()
                .id(1L)
                .nombre("Test Actualizado")
                .apellido("Usuario Actualizado")
                .email("test@example.com")
                .contraseña("password123")
                .puntosFidelidad(10)
                .build();
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteActualizado);

        // When
        Cliente result = clienteService.actualizarCliente(clienteActualizado);

        // Then
        assertNotNull(result);
        assertEquals("Test Actualizado", result.getNombre());
        assertEquals("Usuario Actualizado", result.getApellido());
        assertEquals(10, result.getPuntosFidelidad());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void eliminar() {
        // Given
        Long idToDelete = 1L;
        doNothing().when(clienteRepository).deleteById(idToDelete);

        // When
        clienteService.eliminar(idToDelete);

        // Then
        verify(clienteRepository, times(1)).deleteById(idToDelete);
    }

    @Test
void testEliminarCliente() {
    Long clienteId = 1L;

    // No importa si existe o no, solo verificamos que se llame deleteById
    doNothing().when(clienteRepository).deleteById(clienteId);

    clienteService.eliminar(clienteId);

    verify(clienteRepository, times(1)).deleteById(clienteId);
}
@Test
void testRegistrarClienteYaRegistrado() {
    Cliente cliente = new Cliente();
    cliente.setEmail("ya@existe.com");

    when(clienteRepository.existsByEmail(cliente.getEmail())).thenReturn(true);

    assertThrows(RuntimeException.class, () -> clienteService.registrarCliente(cliente));
}
@Test
void registrarClienteSinRolExistente() {
    String rawPassword = "password123";
    String encodedPassword = "encoded_password";

    when(clienteRepository.existsByEmail(anyString())).thenReturn(false);
    when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
    when(roleRepository.findByNombre("CLIENTE")).thenReturn(Optional.empty());
    when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

    Cliente clienteInput = Cliente.builder()
            .nombre("Test")
            .apellido("Usuario")
            .email("nuevo@cliente.com")
            .contraseña(rawPassword)
            .build();

    Cliente result = clienteService.registrarCliente(clienteInput);

    assertNotNull(result);
    verify(roleRepository, times(1)).findByNombre("CLIENTE");
    verify(clienteRepository, times(1)).save(any(Cliente.class));
}

}
