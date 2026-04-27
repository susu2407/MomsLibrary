package com.susuproject.MomsLibrary.repository;

import com.susuproject.MomsLibrary.model.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity,Integer> {

    // 전체 목록 조회 (JPA 자동) findAll()

    // 상위 카테고리만 조회 (parent_id가 없는 것)
    List<CategoryEntity> findByParentIsNull();

    // 특정 상위 카테고리의 하위 카테고리 조회
    // SELECT * FROM Category WHERE parent = ?
    List<CategoryEntity> findByParent(CategoryEntity parent);

}
