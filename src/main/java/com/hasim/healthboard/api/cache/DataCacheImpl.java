
package com.hasim.healthboard.api.cache;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;


/**
 * Implementation for data caching using spring cache
 * 
 * @author hmollah
 *
 * @param <K>
 * @param <V>
 */
public class DataCacheImpl<K, V> implements DataCache<K, V> {

    private final Cache cache;

    private Set<V> cachesValues;

    public DataCacheImpl(Cache cache) {
        this.cache = cache;
        cachesValues = new HashSet();
    }

    /**
     * (non-Javadoc)
     * 
     */
    @Override
    @SuppressWarnings("unchecked")
    public V getValue(K key) {
        ValueWrapper value = cache.get(key);
        return (value == null ? null : value.get() == null ? null : (V) value.get());
    }

    /**
     * (non-Javadoc)
     * 
     *
     */
    @Override
    public V putValue(K key, V value) {
        cache.put(key, value);
        cachesValues.add(value);
        return value;
    }

    /**
     * (non-Javadoc)
     * 
     */
    @Override
    public void clearAll() {
        cache.clear();
    }

    /**
     * (non-Javadoc)
     * 
     */
    @Override
    public V removeKey(K key) {
        V value = getValue(key);
        cache.evict(key);
        cachesValues.remove(value);
        return value;
    }

    /**
     * (non-Javadoc)
     * 
     */
    @Override
    public void putValue(Map<K, V> keyValueMap) {
        keyValueMap.forEach((key, value) -> putValue(key, value));
    }

    /**
     * (non-Javadoc)
     * 
     */
    @Override
    public void refresh(Map<K, V> keyValueMap) {
        if (keyValueMap != null) {
            cache.clear();
            cachesValues = new HashSet(
                keyValueMap.values());
            keyValueMap.forEach((key, value) -> putValue(key, value));
            System.out.println(":::::: cachesValues ::::" + cachesValues.size());
        }

    }

    @Override
    public Set<V> getAllCacheValues() {
        return cachesValues;
    }

}
