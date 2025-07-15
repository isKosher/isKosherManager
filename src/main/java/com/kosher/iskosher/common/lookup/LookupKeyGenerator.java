package com.kosher.iskosher.common.lookup;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

@Component("lookupKeyGenerator")
public class LookupKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        String entityType = getEntityType(target);
        String name = (String) params[0];
        return entityType + ":" + name;
    }

    private String getEntityType(Object target) {
        ParameterizedType superClass = (ParameterizedType) target.getClass().getGenericSuperclass();
        Class<?> entityClass = (Class<?>) superClass.getActualTypeArguments()[0];
        return entityClass.getSimpleName();
    }
}
