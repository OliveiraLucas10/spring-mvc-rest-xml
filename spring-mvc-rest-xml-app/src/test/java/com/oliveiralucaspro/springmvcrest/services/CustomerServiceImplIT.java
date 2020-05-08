package com.oliveiralucaspro.springmvcrest.services;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.oliveiralucaspro.springmvcrest.api.v1.mapper.CustomerMapper;
import com.oliveiralucaspro.springmvcrest.api.v1.model.CustomerDTO;
import com.oliveiralucaspro.springmvcrest.bootstrap.Bootstrap;
import com.oliveiralucaspro.springmvcrest.domain.Customer;
import com.oliveiralucaspro.springmvcrest.repositories.CategoryRepository;
import com.oliveiralucaspro.springmvcrest.repositories.CustomerRepository;
import com.oliveiralucaspro.springmvcrest.repositories.VendorRepository;

@DataJpaTest
public class CustomerServiceImplIT {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    VendorRepository vendorRepository;

    CustomerService customerService;

    @BeforeEach
    public void setUp() throws Exception {
	System.out.println("Loading Customer Data");
	System.out.println(customerRepository.findAll().size());

	// setup data for testing
	Bootstrap bootstrap = new Bootstrap(categoryRepository, customerRepository, vendorRepository);
	bootstrap.run(); // load data

	customerService = new CustomerServiceImpl(customerRepository, CustomerMapper.INSTANCE);
    }

    @Test
    void patchCustomerUpdateFirstName() throws Exception {
	String updatedName = "UpdatedName";
	long id = getCustomerIdValue();

	Customer originalCustomer = customerRepository.getOne(id);
	assertNotNull(originalCustomer);
	// save original first name
	String originalFirstName = originalCustomer.getFirstName();
	String originalLastName = originalCustomer.getLastName();

	CustomerDTO customerDTO = new CustomerDTO();
	customerDTO.setFirstName(updatedName);

	customerService.patchCustomer(id, customerDTO);

	Customer updatedCustomer = customerRepository.findById(id).get();

	assertNotNull(updatedCustomer);
	assertEquals(updatedName, updatedCustomer.getFirstName());
	assertThat(originalFirstName, not(equalTo(updatedCustomer.getFirstName())));
	assertThat(originalLastName, equalTo(updatedCustomer.getLastName()));
    }

    @Test
    void patchCustomerUpdateLastName() throws Exception {
	String updatedName = "UpdatedName";
	long id = getCustomerIdValue();

	Customer originalCustomer = customerRepository.getOne(id);
	assertNotNull(originalCustomer);

	// save original first/last name
	String originalFirstName = originalCustomer.getFirstName();
	String originalLastName = originalCustomer.getLastName();

	CustomerDTO customerDTO = new CustomerDTO();
	customerDTO.setLastName(updatedName);

	customerService.patchCustomer(id, customerDTO);

	Customer updatedCustomer = customerRepository.findById(id).get();

	assertNotNull(updatedCustomer);
	assertEquals(updatedName, updatedCustomer.getLastName());
	assertThat(originalFirstName, equalTo(updatedCustomer.getFirstName()));
	assertThat(originalLastName, not(equalTo(updatedCustomer.getLastName())));
    }

    private Long getCustomerIdValue() {
	List<Customer> customers = customerRepository.findAll();

	System.out.println("Customers Found: " + customers.size());

	// return first id
	return customers.get(0).getId();
    }
}
