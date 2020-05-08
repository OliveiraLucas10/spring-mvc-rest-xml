package com.oliveiralucaspro.springmvcrest.controllers.v1;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.oliveiralucaspro.springmvcrest.api.v1.model.CategoryDTO;
import com.oliveiralucaspro.springmvcrest.controllers.RestResponseEntityExceptionHandler;
import com.oliveiralucaspro.springmvcrest.services.CategoryService;
import com.oliveiralucaspro.springmvcrest.services.ResourceNotFoundException;

class CategoryControllerTest {

    public static final String NAME = "Jim";
    public static final Long ID = 1L;

    @Mock
    CategoryService categoryService;

    @InjectMocks
    CategoryController categoryController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() throws Exception {
	MockitoAnnotations.initMocks(this);

	mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
		.setControllerAdvice(new RestResponseEntityExceptionHandler()).build();
    }

    @Test
    void testListCategories() throws Exception {
	CategoryDTO categoryDTO1 = new CategoryDTO();
	categoryDTO1.setId(ID);
	categoryDTO1.setName(NAME);

	CategoryDTO categoryDTO2 = new CategoryDTO();
	categoryDTO1.setId(2L);
	categoryDTO1.setName("Bob");

	List<CategoryDTO> categories = Arrays.asList(categoryDTO1, categoryDTO2);

	when(categoryService.getAllCategories()).thenReturn(categories);

	mockMvc.perform(get(CategoryController.BASE_URL)
		.accept(MediaType.APPLICATION_JSON)
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andExpect(jsonPath("$.categories", hasSize(2)));

    }

    @Test
    void testGetByNameCategories() throws Exception {
	CategoryDTO categoryDTO1 = new CategoryDTO();
	categoryDTO1.setId(ID);
	categoryDTO1.setName(NAME);

	when(categoryService.getCategoryByName(anyString())).thenReturn(categoryDTO1);

	mockMvc.perform(get(CategoryController.BASE_URL + "/Jim").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andExpect(jsonPath("$.name", equalTo(NAME)));

    }

    @Test
    void testGetByNameNotFound() throws Exception {

	when(categoryService.getCategoryByName(anyString())).thenThrow(ResourceNotFoundException.class);

	mockMvc.perform(get(CategoryController.BASE_URL + "/Foo").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
    }

}
