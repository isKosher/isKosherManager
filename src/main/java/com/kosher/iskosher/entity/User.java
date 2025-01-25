package com.kosher.iskosher.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kosher.iskosher.common.interfaces.NamedEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User implements NamedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "google_id", nullable = false, unique = true)
    private String googleId;

    @NotNull
    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;

    @NotNull
    @Column(name = "email", nullable = false, length = Integer.MAX_VALUE)
    private String email;

    @NotNull
    @Column(name = "is_manager", nullable = false)
    private Boolean isManager = false;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<UsersBusiness> usersBusinesses = new LinkedHashSet<>();

    public User(String googleId, String name, String email, Boolean isManager) {
        this.googleId = googleId;
        this.name = name;
        this.email = email;
        this.isManager = isManager;
    }
}