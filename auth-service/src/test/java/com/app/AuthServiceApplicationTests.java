package com.app;

import com.app.dto.AuthResponseDto;
import com.app.dto.SignupDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class AuthServiceApplicationTests extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();

    }

    @BeforeEach
    public void setUp() {
        baseUrl = baseUrl.concat(":").concat(port + "").concat("/api/v1/auth");
    }


    @Test
    @DisplayName("Test API to register user")
    void testAuthRegister() {
        SignupDto signupDto = SignupDto.builder()
                .firstName("Gabin")
                .lastName("Ishimwe")
                .email("g.ishimwe@alustudent.com")
                .password("#Password123")
                .phoneNumber("+250787857036")
                .build();

        AuthResponseDto responseDto = restTemplate.postForObject(baseUrl + "/sign-up", signupDto, AuthResponseDto.class);

        assert responseDto != null;
        Assertions.assertEquals("Verify email", responseDto.getMessage());
    }
}
