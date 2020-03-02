
package com.hasim.healthboard.api.constant;

public class CommonConstant {
    public static final String CACHE_APPLICATION = "application-cache";

    public static final String CACHE_APPLICATIONS = "applications-cache";

    public static final String SLASH = "/";
    public static final String EMPTY = "";
    public static final String H2_DB_URL_PART1 = "jdbc:h2:file:";
    public static final String H2_DB_URL_PART2 = "health-board_db;DB_CLOSE_ON_EXIT=FALSE;MODE=PostgreSQL;INIT=CREATE SCHEMA IF NOT EXISTS healthboard";

    public static final String CACHE_REFRESH_INTERVAL_APPLICATION =
        "${cache.evict.interval.default:180000}";

    public static final String DATE_FORMAT = "yyyy.MM.dd.HH.mm.ss";

    public static final String APP_DATA_REFRESH_INTERVAL_APPLICATION = "${application.refreshInterval:180000}";

	public static final String DATA_PATH_INPUT = "data_path";

	public static final String DATA_PATH_DEFAULT = "./health-board-data";

	public static final String H2_DB_USER = "sa";

	public static final String H2_DB_PASSWORD = "";

	public static final String H2_DRIVER = "org.h2.Driver";

	public static final String WEB_SOCKET_ENDPOINT = "/health-board-websocket";

	public static final String TOPIC_PATH = "/topic";

	public static final String ASTERIX = "*";
	public static final String SPRING_CONFIG_COMMAND = "spring.config";

	public static final String DEAD_COUNT = "deadCount";

	public static final String LIVE_COUNT = "liveCount";

	public static final String NAME = "name";

	public static final String ENVIRONMENT = "environment";

	public static final String LAB = "lab";

	public static final String TOPIC_APPLICATIONS = "/topic/applications";

	public static final String TOPIC_APPLICATIONS_STAT = "/topic/applicationstat";
}
