package com.susuproject.MomsLibrary.service;
 
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
 
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.susuproject.MomsLibrary.exception.CategoryNotFoundException;
import com.susuproject.MomsLibrary.model.CategoryEntity;
import com.susuproject.MomsLibrary.repository.CategoryRepository;
 
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
 
    @Mock
    private CategoryRepository categoryRepository;
 
    @InjectMocks
    private CategoryService categoryService;
 
    @Test
    void deleteCategory_존재하지않는id_예외발생() {
        // Given: id=999는 존재하지 않는다고 가정
        given(categoryRepository.existsById(999)).willReturn(false);
 
        // When & Then
        assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.deleteCategory(999);
        });
    }
 
    @Test
    void updateCategory_존재하지않는id_예외발생() {
        // Given: id=999인 카테고리로 수정 시도, 실제로는 존재하지 않음
        CategoryEntity category = new CategoryEntity(null, null);
        ReflectionTestUtils.setField(category, "id", 999);
        given(categoryRepository.existsById(999)).willReturn(false);
 
        // When & Then
        assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.updateCategory(category);
        });
    }
}
 
