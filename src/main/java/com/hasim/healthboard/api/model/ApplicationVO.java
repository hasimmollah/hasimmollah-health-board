
package com.hasim.healthboard.api.model;

import java.util.Date;

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
public class ApplicationVO {
    private String name;

    private boolean appStatus;

    private Date lastAlive;
    private Date downSince;

    private String url;
    private long id;
    private String appId;
    private String environment;
    
    private String lab;

    public boolean equals(Object applicationVO) {
        if (applicationVO == null) return false;
        if (applicationVO == this) return true; 
        if(applicationVO instanceof ApplicationVO) {
            return ((this.appId).equals(((ApplicationVO)applicationVO).appId));
        }
        return false;
    }
    public int hashCode() {
        return this.appId.hashCode();
    }
}
