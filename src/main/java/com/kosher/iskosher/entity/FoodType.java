package com.kosher.iskosher.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kosher.iskosher.common.interfaces.NamedEntity;
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
@Table(name = "food_types")
public class FoodType implements NamedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;
    @OneToMany(mappedBy = "foodType", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<FoodTypeBusiness> foodTypeBusinesses = new LinkedHashSet<>();

}