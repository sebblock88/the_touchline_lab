package com.thetouchlinelab.fpl.repository;

import com.thetouchlinelab.fpl.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
