package com.oliveiralucaspro.springmvcrest.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.oliveiralucaspro.springmvcrest.api.v1.mapper.CustomerMapper;
import com.oliveiralucaspro.springmvcrest.api.v1.model.CustomerDTO;
import com.oliveiralucaspro.springmvcrest.domain.Customer;
import com.oliveiralucaspro.springmvcrest.repositories.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private static final String ROOT_URL = "/api/v1/customer/";
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDTO> getAllCustomers() {
	return customerRepository.findAll().stream().map(customer -> {
	    CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
	    customerDTO.setCustomerUrl(ROOT_URL + customer.getId());
	    return customerDTO;
	}).collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
	return customerRepository.findById(id).map(customerMapper::customerToCustomerDTO)
		.orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public CustomerDTO createNewCustomer(CustomerDTO customerDTO) {
	return saveAndReturnDTO(customerMapper.customerDTOToCustomer(customerDTO));
    }

    @Override
    public CustomerDTO saveCustomerByDTO(Long id, CustomerDTO customerDTO) {
	Customer customer = customerMapper.customerDTOToCustomer(customerDTO);
	customer.setId(id);

	return saveAndReturnDTO(customer);
    }

    private CustomerDTO saveAndReturnDTO(Customer customer) {
	Customer savedCustomer = customerRepository.save(customer);
	CustomerDTO returnCustomerDTO = customerMapper.customerToCustomerDTO(savedCustomer);
	returnCustomerDTO.setCustomerUrl(ROOT_URL + savedCustomer.getId());
	return returnCustomerDTO;
    }

    @Override
    public CustomerDTO patchCustomer(Long id, CustomerDTO customerDTO) {
	return customerRepository.findById(id).map(customer -> {

	    if (customerDTO.getFirstName() != null) {
		customer.setFirstName(customerDTO.getFirstName());
	    }

	    if (customerDTO.getLastName() != null) {
		customer.setLastName(customerDTO.getLastName());
	    }

	    CustomerDTO returnDto = customerMapper.customerToCustomerDTO(customerRepository.save(customer));

	    returnDto.setCustomerUrl(ROOT_URL + id);

	    return returnDto;

	}).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public void deleteCustomerById(Long id) {
	customerRepository.deleteById(id);
    }

}
