package com.axell.ecommerce.checkout.steps;

import com.axell.ecommerce.checkout.SpringIntegrationTest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class StepDefinitions extends SpringIntegrationTest {

    @Autowired
    protected TestRestTemplate template;
    private String responseBody;

    private HttpHeaders responseHeaders;

    private HttpStatus statusCode;

    private static String getBody(ResponseEntity response) {
        final StringBuilder body = new StringBuilder();
        if (response.getBody() != null) {
            body.append(response.getBody().toString());
        }
        return body.toString();
    }

    @Then("the client receives status code of {int}")
    public void theClientReceivesStatusCodeOf(int expectedStatusCode) {
        Assert.assertEquals(expectedStatusCode, statusCode.value());
    }

    @When("the client calls {string}")
    public void theClientCalls(String path) throws IOException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            final String body = """
                    {
                    \t"firstName": "",
                    \t"lastName": "",
                    \t"username": "",
                    \t"email": "",
                    \t"address": "",
                    \t"complement": "",
                    \t"country": "",
                    \t"state": "",
                    \t"cep": "",
                    \t"saveAddress": "",
                    \t"saveInfo": "",
                    \t"paymentMethod": "",
                    \t"cardName": "",
                    \t"cardNumber": "",
                    \t"cardDate": "dasd",
                    \t"cardCvv": "sadasd",
                    \t"products": [
                    \t\t"produtoA",
                    \t\t"produtoB"
                    \t]
                    }""";

            HttpEntity<String> entity = new HttpEntity<>(body, headers);
            final ResponseEntity<?> response = template.postForEntity(
                    path,
                    entity,
                    String.class);
            responseBody = getBody(response);
            responseHeaders = response.getHeaders();
            statusCode = response.getStatusCode();
        } catch (HttpStatusCodeException ex) {
            statusCode = ex.getStatusCode();
            assertEquals("", ex.getResponseBodyAsString());
        }
    }

    @And("response is {string}")
    public void responseIs(String expectedResponseBody) {
        assertEquals(expectedResponseBody, responseBody);
    }
}
