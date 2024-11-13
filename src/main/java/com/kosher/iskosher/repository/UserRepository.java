package com.kosher.iskosher.repository;

import com.kosher.iskosher.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.usersBusinesses ub LEFT JOIN FETCH ub.business")
    List<User> findAllUsersWithBusinesses();
}