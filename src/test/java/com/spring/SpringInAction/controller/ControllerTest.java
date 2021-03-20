package com.spring.SpringInAction.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.SpringInAction.study06.dto.MonoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        objectMapper = new ObjectMapper();
    }

    @DisplayName("Spring Properties")
    @Test
    public void orderForUserTest() throws Exception {
        String requestURL = "http://localhost:" + port + "/orders/orderForUser";
        String expect = "10";
        String result = testRestTemplate.getForObject(requestURL, String.class);
        assertEquals(expect, result);
    }

    @DisplayName("Mono Test")
    @Test
    public void monoTest() throws JsonProcessingException {
        String requestURL = "http://localhost:" + port + "/webflux/mono";
        String expect = objectMapper.writeValueAsString(new MonoDTO("data1", "data2"));
        String result = testRestTemplate.getForObject(requestURL, String.class);
        assertEquals(expect, result);
    }

    @DisplayName("Mono Parameter Test")
    @Test
    public void monoParameterTest() throws JsonProcessingException {
        String requestURL = "http://localhost:" + port + "/webflux/mono";
        String expect = objectMapper.writeValueAsString(new MonoDTO("requestData1", "requestData2"));
        String result = testRestTemplate.postForObject(requestURL, new MonoDTO("requestData1", "requestData2"), String.class);
        assertEquals(expect, result);
    }

    @DisplayName("Functional Router Simple Test")
    @Test
    public void functionalRouterSimpleTest() {
        String requestURL = "http://localhost:" + port + "/functional/main";
        String expect = "Hello Spring";
        String result = testRestTemplate.getForObject(requestURL, String.class);
        assertEquals(expect, result);
    }

    @DisplayName("Functinal Router Chaining Test")
    @Test
    public void functionalRouterChainingTest() {
        String requestURL = "http://localhost:" + port + "/functional/chain1";
        String expect = "chain1";
        String result = testRestTemplate.getForObject(requestURL, String.class);
        assertEquals(expect, result);
        requestURL = "http://localhost:" + port + "/functional/chain2";
        expect = "chain2";
        result = testRestTemplate.getForObject(requestURL, String.class);
        assertEquals(expect, result);
    }
}
