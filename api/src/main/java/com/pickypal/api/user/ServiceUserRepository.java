package com.pickypal.api.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceUserRepository extends JpaRepository<ServiceUser, String> {
    Optional<ServiceUser> findByEmail(String email);
}
