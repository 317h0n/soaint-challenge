package com.soaint.shoppingcar.models.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.soaint.shoppingcar.common.Util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "ventas_items")
@ApiModel("Model Venta - Item")
@Getter 
@Setter
public class VentaItem implements Serializable {
	
	@Getter(value = AccessLevel.NONE)
	@Setter(value = AccessLevel.NONE)
	private static final long serialVersionUID = -9097792314337526343L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(value = "Id of the model", required = true)
	private Long id;

	@ApiModelProperty(value = "Number of productos", required = true)
	private Integer cantidad;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="item_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@ApiModelProperty(value = "Producto of the sale", required = true)
	private Producto producto;
	
	public Double calcularImporte() {
		return Util.round(cantidad.doubleValue() * producto.getPrice(), 2);
	}
}
