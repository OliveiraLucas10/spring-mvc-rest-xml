package com.oliveiralucaspro.springmvcrest.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oliveiralucaspro.springmvcrest.api.v1.mapper.CategoryMapper;
import com.oliveiralucaspro.springmvcrest.api.v1.model.CategoryDTO;
import com.oliveiralucaspro.springmvcrest.domain.Category;
import com.oliveiralucaspro.springmvcrest.repositories.CategoryRepository;

class CategoryServiceImplTest {

    private static final Long ID = 1L;
    private static final String NAME = "Pedro";
    CategoryService categoryService;

    @Mock
    CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() throws Exception {
	MockitoAnnotations.initMocks(this);

	categoryService = new CategoryServiceImpl(categoryRepository, CategoryMapper.INSTANCE);
    }

    @Test
    void testGetAllCategories() {
	// given
	List<Category> categories = Arrays.asList(new Category(), new Category(), new Category());

	when(categoryRepository.findAll()).thenReturn(categories);

	// when
	List<CategoryDTO> allCategories = categoryService.getAllCategories();

	// then
	assertEquals(3, allCategories.size());
    }

    @Test
    void testGetCategoryByName() {
	// given
	Category category = new Category();
	category.setId(ID);
	category.setName(NAME);

	when(categoryRepository.findByName(anyString())).thenReturn(category);

	// when
	CategoryDTO categoryDTO = categoryService.getCategoryByName(NAME);

	// then
	assertEquals(ID, categoryDTO.getId());
	assertEquals(NAME, categoryDTO.getName());

    }

}
