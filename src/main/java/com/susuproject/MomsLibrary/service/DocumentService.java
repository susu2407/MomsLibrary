package com.susuproject.MomsLibrary.service;

import com.susuproject.MomsLibrary.dto.DocumentDto;
import com.susuproject.MomsLibrary.exception.CategoryNotFoundException;
import com.susuproject.MomsLibrary.exception.DocumentNotFoundException;
import com.susuproject.MomsLibrary.model.CategoryEntity;
import com.susuproject.MomsLibrary.model.DocumentEntity;
import com.susuproject.MomsLibrary.model.TagEntity;
import com.susuproject.MomsLibrary.repository.CategoryRepository;
import com.susuproject.MomsLibrary.repository.DocumentRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentService {

    // DB 접근 객체(변경 불가(final)) + 생성자 주입
    private final DocumentRepository documentRepository;
    private final CategoryRepository categoryRepository;

    private final TagService tagService;
    private final DocumentTagService documentTagService;

    public DocumentService(DocumentRepository documentRepository,
                           CategoryRepository categoryRepository,
                           TagService tagService,
                           DocumentTagService documentTagService) {
        this.documentRepository = documentRepository;
        this.categoryRepository = categoryRepository;
        this.tagService = tagService;
        this.documentTagService = documentTagService;
    }

    // ────────────────────────────────── 등록 
    //자료 등록 (code 자동 생성 포함)
    @Transactional
    public void createDocument(DocumentDto dto) {

        // 1. document 저장
        DocumentEntity document = toEntity(dto);

        // 등록일 직접 세팅
        document.setCreatedAt(
                LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"))
        );

        documentRepository.save(document);

        // 2. 기존 + 신규 태그 합치기 (등록/수정 공통 로직 호출)
        assignTags(document.getId(), dto.getTagIds(), dto.getNewTags());
    }

    // ────────────────────────────────── 수정 
    //자료 수정 + 예외처리
    @Transactional
    public void updateDocument(Integer id, DocumentDto dto) {
        // 1. 수정 대상 조회 (없으면 예외)
        DocumentEntity entity = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException(id));
        
        // 2. dto 값으로 기존 entity 필드 덮어쓰기
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

        // 카테고리 실제 조회해서 세팅(미분류 선택 시 null 허용)
        if (dto.getCategoryId() != null) {
            CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException(dto.getCategoryId()));
            entity.setCategory(category);
        } else {
            entity.setCategory(null);
        }

        // 3. 기존 + 신규 캐그 처리 (등록/수정 공통 로직 호출)
        assignTags(entity.getId(), dto.getTagIds(), dto.getNewTags());
    }

    // ────────────────────────────────── 태그 처리 (등록/수정 공통)
    private void assignTags(Integer documentId, List<Integer> tagIds, List<String> newTagNames) {
        // 수정 시 기존 연결을 깨끗이 지우고 다시 만들기 위해 선삭제
        documentTagService.deleteAllDocumentTags(documentId);

        List<Integer> allTagIds = new ArrayList<>();

        if (tagIds != null) {
            allTagIds.addAll(tagIds);
        }

        if (newTagNames != null) {
            for (String name : newTagNames) {
                if (name == null || name.trim().isEmpty()) continue;    // 공백 방지
                TagEntity tag = tagService.createdTag(name);
                allTagIds.add(tag.getId());
            }
        }

        // DocumentTag 연결
        documentTagService.saveDocumentTags(documentId, allTagIds);
    }

    // ────────────────────────────────── 삭제 
    //자료 삭제
    @Transactional
    public void deleteDocument(Integer id) {
        documentRepository.findById(id).ifPresent(documentRepository::delete);
    }

    // ────────────────────────────────── 검색 / 조회 
    //자료 전체 목록 조회 (최신순) - 첫 화면 출력용
    public List<DocumentEntity> getAllDocuments() {
        return documentRepository.findAllByOrderByCreatedAtDesc();
    }

    // ID 조회: Entity → DTO 변환
    public DocumentDto findById(Integer id) {
        DocumentEntity entity = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException(id));
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

    // ────────────────────────────────── 정렬 
    // 동적 코드 사용해보기
    public List<DocumentEntity> orderByOptionDocument() {
        return documentRepository.findAll(
                Sort.by(Sort.Direction.DESC, "createdAt")           // 최신순
                        .and(Sort.by(Sort.Direction.ASC, "title"))  // 제목 가나다순
        );
    }

    // ────────────────────────────────── 데이터 변환 
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

        // 카테고리 실제 조회해서 세팅 (없으면 미분류 처리)
        if (dto.getCategoryId() != null) {
            CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException(dto.getCategoryId()));
            entity.setCategory(category);
        } else {
            entity.setCategory(null);
        }

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
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setCategoryId(entity.getCategory() != null ?
                entity.getCategory().getId() : null);
        dto.setCategoryName(entity.getCategory() != null ?
                entity.getCategory().getName() : null);
        dto.setTagIds(documentTagService.getTagIdsByDocument(entity.getId()));
        return dto;
    }

}
