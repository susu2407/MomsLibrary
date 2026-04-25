package com.susuproject.MomsLibrary.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

// 자료별 메모
@Entity
@Table(name = "DocumentNote")
public class DocumentNoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private DocumentEntity document;

    @Column(name = "content")
    private String content;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime  createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // JPA 표준에 따른 기본 생성자
    protected DocumentNoteEntity() {}

}
