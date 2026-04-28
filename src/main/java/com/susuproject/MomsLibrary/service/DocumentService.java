package com.susuproject.MomsLibrary.service;

import com.susuproject.MomsLibrary.model.CategoryEntity;
import com.susuproject.MomsLibrary.model.DocumentEntity;
import com.susuproject.MomsLibrary.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {

    // DB 접근 객체(변경 불가(final)) + 생성자 주입
    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }


    //자료 전체 목록 조회 (최신순)
    public List<DocumentEntity> getAllDocuments() {
        return documentRepository.findAll();
    }

    //자료 등록 (code 자동 생성 포함)
    public DocumentEntity createDocument(DocumentEntity documentEntity) {
        return documentRepository.save(documentEntity);
    }

    //자료 수정
    public DocumentEntity updateDocument(DocumentEntity documentEntity) {
        return documentRepository.save(documentEntity);
    }

    //자료 삭제
    public void deleteDocument(int id) {
        documentRepository.deleteById(id);
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
