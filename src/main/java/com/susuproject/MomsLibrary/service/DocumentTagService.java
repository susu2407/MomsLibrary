package com.susuproject.MomsLibrary.service;

import com.susuproject.MomsLibrary.model.DocumentEntity;
import com.susuproject.MomsLibrary.model.DocumentTagEntity;
import com.susuproject.MomsLibrary.model.TagEntity;
import com.susuproject.MomsLibrary.repository.DocumentRepository;
import com.susuproject.MomsLibrary.repository.DocumentTagRepository;
import com.susuproject.MomsLibrary.repository.TagRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentTagService {

    private final DocumentTagRepository documentTagRepository;
    private final DocumentRepository documentRepository;
    private final TagRepository tagRepository;

    public DocumentTagService(DocumentTagRepository documentTagRepository,
                              DocumentRepository documentRepository,
                              TagRepository tagRepository) {
        this.documentTagRepository = documentTagRepository;
        this.documentRepository = documentRepository;
        this.tagRepository = tagRepository;
    }

    // 특정 자료의 태그 목록 조회
    public List<Integer> getTagIdsByDocument (Integer documentId) {

        DocumentEntity document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("해당 자료를 찾을 수 없습니다."));

        return documentTagRepository.findByDocument(document)
                .stream()
                .map(dt -> dt.getTag().getId())
                .toList();
    }

    // 자료에 태그 연결 (여러 개)
    @Transactional
    public void saveDocumentTags (Integer documentId, List<Integer> tagIds) {

        if (tagIds == null || tagIds.isEmpty()) return;
        DocumentEntity document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("해당 자료를 찾을 수 없습니다."));

        for (Integer tagId : tagIds) {
            TagEntity tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new RuntimeException("해당 태그는 없습니다."));

            documentTagRepository.save(new DocumentTagEntity(document, tag));
        }
    }

    // 자료에서 태그 전체 해제 (수정 시 기존 것 지우고 새로 저장하기 위해)
    public void deleteAllDocumentTags (Integer documentId) {
        DocumentEntity document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("해당 자료를 찾을 수 없습니다."));
        documentTagRepository.deleteByDocument(document);
    }
}
