package com.susuproject.MomsLibrary.service;

import com.susuproject.MomsLibrary.dto.CategoryDto;
import com.susuproject.MomsLibrary.exception.CategoryHasChildrenException;
import com.susuproject.MomsLibrary.exception.CategoryNotFoundException;
import com.susuproject.MomsLibrary.model.CategoryEntity;
import com.susuproject.MomsLibrary.model.DocumentEntity;
import com.susuproject.MomsLibrary.repository.CategoryRepository;
import com.susuproject.MomsLibrary.repository.DocumentRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    // DB 접근 객체 - 변경 불가(final)
    private final CategoryRepository categoryRepository;
    private final DocumentRepository documentRepository;

    // 생성자 주입 - Spring이 자동으로 넣어줌
    public CategoryService(CategoryRepository categoryRepository, 
                           DocumentRepository documentRepository) {
        this.categoryRepository = categoryRepository;
        this.documentRepository = documentRepository;
    }

    // ────────────────────────────────── 조회
    // 전체 카테고리 목록 조회 (이름 가나다 순)
    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAllByOrderByNameAsc();
    }

    // 상위 카테고리만 조회 (parent_id가 null인 것)
    public List<CategoryEntity> getTopCategories() {
        return categoryRepository.findByParentIsNull();
    }

    // 특정 상위 카테고리의 하위 카테고리 조회
    public List<CategoryEntity> getSubCategories(CategoryEntity parent) {
        return categoryRepository.findByParent(parent);
    }

    // ────────────────────────────────── 등록
    // 카테고리 등록 + 예외처리
    @Transactional
    public CategoryEntity createCategory(CategoryDto dto) {
        CategoryEntity parent = null;

        // 상위 카테고리를 선택했으면 조회
        if (dto.getParentId() != null) {
            parent = categoryRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new CategoryNotFoundException(dto.getParentId()));
        }

        CategoryEntity categoryE = new CategoryEntity(dto.getName(), parent);
        return categoryRepository.save(categoryE);
    }

    // ────────────────────────────────── 수정
    // 카테고리 수정 + 예외처리
    @Transactional
    public CategoryEntity updateCategory(Integer id, CategoryDto dto) {
        CategoryEntity entity = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        CategoryEntity parent = null;
        if (dto.getParentId() != null) {
            parent = categoryRepository.findById(dto.getParentId()).
            orElseThrow(() -> new CategoryNotFoundException(dto.getParentId()));
        }

        entity.setName(dto.getName());
        entity.setParent(parent);

        return categoryRepository.save(entity);
    }

    // 수정을 위한 단건 조회
    public CategoryEntity findById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    // ────────────────────────────────── 삭제
    // 카테고리 삭제 + 예외처리
    @Transactional
    public void deleteCategory(Integer id) {
        // 이 카테고리가 존재하는가
        CategoryEntity categoryE = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        
        // 1. 하위 카테고리가 있으면 삭제 금지
        List<CategoryEntity> children = categoryRepository.findByParent(categoryE);
        if (!children.isEmpty()) {
            throw new CategoryHasChildrenException(id);
        }

        // 2. "기타" 카테고리 조회 (연결된 자료를 옮길 곳)
        CategoryEntity etcCategory = categoryRepository.findByName("기타")
                .orElseThrow(() -> new IllegalStateException("'기타' 카테고리가 존재하지 않습니다."));

        // 3. 이 카테고리를 쓰던 자료들 -> '기타'로 이동
        List<DocumentEntity> documents = documentRepository.findByCategory(categoryE);
        for (DocumentEntity document : documents) {
            document.setCategory(etcCategory);
            documentRepository.save(document);
        }

        // 4. 카테고리 삭제
        categoryRepository.delete(categoryE);
    }
}