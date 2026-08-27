package com.susuproject.MomsLibrary.controller;

import com.susuproject.MomsLibrary.service.CategoryService;
import com.susuproject.MomsLibrary.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CategoryController {

    private final CategoryService categoryService;
    private final TagService tagService;

    public CategoryController(CategoryService categoryService,
                              TagService tagService) {
        this.categoryService = categoryService;
        this.tagService = tagService;
    }

    // ────────────────────────────────── 카테고리 목록
    @GetMapping("/categories")
    public String categoryList(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "category/manage";
    }

    // ────────────────────────────────── 카테고리 삭제
    @PostMapping("categories/{id}/delete")
    public String deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }

    // ────────────────────────────────── 태그 삭제
    @PostMapping("/tags/{name}/delete")
    public String deleteTag(@PathVariable String name) {
        tagService.deletedTag(name);
        return "redirect:/categories";
    }

}
