package com.example.graphqldemo;

import com.example.graphqldemo.config.GraphQLTemplateTest;
import com.example.graphqldemo.dto.Component1;
import com.example.graphqldemo.dto.Component2;
import com.example.graphqldemo.dto.Component3;
import com.example.graphqldemo.dto.User;
import com.example.graphqldemo.filter.UserFilter;
import com.example.graphqldemo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graphql.spring.boot.test.GraphQLResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = GraphQlDemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(GraphQlDemoApplicationTests.ContextGraphQLConfig.class)
@ExtendWith(SpringExtension.class)
@DirtiesContext
class GraphQlDemoApplicationTests {

    @Autowired
    private GraphQLTemplateTest graphQLTemplateTest;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @Test
    void searchUserQuery() throws Exception {
        List<User> userList = new ArrayList<>();
        Component1 component1 = new Component1("component1Field1_value", "component1Field2_value");
        Component2 component2 = new Component2("component2Field1_value", "component2Field2_value");
        Component3 component3 = new Component3(156L, LocalDate.now());
        User user = new User("username1", "password1");
        user.setComponent1(component1);
        user.setComponent2(component2);
        user.setComponent3(component3);
        userList.add(user);

        when(userService.searchUsers(any(UserFilter.class))).thenReturn(userList);
        String request = "samples/graphql/query.graphql";
        GraphQLResponse graphQLResponse = executeRequest(request);
        assertThat(graphQLResponse.isOk()).isTrue();
    }

    private GraphQLResponse executeRequest(String body) throws Exception {
        return graphQLTemplateTest.withBearerAuth("too").postForResource(body);
    }

    @TestConfiguration
    static class ContextGraphQLConfig {
        @Bean
        public GraphQLTemplateTest graphQLTemplateTest() {
            return new GraphQLTemplateTest();
        }
    }

}
