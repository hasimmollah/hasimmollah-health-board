
package com.hasim.healthboard.api.model;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.hasim.healthboard.api.validator.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonTypeName("ApplicationRequest")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationRequest {
	@JsonProperty("appName")
	@Size(min = 1, name = "Application Name is mandatory")
	@Valid
	private String name;

	@JsonProperty("appUrl")
	@Size(min = 1, name = "Application URL is mandatory")
	@Valid
	private String url;

	@JsonProperty("appId")
	@Size(min = 1, name = "Application Id is mandatory")
	@Valid
	private String appId;

	@JsonProperty("appLab")
	@Size(min = 1, name = "Application Lab is mandatory")
	@Valid
	private String lab;

	@JsonProperty("appEnvironment")
	@Size(min = 1, name = "Environment is mandatory")
	@Valid
	private String environment;

}
