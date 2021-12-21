package com.example.graphqldemo.configuration;

import com.coxautodev.graphql.tools.SchemaParser;
import com.coxautodev.graphql.tools.SchemaParserBuilder;
import com.example.graphqldemo.resolver.UserSearchResolver;
import com.example.graphqldemo.service.UserService;
import com.google.common.io.Resources;
import graphql.Scalars;
import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLSchema;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class GraphQLConfiguration {

    @Bean
    public GraphQLSchema schema(@Value("classpath:graphql/schema.graphqls") final Resource schema,
                                @Value("classpath:graphql/{filename:[a-zA-Z0-9\\-]+-model}.graphqls") final Resource[] modelResources,
                                UserService userService) throws IOException {
        SchemaParserBuilder schemaParserBuilder = SchemaParser.newParser()
                .schemaString(Resources.toString(schema.getURL(), StandardCharsets.UTF_8))
                .resolvers(new UserSearchResolver(userService))
                .scalars(Scalars.GraphQLLong, ExtendedScalars.Date, ExtendedScalars.Object);

        for (Resource resource : modelResources) {
            schemaParserBuilder.schemaString(Resources.toString(resource.getURL(), StandardCharsets.UTF_8));
        }

        return schemaParserBuilder.build().makeExecutableSchema();
    }

}
