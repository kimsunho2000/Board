package com.pratice.demo.service;

import com.pratice.demo.model.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    Member saveMember(Member member);

    Optional<Member> findMemberById(Long id);

    Optional<Member> findMemberByUserId(String userId);

    List<Member> findAllMembers();

    void deleteMember(Long id);

    boolean existsByUserId(String userId);

    boolean authenticateUser(String userId, String rawPassword);
}
