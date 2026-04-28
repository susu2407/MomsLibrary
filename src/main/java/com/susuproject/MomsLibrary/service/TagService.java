package com.susuproject.MomsLibrary.service;

import com.susuproject.MomsLibrary.model.TagEntity;
import com.susuproject.MomsLibrary.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    // DB 연결 + 생성자
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    //전체 태그 목록 조회
    public List<TagEntity> findAll() {
        return tagRepository.findAll();
    }

    //태그 등록 (중복 체크 포함)
    public TagEntity createdTag(String name) {
//        TagEntity tag = new TagEntity(name);
//        return tagRepository.save(tag);       간단하게 작성 가능.

        return tagRepository.save(new TagEntity(name));
    }
    //태그 수정
    public TagEntity updatedTag(String name) {
        return tagRepository.save(new TagEntity(name));
    }

    //태그 삭제
    public void deletedTag(String name) {
        tagRepository.delete(new TagEntity(name));
    }
}
