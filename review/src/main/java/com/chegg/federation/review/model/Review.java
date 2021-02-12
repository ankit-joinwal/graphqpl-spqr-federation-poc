package com.chegg.federation.review.model;

import com.chegg.federation.review.directive.FederationKeyDirective;
import com.chegg.federation.review.directive.FederationProvidesDirective;

@FederationKeyDirective(fields = "id")
public class Review {

    private String id;
    private String body;
    @FederationProvidesDirective(fields = "username")
    private User author ;
    private Product product;

    public Review(final String id, final String body, final User author, final Product product) {
        this.id = id;
        this.body = body;
        this.author = author;
        this.product = product;
    }

    public Review(final String id, final String body) {
        this.id = id;
        this.body = body;
    }

    public Review() {
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(final User author) {
        this.author = author;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(final Product product) {
        this.product = product;
    }
}