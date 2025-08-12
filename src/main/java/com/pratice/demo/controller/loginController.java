package com.pratice.demo.controller;

import com.pratice.demo.dto.login.LoginRequest;
import com.pratice.demo.model.Member;
import com.pratice.demo.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Slf4j
@Controller
public class loginController {

    private final MemberService memberService;

    @Autowired
    public loginController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping( {"/login","/"})
    public String loginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        log.info("Login form loaded with empty LoginRequest object");
        return "login"; // login.html 템플릿을 렌더링
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginRequest loginRequest,
                       BindingResult bindingResult,
                       HttpSession httpSession,
                       Model model) {

        log.info("Login attempt for userId: {}", loginRequest.getUserId());
        log.info("BindingResult has errors: {}", bindingResult.hasErrors());

        // Validation 오류가 있으면 로그인 폼으로 돌아감
        if (bindingResult.hasErrors()) {
            log.info("Validation errors found: {}", bindingResult.getAllErrors());

            // 각 필드별 오류 메시지를 Model에 직접 추가
            if (bindingResult.hasFieldErrors("userId")) {
                model.addAttribute("userIdError", bindingResult.getFieldError("userId").getDefaultMessage());
            }
            if (bindingResult.hasFieldErrors("password")) {
                model.addAttribute("passwordError", bindingResult.getFieldError("password").getDefaultMessage());
            }

            return "login";
        }

        // 로그인 처리 로직 - 암호화된 비밀번호 검증 적용
        if (memberService.authenticateUser(loginRequest.getUserId(), loginRequest.getPassword())) {
            Optional<Member> memberOptional = memberService.findMemberByUserId(loginRequest.getUserId());
            Member member = memberOptional.get();
            httpSession.setAttribute("loginMember", member);
            log.info("Login successful for userId: {}", loginRequest.getUserId());
            return "redirect:/home";
        } else {
            log.info("Login failed for userId: {}", loginRequest.getUserId());
            model.addAttribute("errorMessage", "아이디 또는 비밀번호가 잘못되었습니다.");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        log.info("User logged out successfully");
        return "redirect:/login"; // 로그인 페이지로 리다이렉트
    }
}
