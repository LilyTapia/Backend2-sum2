package com.letrasypapeles.backend.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginResponseTest {

    @Test
    void testLoginResponseConstructor() {
        // Probar el constructor con parámetro
        LoginResponse loginResponse = new LoginResponse("Bienvenido");

        // Verificar que el valor del token se ha asignado correctamente
        assertEquals("Bienvenido", loginResponse.getToken());
    }

    @Test
    void testLoginResponseDefaultConstructor() {
        // Probar el constructor vacío
        LoginResponse loginResponse = new LoginResponse();

        // Verificar que el valor por defecto es null (si no se asigna token)
        assertNull(loginResponse.getToken());
    }

    @Test
    void testLoginResponseSetter() {
        // Crear el objeto LoginResponse
        LoginResponse loginResponse = new LoginResponse("Bienvenido");

        // Cambiar el valor del token con el setter
        loginResponse.setToken("newToken");

        // Verificar que el setter ha cambiado el valor
        assertEquals("newToken", loginResponse.getToken());
    }
}
