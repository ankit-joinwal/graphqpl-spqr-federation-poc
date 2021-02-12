package com.chegg.federation.user.directive;

import io.leangen.graphql.annotations.types.GraphQLDirective;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@GraphQLDirective(name = "extends")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface FederationExtendsDirective {
}
