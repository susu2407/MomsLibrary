package com.susuproject.MomsLibrary.repository;

import com.susuproject.MomsLibrary.model.DocumentEntity;
import com.susuproject.MomsLibrary.model.DocumentTagEntity;
import com.susuproject.MomsLibrary.model.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentTagRepository extends JpaRepository<DocumentTagEntity,Integer> {

    // 특정 Document에 연결된 태그 목록 조회
    // SELECT * FROM DocumentTag dt JOIN Document d WHERE dt.id = d.id
    List<DocumentTagEntity> findByDocument (DocumentEntity document);

    // 특정 Tag가 연결된 Document 목록 조회
    List<DocumentTagEntity> findByTag (TagEntity tag);

}
