package com.letrasypapeles.backend.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class JwtResponseTest {

    @Test
    void testJwtResponse() {
        // Crear lista de roles
        List<String> roles = new ArrayList<>();
        roles.add("ROL_ADMIN");

        // Crear el objeto JwtResponse
        JwtResponse jwt = new JwtResponse("token123", "usuario", roles);

        // Verificar que los valores se han asignado correctamente
        assertEquals("token123", jwt.getToken());
        assertEquals("usuario", jwt.getEmail());  // Usamos getEmail() en lugar de getUsername()
        assertEquals("ROL_ADMIN", jwt.getRoles().get(0)); // Accede al primer rol
    }

    @Test
    void testJwtResponseSetters() {
        List<String> roles = new ArrayList<>();
        roles.add("ROL_ADMIN");

        JwtResponse jwt = new JwtResponse("token123", "usuario", roles);

        // Cambiar valores con setters
        jwt.setToken("newToken");
        jwt.setEmail("newUser");  // Usamos setEmail() en lugar de setUsername()
        jwt.setRoles(new ArrayList<>(List.of("NEW_ROLE")));  // Cambiamos setRol() por setRoles()

        // Verificar que los valores se actualizan correctamente
        assertEquals("newToken", jwt.getToken());
        assertEquals("newUser", jwt.getEmail());  // Usamos getEmail() en lugar de getUsername()
        assertEquals("NEW_ROLE", jwt.getRoles().get(0));  // Accede al primer rol
    }
}
