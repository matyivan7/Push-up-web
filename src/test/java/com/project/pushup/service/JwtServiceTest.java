package com.project.pushup.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_SECRET = "testsecretkeytestsecretkeytestsecretkeytestsecretkeytestsecretkeytestsecretkey";
    private static final int TEST_EXPIRATION_MS = 3600000; // 1 hour

    @BeforeEach
    void setUp() {
        // Set the private fields using ReflectionTestUtils
        ReflectionTestUtils.setField(jwtService, "jwtSecret", TEST_SECRET);
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", TEST_EXPIRATION_MS);
    }

    @Test
    void should_generate_valid_token() {
        // Act
        String token = jwtService.generateToken(TEST_USERNAME);

        // Assert
        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertTrue(jwtService.validateToken(token));
    }

    @Test
    void should_extract_username_from_token() {
        // Arrange
        String token = jwtService.generateToken(TEST_USERNAME);

        // Act
        String extractedUsername = jwtService.getUsernameFromJWT(token);

        // Assert
        assertEquals(TEST_USERNAME, extractedUsername);
    }

    @Test
    void should_validate_token_with_correct_issuer_and_audience() {
        // Arrange
        String token = jwtService.generateToken(TEST_USERNAME);

        // Act & Assert
        assertTrue(jwtService.validateToken(token));
    }

    @Test
    void should_reject_expired_token() {
        // Arrange
        // Set a very short expiration time
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", 1); // 1 millisecond
        String token = jwtService.generateToken(TEST_USERNAME);

        // Wait for token to expire
        try {
            Thread.sleep(10); // Wait 10 milliseconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Act & Assert
        assertFalse(jwtService.validateToken(token));

        // Reset expiration time
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", TEST_EXPIRATION_MS);
    }

    @Test
    void should_reject_malformed_token() {
        // Arrange
        String malformedToken = "malformed.token.string";

        // Act & Assert
        assertFalse(jwtService.validateToken(malformedToken));
    }

    @Test
    void should_reject_token_with_invalid_signature() {
        // Arrange
        String token = jwtService.generateToken(TEST_USERNAME);
        
        // Tamper with the token by changing the last character
        String tamperedToken = token.substring(0, token.length() - 1) + (token.charAt(token.length() - 1) == 'A' ? 'B' : 'A');

        // Act & Assert
        assertFalse(jwtService.validateToken(tamperedToken));
    }

    @Test
    void should_set_correct_expiration_time() {
        // Arrange
        String token = jwtService.generateToken(TEST_USERNAME);
        
        // Parse the token to get the claims
        Claims claims = Jwts.parser()
                .setSigningKey(TEST_SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        Date issuedAt = claims.getIssuedAt();
        Date expiration = claims.getExpiration();
        
        // Act
        long durationMs = expiration.getTime() - issuedAt.getTime();
        
        // Assert
        assertEquals(TEST_EXPIRATION_MS, durationMs, 100); // Allow small tolerance for test execution time
    }
}