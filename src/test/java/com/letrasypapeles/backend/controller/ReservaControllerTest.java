package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Reserva;
import com.letrasypapeles.backend.service.ReservaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReservaControllerTest {

    @Mock
    private ReservaService reservaService;

    @InjectMocks
    private ReservaController reservaController;

    private Reserva reserva;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reserva = new Reserva();
        reserva.setId(1L);
    }

    @Test
    void testObtenerTodas() {
        List<Reserva> reservas = Arrays.asList(reserva);
        when(reservaService.obtenerTodas()).thenReturn(reservas);

        ResponseEntity<List<Reserva>> response = reservaController.obtenerTodas();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testObtenerPorId() {
        when(reservaService.obtenerPorId(1L)).thenReturn(Optional.of(reserva));

        ResponseEntity<Reserva> response = reservaController.obtenerPorId(1L);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testObtenerPorIdNoEncontrado() {
        when(reservaService.obtenerPorId(1L)).thenReturn(Optional.empty());

        ResponseEntity<Reserva> response = reservaController.obtenerPorId(1L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testObtenerPorClienteId() {
        List<Reserva> reservas = Arrays.asList(reserva);
        when(reservaService.obtenerPorClienteId(1L)).thenReturn(reservas);

        ResponseEntity<List<Reserva>> response = reservaController.obtenerPorClienteId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testCrearReserva() {
        when(reservaService.guardar(any(Reserva.class))).thenReturn(reserva);

        ResponseEntity<Reserva> response = reservaController.crearReserva(reserva);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testActualizarReserva() {
        when(reservaService.obtenerPorId(1L)).thenReturn(Optional.of(reserva));
        when(reservaService.guardar(any(Reserva.class))).thenReturn(reserva);

        ResponseEntity<Reserva> response = reservaController.actualizarReserva(1L, reserva);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testActualizarReservaNoEncontrada() {
        when(reservaService.obtenerPorId(1L)).thenReturn(Optional.empty());

        ResponseEntity<Reserva> response = reservaController.actualizarReserva(1L, reserva);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testEliminarReserva() {
        when(reservaService.obtenerPorId(1L)).thenReturn(Optional.of(reserva));
        doNothing().when(reservaService).eliminar(1L);

        ResponseEntity<Void> response = reservaController.eliminarReserva(1L);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testEliminarReservaNoEncontrada() {
        when(reservaService.obtenerPorId(1L)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = reservaController.eliminarReserva(1L);

        assertEquals(404, response.getStatusCodeValue());
    }
}