package com.kosher.iskosher.service.implementation;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.kosher.iskosher.dto.request.BusinessCreateRequest;
import com.kosher.iskosher.dto.request.BusinessFilterCriteria;
import com.kosher.iskosher.dto.request.BusinessUpdateRequest;
import com.kosher.iskosher.dto.response.*;
import com.kosher.iskosher.entity.*;
import com.kosher.iskosher.exception.BusinessCreationException;
import com.kosher.iskosher.exception.EntityNotFoundException;
import com.kosher.iskosher.repository.*;
import com.kosher.iskosher.service.*;
import com.kosher.iskosher.service.lookups.*;
import com.kosher.iskosher.types.LocationDetails;
import com.kosher.iskosher.types.TravelInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.kosher.iskosher.common.constant.AppConstants.*;
import static com.kosher.iskosher.common.utils.ManyToManyUpdateUtil.updateManyToManyRelationship;

@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {

    //region Repository Dependencies
    private final KosherTypeBusinessRepository kosherTypeBusinessRepository;
    private final UserService userService;
    private final BusinessRepository businessRepository;
    private final CityService cityService;
    private final AddressService addressService;
    private final KosherTypeService kosherTypeService;
    private final KosherCertificateService kosherCertificateService;
    private final BusinessTypeService businessTypeService;
    private final FoodTypeService foodTypeService;
    private final FoodItemTypeService foodItemTypeService;
    private final KosherSupervisorService kosherSupervisorService;
    private final LocationService locationService;
    private final PhotoService photoService;
    private final SupervisorsBusinessRepository supervisorsBusinessRepository;
    private final CertificateBusinessRepository certificateBusinessRepository;
    private final FoodTypeBusinessRepository foodTypeBusinessRepository;
    private final FoodItemTypeBusinessRepository foodItemTypeBusinessRepository;
    private final BusinessPhotosBusinessRepository businessPhotosBusinessRepository;
    private final UsersBusinessRepository usersBusinessRepository;
    private final TravelTimeService travelTimeService;
    //endregion

    //region Caching Configurations
    private final LoadingCache<String, KosherType> kosherTypeCache = CacheBuilder.newBuilder()
            .maximumSize(MAXIMUM_SIZE_CACHE)
            .expireAfterWrite(DURATION_CACHE_EXPIRE, TIME_UNIT_CACHE_EXPIRE)
            .build(new CacheLoader<>() {
                @Override
                public KosherType load(String name) {
                    return kosherTypeService.getOrCreateEntity(name);
                }
            });

    private final LoadingCache<String, BusinessType> businessTypeCache = CacheBuilder.newBuilder()
            .maximumSize(MAXIMUM_SIZE_CACHE)
            .expireAfterWrite(DURATION_CACHE_EXPIRE, TIME_UNIT_CACHE_EXPIRE)
            .build(new CacheLoader<>() {
                @Override
                public BusinessType load(String name) {
                    return businessTypeService.getOrCreateEntity(name);
                }
            });

    private final LoadingCache<String, City> cityCache = CacheBuilder.newBuilder()
            .maximumSize(MAXIMUM_SIZE_CACHE)
            .expireAfterWrite(DURATION_CACHE_EXPIRE, TIME_UNIT_CACHE_EXPIRE)
            .build(new CacheLoader<>() {
                @Override
                public City load(String key) {
                    String[] parts = key.split("::");
                    String cityName = parts[0];
                    String regionName = parts[1];
                    return cityService.createOrGetCity(cityName, regionName);
                }
            });

    private final LoadingCache<String, Address> addressCache = CacheBuilder.newBuilder()
            .maximumSize(MAXIMUM_SIZE_CACHE)
            .expireAfterWrite(DURATION_CACHE_EXPIRE, TIME_UNIT_CACHE_EXPIRE)
            .build(new CacheLoader<>() {
                @Override
                public Address load(String name) {
                    return addressService.getOrCreateEntity(name);
                }
            });

    private final LoadingCache<String, FoodType> foodTypeCache = CacheBuilder.newBuilder()
            .maximumSize(MAXIMUM_SIZE_CACHE)
            .expireAfterWrite(DURATION_CACHE_EXPIRE, TIME_UNIT_CACHE_EXPIRE)
            .build(new CacheLoader<>() {
                @Override
                public FoodType load(String name) {
                    return foodTypeService.getOrCreateEntity(name);
                }
            });

    private final LoadingCache<String, FoodItemType> foodItemTypeCache = CacheBuilder.newBuilder()
            .maximumSize(MAXIMUM_SIZE_CACHE)
            .expireAfterWrite(DURATION_CACHE_EXPIRE, TIME_UNIT_CACHE_EXPIRE)
            .build(new CacheLoader<>() {
                @Override
                public FoodItemType load(String name) {
                    return foodItemTypeService.getOrCreateEntity(name);
                }
            });
    //endregion

    @Override
    @Transactional
    public BusinessResponse createBusiness(UUID userId, BusinessCreateRequest dto) {
        try {
            kosherCertificateService.existsByCertificate(dto.kosherCertificate().certificate());

            //region Asynchronous Entity Preparation
            CompletableFuture<City> cityFuture = CompletableFuture.supplyAsync(() ->
                    cityCache.getUnchecked(dto.location().city() + "::" + dto.location().region())
            );
            CompletableFuture<Address> addressFuture =
                    CompletableFuture.supplyAsync(() -> addressCache.getUnchecked(dto.location().address()));
            CompletableFuture<BusinessType> businessTypeFuture =
                    CompletableFuture.supplyAsync(() -> businessTypeCache.getUnchecked(dto.businessTypeName()));
            //endregion

            //region Entity Creation
            City city = cityFuture.get();
            Address address = addressFuture.get();
            BusinessType businessType = businessTypeFuture.get();

            List<BusinessPhoto> photos = photoService.createBusinessPhotos(dto.businessPhotos(),
                    dto.foodItemTypes().get(new Random().nextInt(dto.foodItemTypes().size())));
            Location location = locationService.createLocation(dto.location(), city, address);
            KosherSupervisor supervisor = kosherSupervisorService.createSupervisorOnly(dto.supervisor());
            KosherCertificate certificate = kosherCertificateService.createCertificate(dto.kosherCertificate());
            User user = userService.getUserByIdAndSetManager(userId);
            Business business = createBusinessEntity(dto, businessType, location);
            batchCreateRelationships(business, certificate, user, supervisor, photos, dto.foodTypes(),
                    dto.foodItemTypes(), dto.kosherTypes());
            //endregion

            log.info("Successfully created business: {} with ID: {}", business.getName(), business.getId());

            //region Return Detailed Response
            return BusinessResponse.builder()
                    .businessId(business.getId())
                    .businessName(business.getName())
                    .businessNumber(business.getBusinessNumber())
                    .createdAt(business.getCreatedAt().toInstant())
                    .build();
            //endregion
        } catch (EntityNotFoundException e) {
            log.error("Entity not found during business creation: {}", e.getMessage());
            throw e;
        } catch (BusinessCreationException e) {
            log.error("Business creation exception: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("General error occurred while creating business: {}", dto.businessName(), e);
            throw new BusinessCreationException("Failed to create business: " + dto.businessName(), e);
        }
    }

    private void batchCreateRelationships(Business business, KosherCertificate certificate, User user,
                                          KosherSupervisor supervisor, List<BusinessPhoto> photos,
                                          List<String> foodTypes, List<String> foodItemTypes,
                                          List<String> kosherTypes) {
        //region Relationship Collections
        List<UsersBusiness> usersRelationships = Collections.singletonList(
                new UsersBusiness(business, user)
        );
        List<CertificateBusiness> certificateRelationships = Collections.singletonList(
                new CertificateBusiness(business, certificate)
        );
        List<SupervisorsBusiness> supervisorRelationships = Collections.singletonList(
                new SupervisorsBusiness(business, supervisor)
        );

        List<BusinessPhotosBusiness> photoRelationships = photos.stream()
                .map(photo -> new BusinessPhotosBusiness(business, photo))
                .collect(Collectors.toList());

        List<FoodTypeBusiness> foodTypeRelationships = foodTypes.stream()
                .map(foodTypeCache::getUnchecked)
                .map(foodType -> new FoodTypeBusiness(business, foodType))
                .collect(Collectors.toList());

        List<FoodItemTypeBusiness> foodItemTypeRelationships = foodItemTypes.stream()
                .map(foodItemTypeCache::getUnchecked)
                .map(foodItemType -> new FoodItemTypeBusiness(business, foodItemType))
                .collect(Collectors.toList());
        // TODO: 2/6/2025  Add icon url to kosher types (like location...)
        List<KosherTypeBusiness> kosherTypeRelationships = kosherTypes.stream()
                .map(kosherTypeCache::getUnchecked)
                .map(kosherType -> new KosherTypeBusiness(business, kosherType))
                .collect(Collectors.toList());
        //endregion

        //region Batch Save All Relationships
        usersBusinessRepository.saveAll(usersRelationships);
        kosherTypeBusinessRepository.saveAll(kosherTypeRelationships);
        certificateBusinessRepository.saveAll(certificateRelationships);
        supervisorsBusinessRepository.saveAll(supervisorRelationships);
        businessPhotosBusinessRepository.saveAll(photoRelationships);
        foodTypeBusinessRepository.saveAll(foodTypeRelationships);
        foodItemTypeBusinessRepository.saveAll(foodItemTypeRelationships);
        //endregion
    }

    private Business createBusinessEntity(BusinessCreateRequest dto, BusinessType businessType, Location location) {
        Business business = new Business();
        business.setName(dto.businessName());
        business.setDetails(dto.businessDetails());
        business.setRating(dto.businessRating() != null ? dto.businessRating().shortValue() : 0);
        business.setBusinessType(businessType);
        business.setLocation(location);
        business.setCreatedAt(OffsetDateTime.now());
        business.setUpdatedAt(OffsetDateTime.now());
        business.setBusinessNumber(dto.businessPhone());
        business.setIsActive(true);
        return businessRepository.save(business);
    }

    @Override
    @Transactional
    public BusinessResponse updateBusiness(UUID userId, BusinessUpdateRequest dto) {
        Business business = businessRepository.findById(dto.businessId())
                .orElseThrow(() -> new EntityNotFoundException("Business", "id", dto.businessId()));

        boolean isOwner = business.getUsersVsBusinesses().stream()
                .anyMatch(ub -> ub.getUser().getId().equals(userId));

        if (!isOwner) {
            log.error("You are not authorized to update this business: {}", dto.businessName());
        }

        if (dto.businessName() != null) business.setName(dto.businessName());
        if (dto.businessPhone() != null) business.setBusinessNumber(dto.businessPhone());
        if (dto.businessDetails() != null) business.setDetails(dto.businessDetails());
        if (dto.businessRating() != null) business.setRating(dto.businessRating());

        if (dto.businessType() != null) {
            business.setBusinessType(businessTypeService.getOrCreateEntity(dto.businessType()));
        }

        if (dto.kosherTypes() != null) {
            updateManyToManyRelationship(
                    new HashSet<>(dto.kosherTypes()),
                    business.getKosherTypeVsBusinesses(),
                    itemType -> new KosherTypeBusiness(business, itemType),
                    kosherTypeService::getOrCreateEntities,
                    kosherTypeBusinessRepository,
                    relation -> relation.getKosherType().getName()
            );
        }

        if (dto.foodTypes() != null) {
            updateManyToManyRelationship(
                    new HashSet<>(dto.foodTypes()),
                    business.getFoodTypeVsBusinesses(),
                    foodType -> new FoodTypeBusiness(business, foodType),
                    foodTypeService::getOrCreateEntities,
                    foodTypeBusinessRepository,
                    relation -> relation.getFoodType().getName()
            );
        }

        if (dto.foodItemTypes() != null) {
            updateManyToManyRelationship(
                    new HashSet<>(dto.foodItemTypes()),
                    business.getFoodItemTypeVsBusinesses(),
                    itemType -> new FoodItemTypeBusiness(business, itemType),
                    foodItemTypeService::getOrCreateEntities,
                    foodItemTypeBusinessRepository,
                    relation -> relation.getFoodItemType().getName()
            );
        }

        business.setUpdatedAt(OffsetDateTime.now());
        businessRepository.save(business);

        return BusinessResponse.builder()
                .businessId(business.getId())
                .businessName(business.getName())
                .businessNumber(business.getBusinessNumber())
                .createdAt(business.getUpdatedAt().toInstant()).build();
    }

    @Override
    public Page<BusinessPreviewResponse> getBusinessPreviews(Pageable pageable) {
        int limit = pageable.getPageSize();
        int offset = (int) pageable.getOffset();
        List<BusinessPreviewResponse> businesses = businessRepository.getAllBusinesses(
                limit,
                offset
        );

        long totalElements = countAllBusinesses();
        return new PageImpl<>(businesses, pageable, totalElements);
    }

    @Override
    public BusinessDetailedResponse getBusinessDetails(UUID id) {
        return businessRepository.getBusinessDetails(id)
                .orElseThrow(() -> new EntityNotFoundException("Business", "id", id));
    }

    private long countAllBusinesses() {
        return businessRepository.countByIsActiveTrue();
    }

    @Override
    public void deleteBusiness(UUID id) {
        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Business", "id", id));
        businessRepository.delete(business);
        log.info("Successfully deleted business with ID: {}", id);
    }

    @Override
    public List<BusinessSearchResponse> searchBusinesses(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return Collections.emptyList();
        }

        return businessRepository.searchBusinessesRaw(searchTerm)
                .stream()
                .map(BusinessSearchResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public Page<BusinessPreviewResponse> filterBusinesses(BusinessFilterCriteria criteria, Pageable pageable) {
        return businessRepository.filterBusinesses(criteria, pageable);
    }

    @Override
    public Page<BusinessPreviewTravelResponse> getNearbyBusinesses(double centerLat, double centerLon,
                                                                   double radiusKm, Pageable pageable) {

        List<BusinessPreviewTravelResponse> businessesNearby = businessRepository.getNearbyBusinesses(
                centerLat, centerLon, radiusKm, EARTH_RADIUS_KM,
                pageable.getPageSize(), (int) pageable.getOffset()
        );

        List<BusinessPreviewTravelResponse> businesses = businessesNearby.stream()
                .map(business -> {
                    // Fetch travel info safely, avoiding NullPointerException
                    Optional<TravelInfo> travelInfoOpt = travelTimeService.getTravelInfo(
                            centerLat, centerLon,((LocationDetails) business.getLocation()).getLatitude() ,((LocationDetails) business.getLocation()).getLongitude()
                    );

                    // If no travel data is available, provide default values to prevent issues
                    TravelInfo travelInfo = travelInfoOpt.orElseGet(() -> TravelInfo.builder()
                            .drivingDistance("N/A")
                            .drivingDuration("N/A")
                            .walkingDistance("N/A")
                            .walkingDuration("N/A")
                            .build());

                    return business.setTravelInfo(travelInfo);
                })
                .filter(dto -> {
                    // Extract and clean distance string, then safely convert it to a double
                    String distanceStr = dto.getTravelInfo().drivingDistance().replace(" ק\"מ", "");
                    try {
                        double actualDistance = new BigDecimal(distanceStr).doubleValue();
                        return actualDistance <= radiusKm; // Filter businesses within the specified radius
                    } catch (NumberFormatException e) {
                        log.warn("Failed to parse distance: {}", distanceStr);
                        return false; // Ignore invalid distances
                    }
                })
                .sorted(Comparator.comparingDouble(dto -> {
                    // Sort businesses by driving distance, handling potential parsing issues
                    String distanceStr = dto.getTravelInfo().drivingDistance().replace(" ק\"מ", "");
                    try {
                        return new BigDecimal(distanceStr).doubleValue();
                    } catch (NumberFormatException e) {
                        return Double.MAX_VALUE; // Move problematic entries to the end
                    }
                }))
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());
        // Calculate total elements (approximate) to maintain pagination behavior
        long totalElements = businessesNearby.size() + pageable.getOffset();

        return new PageImpl<>(businesses, pageable, totalElements);
    }

}
