package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.entity.Producto;
import com.letrasypapeles.backend.entity.Reserva;
import com.letrasypapeles.backend.repository.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaService reservaService;

    private Reserva reserva;
    private Cliente cliente;
    private Producto producto;
    private LocalDateTime fechaReserva;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        fechaReserva = LocalDateTime.now();
        
        cliente = Cliente.builder()
                .id(1L)
                .nombre("Test")
                .apellido("Usuario")
                .email("test@example.com")
                .build();

        producto = Producto.builder()
                .id(1L)
                .nombre("El Quijote")
                .build();

        reserva = Reserva.builder()
                .id(1L)
                .fechaReserva(fechaReserva)
                .estado("PENDIENTE")
                .cliente(cliente)
                .producto(producto)
                .build();
    }

    @Test
    void obtenerTodas() {
        // Given
        List<Reserva> reservas = Arrays.asList(reserva);
        when(reservaRepository.findAll()).thenReturn(reservas);

        // When
        List<Reserva> result = reservaService.obtenerTodas();

        // Then
        assertEquals(1, result.size());
        assertEquals("PENDIENTE", result.get(0).getEstado());
        verify(reservaRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId() {
        // Given
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        // When
        Optional<Reserva> result = reservaService.obtenerPorId(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("PENDIENTE", result.get().getEstado());
        assertEquals(cliente.getId(), result.get().getCliente().getId());
        assertEquals(producto.getId(), result.get().getProducto().getId());
        verify(reservaRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerPorIdNoExistente() {
        // Given
        when(reservaRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        Optional<Reserva> result = reservaService.obtenerPorId(99L);

        // Then
        assertFalse(result.isPresent());
        verify(reservaRepository, times(1)).findById(99L);
    }

    @Test
    void guardar() {
        // Given
        Reserva reservaNueva = Reserva.builder()
                .fechaReserva(fechaReserva)
                .estado("NUEVA")
                .cliente(cliente)
                .producto(producto)
                .build();

        Reserva reservaGuardada = Reserva.builder()
                .id(2L)
                .fechaReserva(fechaReserva)
                .estado("NUEVA")
                .cliente(cliente)
                .producto(producto)
                .build();
        
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaGuardada);

        // When
        Reserva result = reservaService.guardar(reservaNueva);

        // Then
        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("NUEVA", result.getEstado());
        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }

    @Test
    void eliminar() {
        // Given
        Long idToDelete = 1L;
        doNothing().when(reservaRepository).deleteById(idToDelete);

        // When
        reservaService.eliminar(idToDelete);

        // Then
        verify(reservaRepository, times(1)).deleteById(idToDelete);
    }

    @Test
    void obtenerPorClienteId() {
        // Given
        List<Reserva> reservas = Arrays.asList(reserva);
        when(reservaRepository.findByClienteId(1L)).thenReturn(reservas);

        // When
        List<Reserva> result = reservaService.obtenerPorClienteId(1L);

        // Then
        assertEquals(1, result.size());
        assertEquals("PENDIENTE", result.get(0).getEstado());
        assertEquals(1L, result.get(0).getCliente().getId());
        verify(reservaRepository, times(1)).findByClienteId(1L);
    }

    @Test
    void obtenerPorProductoId() {
        // Given
        List<Reserva> reservas = Arrays.asList(reserva);
        when(reservaRepository.findByProductoId(1L)).thenReturn(reservas);

        // When
        List<Reserva> result = reservaService.obtenerPorProductoId(1L);

        // Then
        assertEquals(1, result.size());
        assertEquals("PENDIENTE", result.get(0).getEstado());
        assertEquals(1L, result.get(0).getProducto().getId());
        verify(reservaRepository, times(1)).findByProductoId(1L);
    }

    @Test
    void obtenerPorEstado() {
        // Given
        List<Reserva> reservas = Arrays.asList(reserva);
        when(reservaRepository.findByEstado("PENDIENTE")).thenReturn(reservas);

        // When
        List<Reserva> result = reservaService.obtenerPorEstado("PENDIENTE");

        // Then
        assertEquals(1, result.size());
        assertEquals("PENDIENTE", result.get(0).getEstado());
        verify(reservaRepository, times(1)).findByEstado("PENDIENTE");
    }
}
