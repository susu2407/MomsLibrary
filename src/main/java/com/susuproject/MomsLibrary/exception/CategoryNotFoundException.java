package com.susuproject.MomsLibrary.exception;

// 존재하지 않는 카테고리를 조회/수정/삭제하려 할 때 발생하는 예외
public class CategoryNotFoundException extends RuntimeException {
    
    public CategoryNotFoundException(Integer id) {
        super("존재하지 않는 카테고리입니다. (id: " + id + ")");
    }
}
