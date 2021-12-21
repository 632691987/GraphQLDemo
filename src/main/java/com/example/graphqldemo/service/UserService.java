package com.example.graphqldemo.service;

import com.example.graphqldemo.dto.Component1;
import com.example.graphqldemo.dto.Component2;
import com.example.graphqldemo.dto.Component3;
import com.example.graphqldemo.dto.User;
import com.example.graphqldemo.filter.UserFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final List<User> userList = new ArrayList<>();

    static {
        Component1 component1 = new Component1("component1Field1_value", "component1Field2_value");
        Component2 component2 = new Component2("component2Field1_value", "component2Field2_value");
        Component3 component3 = new Component3(156L, LocalDate.now());
        User user = new User("username1", "password1");
        user.setComponent1(component1);
        user.setComponent2(component2);
        user.setComponent3(component3);
        userList.add(user);
    }

    public List<User> searchUsers(UserFilter userFilter) {
        return userList.stream()
                .filter(predicateUsername(userFilter.getUsername()))
                .filter(predicatePassword(userFilter.getPassword()))
                .collect(Collectors.toList());
    }

    private Predicate<User> predicateUsername(String filteredUsername) {
        return user -> {
            if (StringUtils.isNotBlank(filteredUsername)) {
                return StringUtils.equals(user.getUsername(), filteredUsername);
            } else {
                return true;
            }
        };
    }

    private Predicate<User> predicatePassword(String filteredPassword) {
        return user -> {
            if (StringUtils.isNotBlank(filteredPassword)) {
                return StringUtils.equals(user.getPassword(), filteredPassword);
            } else {
                return true;
            }
        };
    }
}
