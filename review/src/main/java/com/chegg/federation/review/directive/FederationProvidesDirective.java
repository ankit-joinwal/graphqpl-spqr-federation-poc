package com.chegg.federation.review.directive;

import io.leangen.graphql.annotations.types.GraphQLDirective;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@GraphQLDirective(name = "provides")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface FederationProvidesDirective {
    String fields();
}
