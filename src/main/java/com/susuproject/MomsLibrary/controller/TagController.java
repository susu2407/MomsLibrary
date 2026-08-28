package com.susuproject.MomsLibrary.controller;

import com.susuproject.MomsLibrary.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }
    // ────────────────────────────────── 태그 목록
    // /categories 화면 사이드바에 포함되어 있어, CategoryController.categoryList()에서 함께 조회함

    // ────────────────────────────────── 태그 등록
    @PostMapping("/tags/new")
    public String createTag(@RequestParam String name) {
        tagService.createdTag(name);
        return "redirect:/categories";
    }

    // ────────────────────────────────── 태그 삭제
    @PostMapping("/tags/{name}/delete")
    public String deleteTag(@PathVariable String name) {
        tagService.deletedTag(name);
        return "redirect:/categories";
    }
}