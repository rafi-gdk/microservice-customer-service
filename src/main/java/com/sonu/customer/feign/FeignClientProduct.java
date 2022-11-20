package com.sonu.customer.feign;

import com.sonu.customer.model.Product;
import com.sonu.customer.model.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

//@FeignClient(value = "feignClientProduct", url = "http://localhost:1003/product")
@FeignClient(name="product-service")
public interface FeignClientProduct {

    @PostMapping(value = "product/add-products")
    public List<ProductResponse> addProducts(@RequestBody List<Product> products);
}