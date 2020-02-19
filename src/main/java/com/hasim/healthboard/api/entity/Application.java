
package com.hasim.healthboard.api.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "application", schema = "healthboard")
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Application {
	@Id
	@SequenceGenerator(name = "seq-gen", sequenceName = "app_seq_gen")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq-gen")
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "app_status")
	private boolean appStatus;

	@Column(name = "last_active")
	private Date lastAlive;

	@Column(name = "down_since")
	private Date downSince;

	@Column(name = "url")
	private String url;

	@Column(name = "app_id", unique = true)
	private String appId;

	@Column(name = "lab")
	private String lab;

	@Column(name = "environment")
	private String environment;

}
