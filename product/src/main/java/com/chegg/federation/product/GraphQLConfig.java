package com.chegg.federation.product;

import com.apollographql.federation.graphqljava.Federation;
import com.apollographql.federation.graphqljava._Entity;
import com.chegg.federation.product.model.Product;
import com.chegg.federation.product.query.ProductQuery;
import com.chegg.federation.product.query.ProductService;
import graphql.introspection.Introspection;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLDirective;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaPrinter;
import io.leangen.graphql.GraphQLSchemaGenerator;
import io.leangen.graphql.metadata.Directive;
import io.leangen.graphql.metadata.strategy.query.DirectiveBuilder;
import io.leangen.graphql.metadata.strategy.query.DirectiveBuilderParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.AnnotatedType;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class GraphQLConfig {

    @Autowired
    ProductQuery productQuery;

    @Autowired
    ProductService productService;
    private static final String MAPPED_TYPE = "_mappedType";
    private static final String TYPE = "type";

    @Bean
    public GraphQLSchema customSchema() {

        GraphQLSchema schema = new GraphQLSchemaGenerator()
                .withBasePackages("com.chegg.federation.poc.product")
                .withOperationsFromSingletons(productQuery)
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


    private GraphQLSchema createSchemaWithDirectives(GraphQLSchema schema) {


        return Federation.transform(schema).fetchEntities(env -> env.<List<Map<String, Object>>>getArgument(_Entity.argumentName)
                .stream()
                .map(values -> {
                    if ("Product".equals(values.get("__typename"))) {
                        final Object upc = values.get("upc");
                        if (upc instanceof String) {
                            return productService.find((String) upc);
                        }
                    }
                    return null;
                })
                .collect(Collectors.toList()))
                .resolveEntityType(env -> {
                    final Object src = env.getObject();
                    if (src instanceof Product) {
                        return env.getSchema().getObjectType("Product");
                    }
                    return null;
                }).build();
    }

    private void printSchema(GraphQLSchema schema) {
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
