package com.susuproject.MomsLibrary.controller;
// URL 받아서 Service 호출하고 화면으로 넘겨주는 역할

import com.susuproject.MomsLibrary.dto.DocumentDto;
import com.susuproject.MomsLibrary.model.DocumentEntity;
import com.susuproject.MomsLibrary.repository.DocumentRepository;
import com.susuproject.MomsLibrary.service.DocumentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.swing.text.Document;

@Controller
public class DocumentController {

    // 'Service 호출하고'-> Service 접근 객체(변경 불가(final) + 생성자 주입
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    // ───────────────── 자료 목록 화면
    @GetMapping("/documents")   // http://localhost:8080/documents
    public String documentList(Model model) {
        model.addAttribute("documents", documentService.getAllDocuments());
        return "document/list";
    }
    // ───────────────── 상세 화면
    @GetMapping("/documents/{id}")
    public String documentDetail(@PathVariable Integer id, Model model) {
        model.addAttribute("document", documentService.findById(id));
        return "document/detail";
    }

    // ───────────────── 자료 등록 ─────────────────
    // 등록 폼 요청
    @GetMapping("/documents/new")
    public String registerForm(Model model) {
        model.addAttribute("document", new DocumentDto());
        model.addAttribute("mode", "register");     // 등록 모드
        return "document/form";
    }
    // 등록처리
    @PostMapping("/documents/new")
    public String registerProcessing (@ModelAttribute DocumentDto dto) {
        documentService.createDocument(dto); // 반환값 없이 호출만
        return "redirect:/documents";        // 목록 URL로 이동 (새로고침 방지)
    }

    // ───────────────── 자료 수정 ─────────────────
    // 수정 폼 요청
    @GetMapping("/documents/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("document", documentService.findById(id));
        model.addAttribute("mode", "edit");         // 수정 모드
        return "document/form";                                             // 같은 파일 재사용
    }

    // 수정 처리


    // ───────────────── 자료 삭제 (팝업으로 확인 창 열어서, 재확인 과정)

    // ───────────────── 자료 검색
    // ───────────────── 자료 필터
    // ───────────────── 자료 정렬
}
