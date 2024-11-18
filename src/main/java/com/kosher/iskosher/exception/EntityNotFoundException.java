package com.kosher.iskosher.exception;

public class EntityNotFoundException extends RuntimeException {
    private final String entityType;
    private final String fieldName;
    private final Object fieldValue;

    public EntityNotFoundException(String entityType, String fieldName, Object fieldValue) {
        super(String.format("%s with %s '%s' not found", entityType, fieldName, fieldValue));
        this.entityType = entityType;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}
