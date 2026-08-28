package com.susuproject.MomsLibrary.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoryDto {

    private Integer id;         // 카테고리 id

    @NotBlank(message = "카테고리명은 필수 항목입니다.")
    private String name;        // 카테고리 이름

    private Integer parentId;

    // 기본 생성자
    public CategoryDto() {}

    // getter/setter
    public Integer getId() { return id;}
    public void setId(Integer id) { this.id = id;}

    public String getName() { return name;}
    public void setName(String name) { this.name = name;}

    public Integer getParentId() { return parentId;}
    public void setParentId(Integer parentId) { this.parentId = parentId;}
}
