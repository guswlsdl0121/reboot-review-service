package com.reboot_course.review_service.domain.member.repository;

import com.reboot_course.review_service.domain.member.entity.Member;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, Long> {
}
