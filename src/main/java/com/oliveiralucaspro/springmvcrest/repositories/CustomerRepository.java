package com.oliveiralucaspro.springmvcrest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oliveiralucaspro.springmvcrest.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
