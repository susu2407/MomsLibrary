package com.susuproject.MomsLibrary.controller;

import com.susuproject.MomsLibrary.dto.CategoryDto;
import com.susuproject.MomsLibrary.model.CategoryEntity;
import com.susuproject.MomsLibrary.service.CategoryService;
import com.susuproject.MomsLibrary.service.TagService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    // ────────────────────────────────── 카테고리 등록
    // 등록 폼 요청
    @GetMapping("/categories/new")
    public String registerFormCategory(Model model) {
        model.addAttribute("category", new CategoryDto());
        model.addAttribute("categories", categoryService.getAllCategories());   // 상위 카테고리 선택 드롭다운용
        model.addAttribute("mode", "register");
        return "category/form";
    }
    // 등록 처리
    @PostMapping("/categories/new")
    public String registerProcessing(@Valid @ModelAttribute("category")CategoryDto dto,
                                     BindingResult bindingResult,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "category/manage";
        }

        categoryService.createCategory(dto);
        return "redirect:/categories";          // 목록 URL로 이동 (새로고침 방지)
    }

    // ────────────────────────────────── 카테고리 삭제
    @PostMapping("categories/{id}/delete")
    public String deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }

    // ────────────────────────────────── 카테고리 수정
    // 수정 폼 요청
    @GetMapping("/categories/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        CategoryEntity entity = categoryService.findById(id);

        CategoryDto dto = new CategoryDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setParentId(entity.getParent() != null ? entity.getParent().getId() : null);

        model.addAttribute("category", dto);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("mode", "edit");
        return "category/form";
    }

    // 수정 처리
    @PostMapping("/categories/{id}/edit")
    public String updateProcessing(@PathVariable Integer id,
                                   @Valid @ModelAttribute("category") CategoryDto dto,
                                   BindingResult bindingResult,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("mode", "edit");
            return "category/form";
        }

        categoryService.updateCategory(id, dto);
        return "redirect:/categories";
    }

    // ────────────────────────────────── 태그 삭제
    @PostMapping("/tags/{name}/delete")
    public String deleteTag(@PathVariable String name) {
        tagService.deletedTag(name);
        return "redirect:/categories";
    }

}
