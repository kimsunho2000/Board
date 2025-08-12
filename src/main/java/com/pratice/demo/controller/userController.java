package com.pratice.demo.controller;

import com.pratice.demo.dto.login.MemberDto;
import com.pratice.demo.model.Member;
import com.pratice.demo.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Slf4j
@RequiredArgsConstructor
public class userController {

    private final PostService postService;

    @GetMapping("/userInfo/{userId}")
    public String userInfo(@PathVariable String userId, HttpSession httpSession, Model model) {
        // 세션에서 현재 로그인한 사용자 정보 가져오기
        Member loginMember = (Member) httpSession.getAttribute("loginMember");

        // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        if (loginMember == null) {
            log.warn("Unauthorized access to userInfo - no login session");
            return "redirect:/login";
        }

        // 본인의 정보만 조회할 수 있도록 권한 확인
        if (!loginMember.getUserId().equals(userId)) {
            log.warn("Unauthorized access attempt - userId: {}, loginUserId: {}", userId, loginMember.getUserId());
            return "redirect:/home"; // 권한이 없으면 홈으로 리다이렉트
        }

        // Member 객체를 MemberDto로 변환
        MemberDto memberDto = MemberDto.builder()
                .id(loginMember.getId())
                .userId(loginMember.getUserId())
                .name(loginMember.getName())
                .postCount(postService.countPostsByUserId(userId))
                .createdAt(loginMember.getCreatedAt())
                .build();


        model.addAttribute("memberDto", memberDto);
        return "userInfo";
    }

    @GetMapping("/userInfo")
    public String userInfoCurrent(HttpSession httpSession) {
        Member loginMember = (Member) httpSession.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        return "redirect:/userInfo/" + loginMember.getUserId();
    }
}
