package com.letrasypapeles.backend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doReturn;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret",
                "mySecretKeyForTestingPurposesOnly123456789012345678901234567890");
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", 86400000);
    }

    @Test
    void testGenerateTokenFromUsername() {
        String username = "testuser";
        String token = jwtUtil.generateTokenFromUsername(username);

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void testGenerateJwtToken() {
        userDetails = mock(UserDetails.class);
        authentication = mock(Authentication.class);

        // Configurar el mock para que devuelva valores
        doReturn("testuser").when(userDetails).getUsername();
        doReturn(userDetails).when(authentication).getPrincipal();

        String token = jwtUtil.generateJwtToken(authentication);

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void testGetUsernameFromToken() {
        String username = "testuser";
        String token = jwtUtil.generateTokenFromUsername(username);

        String extractedUsername = jwtUtil.getUsernameFromToken(token);

        assertEquals(username, extractedUsername);
    }

    @Test
    void testGetExpirationDateFromToken() {
        String username = "testuser";
        String token = jwtUtil.generateTokenFromUsername(username);

        Date expiration = jwtUtil.getExpirationDateFromToken(token);

        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void testValidateJwtToken_ValidToken() {
        String username = "testuser";
        String token = jwtUtil.generateTokenFromUsername(username);

        boolean isValid = jwtUtil.validateJwtToken(token);

        assertTrue(isValid);
    }

    @Test
    void testValidateJwtToken_InvalidToken() {
        String invalidToken = "invalid.token.here";

        boolean isValid = jwtUtil.validateJwtToken(invalidToken);

        assertFalse(isValid);
    }

    @Test
    void testValidateJwtToken_ExpiredToken() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret",
                "mySecretKeyForTestingPurposesOnly123456789012345678901234567890");
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", -1000); // Token expired

        String username = "testuser";
        String expiredToken = jwtUtil.generateTokenFromUsername(username);

        boolean isValid = jwtUtil.validateJwtToken(expiredToken);

        assertFalse(isValid);
    }

    @Test
    void testValidateJwtToken_EmptyToken() {
        boolean isValid = jwtUtil.validateJwtToken("");

        assertFalse(isValid);
    }

    @Test
    void testValidateJwtToken_NullToken() {
        boolean isValid = jwtUtil.validateJwtToken(null);

        assertFalse(isValid);
    }

    @Test
    void testValidateJwtToken_MalformedToken() {
        String malformedToken = "malformed.jwt";

        boolean isValid = jwtUtil.validateJwtToken(malformedToken);

        assertFalse(isValid);
    }

    @Test
    void testValidateJwtToken_UnsupportedToken() {
        String unsupportedToken = "eyJhbGciOiJub25lIn0.eyJzdWIiOiJ0ZXN0In0.";

        boolean isValid = jwtUtil.validateJwtToken(unsupportedToken);

        assertFalse(isValid);
    }

    @Test
    void testValidateJwtToken_SecurityException() {
        String tokenWithWrongSignature = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0IiwiZXhwIjo5OTk5OTk5OTk5fQ.wrongsignature";

        boolean isValid = jwtUtil.validateJwtToken(tokenWithWrongSignature);

        assertFalse(isValid);
    }

    @Test
    void testGetClaimFromToken() {
        String username = "testuser";
        String token = jwtUtil.generateTokenFromUsername(username);

        String subject = jwtUtil.getClaimFromToken(token, claims -> claims.getSubject());

        assertEquals(username, subject);
    }

    @Test
    void testValidateJwtToken_ExpiredJwtException() {
        // Crear un token que expire inmediatamente
        JwtUtil expiredJwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(expiredJwtUtil, "jwtSecret",
                "mySecretKeyForTestingPurposesOnly123456789012345678901234567890");
        ReflectionTestUtils.setField(expiredJwtUtil, "jwtExpirationMs", 1); // 1ms para que expire rápido

        String token = expiredJwtUtil.generateTokenFromUsername("testuser");

        // Esperar un poco para que expire
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        boolean isValid = jwtUtil.validateJwtToken(token);
        assertFalse(isValid);
    }

    @Test
    void testValidateJwtToken_IllegalArgumentException() {
        // Token con claims vacías que puede causar IllegalArgumentException
        String emptyClaimsToken = "";

        boolean isValid = jwtUtil.validateJwtToken(emptyClaimsToken);

        assertFalse(isValid);
    }
}