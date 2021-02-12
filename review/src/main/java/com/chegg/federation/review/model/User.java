package com.chegg.federation.review.model;

import com.chegg.federation.review.directive.FederationExtendsDirective;
import com.chegg.federation.review.directive.FederationExternalDirective;
import com.chegg.federation.review.directive.FederationKeyDirective;

import java.util.List;

@FederationKeyDirective(fields = "id")
@FederationExtendsDirective
public class User {

    @FederationExternalDirective
    private final String id;

    @FederationExternalDirective
    private String username;
    private List<Review> reviews;

    public User(final String id) {
        this.id = id;
    }

    public User(final String id, final String username) {
        this.id = id;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(final List<Review> reviews) {
        this.reviews = reviews;
    }
}