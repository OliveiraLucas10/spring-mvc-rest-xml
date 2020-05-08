package com.oliveiralucaspro.springmvcrest.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.oliveiralucaspro.springmvcrest.api.v1.mapper.CategoryMapper;
import com.oliveiralucaspro.springmvcrest.api.v1.model.CategoryDTO;
import com.oliveiralucaspro.springmvcrest.repositories.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDTO> getAllCategories() {
	return categoryRepository.findAll().stream().map(categoryMapper::categoryToCategoryDTO)
		.collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getCategoryByName(String name) {
	return categoryMapper.categoryToCategoryDTO(categoryRepository.findByName(name));
    }

}
