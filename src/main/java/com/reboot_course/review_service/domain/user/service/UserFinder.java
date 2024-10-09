package com.reboot_course.review_service.domain.user.service;

import com.reboot_course.review_service.domain.user.entity.User;
import com.reboot_course.review_service.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFinder {
    private final UserRepository userRepository;

    public User fetch(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("사용자(id : %d)를 찾을 수 없습니다.", userId)));
    }
}
