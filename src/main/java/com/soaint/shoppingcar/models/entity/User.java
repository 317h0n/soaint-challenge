package com.soaint.shoppingcar.models.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "users")
@ApiModel("Response")
@Getter @Setter
public class User implements Serializable {

	private static final long serialVersionUID = 710155672811572057L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(value = "Id of the model", required = true)
	private Long id;

	@Column(length = 30, unique = true)
	@ApiModelProperty(value = "Username of the user", required = true)
	private String username;

	@Column(length = 60)
	@ApiModelProperty(value = "Password of the user", required = true)
	private String password;

	@ApiModelProperty(value = "Show if the user is enabled", required = true)
	private Boolean enabled;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	@ApiModelProperty(value = "Roles of the user", required = true)
	private List<Role> roles;

}
