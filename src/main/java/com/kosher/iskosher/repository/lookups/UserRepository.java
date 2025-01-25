package com.kosher.iskosher.repository.lookups;

import com.kosher.iskosher.common.lookup.LookupRepository;
import com.kosher.iskosher.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends LookupRepository<User> {
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.usersBusinesses ub LEFT JOIN FETCH ub.business")
    List<User> findAllUsersWithBusinesses();
    Optional<User> findByGoogleId(String googleId);
}