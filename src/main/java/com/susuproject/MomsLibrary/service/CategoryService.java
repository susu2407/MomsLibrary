package com.susuproject.MomsLibrary.service;

import com.susuproject.MomsLibrary.exception.CategoryNotFoundException;
import com.susuproject.MomsLibrary.model.CategoryEntity;
import com.susuproject.MomsLibrary.repository.CategoryRepository;
import jakarta.transaction.Transactional;
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

    // 카테고리 등록 + 예외처리
    @Transactional
    public CategoryEntity createCategory(CategoryEntity parent, String categoryName) {
        CategoryEntity category = new CategoryEntity(categoryName, parent);
        return categoryRepository.save(category);
    }

    // 카테고리 수정 + 예외처리
    @Transactional
    public CategoryEntity updateCategory(CategoryEntity categoryEntity) {
        Integer id = categoryEntity.getId();

        if (id == null) {
            throw new IllegalArgumentException("수정할 카테고리의 id가 없습니다.");
        }
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException(id);
        }
        return categoryRepository.save(categoryEntity);
    }

    // 카테고리 삭제 + 예외처리
    @Transactional
    public void deleteCategory(Integer id) {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException(id);
        }
        categoryRepository.deleteById(id);
    }
}