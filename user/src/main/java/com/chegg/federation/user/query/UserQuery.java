package com.chegg.federation.user.query;

import com.chegg.federation.user.model.User;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserQuery {

    @Autowired
    private UserService userService;

    @GraphQLQuery(name = "getUserById")
    public User getUser(@GraphQLArgument(name = "id") String id){
        return userService.lookupUser(id);
    }
}
