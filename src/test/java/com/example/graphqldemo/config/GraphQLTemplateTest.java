package com.example.graphqldemo.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class GraphQLTemplateTest {

    private final HttpHeaders headers = new HttpHeaders();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private TestRestTemplate restTemplate;

    private String graphqlMapping = "/graphql";

    public GraphQLTemplateTest() {

    }

    private String createJsonQuery(String graphql, ObjectNode variables) throws JsonProcessingException {
        ObjectNode wrapper = this.objectMapper.createObjectNode();
        wrapper.put("query", graphql);
        wrapper.put("variables", variables);
        return this.objectMapper.writeValueAsString(wrapper);
    }

    private String loadQuery(String location) throws IOException {
        Resource resource = this.resourceLoader.getResource("classpath:" + location);
        return this.loadResource(resource);
    }

    private String loadResource(Resource resource) throws IOException {
        try (InputStream inputStream = resource.getInputStream()) {
            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        }
    }

    public GraphQLTemplateTest withBearerAuth(@NonNull final String token) {
        headers.setBearerAuth(token);
        return this;
    }

    public GraphQLTemplateTest withAdditionalHeader(final String name, final String... value) {
        headers.addAll(name, Arrays.asList(value));
        return this;
    }

    public GraphQLResponse perform(String graphqlResource, ObjectNode variables) throws IOException {
        String graphql = this.loadQuery(graphqlResource);
        String payload = this.createJsonQuery(graphql, variables);
        return this.post(payload);
    }

    public GraphQLResponse postForResource(String graphqlResource) throws IOException {
        return this.perform(graphqlResource, null);
    }

    public GraphQLResponse post(String payload) {
        HttpEntity<Object> request = forJson(payload, headers);
        return postRequest(request);
    }

    private GraphQLResponse postRequest(HttpEntity<Object> request) {
        ResponseEntity<String> response = restTemplate.exchange(graphqlMapping, HttpMethod.POST, request, String.class);
        return new GraphQLResponse(response);
    }

    private HttpEntity<Object> forJson(String json, HttpHeaders headers) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(json, headers);
    }
}
