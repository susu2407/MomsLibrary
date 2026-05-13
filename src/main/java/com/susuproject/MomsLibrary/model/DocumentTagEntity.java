package com.susuproject.MomsLibrary.model;

import jakarta.persistence.*;

// 자료-태그 N:M 연결
@Entity
@Table(
        name = "document_tag",
        uniqueConstraints = @UniqueConstraint(columnNames = {"document_id", "tag_id"})
)
public class DocumentTagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private DocumentEntity document;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private TagEntity tag;


    // JPA 표준에 따른 기본 생성자
    protected DocumentTagEntity() {}

    // 객체 생성을 위한 생성자
    public DocumentTagEntity(DocumentEntity document, TagEntity tag) {
        this.document = document;
        this.tag = tag;
    }

    // Document:Tag (N:M 관계)를 연결하는 편의 메서드
    public static DocumentTagEntity connect(DocumentEntity document, TagEntity tag) {
        DocumentTagEntity dt = new DocumentTagEntity();
        dt.document = document;
        dt.tag = tag;
        return dt;
    }

    public Integer getId() { return id; }
    public DocumentEntity getDocument() { return document; }
    public TagEntity getTag() { return tag; }
}
