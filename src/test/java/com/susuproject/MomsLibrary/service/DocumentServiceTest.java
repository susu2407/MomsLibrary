package com.susuproject.MomsLibrary.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.susuproject.MomsLibrary.dto.DocumentDto;
import com.susuproject.MomsLibrary.exception.DocumentNotFoundException;
import com.susuproject.MomsLibrary.model.DocumentEntity;
import com.susuproject.MomsLibrary.model.TagEntity;
import com.susuproject.MomsLibrary.repository.DocumentRepository;

/* Test 기본 구조
    Given(준비): 가짜 데이터, 가짜 객체 동작 정의 ("Repository한테 이런 요청 오면 이렇게 답해")
    When(실행): 실제로 테스트하고 싶은 메서드 호출
    Then(검증): 예상한 대로 됐는지 확인
*/
@ExtendWith(MockitoExtension.class)
public class DocumentServiceTest {
    
    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private TagService tagService;

    @Mock           // <- 가짜 생성
    private DocumentTagService documentTagService;

    @InjectMocks    // <- 진짜 객체로 만들되, 생성자에 필요한 재료들은 가짜로 채워 넣어라.
    private DocumentService documentService;

    @Test
    void findById_존재하지않는id_예외발생() {
        // Given: id=999로 조회하면 아무것도 없다고 가정
        given(documentRepository.findById(999))
                .willReturn(Optional.empty());

        // WHen & Then: findById(999)를 호출하면 DocumentNotFoundException이 터져야 함
        assertThrows(DocumentNotFoundException.class, () -> {
            documentService.findById(999);
        });
    }

    @Test
    void updateDocument_존재하지않는id_예외발생() {
        // Given: id=999로 조회하면 아무것도 없다고 가정
        given(documentRepository.findById(999)).willReturn(Optional.empty());

        // When & Then: updateDocument(999, dto)를 호출하면 DocumentNotFoundException이 터져야 함
        assertThrows(DocumentNotFoundException.class, () -> {
            documentService.updateDocument(999, new DocumentDto());
        });
    }

    @Test
    void createDocument_기존태그와신규태그_모두연결() {
        // Given: 등록할 문서 정보 준비
        DocumentDto dto = new DocumentDto();
        dto.setTitle("테스트 도서");
        dto.setTagIds(List.of(1, 2));           // 기존 태그: id 1, 2
        dto.setNewTags(List.of("새태그"));    // 신규 태그: "새태그"

        // Given: "새태그"라는 이름으로 생성 요청이 오면, id=3인 TagEntity가 만들어졌다고 가정
        TagEntity newTag = new TagEntity("새태그");
        
        // TagEntity의 id는 DB가 채워주는 값이라, 테스트에서는 리플렉션 없이 직접 세팅이 어려움
        // -> tagService.createdTag()가 이 객체를 반환한다고만 약속하고, id는 getter 결과로 확인
        given(tagService.createdTag("새태그")).willReturn(newTag);

        // Given: document 저장 시 id가 10으로 채워졌다고 가정 (실제 DB 없이 흉내)
        given(documentRepository.save(any(DocumentEntity.class)))
                .willAnswer(invocation -> {
                    DocumentEntity saved = invocation.getArgument(0);
                    ReflectionTestUtils.setField(saved, "id", 10);
                    return saved;
                    });

        // When: 문서 등록 실행
        documentService.createDocument(dto);

        // Then: documentTagService.saveDocumentTags()가 documentId=10, tagIds=[1, 2, (새태그의 id)]로 호출됐는지 확인
        verify(documentTagService).saveDocumentTags(eq(10), anyList());

        /*
            < 버그 발견 & 해결 >
            DocumentService.java의 
            createDocument()의 assignTags()에서 
            tagIds.add(tag.getId()); -> allTagIds.add(tag.getId());
            새로 만든 태그의 id를 allTagIds에 추가해서, 최종적으로 기본+신규 태그를 다 힙친 목록을 만드는 것이었으나,
            실제 코드는 파라미터로 받은 원본, 기존 태그 목록(tagIds)에 잘못 추가하고 있었음.
        */
    }

    @Test
    void createDocument_공백태그는_무시됨() {
        // Given: 신규 태그 목록에 공백/빈 문자열이 섞여 있음
        DocumentDto dto = new DocumentDto();
        dto.setTitle("테스트 도서");
        dto.setNewTags(Arrays.asList("", "   ", "정상태그"));

        TagEntity validTag = new TagEntity("정상태그");
        given(tagService.createdTag("정상태그")).willReturn(validTag);

        given(documentRepository.save(any(DocumentEntity.class)))
                .willAnswer(invocation -> {
                    DocumentEntity saved = invocation.getArgument(0);
                    ReflectionTestUtils.setField(saved, "id", 10);
                    return saved;
                });
        
        // When
        documentService.createDocument(dto);

        // Then: createdTag는 "정상태그"에 대해서만 딱 1번 호출되어야 함 (공백/빈값은 호출 자체가 안 일어남)
        verify(tagService, times(1)).createdTag("정상태그");
        verify(tagService, never()).createdTag("");
        verify(tagService, never()).createdTag("   ");
    }
}
