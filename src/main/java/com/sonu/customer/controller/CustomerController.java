package com.sonu.customer.controller;

import com.sonu.customer.model.CustomerRequest;
import com.sonu.customer.model.CustomerResponse;
import com.sonu.customer.service.CustomerService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@Slf4j
@RestController
@RequestMapping(value = "/customer")
public class CustomerController {

    Logger logger = Logger.getLogger("CustomerController");

    @Autowired
    CustomerService customerService;

    @GetMapping("/get-customer/{id}")
    public CustomerResponse getCustomer(@PathVariable("id") Integer id) {
        logger.info("id-->" + id);
        return customerService.getCustomer(id);
    }

    @PostMapping("/add-customer")
    public CustomerResponse addCustomer(@RequestBody CustomerRequest customer) {
        logger.info("customer-->" + customer);
        return customerService.addCustomer(customer);
    }

    @PostMapping("/feign-add-customer")
    public CustomerResponse addFeignCustomer(@RequestBody CustomerRequest customer) {
        logger.info("customer-->" + customer);
        return customerService.addFeignCustomer(customer);
    }

    @GetMapping(value = "/test-resilience4j")
    //@Retry(name = "/test-resilience4j", fallbackMethod = "fallbackMethod")
    //@RateLimiter(name = "/test-resilience4j", fallbackMethod = "handleFailureMethod")
    @CircuitBreaker(name = "test-resilience4j", fallbackMethod = "handleFailureMethod")
    public CustomerResponse testResilience4j() {
        return customerService.getCustomer(null);
    }

    public CustomerResponse handleFailureMethod(Exception e) {
        return new CustomerResponse();
    }

}
