package com.axell.ecommerce.checkout;

import com.axell.ecommerce.checkout.setup.PostgreSQLSetup;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@CucumberContextConfiguration
@SpringBootTest(classes = {
        CheckoutApplication.class,
        PostgreSQLSetup.class
}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 8888)
public class SpringIntegrationTest {
}
