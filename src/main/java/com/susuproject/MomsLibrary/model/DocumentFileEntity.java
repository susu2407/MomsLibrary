package com.susuproject.MomsLibrary.model;

import jakarta.persistence.*;

import java.time.LocalDate;

// 파일 첨부
@Entity
@Table(name = "DocumentFile")
public class DocumentFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private DocumentEntity document;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "uploaded_at")
    private LocalDate uploadedAt;

    // JPA 표준에 따른 기본 생성자
    protected DocumentFileEntity() {}

}
