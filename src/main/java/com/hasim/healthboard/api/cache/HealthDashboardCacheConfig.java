
package com.hasim.healthboard.api.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.hasim.healthboard.api.constant.CommonConstant;
import com.hasim.healthboard.api.model.ApplicationVO;


/**
 * Class to describe cache configuration of reference data
 * 
 * @author hasmolla
 *
 */
@Configuration
@EnableCaching
@EnableScheduling
public class HealthDashboardCacheConfig {

    @Autowired
    private CacheManager cacheManager;

    @Bean(CommonConstant.CACHE_APPLICATION)
    public DataCache<String, ApplicationVO> applicationCache() {
        return createDataCache(CommonConstant.CACHE_APPLICATION);
    }

    private <K, V> DataCache<K, V> createDataCache(String cacheName) {
        DataCache<K, V> dataCache = new DataCacheImpl<>(
            cacheManager.getCache(cacheName));
        return dataCache;
    }
}
