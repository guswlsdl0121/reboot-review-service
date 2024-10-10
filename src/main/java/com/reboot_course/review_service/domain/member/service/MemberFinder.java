package com.reboot_course.review_service.domain.member.service;

import com.reboot_course.review_service.domain.member.entity.Member;
import com.reboot_course.review_service.domain.member.exception.MemberNotFoundException;
import com.reboot_course.review_service.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberFinder {
    private final MemberRepository memberRepository;

    public Member fetchOne(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
    }
}
