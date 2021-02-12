package com.chegg.federation.product.query;

import com.chegg.federation.product.model.Product;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ProductQuery {


    @GraphQLQuery(name = "getProductsInStock")
    public Set<Product> getProductsInStock(){
        Set<Product> mockResult = new HashSet<>();
        Product product1 = new Product("1","Table", 899);
        mockResult.add(product1);
        Product product2 = new Product("2","Couch", 500);
        mockResult.add(product2);
        return mockResult;
    }


}
