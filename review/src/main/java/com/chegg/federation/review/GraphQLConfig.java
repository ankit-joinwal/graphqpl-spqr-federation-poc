package com.chegg.federation.review;

import com.apollographql.federation.graphqljava.Federation;
import com.apollographql.federation.graphqljava._Entity;
import com.chegg.federation.review.model.Product;
import com.chegg.federation.review.model.User;
import com.chegg.federation.review.query.ProductService;
import com.chegg.federation.review.query.ReviewQuery;
import com.chegg.federation.review.query.UserService;
import graphql.introspection.Introspection;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLDirective;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaPrinter;
import io.leangen.graphql.GraphQLSchemaGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class GraphQLConfig {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ReviewQuery reviewQuery;

    @Bean
    public GraphQLSchema customSchema() {

        GraphQLSchema schema = new GraphQLSchemaGenerator()
                .withBasePackages("com.chegg.federation.poc.review")
               .withOperationsFromSingletons(reviewQuery)
                .generate();

        // UNREPRESENTABLE scalar
        GraphQLScalarType unrepresentableScalar = (GraphQLScalarType) schema.getType("UNREPRESENTABLE");

        // _mappedType directive definition
        GraphQLDirective mappedTypeDirective = GraphQLDirective.newDirective()
                .name("_mappedType")
                .description("")
                .validLocation(Introspection.DirectiveLocation.OBJECT)
                .argument(GraphQLArgument.newArgument()
                        .name("type")
                        .description("")
                        .type(unrepresentableScalar)
                        .build()
                )
                .build();

        // _mappedOperation directive definition
        GraphQLDirective mappedOperationDirective = GraphQLDirective.newDirective()
                .name("_mappedOperation")
                .description("")
                .validLocation(Introspection.DirectiveLocation.FIELD_DEFINITION)
                .argument(GraphQLArgument.newArgument()
                        .name("operation")
                        .description("")
                        .type(unrepresentableScalar)
                        .build()
                )
                .build();

        // _mappedInputField directive definition
        GraphQLDirective mappedInputFieldDirective = GraphQLDirective.newDirective()
                .name("_mappedInputField")
                .description("")
                .validLocation(Introspection.DirectiveLocation.INPUT_FIELD_DEFINITION)
                .argument(GraphQLArgument.newArgument()
                        .name("inputField")
                        .description("")
                        .type(unrepresentableScalar)
                        .build()
                )
                .build();

        // Add new definitions to schema
        GraphQLSchema newSchema = GraphQLSchema.newSchema(schema)
                .additionalDirective(mappedTypeDirective)
                .additionalDirective(mappedOperationDirective)
                .additionalDirective(mappedInputFieldDirective)
                .build();

        GraphQLSchema federatedSchema = createSchemaWithDirectives(newSchema);
        printSchema(federatedSchema);
        return federatedSchema;
    }


    private GraphQLSchema createSchemaWithDirectives(GraphQLSchema schema){
        return Federation.transform(schema)
                .fetchEntities(env -> env.<List<Map<String, Object>>>getArgument(_Entity.argumentName)
                        .stream()
                        .map(values -> {
                            if ("User".equals(values.get("__typename"))) {
                                final Object id = values.get("id");
                                if (id instanceof String) {
                                    return userService.lookupUser((String) id);
                                }
                            }
                            if ("Product".equals(values.get("__typename"))) {
                                final Object upc = values.get("upc");
                                if (upc instanceof String) {
                                    return productService.lookupProduct((String) upc);
                                }
                            }
                            return null;
                        })
                        .collect(Collectors.toList())
                )
                .resolveEntityType(env -> {
                    final Object src = env.getObject();
                    if (src instanceof User) {
                        return env.getSchema().getObjectType("User");
                    }
                    if (src instanceof Product) {
                        return env.getSchema().getObjectType("Product");
                    }
                    return null;
                })
                .build();
    }

    private void printSchema(GraphQLSchema schema){
        System.out.println("Schema With Federation >>>");
        String printedSchema = new SchemaPrinter(
                // Tweak the options accordingly
                SchemaPrinter.Options.defaultOptions().
                        includeDirectives(true)


        ).print(schema);
        System.out.println(printedSchema);
        System.out.println(" >>>>>>>>>>>    ");

    }
}
