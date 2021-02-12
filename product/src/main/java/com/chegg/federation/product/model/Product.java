package com.chegg.federation.product.model;

import com.chegg.federation.product.directive.FederationKeyDirective;
import io.leangen.graphql.annotations.GraphQLQuery;

@FederationKeyDirective(fields = "upc")
public class Product {
    private String upc;
    private String name;
    private Integer price;

    public Product() {
    }

    public Product(String upc, String name, Integer price) {
        this.upc = upc;
        this.name = name;
        this.price = price;
    }

    @GraphQLQuery(name = "upc")
    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    @GraphQLQuery(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @GraphQLQuery(name = "price")
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return upc != null ? upc.equals(product.upc) : product.upc == null;

    }

    @Override
    public int hashCode() {
        return upc != null ? upc.hashCode() : 0;
    }
}
