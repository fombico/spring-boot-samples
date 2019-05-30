package com.fombico.testcontainers.samples;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiControllerIntegrationTest {

    static {
        GenericContainer redis = new GenericContainer("redis:5.0.5-alpine")
                .withExposedPorts(6379);
        redis.start();

        System.setProperty("spring.redis.host", redis.getContainerIpAddress());
        System.setProperty("spring.redis.port", redis.getFirstMappedPort() + "");

        log.info("Redis config: =======> {}:{}", redis.getContainerIpAddress(), redis.getFirstMappedPort());
    }

    private static final String LOCALHOST = "http://localhost:";

    @LocalServerPort
    private int port;

    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void redis_string_integration() {
        String url = LOCALHOST + port + "/foo";
        log.info("API URL: =======> {}", url);

        restTemplate.put(url, "hello");
        String response = restTemplate.getForObject(url, String.class);

        assertThat(response).isEqualTo("hello");
    }

    @Test
    public void redis_repo_integration() throws Exception {
        String url = LOCALHOST + port + "/bar";
        log.info("API URL: =======> {}", url);

        Book book = Book.builder().title("Hamlet").author("William Shakespeare").build();

        restTemplate.postForLocation(url, book);

        log.info("Posted book");

        List<Book> books = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Book>>(){}).getBody();

        log.info("Retrieved books");

        assertThat(books.get(0)).isEqualToIgnoringGivenFields(book, "id");
    }
}
