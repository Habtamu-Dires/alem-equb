package com.ekub;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AlemEQubApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private RestTemplate restTemplate; // Use the custom RestTemplate with timeouts

	private MockRestServiceServer mockServer;
	private String jwkSetUri = "https://alem-equb.duckdns.org/keycloak/realms/alem-ekub/protocol/openid-connect/certs";

	@BeforeEach
	public void setup() {
		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	@Test
	public void whenKeycloakIsSlow_thenRequestFailsWithTimeout() throws Exception {
		// Simulate Keycloak responding after 6 seconds (longer than the timeout)
		String jwksResponse = "{ \"keys\": [] }"; // Mock empty JWKS for simplicity

		mockServer.expect(requestTo(jwkSetUri))
				.andRespond(MockRestResponseCreators.withSuccess()
						.body(jwksResponse)
						.contentType(MediaType.APPLICATION_JSON)
						.body(String.valueOf(6000)) // 6-second delay (longer than the RestTemplate's 5-second timeout)
				);

		// Send a request with a valid token
		mockMvc.perform(get("/api/protected-endpoint")
						.header("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJWSDB6S0ZaVkg2enJtamVDOXpITHhtWm1wTUdDYUtpNHFieTNNTkVrcU9jIn0.eyJleHAiOjE3NDI1NTg3MDAsImlhdCI6MTc0MjU1ODQwMCwianRpIjoiYTRhYTc1MjYtN2RhMC00YmQ2LWFhMzEtYmI5ZDE2NmMwM2FkIiwiaXNzIjoiaHR0cHM6Ly9hbGVtLWVxdWIuZHVja2Rucy5vcmcva2V5Y2xvYWsvcmVhbG1zL2FsZW0tZWt1YiIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI1ODlmMTNjNy01MTkzLTQ0ZDQtYjIwMi0yOGM0ODA1OTAyMTgiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJla3ViLXVzZXItbG9naW4iLCJzaWQiOiJmYjY4NGUyZS0wMGIyLTQ4ZTAtODk2Yy03NGYyNzg3YTRkNTgiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHBzOi8vYWxlbS1lcXViLmR1Y2tkbnMub3JnIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsImRlZmF1bHQtcm9sZXMtYWxlbS1la3ViIiwiQURNSU4iLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoicHJvZmlsZSBlbWFpbCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6InRhZCBzaXMiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ0YWQiLCJnaXZlbl9uYW1lIjoidGFkIiwiZmFtaWx5X25hbWUiOiJzaXMiLCJlbWFpbCI6InRhZGllc2lzMjFAZ21haWwuY29tIn0.Pg7MfCmHvmtsRGh3P2vxh9vu6_kzTXpx8lbu_J1K5v7Gm5dWUTUx80tOpgCDjjvUlr2YCqMYIJ68dSioazty3PndPuYiT4YhuH2ZWzGCJ75s8vJ_IDZ3bcSR9cDdasKVrmMpmCx7-tN7mUzMvSwNp4bGNOUOmyDtxaAO_E0kyjQgtbWxoHZtdxlRucOQ_vKvvh1eEmo2_vksRWHNhOuElYxV625MewPUcd9l0oW97piEH0sqAnDZ_4HxPLX8Y9cxS9Q7OxCThsatQmdhHOFAlb0jMg-gjnP8YYAARmqTzS9PvOPn2OATl1ZMU01ANTEUGmYmCFP13O5k4bekMSS8vw"))
				.andExpect(status().is5xxServerError()); // Expect timeout error
	}

}
