
package com.hasim.healthboard.api.model;

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
public class ApplicationData {
    @JsonProperty("name")
    private String name;

    @JsonProperty("appStatus")
    private boolean appStatus;

    @JsonProperty("lastAlive")
    private String lastAlive;
    @JsonProperty("downSince")
    private String downSince;

    @JsonProperty("url")
    private String url;
    @JsonProperty("id")
    private long id;
    @JsonProperty("appId")
    private String appId;
    
    @JsonProperty("lab")
    private String lab;
    
    
    @JsonProperty("environment")
    private String environment;

}
