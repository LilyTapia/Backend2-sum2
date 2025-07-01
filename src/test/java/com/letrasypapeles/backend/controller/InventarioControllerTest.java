package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Inventario;
import com.letrasypapeles.backend.service.InventarioService;
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

class InventarioControllerTest {

    @Mock
    private InventarioService inventarioService;

    @InjectMocks
    private InventarioController inventarioController;

    private Inventario inventario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        inventario = new Inventario();
        inventario.setId(1L);
        inventario.setCantidad(100);
    }

    @Test
    void testObtenerTodos() {
        List<Inventario> inventarios = Arrays.asList(inventario);
        when(inventarioService.obtenerTodos()).thenReturn(inventarios);

        ResponseEntity<List<Inventario>> response = inventarioController.obtenerTodos();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testObtenerPorId() {
        when(inventarioService.obtenerPorId(1L)).thenReturn(Optional.of(inventario));

        ResponseEntity<Inventario> response = inventarioController.obtenerPorId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(100, response.getBody().getCantidad());
    }

    @Test
    void testObtenerPorIdNoEncontrado() {
        when(inventarioService.obtenerPorId(1L)).thenReturn(Optional.empty());

        ResponseEntity<Inventario> response = inventarioController.obtenerPorId(1L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testObtenerPorProductoId() {
        List<Inventario> inventarios = Arrays.asList(inventario);
        when(inventarioService.obtenerPorProductoId(1L)).thenReturn(inventarios);

        ResponseEntity<List<Inventario>> response = inventarioController.obtenerPorProductoId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testObtenerPorSucursalId() {
        List<Inventario> inventarios = Arrays.asList(inventario);
        when(inventarioService.obtenerPorSucursalId(1L)).thenReturn(inventarios);

        ResponseEntity<List<Inventario>> response = inventarioController.obtenerPorSucursalId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testCrearInventario() {
        when(inventarioService.guardar(any(Inventario.class))).thenReturn(inventario);

        ResponseEntity<Inventario> response = inventarioController.crearInventario(inventario);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(100, response.getBody().getCantidad());
    }

    @Test
    void testActualizarInventario() {
        when(inventarioService.obtenerPorId(1L)).thenReturn(Optional.of(inventario));
        when(inventarioService.guardar(any(Inventario.class))).thenReturn(inventario);

        ResponseEntity<Inventario> response = inventarioController.actualizarInventario(1L, inventario);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(100, response.getBody().getCantidad());
    }

    @Test
    void testActualizarInventarioNoEncontrado() {
        when(inventarioService.obtenerPorId(1L)).thenReturn(Optional.empty());

        ResponseEntity<Inventario> response = inventarioController.actualizarInventario(1L, inventario);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testEliminarInventario() {
        when(inventarioService.obtenerPorId(1L)).thenReturn(Optional.of(inventario));
        doNothing().when(inventarioService).eliminar(1L);

        ResponseEntity<Void> response = inventarioController.eliminarInventario(1L);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testEliminarInventarioNoEncontrado() {
        when(inventarioService.obtenerPorId(1L)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = inventarioController.eliminarInventario(1L);

        assertEquals(404, response.getStatusCodeValue());
    }
}