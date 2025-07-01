package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Inventario;
import com.letrasypapeles.backend.entity.Producto;
import com.letrasypapeles.backend.entity.Sucursal;
import com.letrasypapeles.backend.repository.InventarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioService inventarioService;

    private Inventario inventario;
    private Producto producto;
    private Sucursal sucursal;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        producto = Producto.builder()
                .id(1L)
                .nombre("El Quijote")
                .build();

        sucursal = Sucursal.builder()
                .id(1L)
                .nombre("Tienda Central")
                .build();

        inventario = Inventario.builder()
                .id(1L)
                .cantidad(50)
                .umbral(10)
                .producto(producto)
                .sucursal(sucursal)
                .build();
    }

    @Test
    void obtenerTodos() {
        // Given
        List<Inventario> inventarios = Arrays.asList(inventario);
        when(inventarioRepository.findAll()).thenReturn(inventarios);

        // When
        List<Inventario> result = inventarioService.obtenerTodos();

        // Then
        assertEquals(1, result.size());
        assertEquals(50, result.get(0).getCantidad());
        verify(inventarioRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId() {
        // Given
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));

        // When
        Optional<Inventario> result = inventarioService.obtenerPorId(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(50, result.get().getCantidad());
        assertEquals(10, result.get().getUmbral());
        verify(inventarioRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerPorIdNoExistente() {
        // Given
        when(inventarioRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        Optional<Inventario> result = inventarioService.obtenerPorId(99L);

        // Then
        assertFalse(result.isPresent());
        verify(inventarioRepository, times(1)).findById(99L);
    }

    @Test
    void guardar() {
        // Given
        Inventario inventarioNuevo = Inventario.builder()
                .cantidad(30)
                .umbral(5)
                .producto(producto)
                .sucursal(sucursal)
                .build();

        Inventario inventarioGuardado = Inventario.builder()
                .id(2L)
                .cantidad(30)
                .umbral(5)
                .producto(producto)
                .sucursal(sucursal)
                .build();
        
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventarioGuardado);

        // When
        Inventario result = inventarioService.guardar(inventarioNuevo);

        // Then
        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals(30, result.getCantidad());
        assertEquals(5, result.getUmbral());
        verify(inventarioRepository, times(1)).save(any(Inventario.class));
    }

    @Test
    void eliminar() {
        // Given
        Long idToDelete = 1L;
        doNothing().when(inventarioRepository).deleteById(idToDelete);

        // When
        inventarioService.eliminar(idToDelete);

        // Then
        verify(inventarioRepository, times(1)).deleteById(idToDelete);
    }

    @Test
    void obtenerPorProductoId() {
        // Given
        List<Inventario> inventarios = Arrays.asList(inventario);
        when(inventarioRepository.findByProductoId(1L)).thenReturn(inventarios);

        // When
        List<Inventario> result = inventarioService.obtenerPorProductoId(1L);

        // Then
        assertEquals(1, result.size());
        assertEquals(50, result.get(0).getCantidad());
        assertEquals("El Quijote", result.get(0).getProducto().getNombre());
        verify(inventarioRepository, times(1)).findByProductoId(1L);
    }

    @Test
    void obtenerPorSucursalId() {
        // Given
        List<Inventario> inventarios = Arrays.asList(inventario);
        when(inventarioRepository.findBySucursalId(1L)).thenReturn(inventarios);

        // When
        List<Inventario> result = inventarioService.obtenerPorSucursalId(1L);

        // Then
        assertEquals(1, result.size());
        assertEquals(50, result.get(0).getCantidad());
        assertEquals("Tienda Central", result.get(0).getSucursal().getNombre());
        verify(inventarioRepository, times(1)).findBySucursalId(1L);
    }

    @Test
    void obtenerInventarioBajoUmbral() {
        // Given
        List<Inventario> inventarios = Arrays.asList(inventario);
        when(inventarioRepository.findByCantidadLessThan(100)).thenReturn(inventarios);

        // When
        List<Inventario> result = inventarioService.obtenerInventarioBajoUmbral(100);

        // Then
        assertEquals(1, result.size());
        assertEquals(50, result.get(0).getCantidad());
        verify(inventarioRepository, times(1)).findByCantidadLessThan(100);
    }
}
