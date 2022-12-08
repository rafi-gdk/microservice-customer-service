package com.sonu.customer.service;

import com.sonu.customer.exception.RecordSaveFailed;
import com.sonu.customer.exception.UserNotFoundException;
import com.sonu.customer.feign.FeignClientProduct;
import com.sonu.customer.model.*;
import com.sonu.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    FeignClientProduct feignClientProduct;

    @Autowired
    private Environment env;


    public CustomerResponse getCustomer(Integer id) {
        CustomerResponse response;
        Optional<Customer> byId = customerRepository.findById(id);
        if (byId.isPresent()) {
            String myProperty = env.getProperty("product-service.url");
            String url = myProperty + "/product/get-products/";
            ResponseEntity<ProductResponse[]> responseEntity = restTemplate.getForEntity(url + id, ProductResponse[].class);
            if (responseEntity.getStatusCode().is2xxSuccessful() && null != responseEntity.getBody()) {
                List<ProductResponse> products = Arrays.asList(responseEntity.getBody());
                Customer customer = byId.get();
                response = new CustomerResponse(customer.getCustomerId(), customer.getCustomerName(), customer.getDateTime(), products);
                return response;
            }
        }
        throw new UserNotFoundException("user not found");
    }

    public CustomerResponse addCustomer(CustomerRequest request) {
        CustomerResponse response;
        Customer customer = new Customer(request.getCustomerId(), request.getCustomerName(), LocalDateTime.now());
        Customer saveCustomer = customerRepository.save(customer);
        if (null != saveCustomer.getCustomerId()) {
            request.getProducts().forEach(product -> product.setCustomerId(request.getCustomerId()));
            HttpEntity<List<Product>> productEntity = new HttpEntity<>(request.getProducts());
            String myProperty = env.getProperty("product-service.url");
            String url = myProperty + "/product/add-products";
            ResponseEntity<ProductResponse[]> responseEntity = restTemplate.postForEntity(url, productEntity, ProductResponse[].class);
            if (responseEntity.getStatusCode().is2xxSuccessful() && null != responseEntity.getBody()) {
                List<ProductResponse> products = Arrays.asList(responseEntity.getBody());
                response = new CustomerResponse(saveCustomer.getCustomerId(), saveCustomer.getCustomerName(), saveCustomer.getDateTime(), products);
                return response;
            }
        }
        throw new RecordSaveFailed("unable to save the customer");
    }

    public CustomerResponse addFeignCustomer(CustomerRequest request) {
        CustomerResponse response;
        Customer customer = new Customer(request.getCustomerId(), request.getCustomerName(), LocalDateTime.now());
        Customer saveCustomer = customerRepository.save(customer);
        if (null != saveCustomer.getCustomerId()) {
            request.getProducts().forEach(product -> product.setCustomerId(request.getCustomerId()));
            List<ProductResponse> productResponses = feignClientProduct.addProducts(request.getProducts());
            if (!productResponses.isEmpty()) {
                response = new CustomerResponse(saveCustomer.getCustomerId(), saveCustomer.getCustomerName(), saveCustomer.getDateTime(), productResponses);
                return response;
            }
        }
        throw new RecordSaveFailed("unable to save the customer");
    }
}
