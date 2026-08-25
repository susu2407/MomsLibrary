package com.susuproject.MomsLibrary.service;

import com.susuproject.MomsLibrary.exception.TagNotFoundException;
import com.susuproject.MomsLibrary.model.TagEntity;
import com.susuproject.MomsLibrary.repository.TagRepository;
import jakarta.transaction.Transactional;
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
    @Transactional
    public TagEntity createdTag(String name) {
        return tagRepository.findByName(name)
                .orElseGet(() -> tagRepository.save(new TagEntity(name)));
    }

    //태그 수정 + 예외처리
    @Transactional
    public TagEntity updatedTag(TagEntity tagEntity) {
        Integer id = tagEntity.getId();
        if (id == null) {
            throw new IllegalArgumentException("수정할 태그의 id가 없습니다.");
        }
        if (!tagRepository.existsById(id)) {
            throw new TagNotFoundException(id);
        }

//      TagEntity tag = new TagEntity(name);
//      return tagRepository.save(tag);       간단하게 작성 가능.

        return tagRepository.save(tagEntity);
    }

    //태그 삭제 + 예외처리
    @Transactional
    public void deletedTag(String name) {
        TagEntity tagEntity = tagRepository.findByName(name).orElseThrow(() -> new TagNotFoundException(name));

        tagRepository.delete(tagEntity);
    }
}
