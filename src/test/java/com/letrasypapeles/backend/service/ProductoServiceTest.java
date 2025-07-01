package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Categoria;
import com.letrasypapeles.backend.entity.Producto;
import com.letrasypapeles.backend.entity.Proveedor;
import com.letrasypapeles.backend.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;
    private Categoria categoria;
    private Proveedor proveedor;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        categoria = Categoria.builder()
                .id(1L)
                .nombre("Libros")
                .build();

        proveedor = Proveedor.builder()
                .id(1L)
                .nombre("Editorial XYZ")
                .build();

        producto = Producto.builder()
                .id(1L)
                .nombre("El Quijote")
                .descripcion("Obra de Miguel de Cervantes")
                .precio(new BigDecimal("29.99"))
                .stock(50)
                .categoria(categoria)
                .proveedor(proveedor)
                .build();
    }

    @Test
    void obtenerTodos() {
        // Given
        List<Producto> productos = Arrays.asList(producto);
        when(productoRepository.findAll()).thenReturn(productos);

        // When
        List<Producto> result = productoService.obtenerTodos();

        // Then
        assertEquals(1, result.size());
        assertEquals("El Quijote", result.get(0).getNombre());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId() {
        // Given
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        // When
        Optional<Producto> result = productoService.obtenerPorId(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("El Quijote", result.get().getNombre());
        assertEquals(new BigDecimal("29.99"), result.get().getPrecio());
        verify(productoRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerPorIdNoExistente() {
        // Given
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        Optional<Producto> result = productoService.obtenerPorId(99L);

        // Then
        assertFalse(result.isPresent());
        verify(productoRepository, times(1)).findById(99L);
    }

    @Test
    void guardar() {
        // Given
        Producto productoNuevo = Producto.builder()
                .nombre("Cien Años de Soledad")
                .descripcion("Obra de Gabriel García Márquez")
                .precio(new BigDecimal("24.99"))
                .stock(30)
                .categoria(categoria)
                .proveedor(proveedor)
                .build();

        Producto productoGuardado = Producto.builder()
                .id(2L)
                .nombre("Cien Años de Soledad")
                .descripcion("Obra de Gabriel García Márquez")
                .precio(new BigDecimal("24.99"))
                .stock(30)
                .categoria(categoria)
                .proveedor(proveedor)
                .build();
        
        when(productoRepository.save(any(Producto.class))).thenReturn(productoGuardado);

        // When
        Producto result = productoService.guardar(productoNuevo);

        // Then
        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("Cien Años de Soledad", result.getNombre());
        assertEquals(new BigDecimal("24.99"), result.getPrecio());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void eliminar() {
        // Given
        Long idToDelete = 1L;
        doNothing().when(productoRepository).deleteById(idToDelete);

        // When
        productoService.eliminar(idToDelete);

        // Then
        verify(productoRepository, times(1)).deleteById(idToDelete);
    }
}
