package com.kosher.iskosher.entity;

import com.kosher.iskosher.dto.response.BusinessDetailedResponse;
import com.kosher.iskosher.dto.response.BusinessPreviewResponse;
import com.kosher.iskosher.dto.response.BusinessPreviewTravelResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
                        @ColumnResult(name = "kosher_types", type = String.class),
                        @ColumnResult(name = "business_type", type = String.class),
                        @ColumnResult(name = "business_details", type = String.class),
                        @ColumnResult(name = "location_details", type = String.class),
                        @ColumnResult(name = "business_rating", type = Short.class),
                        @ColumnResult(name = "kosher_supervisors", type = String.class),
                        @ColumnResult(name = "kosher_certificates", type = String.class)
                }
        )
)
@NamedNativeQuery(
        name = "Business.getBusinessDetails",
        query = "SELECT * FROM get_business_details(:businessId)",
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
                        @ColumnResult(name = "kosher_types", type = String.class),
                        @ColumnResult(name = "business_type", type = String.class)
                }
        )
)
@NamedNativeQuery(
        name = "Business.getAllBusinesses",
        query = "SELECT * FROM get_all_businesses(:limitParam, :offsetParam)",
        resultSetMapping = "BusinessPreviewResponseMapping"
)
@SqlResultSetMapping(
        name = "BusinessPreviewTravelResponseMapping",
        classes = @ConstructorResult(
                targetClass = BusinessPreviewTravelResponse.class,
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
                        @ColumnResult(name = "kosher_types", type = String.class),
                        @ColumnResult(name = "business_type", type = String.class),
                }
        )
)
//Return also calculated_distance if need...
@NamedNativeQuery(
        name = "Business.getNearbyBusinesses",
        query = "SELECT * FROM get_nearby_businesses_with_travel(:lat, :lon, :radius, :earthRadius, :limitParam, :offsetParam)",
        resultSetMapping = "BusinessPreviewTravelResponseMapping"
)

public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "location_id")
    private Location location;

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
    private Set<SupervisorsBusiness> supervisorsVsBusinesses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "business")
    private Set<UsersBusiness> usersVsBusinesses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "business")
    private Set<CertificateBusiness> certificateVsBusinesses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "business")
    private Set<KosherTypeBusiness> kosherTypeVsBusinesses = new LinkedHashSet<>();

}