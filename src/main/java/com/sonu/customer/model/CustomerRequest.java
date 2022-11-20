package com.sonu.customer.model;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

@Data
public class CustomerRequest implements Serializable {

    @Id
    private Integer customerId;
    private String customerName;
    private List<Product> products;
}
