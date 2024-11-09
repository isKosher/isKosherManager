package com.kosher.iskosher.repository;

import com.kosher.iskosher.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.usersVsBusinesses ub LEFT JOIN FETCH ub.business")
    List<User> findAllUsersWithBusinesses();
}