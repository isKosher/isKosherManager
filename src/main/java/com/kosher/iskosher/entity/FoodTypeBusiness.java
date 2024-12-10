package com.kosher.iskosher.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "food_type_businesses")
public class FoodTypeBusiness {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "business_id", nullable = false)
    @JsonBackReference
    private Business business;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "food_type_id", nullable = false)
    @JsonBackReference
    private FoodType foodType;

    public FoodTypeBusiness(Business business, FoodType foodType) {
        this.business = business;
        this.foodType = foodType;
    }

    public FoodTypeBusiness() {
    }
}