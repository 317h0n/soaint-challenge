package com.soaint.shoppingcar.responses;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("Response")
@Getter 
@Setter
public class MyListApiResponse<T> {

	@ApiModelProperty(value = "Confirm if the request has a error", required = true)
	private boolean error;
	
	@ApiModelProperty(value = "Show error of the request", required = true)
	private String message;
	
	@ApiModelProperty(value = "Show HttpStatus code of the request", required = true)
	private String httpStatus;
	
	@ApiModelProperty(value = "Show error code of the request for the developer", required = true)
	private String code;
	
	@ApiModelProperty(value = "Show error of the request for the developer", required = true)
	private String backendMessage;
	
	@ApiModelProperty(value = "Total elements of the request", required = true)
	private long elements;
	
	@ApiModelProperty(value = "Total pages of the request", required = true)
	private int pages;
	
	@ApiModelProperty(value = "Current page of the request", required = true)
	private int page;
	
	@ApiModelProperty(value = "The models corresponding to the request", required = true)
	private List<T> content;

	public MyListApiResponse() {
	}
	
}