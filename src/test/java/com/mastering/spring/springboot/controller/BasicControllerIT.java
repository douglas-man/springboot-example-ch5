package com.mastering.spring.springboot.controller;

import com.mastering.spring.springboot.FirstSpringBootExampleApplication;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FirstSpringBootExampleApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BasicControllerIT {

    private static final String LOCAL_HOST = "http://localhost:";

    @LocalServerPort
    private int port;

    private TestRestTemplate template = new TestRestTemplate();


    @Test
    public void welcome() {
        ResponseEntity<String> response = template
                .getForEntity(createURL("/welcome"), String.class);
        assertThat(response.getBody(), equalTo("Hello World"));
    }


    @Test
    public void welcomeWithObject() {
        ResponseEntity<String> response =
                template.getForEntity(createURL("/welcome-with-object"),
                        String.class);
        assertThat(response.getBody(),
                containsString("Hello World"));
    }

    @Test
    public void welcomeWithParameter() {
        ResponseEntity<String> response =
                template.getForEntity(
                        createURL("/welcome-with-parameter/name/Buddy"), String.class);
        assertThat(response.getBody(),
                containsString("Hello World, Buddy"));
    }

    private String createURL(String uri) {
        return LOCAL_HOST + port + uri;
    }
}


