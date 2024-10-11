package com.reboot_course.review_service.domain.member.repository;

import com.reboot_course.review_service.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
