package com.susuproject.MomsLibrary.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 자료 데이터 구조
@Entity
@Table(name = "Document")
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;                 // PK

    // Document -> Category (N:1 관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;    // FK, 자료 종류

    @Column(name = "title", nullable = false)
    private String title;               // 제목(필수)

    @Column(name = "author")
    private String author;              // 저자/작성자

    @Column(name="published_at")
    private String publishedAt;      // 출판/게시 시기

    @Column(name="purchased_at")
    private String purchasedAt;      // 구매 시기

    @Column(name = "purpose")
    private String purpose;             // 구매 계기/이유/목적

    @Column(name = "read_at")
    private String readAt;           // 독서한 시기

    @Column(name = "source")
    private String source;              // 출처

    @Column(name = "memo")
    private String memo;                // 한 줄 메모

    @Column(name = "extra_info")
    private String extraInfo;           // 종류별 추가 정보

    @Column(name = "file_path")
    private String filePath;            // 파일 위치

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;    // 등록일

    // Document와 Tag를 연결을 위한 사전 작업
    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentTagEntity> documentTags = new ArrayList<>();

    //  Document와 Tag를 연결하는 편의 메서드
    public void addTag(TagEntity tag) {
        DocumentTagEntity dt = DocumentTagEntity.connect(this, tag);
        this.documentTags.add(dt);
    }

    // JPA 표준에 따른 기본 생성자
    protected DocumentEntity() {}

    // 객체 생성을 위한 생성자
    public DocumentEntity(String title, CategoryEntity category) {
        this.title = title;
        this.category = category;
    }


}
