package com.kosher.iskosher.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "business_photos")
public class BusinessPhoto {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "url", nullable = false, length = Integer.MAX_VALUE)
    private String url;

    @OneToMany(mappedBy = "businessPhotos",fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<BusinessPhotosBusiness> businessPhotosVsBusinesses = new LinkedHashSet<>();

}