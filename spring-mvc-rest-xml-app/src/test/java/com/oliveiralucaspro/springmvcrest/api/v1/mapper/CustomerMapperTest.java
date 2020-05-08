package com.oliveiralucaspro.springmvcrest.api.v1.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.oliveiralucaspro.model.CustomerDTO;
import com.oliveiralucaspro.springmvcrest.domain.Customer;

class CustomerMapperTest {

    private static final String LAST_NAME = "Favela";
    private static final String FIRST_NAME = "Pedro";
    private static final long ID = 1L;

    CustomerMapper customerMapper = CustomerMapper.INSTANCE;

    @Test
    void testCustomerToCustomerDTO() {
	// given
	Customer customer = new Customer();
	customer.setId(ID);
	customer.setFirstName(FIRST_NAME);
	customer.setLastName(LAST_NAME);

	// when
	CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);

	// then
	assertEquals(FIRST_NAME, customerDTO.getFirstName());
	assertEquals(LAST_NAME, customerDTO.getLastName());

    }

    @Test
    void testCustomerDTOToCustomer() {
	// given
	CustomerDTO customerDTO = new CustomerDTO();
	customerDTO.setFirstName(FIRST_NAME);
	customerDTO.setLastName(LAST_NAME);

	// when
	Customer customer = customerMapper.customerDTOToCustomer(customerDTO);

	// then
	assertEquals(FIRST_NAME, customer.getFirstName());
	assertEquals(LAST_NAME, customer.getLastName());

    }

}
