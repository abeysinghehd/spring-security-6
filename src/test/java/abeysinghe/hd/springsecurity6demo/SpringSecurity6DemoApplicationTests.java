package abeysinghe.hd.springsecurity6demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringSecurity6DemoApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void givenMemUsers_whenGetPingWithValidUser_thenOk() {
		ResponseEntity<String> result
				= makeRestCallToGetPing("memuser", "pass");

		assertThat(result.getStatusCode().value()).isEqualTo(200);
		assertThat(result.getBody()).isEqualTo("OK");
	}

	@Test
	public void givenExternalUsers_whenGetPingWithValidUser_thenOK() {
		ResponseEntity<String> result
				= makeRestCallToGetPing("user", "pass");

		assertThat(result.getStatusCode().value()).isEqualTo(200);
		assertThat(result.getBody()).isEqualTo("OK");
	}

	@Test
	public void givenAuthProviders_whenGetPingWithNoCred_then401() {
		ResponseEntity<String> result = makeRestCallToGetPing();
		assertThat(result.getStatusCode().value()).isEqualTo(401);
	}

	@Test
	public void givenAuthProviders_whenGetPingWithBadCred_then401() {
		ResponseEntity<String> result
				= makeRestCallToGetPing("user", "bad_password");

		assertThat(result.getStatusCode().value()).isEqualTo(401);
	}

	private ResponseEntity<String>
	makeRestCallToGetPing(String username, String password) {
		return restTemplate.withBasicAuth(username, password)
				.getForEntity("/api/ping", String.class, Collections.emptyMap());
	}

	private ResponseEntity<String> makeRestCallToGetPing() {
		return restTemplate
				.getForEntity("/api/ping", String.class, Collections.emptyMap());
	}

}
