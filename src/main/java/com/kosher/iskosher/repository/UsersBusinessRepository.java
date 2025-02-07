package com.kosher.iskosher.repository;

import com.kosher.iskosher.entity.UsersBusiness;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface UsersBusinessRepository extends JpaRepository<UsersBusiness, UUID> {
    @Query("SELECT COUNT(ub) > 0 FROM UsersBusiness ub JOIN ub.user u WHERE ub.user.id = :userId AND ub.business.id = :businessId AND u.isManager = true")
    boolean isUserManagingBusiness(@Param("userId") UUID userId, @Param("businessId") UUID businessId);
}
