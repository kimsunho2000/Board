package com.pratice.demo.controller;

import com.pratice.demo.dto.content.postCreateRequest;
import com.pratice.demo.dto.content.postUpdateRequest;
import com.pratice.demo.model.Content;
import com.pratice.demo.model.Member;
import com.pratice.demo.service.PostService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class postController {

    private final PostService postService;

    public postController(PostService postService) {
        this.postService = postService;
    }

    // 세션에서 로그인한 사용자 정보를 가져오는 헬퍼 메서드
    private Member getLoginMember(HttpSession session) {
        return (Member) session.getAttribute("loginMember");
    }

    // 로그인 여부를 확인하는 헬퍼 메서드
    private boolean isLoggedIn(HttpSession session) {
        return getLoginMember(session) != null;
    }

    @GetMapping("/posts")
    public String post(Model model) {
        model.addAttribute("postCreateRequest", new postCreateRequest());
        return "posts";
    }

    @PostMapping("/api/posts")
    public String save(@Valid @ModelAttribute postCreateRequest createRequest,
                       BindingResult bindingResult,
                       HttpSession session,
                       Model model) {

        // Validation 오류가 있으면 글 작성 폼으로 돌아감
        if (bindingResult.hasErrors()) {
            return "posts";
        }

        Member loginMember = getLoginMember(session);
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        String userId = loginMember.getUserId();
        postService.saveContent(createRequest, userId);
        return "redirect:/home";
    }

    @PutMapping("api/posts/{contentId}")
    public String update(@PathVariable Long contentId,
                         @Valid @ModelAttribute postUpdateRequest updateRequest,
                         BindingResult bindingResult,
                         HttpSession session,
                         Model model) {

        Member loginMember = getLoginMember(session);
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        // Validation 오류가 있으면 수정 폼으로 돌아감
        if (bindingResult.hasErrors()) {
            model.addAttribute("contentId", contentId);
            return "edit";
        }

        // 게시물 작성자 ID 가져오기
        String postAuthorId = postService.getId(contentId);
        String currentUserId = loginMember.getUserId();
        log.info(postAuthorId, currentUserId);
        if (!postAuthorId.equals(currentUserId)) {
            return "redirect:/home"; // 권한이 없으면 홈으로 리다이렉트
        }

        postService.updateContent(updateRequest, contentId);
        return "redirect:/home"; // 수정 완료 후 홈으로 리다이렉트
    }

    @GetMapping("api/posts/{contentId}")
    public String read(@PathVariable Long contentId, Model model, HttpSession session) {
        Content content = postService.readContent(null, contentId);
        model.addAttribute("content", content);

        // 현재 로그인한 사용자 정보 추가
        Member loginMember = getLoginMember(session);
        if (loginMember != null) {
            model.addAttribute("currentUserId", loginMember.getUserId());
        }

        return "content";
    }

    @DeleteMapping("api/posts/{contentId}")
    public ResponseEntity<String> delete(@PathVariable Long contentId, HttpSession session) {
        Member loginMember = getLoginMember(session);
        if (!isLoggedIn(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        // 게시물 작성자 ID 가져오기
        String postAuthorId = postService.getId(contentId);
        String currentUserId = loginMember.getUserId();

        // 작성자와 현재 사용자가 다르면 403 Forbidden 반환
        if (!postAuthorId.equals(currentUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
        }

        postService.deleteContent(contentId);

        log.info("Content deletion completed - contentId: {}, userId: {}", contentId, currentUserId);
        return ResponseEntity.ok("삭제되었습니다.");
    }

    @GetMapping("/api/posts/{contentId}/edit")
    public String editForm(@PathVariable Long contentId, Model model, HttpSession session) {
        Member loginMember = getLoginMember(session);
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        // 게시물 조회
        Content content = postService.readContent(null, contentId);

        // 작성자 권한 확인
        if (!content.getUserId().equals(loginMember.getUserId())) {
            return "redirect:/home"; // 권한이 없으면 홈으로 리다이렉트
        }

        // postUpdateRequest 객체에 기존 데이터 설정
        postUpdateRequest updateRequest = new postUpdateRequest();
        updateRequest.setTitle(content.getTitle());
        updateRequest.setContent(content.getContent());

        model.addAttribute("postUpdateRequest", updateRequest);
        model.addAttribute("contentId", contentId);
        return "edit";
    }
}
