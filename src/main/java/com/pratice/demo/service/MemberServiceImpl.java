package com.pratice.demo.service;

import com.pratice.demo.model.Member;
import com.pratice.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordService passwordService;

    @Override
    public Member saveMember(Member member) {
        // 비밀번호 암호화 적용
        String encryptedPassword = passwordService.encryptPassword(member.getPassword());
        member.setPassword(encryptedPassword);

        return memberRepository.save(member);
    }

    @Override
    public Optional<Member> findMemberById(Long id) {
        return memberRepository.findById(id);
    }

    @Override
    public Optional<Member> findMemberByUserId(String userId) {
        return memberRepository.findByUserId(userId);
    }

    @Override
    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    @Override
    public boolean existsByUserId(String userId) {
        return memberRepository.existsByUserId(userId);
    }
    public boolean authenticateUser(String userId, String rawPassword) {
        Optional<Member> memberOptional = findMemberByUserId(userId);
        if (memberOptional.isEmpty()) {
            return false;
        }

        Member member = memberOptional.get();
        return passwordService.matches(rawPassword, member.getPassword());
    }
}
