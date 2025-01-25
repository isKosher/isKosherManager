package com.kosher.iskosher.repository;

import com.kosher.iskosher.entity.UsersBusiness;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsersBusinessRepository extends JpaRepository<UsersBusiness, UUID> {
}