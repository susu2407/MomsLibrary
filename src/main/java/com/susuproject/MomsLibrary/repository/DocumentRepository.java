package com.susuproject.MomsLibrary.repository;

import com.susuproject.MomsLibrary.model.CategoryEntity;
import com.susuproject.MomsLibrary.model.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity,Integer> {

    // ================================ R 검색 ================================
    // 제목으로 검색 (검색 키워드 포함된 제목 모두 찾기)
    List<DocumentEntity> findByTitleContaining(String title);

    // 저자로 검색
    List<DocumentEntity> findByAuthorContaining(String author);

    // 제목 또는 저자로 검색 (통합 검색)
    List<DocumentEntity> findByTitleOrAuthor(String title, String author);

    // 카테고리로 필터
    List<DocumentEntity> findByCategory(CategoryEntity category);

    // 전체 최신순 조회
    List<DocumentEntity> findAllByOrderByCreatedAtDesc();
}
