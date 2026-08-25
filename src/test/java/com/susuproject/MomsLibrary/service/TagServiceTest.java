package com.susuproject.MomsLibrary.service;
 
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

import java.util.Optional;
 
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.susuproject.MomsLibrary.exception.TagNotFoundException;
import com.susuproject.MomsLibrary.model.TagEntity;
import com.susuproject.MomsLibrary.repository.DocumentTagRepository;
import com.susuproject.MomsLibrary.repository.TagRepository;
 
@ExtendWith(MockitoExtension.class)
class TagServiceTest {
 
    @Mock
    private TagRepository tagRepository;

    @Mock
    private DocumentTagRepository documentTagRepository;
 
    @InjectMocks
    private TagService tagService;
 
    @Test
    void deletedTag_존재하지않는이름_예외발생() {
        // Given: "없는태그"라는 이름의 태그는 존재하지 않는다고 가정
        given(tagRepository.findByName("없는태그")).willReturn(Optional.empty());
 
        // When & Then
        assertThrows(TagNotFoundException.class, () -> {
            tagService.deletedTag("없는태그");
        });
    }
 
    @Test
    void updatedTag_존재하지않는id_예외발생() {
        // Given: id=999인 태그로 수정 시도, 실제로는 존재하지 않음
        TagEntity tag = new TagEntity("아무이름");
        ReflectionTestUtils.setField(tag, "id", 999);
        given(tagRepository.existsById(999)).willReturn(false);
 
        // When & Then
        assertThrows(TagNotFoundException.class, () -> {
            tagService.updatedTag(tag);
        });
    }

    @Test
    void deletedTag_삭제시_DocumentTag가먼저삭제됨() {
        // Given: name="테스트태그"인 태그가 존재
        TagEntity tag = new TagEntity("테스트태그");
        given(tagRepository.findByName("테스트태그")).willReturn(Optional.of(tag));

        // When: deletedTag("테스트태그") 호출
        tagService.deletedTag("테스트태그");

        // THen:
        //   1) documentTagRepository.deleteByTag()가 호출됨
        verify(documentTagRepository).deleteByTag(tag);

        //   2) tagRepository.delete()가 호출됨
        verify(tagRepository).delete(tag);

        //   3) deleteByTag가 delete보다 먼저 호출됨 - InOrder로 순서까지 검증
        InOrder inOrder = inOrder(documentTagRepository, tagRepository);
        inOrder.verify(documentTagRepository).deleteByTag(tag);
        inOrder.verify(tagRepository).delete(tag);
    }
}
 
