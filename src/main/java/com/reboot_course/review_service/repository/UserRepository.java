package com.reboot_course.review_service.repository;

import com.reboot_course.review_service.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
