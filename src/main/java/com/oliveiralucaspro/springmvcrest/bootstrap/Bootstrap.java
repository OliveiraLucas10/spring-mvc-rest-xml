package com.oliveiralucaspro.springmvcrest.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.oliveiralucaspro.springmvcrest.domain.Category;
import com.oliveiralucaspro.springmvcrest.domain.Customer;
import com.oliveiralucaspro.springmvcrest.domain.Vendor;
import com.oliveiralucaspro.springmvcrest.repositories.CategoryRepository;
import com.oliveiralucaspro.springmvcrest.repositories.CustomerRepository;
import com.oliveiralucaspro.springmvcrest.repositories.VendorRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final CustomerRepository customerRepository;
    private final VendorRepository vendorRepository;

    @Override
    public void run(String... args) throws Exception {
	loadCategory();
	loadCustomer();
	loadVendor();
    }

    private void loadCategory() {
	Category fruits = new Category();
	fruits.setName("Fruits");

	Category dried = new Category();
	dried.setName("Dried");

	Category fresh = new Category();
	fresh.setName("Fresh");

	Category exotic = new Category();
	exotic.setName("Exotic");

	Category nuts = new Category();
	nuts.setName("Nuts");

	categoryRepository.save(fruits);
	categoryRepository.save(dried);
	categoryRepository.save(fresh);
	categoryRepository.save(exotic);
	categoryRepository.save(nuts);

	System.out.println("Category Loaded = " + categoryRepository.count());
    }

    private void loadCustomer() {
	Customer pedro = new Customer();
	pedro.setFirstName("Pedro");
	pedro.setLastName("Favela");

	Customer sergio = new Customer();
	sergio.setFirstName("Sergio");
	sergio.setLastName("Mariola");

	customerRepository.save(pedro);
	customerRepository.save(sergio);

	System.out.println("Customer Loaded = " + customerRepository.count());
    }

    private void loadVendor() {
	Vendor western = new Vendor();
	western.setName("Western Tasty Fruits Ltd.");

	Vendor exotic = new Vendor();
	exotic.setName("Exotic Fruits Company");

	Vendor home = new Vendor();
	home.setName("Home Fruits");

	vendorRepository.save(western);
	vendorRepository.save(exotic);
	vendorRepository.save(home);

	System.out.println("Vendor Loaded = " + vendorRepository.count());
    }
}
