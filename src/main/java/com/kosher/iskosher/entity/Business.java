package com.kosher.iskosher.entity;

import com.kosher.iskosher.dto.response.BusinessPreviewResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "businesses")
@SqlResultSetMapping(
        name = "BusinessDTOMapping",
        classes = @ConstructorResult(
                targetClass = BusinessPreviewResponse.class,
                columns = {
                        @ColumnResult(name = "business_id", type = UUID.class),
                        @ColumnResult(name = "business_name", type = String.class),
                        @ColumnResult(name = "food_types", type = String.class),
                        @ColumnResult(name = "food_item_types", type = String.class),
                        @ColumnResult(name = "address", type = String.class),
                        @ColumnResult(name = "street_number", type = Integer.class),
                        @ColumnResult(name = "city", type = String.class),
                        @ColumnResult(name = "business_photos", type = String.class),
                        @ColumnResult(name = "kosher_type", type = String.class),
                        @ColumnResult(name = "business_type", type = String.class)
                }
        )
)
@NamedNativeQuery(
        name = "Business.getAllBusinesses",
        query = """
        SELECT 
            result.business_id,
            result.business_name,
            result.food_types,
            result.food_item_types,
            result.address,
            result.street_number,
            result.city,
            result.business_photos,
            result.kosher_type,
            result.business_type
        FROM get_all_businesses() AS result
        """,
        resultSetMapping = "BusinessDTOMapping"
)
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
    private KosherType kosherType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kosher_certificate", nullable = false)
    private KosherCertificate kosherCertificate;

    @Column(name = "details", length = Integer.MAX_VALUE)
    private String details;

    @Column(name = "rating")
    private Short rating;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_type", nullable = false)
    private BusinessType businessType;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

    @Column(name = "business_number", length = Integer.MAX_VALUE)
    private String businessNumber;

    @OneToMany(mappedBy = "businesses")
    private Set<BusinessPhotosBusiness> businessPhotosVsBusinesses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "business")
    private Set<FoodItemTypeBusiness> foodItemTypeVsBusinesses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "business")
    private Set<FoodTypeBusiness> foodTypeVsBusinesses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "business")
    private Set<LocationsBusiness> locationsVsBusinesses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "business")
    private Set<SupervisorsBusiness> supervisorsVsBusinesses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "business")
    private Set<UsersBusiness> usersVsBusinesses = new LinkedHashSet<>();

}