package com.susuproject.MomsLibrary.model;

import jakarta.persistence.*;

// 태그 데이터 구조
@Entity
@Table(name = "tag")
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    // JPA 표준에 따른 기본 생성자
    protected TagEntity() {}

    // 객체 생성을 위한 생성자
    public TagEntity(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }
    public String getName() { return name;}
}
