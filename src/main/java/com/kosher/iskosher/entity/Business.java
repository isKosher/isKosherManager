package com.kosher.iskosher.entity;

import com.kosher.iskosher.dto.response.BusinessDetailedResponse;
import com.kosher.iskosher.dto.response.BusinessPreviewResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "businesses")
@SqlResultSetMapping(
        name = "BusinessDetailedResponseMapping",
        classes = @ConstructorResult(
                targetClass = BusinessDetailedResponse.class,
                columns = {
                        @ColumnResult(name = "business_id", type = UUID.class),
                        @ColumnResult(name = "business_name", type = String.class),
                        @ColumnResult(name = "food_types", type = String.class),
                        @ColumnResult(name = "food_item_types", type = String.class),
                        @ColumnResult(name = "address", type = String.class),
                        @ColumnResult(name = "street_number", type = Integer.class),
                        @ColumnResult(name = "city", type = String.class),
                        @ColumnResult(name = "longitude", type = Double.class),
                        @ColumnResult(name = "latitude", type = Double.class),
                        @ColumnResult(name = "business_photos", type = String.class),
                        @ColumnResult(name = "kosher_type", type = String.class),
                        @ColumnResult(name = "business_type", type = String.class),
                        @ColumnResult(name = "business_details", type = String.class),
                        @ColumnResult(name = "location_details", type = String.class),
                        @ColumnResult(name = "business_rating", type = Short.class),
                        @ColumnResult(name = "supervisor_name", type = String.class),
                        @ColumnResult(name = "supervisor_contact", type = String.class),
                        @ColumnResult(name = "supervisor_authority", type = String.class),
                        @ColumnResult(name = "business_certificate", type = String.class),
                        @ColumnResult(name = "expiration_date", type = LocalDate.class)
                }
        )
)
@NamedNativeQuery(
        name = "Business.getBusinessDetails",
        query = """
                SELECT 
                    b.business_id,
                    b.business_name,
                    b.food_types,
                    b.food_item_types,
                    b.address,
                    b.street_number,
                    b.city,
                    b.longitude,
                    b.latitude,
                    b.business_photos,
                    b.kosher_type,
                    b.business_type,
                    b.business_details,
                    b.location_details,
                    b.business_rating,
                    b.supervisor_name,
                    b.supervisor_contact,
                    b.supervisor_authority,
                    b.business_certificate,
                    b.expiration_date
                FROM get_business_details(:businessId) AS b
                """,
        resultSetMapping = "BusinessDetailedResponseMapping"
)
@SqlResultSetMapping(
        name = "BusinessPreviewResponseMapping",
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
                     FROM get_all_businesses(:limitParam, :offsetParam) AS result
                """,
        resultSetMapping = "BusinessPreviewResponseMapping"
)

public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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