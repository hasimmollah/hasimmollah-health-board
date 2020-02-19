
package com.hasim.healthboard.api.constant;

public class CommonConstant {
    public static final String CACHE_APPLICATION = "application-cache";

    public static final String CACHE_APPLICATIONS = "applications-cache";

    
    public static final String CACHE_REFRESH_INTERVAL_APPLICATION =
        "${cache.evict.interval.default:180000}";

    public static final String DATE_FORMAT = "yyyy.MM.dd.HH.mm.ss";

    public static final String APP_DATA_REFRESH_INTERVAL_APPLICATION = "${application.refreshInterval:180000}";
}
