package com.susuproject.MomsLibrary.service;

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
        if (tagEntity.getId() == null) {
            throw new IllegalArgumentException("존재하지 않는 태그입니다. 수정이 불가합니다.");
        }

//      TagEntity tag = new TagEntity(name);
//      return tagRepository.save(tag);       간단하게 작성 가능.

        return tagRepository.save(tagEntity);
    }

    //태그 삭제 + 예외처리
    @Transactional
    public void deletedTag(String name) {
        tagRepository.findByName(name).ifPresent(tagRepository::delete);
    }
}
