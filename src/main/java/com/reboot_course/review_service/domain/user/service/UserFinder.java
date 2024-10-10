package com.reboot_course.review_service.domain.user.service;

import com.reboot_course.review_service.domain.user.entity.User;
import com.reboot_course.review_service.domain.user.exception.UserNotFoundException;
import com.reboot_course.review_service.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFinder {
    private final UserRepository userRepository;

    public User fetch(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
