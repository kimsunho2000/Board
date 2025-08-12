package com.pratice.demo.controller;

import com.pratice.demo.dto.login.MemberSignupRequest;
import com.pratice.demo.model.Member;
import com.pratice.demo.service.MemberService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class signUpController {

    private final MemberService memberService;

    public signUpController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("signup")
    public String signupForm(Model model) {
        model.addAttribute("memberSignupRequest", new MemberSignupRequest());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupRequest(@Valid @ModelAttribute MemberSignupRequest memberSignupRequest,
                               BindingResult bindingResult,
                               Model model) {

        // Validation 오류가 있으면 회원가입 폼으로 돌아감
        if (bindingResult.hasErrors()) {
            // 각 필드별 오류 메시지를 Model에 직접 추가
            if (bindingResult.hasFieldErrors("userId")) {
                model.addAttribute("userIdError", bindingResult.getFieldError("userId").getDefaultMessage());
            }
            if (bindingResult.hasFieldErrors("password")) {
                model.addAttribute("passwordError", bindingResult.getFieldError("password").getDefaultMessage());
            }
            if (bindingResult.hasFieldErrors("name")) {
                model.addAttribute("nameError", bindingResult.getFieldError("name").getDefaultMessage());
            }
            return "signup";
        }

        // 아이디 중복 체크
        if (memberService.findMemberByUserId(memberSignupRequest.getUserId()).isPresent()) {
            model.addAttribute("userIdError", "이미 사용 중인 아이디입니다.");
            return "signup";
        }

        Member member = memberSignupRequest.toMember();
        memberService.saveMember(member);
        log.info("Member registration successful for userId: {}", member.getUserId());
        return "redirect:/login"; // 회원 가입 후 로그인 페이지로 리다이렉트(PRG 패턴 적용)
    }
}
