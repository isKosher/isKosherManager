package com.kosher.iskosher.service.implementation;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.kosher.iskosher.dto.request.BusinessCreateRequest;
import com.kosher.iskosher.dto.response.BusinessCreateResponse;
import com.kosher.iskosher.dto.response.BusinessDetailedResponse;
import com.kosher.iskosher.dto.response.BusinessPreviewResponse;
import com.kosher.iskosher.entity.*;
import com.kosher.iskosher.exception.BusinessCreationException;
import com.kosher.iskosher.exception.EntityNotFoundException;
import com.kosher.iskosher.repository.*;
import com.kosher.iskosher.service.*;
import com.kosher.iskosher.service.lookups.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.kosher.iskosher.common.constant.AppConstants.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {

    //region Repository Dependencies
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
    private final LocationsBusinessRepository locationsBusinessRepository;
    private final FoodTypeBusinessRepository foodTypeBusinessRepository;
    private final FoodItemTypeBusinessRepository foodItemTypeBusinessRepository;
    private final BusinessPhotosBusinessRepository businessPhotosBusinessRepository;
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
    public BusinessCreateResponse createBusiness(BusinessCreateRequest dto) {
        try {
            //region Asynchronous Entity Preparation
            CompletableFuture<City> cityFuture = CompletableFuture.supplyAsync(() ->
                    cityCache.getUnchecked(dto.cityName() + "::" + dto.region())
            );
            CompletableFuture<Address> addressFuture =
                    CompletableFuture.supplyAsync(() -> addressCache.getUnchecked(dto.addressName()));
            CompletableFuture<BusinessType> businessTypeFuture =
                    CompletableFuture.supplyAsync(() -> businessTypeCache.getUnchecked(dto.businessTypeName()));
            CompletableFuture<KosherType> kosherTypeFuture =
                    CompletableFuture.supplyAsync(() -> kosherTypeCache.getUnchecked(dto.kosherTypeName()));
            //endregion

            //region Entity Creation
            City city = cityFuture.get();
            Address address = addressFuture.get();
            BusinessType businessType = businessTypeFuture.get();
            KosherType kosherType = kosherTypeFuture.get();
            //TODO add business type to list food item type
            List<BusinessPhoto> photos = photoService.createBusinessPhotos(dto.businessPhotos(),
                    dto.foodItemTypes().get(new Random().nextInt(dto.foodItemTypes().size())));
            Location location = locationService.createLocation(dto, city, address);
            KosherSupervisor supervisor = kosherSupervisorService.createSupervisor(dto.supervisor());
            KosherCertificate certificate = kosherCertificateService.createCertificate(dto.kosherCertificate());

            Business business = createBusinessEntity(dto, kosherType, businessType, certificate);
            batchCreateRelationships(business, location, supervisor, photos, dto.foodTypes(), dto.foodItemTypes());
            //endregion

            log.info("Successfully created business: {} with ID: {}", business.getName(), business.getId());

            //region Return Detailed Response
            return BusinessCreateResponse.builder()
                    .businessId(business.getId())
                    .businessName(business.getName())
                    .businessNumber(business.getBusinessNumber())
                    .createdAt(business.getCreatedAt())
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

    private void batchCreateRelationships(Business business, Location location,
                                          KosherSupervisor supervisor, List<BusinessPhoto> photos,
                                          List<String> foodTypes, List<String> foodItemTypes) {
        //region Relationship Collections
        List<LocationsBusiness> locationRelationships = Collections.singletonList(
                new LocationsBusiness(business, location)
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
        //endregion

        //region Batch Save All Relationships
        locationsBusinessRepository.saveAll(locationRelationships);
        supervisorsBusinessRepository.saveAll(supervisorRelationships);
        businessPhotosBusinessRepository.saveAll(photoRelationships);
        foodTypeBusinessRepository.saveAll(foodTypeRelationships);
        foodItemTypeBusinessRepository.saveAll(foodItemTypeRelationships);
        //endregion
    }

    private Business createBusinessEntity(BusinessCreateRequest dto, KosherType kosherType,
                                          BusinessType businessType, KosherCertificate certificate) {
        Business business = new Business();
        business.setName(dto.businessName());
        business.setDetails(dto.businessDetails());
        business.setRating(dto.businessRating().shortValue());
        business.setKosherType(kosherType);
        business.setBusinessType(businessType);
        business.setKosherCertificate(certificate);
        business.setCreatedAt(OffsetDateTime.now());
        business.setUpdatedAt(OffsetDateTime.now());
        business.setBusinessNumber(dto.businessPhone());
        business.setIsActive(true);
        return businessRepository.save(business);
    }

    @Override
    public List<BusinessPreviewResponse> getBusinessPreviews() {
        return businessRepository.getAllBusinesses();
    }

    @Override
    public BusinessDetailedResponse getBusinessDetails(UUID id) {
        return businessRepository.getBusinessDetails(id)
                .orElseThrow(() -> new EntityNotFoundException("Business", "id", id));
    }

    @Override
    public void deleteBusiness(UUID id) {
        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Business", "id", id));
        businessRepository.delete(business);
        log.info("Successfully deleted business with ID: {}", id);
    }

}
