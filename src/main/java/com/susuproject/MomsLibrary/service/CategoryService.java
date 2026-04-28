package com.susuproject.MomsLibrary.service;

import com.susuproject.MomsLibrary.model.CategoryEntity;
import com.susuproject.MomsLibrary.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    // DB 접근 객체 - 변경 불가(final)
    private final CategoryRepository categoryRepository;

    // 생성자 주입 - Spring이 자동으로 CategoryRepository를 넣어줌
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    // 전체 카테고리 목록 조회
    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }

    // 상위 카테고리만 조회 (parent_id가 null인 것)
    public List<CategoryEntity> getTopCategories() {
        return categoryRepository.findByParentIsNull();
    }

    // 특정 상위 카테고리의 하위 카테고리 조회
    public List<CategoryEntity> getSubCategories(CategoryEntity parent) {
        return categoryRepository.findByParent(parent);
    }

    // 카테고리 등록
    public CategoryEntity createCategory(CategoryEntity parent, String categoryName) {
        CategoryEntity category = new CategoryEntity(categoryName, parent);
        return categoryRepository.save(category);
    }

    // 카테고리 삭제
    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }
}