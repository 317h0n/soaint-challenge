package com.soaint.shoppingcar.responses;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("Response")
@Getter 
@Setter
public class MyApiResponse<T> {

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
	
	@ApiModelProperty(value = "The models corresponding to the request", required = true)
	private T content;

	public MyApiResponse() {
	}
}
