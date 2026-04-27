package com.susuproject.MomsLibrary.repository;

import com.susuproject.MomsLibrary.model.DocumentEntity;
import com.susuproject.MomsLibrary.model.DocumentFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentFileRepository extends JpaRepository<DocumentFileEntity,Integer> {
    // 특정 Document에 연결된 파일 목록 조회
    List<DocumentFileEntity> findByDocument(DocumentEntity document);
}
