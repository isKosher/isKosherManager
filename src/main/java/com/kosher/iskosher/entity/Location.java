package com.kosher.iskosher.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "locations")
public class Location {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    @JsonBackReference
    private City city;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id", nullable = false)
    @JsonBackReference
    private Address address;

    @NotNull
    @Column(name = "street_number", nullable = false)
    private Integer streetNumber;

    @NotNull
    @Column(name = "details", nullable = false, length = Integer.MAX_VALUE)
    private String details;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<LocationsBusiness> locationsVsBusinesses = new LinkedHashSet<>();

}