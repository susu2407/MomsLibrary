package com.susuproject.MomsLibrary.exception;

// 존재하지 않는 태그를 조회/수정/삭제하려 할 때 발생하는 예외
// Tag는 id 기반 조회(수정)와 name 기반 조회(삭제)가 모두 있어 생성자를 두 개 둠
public class TagNotFoundException extends RuntimeException {


    public TagNotFoundException(Integer id) {
        super("존재하지 않는 태그입니다. (id: " + id + ")");
    }
 
    public TagNotFoundException(String name) {
        super("존재하지 않는 태그입니다. (name: " + name + ")");
    }

}
