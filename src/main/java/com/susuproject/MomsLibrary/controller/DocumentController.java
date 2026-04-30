package com.susuproject.MomsLibrary.controller;
// URL 받아서 Service 호출하고 화면으로 넘겨주는 역할

import com.susuproject.MomsLibrary.service.DocumentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DocumentController {

    // 'Service 호출하고'-> Service 접근 객체(변경 불가(final) + 생성자 주입
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    // 자료 목록 화면
    @GetMapping("/documents")   // http://localhost:8080/documents
    public String documentList(Model model) {
        model.addAttribute("documents", documentService.getAllDocuments());
        return "document/list";
    }
    // 상세 화면 -> document/detail.html

    // 자료 등록
    // 등록 폼 화면
    // 등록처리

    // 자료 수정
    // 수정 폼 화면
    // 수정 처리

    // 자료 삭제 (팝업으로 확인 창 열어서, 재확인 과정)

    // 자료 검색
    // 자료 필터
    // 자료 정렬
}
