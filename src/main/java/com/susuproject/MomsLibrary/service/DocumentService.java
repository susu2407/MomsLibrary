package com.susuproject.MomsLibrary.service;

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


    //자료 전체 목록 조회 (최신순) - 첫 화면 출력용
    public List<DocumentEntity> getAllDocuments() {
        return documentRepository.findAllByOrderByCreatedAtDesc();
    }

    //자료 등록 (code 자동 생성 포함)
    @Transactional
    public DocumentEntity createDocument(DocumentEntity documentEntity) {
        return documentRepository.save(documentEntity);
    }

    //자료 수정 + 예외처리
    @Transactional
    public DocumentEntity updateDocument(DocumentEntity documentEntity) {
        if (documentEntity.getId() == null) {
            throw new IllegalArgumentException("존재하지 않는 자료입니다. 수정이 불가합니다.");
        }
        return documentRepository.save(documentEntity);
    }

    //자료 삭제
    @Transactional
    public void deleteDocument(Integer id) {
        documentRepository.findById(id).ifPresent(documentRepository::delete);
    }

    //제목으로 검색
    public List<DocumentEntity> searchTitleDocument(String title) {
        return documentRepository.findByTitleContaining(title);
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

    // ------ 정렬 -------------------------
    // 최신순 정렬
    // 제목 가나다 정렬
    // 제목 하파타 정렬
    // No 정렬

}
