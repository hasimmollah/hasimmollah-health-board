
package com.hasim.healthboard.api;

import java.util.Arrays;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import com.hasim.healthboard.api.constant.CommonConstant;

@SpringBootApplication
@EnableCircuitBreaker
@EnableAsync
@ComponentScan(basePackages = { "com.hasim.healthboard.api.*" })
public class HealthBoardApiApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		if (args.length > 0 && !args[0].contains(CommonConstant.SPRING_CONFIG_COMMAND ) ) {
			System.setProperty(CommonConstant.DATA_PATH_INPUT, args[0]);
		}

		SpringApplication.run(HealthBoardApiApplication.class, args);
	}

	@Override
	public SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(HealthBoardApiApplication.class);
	}

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.TEXT_XML,
				MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM));
		restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
		return restTemplate;
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	@Primary
	public DataSource dataSource() {
		StringBuilder url = new StringBuilder();
		url.append(CommonConstant.H2_DB_URL_PART1);
		String existingPath = System.getProperty(CommonConstant.DATA_PATH_INPUT);
		existingPath = StringUtils.isNotBlank(existingPath) ? existingPath : CommonConstant.DATA_PATH_DEFAULT;
		url.append(existingPath);
		url.append(existingPath.endsWith(CommonConstant.SLASH) ? CommonConstant.EMPTY : CommonConstant.SLASH);
		url.append(CommonConstant.H2_DB_URL_PART2);

		return DataSourceBuilder.create().username(CommonConstant.H2_DB_USER).password(CommonConstant.H2_DB_PASSWORD)
				.url(url.toString()).driverClassName(CommonConstant.H2_DRIVER).build();
	}
}
