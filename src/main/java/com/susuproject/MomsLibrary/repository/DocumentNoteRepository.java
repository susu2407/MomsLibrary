package com.susuproject.MomsLibrary.repository;

import com.susuproject.MomsLibrary.model.DocumentEntity;
import com.susuproject.MomsLibrary.model.DocumentNoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentNoteRepository extends JpaRepository<DocumentNoteEntity,Integer> {
    // 특정 Document의 메모 조회
    List<DocumentNoteEntity> findByDocument(DocumentEntity document);
}
