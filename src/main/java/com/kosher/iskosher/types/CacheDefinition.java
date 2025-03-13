package com.kosher.iskosher.types;

import java.time.Duration;

public record CacheDefinition<K, V>(String name, Class<K> keyType, Class<V> valueType, Duration ttl, int size) {}
