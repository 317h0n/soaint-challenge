package com.soaint.shoppingcar.models.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.soaint.shoppingcar.common.Util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
//@JsonIdentityInfo(
//		  generator = ObjectIdGenerators.PropertyGenerator.class, 
//		  property = "id")
@Table(name = "ventas")
@ApiModel("Model Venta")
@Getter 
@Setter
public class Venta implements Serializable {
	
	@Getter(value = AccessLevel.NONE)
	@Setter(value = AccessLevel.NONE)
	private static final long serialVersionUID = 1688285570337516302L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(value = "Id of the model", required = true)
	private Long id;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "Date of the sale", required = true)
	private Date fecha;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	@ApiModelProperty(value = "Cliente Model", required = true)
	private Cliente cliente;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "venta_id")
	@ApiModelProperty(value = "Products of the sale", required = true)
	private Set<VentaItem> items;
	
	@PrePersist
	public void prePersist() {
		fecha = new Date();
	}

	public Venta() {
		this.items = new LinkedHashSet<>();
	}
	
	public void addVentaItem(VentaItem item) {
		this.items.add(item);
	}

	public Double getTotal() {
		Double total = 0.0;
		for (VentaItem ventaItem : items) {
			total += ventaItem.calcularImporte();
		}
		return Util.round(total, 2);
	}
}
