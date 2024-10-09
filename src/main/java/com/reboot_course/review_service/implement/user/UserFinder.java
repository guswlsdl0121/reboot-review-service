package com.reboot_course.review_service.implement.user;

import com.reboot_course.review_service.entity.User;
import com.reboot_course.review_service.repository.UserRepository;
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
