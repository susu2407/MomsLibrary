package com.susuproject.MomsLibrary.model;

import jakarta.persistence.*;

// 분류 데이터 구조 (트리 구조)
@Entity
@Table(name = "Category")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;                     // FK

    @Column(name = "name", nullable = false)
    private String name;                    // 카테고이 이름

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CategoryEntity parent;          // 부모 카테고리 참조 (Self-Referencing FK)

    // JPA 표준에 따른 기본 생성자
    protected CategoryEntity() {}

    // 객체 생성을 위한 생성자
    public CategoryEntity(String name) {
        this.name = name;
    }

}
