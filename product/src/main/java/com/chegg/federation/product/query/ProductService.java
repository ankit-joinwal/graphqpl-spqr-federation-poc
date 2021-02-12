package com.chegg.federation.product.query;

import com.chegg.federation.product.model.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    public Product find(String upc){
        Product product1 = new Product("1","Table", 899);
        return product1;
    }
}
