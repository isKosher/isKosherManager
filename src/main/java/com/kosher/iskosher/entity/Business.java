package com.kosher.iskosher.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
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
@Table(name = "businesses")
public class Business {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kosher_type_id", nullable = false)
    @JsonBackReference
    private KosherType kosherType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kosher_certificate", nullable = false)
    @JsonBackReference
    private KosherCertificate kosherCertificate;

    @Column(name = "details", length = Integer.MAX_VALUE)
    private String details;

    @Column(name = "rating")
    private Short rating;


    @Column(name = "image_url")
    private String imageUrl;


    @NotNull
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_type", nullable = false)
    @JsonBackReference
    private BusinessType businessType;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

    @OneToMany(mappedBy = "business", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<FoodTypeBusiness> foodTypeVsBusinesses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "business", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<LocationsBusiness> locationsVsBusinesses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "business", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<SupervisorsBusiness> supervisorsVsBusinesses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "business", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<UsersBusiness> usersVsBusinesses = new LinkedHashSet<>();
}