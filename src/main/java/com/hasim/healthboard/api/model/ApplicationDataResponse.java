package com.hasim.healthboard.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDataResponse {
	@JsonProperty("environment")
    private String environment;
	@JsonProperty("applicationDataInformationList")
	private List<ApplicationDataInformation> applicationDataInformationList;
}
