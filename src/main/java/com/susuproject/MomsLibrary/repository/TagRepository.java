package com.susuproject.MomsLibrary.repository;

import com.susuproject.MomsLibrary.model.DocumentFileEntity;
import com.susuproject.MomsLibrary.model.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<TagEntity,Integer>{

    // 태그 이름으로 찾기 (중복 방지용)
    Optional<TagEntity> findByName(String name);

}
