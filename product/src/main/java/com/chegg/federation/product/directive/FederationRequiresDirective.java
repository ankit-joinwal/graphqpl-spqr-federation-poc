package com.chegg.federation.product.directive;

import io.leangen.graphql.annotations.types.GraphQLDirective;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@GraphQLDirective(name = "requires")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface FederationRequiresDirective {
    String fields();
}
