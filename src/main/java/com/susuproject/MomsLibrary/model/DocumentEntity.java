package com.susuproject.MomsLibrary.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 자료 데이터 구조
@Entity
@Table(name = "document")
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

    @Column(name = "publisher")
    private String publisher;           // 출판사

    @Column(name="published_at")
    private String publishedAt;         // 출판/게시 시기

    @Column(name="purchased_at")
    private String purchasedAt;         // 구매 시기

    @Column(name = "purpose")
    private String purpose;             // 구매 계기/이유/목적

    @Column(name = "read_at")
    private String readAt;              // 독서한 시기

    @Column(name = "source")
    private String source;              // 출처(해당 정보를 알게된 루트, 정보의 신뢰성에 영향을 줌.)

    @Column(name = "memo")
    private String memo;                // 한 줄 메모

    @Column(name = "extra_info")
    private String extraInfo;           // 종류별 추가 정보

    @Column(name = "file_path")
    private String filePath;            // 파일 위치

    @Column(name = "created_at")
    private String createdAt;           // 등록일

    // Document와 Tag를 연결을 위한 사전 작업
    @OneToMany(mappedBy = "document",
            cascade = CascadeType.REMOVE,   // Document 삭제 시 연결 정보만 삭제
            orphanRemoval = true)
    private List<DocumentTagEntity> documentTags = new ArrayList<>();

    //  Document와 Tag를 연결하는 편의 메서드
    public void addTag(TagEntity tag) {
        DocumentTagEntity dt = DocumentTagEntity.connect(this, tag);
        this.documentTags.add(dt);
    }

    // JPA 표준에 따른 기본 생성자
    public DocumentEntity() {}

    // 객체 생성을 위한 생성자
    public DocumentEntity(String title, CategoryEntity category) {
        this.title = title;
        this.category = category;
    }

    // getter / setter
    public Integer getId() { return id;}
    public void setId(Integer id) { this.id = id;}

    public CategoryEntity getCategory() { return category; }
    public void setCategory(CategoryEntity category) { this.category = category; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author;}

    public String getPublisher() { return publisher;}
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public String getPublishedAt() { return publishedAt; }
    public void setPublishedAt(String publishedAt) { this.publishedAt = publishedAt; }

    public String getPurchasedAt() { return purchasedAt; }
    public void setPurchasedAt(String purchasedAt) { this.purchasedAt = purchasedAt; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public String getReadAt() { return readAt; }
    public void setReadAt(String readAt) { this.readAt = readAt; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }

    public String getExtraInfo() { return extraInfo; }
    public void setExtraInfo(String extraInfo) { this.extraInfo = extraInfo; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public List<DocumentTagEntity> getDocumentTags() { return documentTags; }
    public void setDocumentTags(List<DocumentTagEntity> documentTags) { this.documentTags = documentTags; }
}
