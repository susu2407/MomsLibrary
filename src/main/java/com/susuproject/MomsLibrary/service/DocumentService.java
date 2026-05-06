package com.susuproject.MomsLibrary.service;

import com.susuproject.MomsLibrary.dto.DocumentDto;
import com.susuproject.MomsLibrary.model.CategoryEntity;
import com.susuproject.MomsLibrary.model.DocumentEntity;
import com.susuproject.MomsLibrary.repository.DocumentRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {

    // DB 접근 객체(변경 불가(final)) + 생성자 주입
    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }


    //자료 등록 (code 자동 생성 포함)
    @Transactional
    public void createDocument(DocumentDto dto) {
        DocumentEntity documentEntity = toEntity(dto);
        documentRepository.save(documentEntity);
    }

    //자료 수정 + 예외처리
    @Transactional
    public void updateDocument(DocumentEntity documentEntity) {
        if (documentEntity.getId() == null) {
            throw new IllegalArgumentException("존재하지 않는 자료입니다. 수정이 불가합니다.");
        }
        documentRepository.save(documentEntity);
    }

    //자료 삭제
    @Transactional
    public void deleteDocument(Integer id) {
        documentRepository.findById(id).ifPresent(documentRepository::delete);
    }

    // ------ 검색 / 조회 ----------------------------------------
    //자료 전체 목록 조회 (최신순) - 첫 화면 출력용
    public List<DocumentEntity> getAllDocuments() {
        return documentRepository.findAllByOrderByCreatedAtDesc();
    }

    // ID 조회: Entity → DTO 변환
    public DocumentDto findById(Integer id) {
        DocumentEntity entity = documentRepository.findById(id)
                .orElseThrow(()->new RuntimeException("없는 자료입니다."));
        return toDto(entity);   // Entity → DTO 변환해서 반환
    }

    //제목으로 검색: Entity → DTO 변환
    public List<DocumentDto> searchTitleDocument() {
        return documentRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toDto)   // Entity 리스트 → DTO 리스트 변환
                .toList();
    }

    //저자로 검색
    public List<DocumentEntity> searchAuthorDocument(String author) {
        return documentRepository.findByAuthorContaining(author);
    }

    //제목+저자 통합 검색
    public List<DocumentEntity> searchTitleOrAuthorDocument(String title, String author) {
        return documentRepository.findByTitleContainingOrAuthorContaining(title, author);
    }

    //카테고리로 필터링
    public List<DocumentEntity> filteringCategoryDocument(CategoryEntity category) {
        return documentRepository.findByCategory(category);
    }

    // ------ 정렬 -----------------------------------------
    // 동적 코드 사용해보기
    public List<DocumentEntity> orderByOptionDocument() {
        return documentRepository.findAll(
                Sort.by(Sort.Direction.DESC, "createdAt")           // 최신순
                        .and(Sort.by(Sort.Direction.ASC, "title"))  // 제목 가나다순
        );
    }


    // DTO → Entity (등록/수정할 때 사용)
    private DocumentEntity toEntity(DocumentDto dto) {
        DocumentEntity entity = new DocumentEntity();
        entity.setTitle(dto.getTitle());
        entity.setAuthor(dto.getAuthor());
        entity.setPublisher(dto.getPublisher());
        entity.setPublishedAt(dto.getPublishedAt());
        entity.setPurchasedAt(dto.getPurchasedAt());
        entity.setReadAt(dto.getReadAt());
        entity.setPurpose(dto.getPurpose());
        entity.setMemo(dto.getMemo());
        entity.setExtraInfo(dto.getExtraInfo());
        entity.setFilePath(dto.getFilePath());
        entity.setSource(dto.getSource());
        return entity;
    }

    // Entity → DTO (화면에 전달할 때 사용)
    private DocumentDto toDto(DocumentEntity entity) {
        DocumentDto dto = new DocumentDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setAuthor(entity.getAuthor());
        dto.setPublisher(entity.getPublisher());
        dto.setPublishedAt(entity.getPublishedAt());
        dto.setPurchasedAt(entity.getPurchasedAt());
        dto.setReadAt(entity.getReadAt());
        dto.setPurpose(entity.getPurpose());
        dto.setMemo(entity.getMemo());
        dto.setExtraInfo(entity.getExtraInfo());
        dto.setFilePath(entity.getFilePath());
        dto.setSource(entity.getSource());
        return dto;
    }

}
