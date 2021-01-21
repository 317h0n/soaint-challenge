package com.soaint.shoppingcar.models.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "clientes")
@ApiModel("Model Cliente")
@Getter 
@Setter
public class Cliente implements Serializable {

	@Getter(value = AccessLevel.NONE)
	@Setter(value = AccessLevel.NONE)
	private static final long serialVersionUID = -2529108618569827764L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(value = "Id of the model", required = true)
	private Long id;

	@ApiModelProperty(value = "Firstname", required = true)
	@Column(length = 50, nullable = false)
	private String firstname;

	@ApiModelProperty(value = "Lastname", required = true)
	@Column(length = 50, nullable = false)
	private String lastname;

	@ApiModelProperty(value = "DNI", required = true)
	@Column(length = 8, nullable = false)
	private String dni;

	@ApiModelProperty(value = "PhoneNumber", required = true)
	@Column(length = 15, nullable = false)
	private String phoneNumber;

	@ApiModelProperty(value = "Email", required = true)
	@Column(length = 100, nullable = false)
	private String email;
}
