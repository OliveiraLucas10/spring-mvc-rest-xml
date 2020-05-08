package com.oliveiralucaspro.springmvcrest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oliveiralucaspro.springmvcrest.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);

}
