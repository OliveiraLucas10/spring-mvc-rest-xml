package com.oliveiralucaspro.springmvcrest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oliveiralucaspro.springmvcrest.domain.Vendor;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

}
