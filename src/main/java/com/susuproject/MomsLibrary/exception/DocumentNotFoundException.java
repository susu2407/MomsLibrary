package com.susuproject.MomsLibrary.exception;

// 존재하지 않는 자료(Document)를 조회/수정/삭제하려 할 때 발생하는 예외
public class DocumentNotFoundException extends RuntimeException {

    public DocumentNotFoundException(Integer id) {
        // RuntimeException의 메시지 생성자를 그대로 활용
        super("존재하지 않는 자료입니다. (id: "  + id + ")");
    }
    
}
