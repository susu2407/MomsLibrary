package com.susuproject.MomsLibrary.service;
 
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import com.susuproject.MomsLibrary.dto.CategoryDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.susuproject.MomsLibrary.exception.CategoryHasChildrenException;
import com.susuproject.MomsLibrary.exception.CategoryNotFoundException;
import com.susuproject.MomsLibrary.model.CategoryEntity;
import com.susuproject.MomsLibrary.model.DocumentEntity;
import com.susuproject.MomsLibrary.repository.CategoryRepository;
import com.susuproject.MomsLibrary.repository.DocumentRepository;
 
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
 
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private DocumentRepository documentRepository;
 
    @InjectMocks
    private CategoryService categoryService;
 
    @Test
    void deleteCategory_존재하지않는id_예외발생() {
        // Given: id=999는 존재하지 않는다고 가정
        given(categoryRepository.findById(999)).willReturn(Optional.empty());
 
        // When & Then
        assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.deleteCategory(999);
        });
    }
 
    @Test
    void updateCategory_존재하지않는id_예외발생() {
        // Given: id=999인 카테고리로 수정 시도, 실제로는 존재하지 않음
        CategoryDto dto = new CategoryDto();
        dto.setName("변경할이름");
        given(categoryRepository.findById(999)).willReturn(Optional.empty());
 
        // When & Then
        assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.updateCategory(999, dto);
        });
    }

    @Test
    void deleteCategory_하위카테고리있으면_예외발생() {
        // Given: id=1인 카테고리(부모)가 있고, 그 부모를 참조하는(parent_id=1) 하위 카테고리 id=50이 존재
        //  1) id=1인 부모 카테고리
        CategoryEntity parent = new CategoryEntity("도서", null);
         ReflectionTestUtils.setField(parent, "id", 1);
         
         // 2) id=50인 하위 카테고리(parent를 가리킴)
         CategoryEntity child = new CategoryEntity("IT", parent);
         ReflectionTestUtils.setField(child, "id", 50);

         // 3) id=1로 조회하면 parent가 리턴됨
         given(categoryRepository.findById(1)).willReturn(Optional.of(parent));

         // 4) parent를 부모로 둔 카테고리를 찾으면 child가 하나 있음 (비어있지 않음)
         given(categoryRepository.findByParent(parent)).willReturn(List.of(child));
        
        // When: id=1(부모)을 삭제 시도
        // Then: CategoryHasChildrenException 발생
        assertThrows(CategoryHasChildrenException.class, () -> {
            categoryService.deleteCategory(1);
        });


    }

    @Test
    void deleteCategory_연결된자료있으면_categoryId가null로변경() {
        // Given: id=50인 카테고리(하위 카테고리 없음, parent_id=null)가 있고, 이 카테고리를 참조하는 Document 1개 있다고 존재
        CategoryEntity child = new CategoryEntity("IT", null);
        ReflectionTestUtils.setField(child, "id", 50);

        // Given: 연결된 자료가 옮겨갈 "기타" 카테고리가 존재
        CategoryEntity etc = new CategoryEntity("기타", null);
        ReflectionTestUtils.setField(etc, "id", 99);
        
        DocumentEntity document = new DocumentEntity();
        document.setTitle("테스트 도서");
        document.setCategory(child);
        ReflectionTestUtils.setField(document, "id", 111);

        // Given: id=50으로 카테고리 조회하면 child가 리턴됨
        given(categoryRepository.findById(50)).willReturn(Optional.of(child));
        
        // Given: child를 부모로 둔 하위 카테고리는 없음
        given(categoryRepository.findByParent(child)).willReturn(List.of());

        // Given: "기타"로 조회하면 etc가 리턴됨
        given(categoryRepository.findByName("기타")).willReturn(Optional.of(etc));

        // Given: child 카테고리를 쓰는 Document가 1개 있음
        given(documentRepository.findByCategory(child)).willReturn(List.of(document));
        
        // WHen: id=50을 삭제 시도
        categoryService.deleteCategory(50);

        // THen: 그 Document의 카테고리가 id=null로 세팅된 채로 documentRepository.save()가 호출됨.
        ArgumentCaptor<DocumentEntity> captor = ArgumentCaptor.forClass(DocumentEntity.class);
        verify(documentRepository).save(captor.capture());
        // 그 값이 null인지 확인
        assertEquals(etc,                  // "기타" 카테고리로 바뀌었는지 확인
                captor.getValue()          // 캡쳐해둔 값을 꺼냄 (= save()에 실제로 넘어갔던 DocumentEntity)
                        .getCategory());   // 그 Document의 category 필드값을 가져옴
        // Then: 카테고리 자체도 삭제됨
        verify(categoryRepository).delete(child);
    }

}
 
