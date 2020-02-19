
package com.hasim.healthboard.api.cache;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Data cache interface
 * 
 * @author hmollah
 *
 * @param <K>
 * @param <V>
 */
public interface DataCache<K, V> {

    
    
    /**
     * Get value for input key from cache
     * 
     * @param key
     * @return 
     */
    public V getValue(K key);

    /**
     * Put input key value pair into cache
     * 
     * @param key
     * @param value
     * @return
     */
    public V putValue(K key, V value);

    /**
     * Clear all key value pairs from cache
     * 
     */
    public void clearAll();

    /**
     * Remove key from the cache
     * 
     * @param key
     * @return
     */
    public V removeKey(K key);

    /**
     * Put key value pairs from map into cache
     * 
     * @param Map<K, V> keyValueMap
     */
    public void putValue(Map<K, V> keyValueMap);

    /**
     * Clear cache and put key value pairs from map
     * 
     * @param keyValueMap
     */
    public void refresh(Map<K, V> keyValueMap);
    
    
    /**
     * Returns  all cache elements
     * 
     * @return keyValueMap
     */
    public Set<V> getAllCacheValues();
    
}
