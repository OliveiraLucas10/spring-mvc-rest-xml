package com.oliveiralucaspro.springmvcrest.controllers.v1;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.oliveiralucaspro.springmvcrest.api.v1.model.CategoryDTO;
import com.oliveiralucaspro.springmvcrest.api.v1.model.CategoryListDTO;
import com.oliveiralucaspro.springmvcrest.services.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(CategoryController.BASE_URL)
@RequiredArgsConstructor
public class CategoryController {

    static final String BASE_URL = "/api/v1/categories";
    private final CategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CategoryListDTO getallCatetories() {
	return new CategoryListDTO(categoryService.getAllCategories());
    }

    @GetMapping("{name}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDTO getCategoryByName(@PathVariable String name) {
	return categoryService.getCategoryByName(name);
    }

}
