package com.soaint.shoppingcar.controllers;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaint.shoppingcar.models.entity.Producto;
import com.soaint.shoppingcar.responses.MyApiResponse;
import com.soaint.shoppingcar.responses.MyListApiResponse;
import com.soaint.shoppingcar.services.IProductoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@ApiRestController
@Api(value = "Service for the Producto resource")
public class ProductoController {
	
	private static final Logger logger = LogManager.getLogger(ProductoController.class);
	final ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	private IProductoService service;
	
	@ApiOperation(value = "Get productos", notes = "Return all productos" )
	@ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                value = "Results page you want to retrieve (1..N)", defaultValue = "1"),
        @ApiImplicitParam(name = "page_size", dataType = "integer", paramType = "query",
                value = "Number of records per page.", defaultValue = "10"),
        @ApiImplicitParam(name = "order_by", dataType = "string", paramType = "query",
                value = "Sorting criteria in the format property. " +
                        "Default sort order is ascending. " +
                        "For descendig sort order use a - before property", defaultValue = "name")
	})
	@GetMapping(value="/productos", produces = "application/json; charset=UTF-8")
	public ResponseEntity<MyListApiResponse<Producto>> index(
			@RequestParam(name = "page", defaultValue = "1", required = false) int page
			, @RequestParam(name = "page_size", defaultValue = "10", required = false) int pageSize
			, @RequestParam(name = "order_by", defaultValue = "name", required = false) String orderBy){
		MyListApiResponse<Producto> response;
		try {
			page = page < 1 ? 1 : page;
			pageSize = pageSize < 1 ? 1 : pageSize;
			response = service.findAll(--page, pageSize, orderBy);			
		} catch (DataAccessException e) {
			response = new MyListApiResponse<Producto>();
			response.setError(true);
			logger.error("error", e);
			response.setMessage("There was an error");
			response.setBackendMessage("Internal error: ".concat(e.getMostSpecificCause().getMessage()));
		}
		if (!response.isError()) {
			response.setCode(String.valueOf(HttpStatus.OK.value()));
			response.setHttpStatus(String.valueOf(HttpStatus.OK.value()));
			logger.info("Code: " + response.getCode() + "\n"
					+ "HttpStatus: " + response.getHttpStatus() + "\n");
			return new ResponseEntity<>(response, HttpStatus.OK); 
		} else {
			response.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
			response.setHttpStatus(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
			logger.error("Code: " + response.getCode() + "\n"
					+ "HttpStatus: " + response.getHttpStatus() + "\n"
					+ "BackendMessage: " + response.getBackendMessage() + "\n");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}	
	
	@ApiOperation(value = "Get a producto with that id", notes = "Return a producto" )
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", dataType = "integer", paramType = "path",
                value = "ID of the producto", required = true)
	})	
	@GetMapping(value = "/productos/{id}", produces = "application/json; charset=UTF-8")
	public ResponseEntity<MyApiResponse<Producto>> show(
			@PathVariable Long id) {
		MyApiResponse<Producto> response = new MyApiResponse<Producto>();
		try {
			response = service.findById(id);
		} catch (DataAccessException e) {
			response.setError(true);
			response.setMessage("There was an error");
			response.setBackendMessage("Internal error: ".concat(e.getMostSpecificCause().getMessage()));
		}
		if (!response.isError()) {
			response.setCode(String.valueOf(HttpStatus.OK.value()));
			response.setHttpStatus(String.valueOf(HttpStatus.OK.value()));
			logger.info("Code: " + response.getCode() + "\n"
					+ "HttpStatus: " + response.getHttpStatus() + "\n");
			return new ResponseEntity<>(response, HttpStatus.OK); 
		} else {
			response.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
			response.setHttpStatus(String.valueOf(HttpStatus.NOT_FOUND.value()));
			logger.error("Code: " + response.getCode() + "\n"
					+ "HttpStatus: " + response.getHttpStatus() + "\n"
					+ "BackendMessage: " + response.getBackendMessage() + "\n");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); 
		}
	}
	
	@ApiOperation(value = "Create a producto", notes = "Return created producto")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "producto", dataType = "Producto.class", value = "Producto to create", required = true),
	})
	@PostMapping(value = "/productos", produces = "application/json; charset=UTF-8")
	public ResponseEntity<MyApiResponse<Producto>> create(@RequestBody Producto producto) {
		MyApiResponse<Producto> response;
		Map<String, String> errors = validateProducto(producto);
		if (!errors.isEmpty()) {
			response = new MyApiResponse<Producto>();
			response.setError(true);
			Map.Entry<String, String> entry = errors.entrySet().iterator().next();
			String key = entry.getKey();
			String value = entry.getValue();
			response.setMessage("There is a problem with " + key + ", " + value);
		} else {
			response = service.save(producto);			
		}
		if (!response.isError()) {
			response.setCode(String.valueOf(HttpStatus.OK.value()));
			response.setHttpStatus(String.valueOf(HttpStatus.OK.value()));
			logger.info("Code: " + response.getCode() + "\n"
					+ "HttpStatus: " + response.getHttpStatus() + "\n");
			return new ResponseEntity<>(response, HttpStatus.OK); 
		} else {
			response.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
			response.setHttpStatus(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
			logger.error("Code: " + response.getCode() + "\n"
					+ "HttpStatus: " + response.getHttpStatus() + "\n"
					+ "BackendMessage: " + response.getBackendMessage() + "\n");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	@ApiOperation(value = "Update a producto", notes = "Return updated producto")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "producto", dataType = "Producto.class", value = "Producto to update", required = true),
		@ApiImplicitParam(name = "id", dataType = "integer", paramType = "path", value = "ID of the producto", required = true)
	})
	@PutMapping(value = "/productos/{id}", produces = "application/json; charset=UTF-8")
	public ResponseEntity<MyApiResponse<Producto>> update(@RequestBody Producto producto, @PathVariable Long id) {
		MyApiResponse<Producto> response;
		Map<String, String> errors = validateProducto(producto);
		if (!errors.isEmpty()) {
			response = new MyApiResponse<Producto>();
			response.setError(true);
			Map.Entry<String, String> entry = errors.entrySet().iterator().next();
			String key = entry.getKey();
			String value = entry.getValue();
			response.setMessage("There is a problem with " + key + ", " + value);
		} else {
			response = service.findById(id);
			if (response.getContent() != null) {
				Producto productoDB = response.getContent();
				productoDB.setName(producto.getName());
				productoDB.setPrice(producto.getPrice());
				response = service.save(productoDB);
			}
		}
		if (!response.isError()) {
			response.setCode(String.valueOf(HttpStatus.OK.value()));
			response.setHttpStatus(String.valueOf(HttpStatus.OK.value()));
			logger.info("Code: " + response.getCode() + "\n"
					+ "HttpStatus: " + response.getHttpStatus() + "\n");
			return new ResponseEntity<>(response, HttpStatus.OK); 
		} else {
			response.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
			response.setHttpStatus(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
			logger.error("Code: " + response.getCode() + "\n"
					+ "HttpStatus: " + response.getHttpStatus() + "\n"
					+ "BackendMessage: " + response.getBackendMessage() + "\n");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	@ApiOperation(value = "Update properties of a producto", notes = "Return updated producto")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "producto", dataType = "Object", value = "Properties to update", required = true),
			@ApiImplicitParam(name = "id", dataType = "integer", paramType = "path", value = "ID of the producto", required = true)
	})
	@PatchMapping(value = "/productos/{id}", produces = "application/json; charset=UTF-8")
	public ResponseEntity<MyApiResponse<Producto>> updatePartial(@RequestBody Map<String, Object> producto
			, @PathVariable Long id) {
		MyApiResponse<Producto> response;
		response = service.findById(id);
		if (response.getContent() != null) {
			Producto productoDB = response.getContent();
			producto.forEach((change, value) -> {
				switch (change) {
				case "name":
					productoDB.setName((String) value);
					break;
				case "price":
					productoDB.setPrice((Double) value);
					break;
				}
			});
			response = service.save(productoDB);
		}
		if (!response.isError()) {
			response.setCode(String.valueOf(HttpStatus.OK.value()));
			response.setHttpStatus(String.valueOf(HttpStatus.OK.value()));
			logger.info("Code: " + response.getCode() + "\n"
					+ "HttpStatus: " + response.getHttpStatus() + "\n");
			return new ResponseEntity<>(response, HttpStatus.OK); 
		} else {
			response.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
			response.setHttpStatus(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
			logger.error("Code: " + response.getCode() + "\n"
					+ "HttpStatus: " + response.getHttpStatus() + "\n"
					+ "BackendMessage: " + response.getBackendMessage() + "\n");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	@ApiOperation(value = "Delete a producto", notes = "Nothing to return")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", dataType = "integer", paramType = "path",
        		value = "ID of the producto", required = true)
	})
	@DeleteMapping(value = "/productos/{id}", produces = "application/json; charset=UTF-8")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		MyApiResponse<Producto> response;
		try {
			response = service.findById(id);
			if (response.getContent() != null) {
				Producto producto = response.getContent();
				service.delete(producto.getId());
				response.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
				response.setHttpStatus(String.valueOf(HttpStatus.NO_CONTENT.value()));
				logger.info("Code: " + response.getCode() + "\n"
						+ "HttpStatus: " + response.getHttpStatus() + "\n");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);					
			} else {
				response.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
				response.setHttpStatus(String.valueOf(HttpStatus.NOT_FOUND.value()));
				logger.error("Code: " + response.getCode() + "\n"
						+ "HttpStatus: " + response.getHttpStatus() + "\n"
						+ "BackendMessage: " + response.getBackendMessage() + "\n");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		} catch (DataAccessException e) {
			response = new MyApiResponse<Producto>();
			response.setContent(null);
			response.setError(true);
			response.setMessage("There was an error");
			response.setBackendMessage("Internal error: ".concat(e.getMostSpecificCause().getMessage()));
			response.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
			response.setHttpStatus(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
			logger.error("Code: " + response.getCode() + "\n"
					+ "HttpStatus: " + response.getHttpStatus() + "\n"
					+ "BackendMessage: " + response.getBackendMessage() + "\n");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private Map<String, String> validateProducto(Producto producto) {
		Map<String, String> errors = new HashMap<>();
		if (producto.getName() == null || producto.getName().isBlank()) {
			errors.put("name", "Name cannot be empty");
		}
		if (producto.getPrice() == null || producto.getPrice() <= 0) {
			errors.put("price", "Price needs to be greater than 0");
		}	
		return errors;
	}
}
