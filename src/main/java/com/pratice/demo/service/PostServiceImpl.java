package com.pratice.demo.service;

import com.pratice.demo.dto.content.postCreateRequest;
import com.pratice.demo.dto.content.postReadRequest;
import com.pratice.demo.dto.content.postUpdateRequest;
import com.pratice.demo.model.Content;
import com.pratice.demo.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    @Transactional
    public Content saveContent(postCreateRequest createRequest, String userId) {
        Content content = Content.builder()
                .userId(userId)
                .title(createRequest.getTitle())
                .content(createRequest.getContent())
                .build();

        log.info("Content saved successfully - userId: {}, title: {}", userId, createRequest.getTitle());

        return postRepository.save(content);
    }

    @Override
    @Transactional
    public Content updateContent(postUpdateRequest updateRequest, long contentId) {
        Content content = postRepository.findById(contentId).orElseThrow(() -> new EntityNotFoundException("Content not found with id: " + contentId));
        content.setTitle(updateRequest.getTitle());
        content.setContent(updateRequest.getContent());
        return content;
    }

    @Override
    @Transactional(readOnly = true)
    public Content readContent(postReadRequest readRequest, long contentId) {
        Content content = postRepository.findById(contentId).orElseThrow(() -> new EntityNotFoundException("Content not found with id: " + contentId));
        return content;
    }


    @Override
    @Transactional
    public void deleteContent(long contentId) {
        Content content = postRepository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Content not found with id: " + contentId));

        log.info("Content deleted - contentId: {}, userId: {}, title: {}",
                contentId, content.getUserId(), content.getTitle());
        postRepository.deleteById(contentId);
    }

    @Override
    public String getId(long contentId) {
        Content content = postRepository.findById(contentId).orElseThrow(() -> new EntityNotFoundException("Content not found with id: " + contentId));
        return content.getUserId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Content> getAllContents() {
        return postRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public int countPostsByUserId(String userId) {
        return postRepository.countByUserId(userId);
    }
}
