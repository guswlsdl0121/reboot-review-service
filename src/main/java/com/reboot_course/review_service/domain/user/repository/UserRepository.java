package com.reboot_course.review_service.domain.user.repository;

import com.reboot_course.review_service.domain.user.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
