package com.example.graphqldemo.resolver;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.example.graphqldemo.dto.User;
import com.example.graphqldemo.filter.UserFilter;
import com.example.graphqldemo.service.UserService;

import java.util.List;

public class UserSearchResolver implements GraphQLQueryResolver {

    private final UserService userService;

    public UserSearchResolver(final UserService userService) {
        this.userService = userService;
    }

    public List<User> searchUsers(UserFilter userFilter) {
        return userService.searchUsers(userFilter);
    }
}
