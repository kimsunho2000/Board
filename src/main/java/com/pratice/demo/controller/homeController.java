package com.pratice.demo.controller;

import com.pratice.demo.model.Content;
import com.pratice.demo.model.Member;
import com.pratice.demo.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class homeController {

    private final PostService postService;

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("loginMember");
        if (member == null) {
            return "redirect:/login";
        }
        String userId = (member != null) ? member.getUserId() : null;
        model.addAttribute("userId", userId);

        List<Content> posts = postService.getAllContents();
        model.addAttribute("posts", posts);
        return "home";
    }
}
