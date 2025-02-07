package com.kosher.iskosher.common.utils;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility class for updating many-to-many relationships efficiently.
 */
public class ManyToManyUpdateUtil {

    /**
     * Updates a many-to-many relationship in an optimized way.
     *
     * <p>It performs the following steps:
     * - Determines what to remove from the database.
     * - Determines what to insert.
     * - Deletes only removed entities and inserts only new ones.
     *
     * @param newValues           The new values to update (e.g., ["Vegan", "Kosher"]).
     * @param existingRelations   The current relationships in the database.
     * @param relationMapper      Function mapping from entity to relationship entity.
     * @param getOrCreateEntities Function to fetch existing or create new entities.
     * @param relationRepository  Repository to delete old relationships and insert new ones.
     * @param getNameFunction     Function to extract the name from the related entity.
     * @param <Entity>            The type of the related entity (e.g., FoodType, FoodItemType).
     * @param <Relation>          The type of the relationship entity (e.g., FoodTypeBusiness).
     * @param <R>                 The repository type handling relationship entities.
     */
    public static <Entity, Relation, R extends JpaRepository<Relation, UUID>>
    void updateManyToManyRelationship(
            Set<String> newValues,
            Set<Relation> existingRelations,
            Function<Entity, Relation> relationMapper,
            Function<List<String>, List<Entity>> getOrCreateEntities,
            R relationRepository,
            Function<Relation, String> getNameFunction) {

        // Extract current entity names from the existing relationships
        Set<String> existingValues = existingRelations.stream()
                .map(getNameFunction)
                .collect(Collectors.toSet());

        // Determine which values to remove and which to add
        Set<String> valuesToRemove = new HashSet<>(existingValues);
        valuesToRemove.removeAll(newValues);

        Set<String> valuesToAdd = new HashSet<>(newValues);
        valuesToAdd.removeAll(existingValues);

        // Store removals in a separate list
        List<Relation> relationsToRemove = existingRelations.stream()
                .filter(relation -> valuesToRemove.contains(getNameFunction.apply(relation)))
                .collect(Collectors.toList());

        // Remove relationships that are no longer needed
        if (!relationsToRemove.isEmpty()) {
            relationRepository.deleteAll(relationsToRemove);
            existingRelations.removeAll(relationsToRemove);
        }

        // Add only the new relationships
        if (!valuesToAdd.isEmpty()) {
            List<Entity> newEntities = getOrCreateEntities.apply(new ArrayList<>(valuesToAdd));
            List<Relation> newRelations = newEntities.stream()
                    .map(relationMapper)
                    .collect(Collectors.toList());

            existingRelations.addAll(newRelations);
            relationRepository.saveAll(newRelations);
        }
    }
}
