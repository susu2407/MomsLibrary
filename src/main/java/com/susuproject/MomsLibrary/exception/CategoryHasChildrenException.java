package com.susuproject.MomsLibrary.exception;

// 하위 카테고리가 있는 상위 카테고리를 삭제 시도할 때 발생하는 예외
public class CategoryHasChildrenException extends RuntimeException {
    
    public CategoryHasChildrenException(Integer id) {
        super("하위 카테고리가 있어 삭제할 수 없습니다. (id: "+ id + ")");
    }
}
