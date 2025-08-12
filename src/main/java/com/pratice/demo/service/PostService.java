package com.pratice.demo.service;

import com.pratice.demo.dto.content.postCreateRequest;
import com.pratice.demo.dto.content.postReadRequest;
import com.pratice.demo.dto.content.postUpdateRequest;
import com.pratice.demo.model.Content;
import java.util.List;

public interface PostService {

    Content saveContent(postCreateRequest createRequest, String userId);

    Content updateContent(postUpdateRequest updateRequest, long contentId);

    Content readContent(postReadRequest readRequest, long contentId);

    void deleteContent(long contentId);

    String getId(long contentId);

    List<Content> getAllContents();

    int countPostsByUserId(String userId);

}
