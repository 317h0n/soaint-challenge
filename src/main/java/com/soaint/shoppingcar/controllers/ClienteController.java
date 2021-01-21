package com.soaint.shoppingcar.controllers;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaint.shoppingcar.models.entity.Cliente;
import com.soaint.shoppingcar.responses.MyApiResponse;
import com.soaint.shoppingcar.responses.MyListApiResponse;
import com.soaint.shoppingcar.services.IClienteService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@ApiRestController
@Api(value = "Service for the Cliente resource")
public class ClienteController {
	
	private static final Logger logger = LogManager.getLogger(ClienteController.class);
	final ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	private IClienteService service;
	
	@ApiOperation(value = "Get clientes", notes = "Return all clientes" )
	@ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                value = "Results page you want to retrieve (1..N)", defaultValue = "1"),
        @ApiImplicitParam(name = "page_size", dataType = "integer", paramType = "query",
                value = "Number of records per page.", defaultValue = "10"),
        @ApiImplicitParam(name = "order_by", dataType = "string", paramType = "query",
                value = "Sorting criteria in the format property. " +
                        "Default sort order is ascending. " +
                        "For descendig sort order use a - before property", defaultValue = "lastname")
	})
	@GetMapping(value="/clientes", produces = "application/json; charset=UTF-8")
	public ResponseEntity<MyListApiResponse<Cliente>> index(
			@RequestParam(name = "page", defaultValue = "1", required = false) int page
			, @RequestParam(name = "page_size", defaultValue = "10", required = false) int pageSize
			, @RequestParam(name = "order_by", defaultValue = "lastname", required = false) String orderBy){
		MyListApiResponse<Cliente> response;
		logger.info("Obteniendo clientes");
		try {
			page = page < 1 ? 1 : page;
			pageSize = pageSize < 1 ? 1 : pageSize;
			response = service.findAll(--page, pageSize, orderBy);			
		} catch (DataAccessException e) {
			response = new MyListApiResponse<Cliente>();
			response.setError(true);
			logger.error("error", e);
			response.setMessage("Internal error: ".concat(e.getMostSpecificCause().getMessage()));
		}
		if (!response.isError()) {
			return new ResponseEntity<>(response, HttpStatus.OK); 
		} else {
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}		
	
	@ApiOperation(value = "Create a cliente", notes = "Return created cliente")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cliente", dataType = "Cliente.class", value = "Cliente to create", required = true),
	})
	@PostMapping(value = "/clientes", produces = "application/json; charset=UTF-8")
	public ResponseEntity<MyApiResponse<Cliente>> create(@RequestBody Cliente cliente) {
		MyApiResponse<Cliente> response;
		Map<String, String> errors = validateCliente(cliente);
		if (!errors.isEmpty()) {
			response = new MyApiResponse<Cliente>();
			response.setError(true);
			Map.Entry<String, String> entry = errors.entrySet().iterator().next();
			String key = entry.getKey();
			String value = entry.getValue();
			response.setMessage("There is a problem with " + key + ", " + value);
		} else {
			response = service.save(cliente);			
		}
		if (!response.isError()) {
			return new ResponseEntity<>(response, HttpStatus.OK); 
		} else {
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}	

	private Map<String, String> validateCliente(Cliente cliente) {
		Map<String, String> errors = new HashMap<>();
		if (cliente.getFirstname() == null || cliente.getFirstname().isBlank()) {
			errors.put("firstname", "Firstname cannot be empty");
		}
		if (cliente.getLastname() == null || cliente.getLastname().isBlank()) {
			errors.put("lastname", "Lastname cannot be empty");
		}	
		if (cliente.getDni() == null || cliente.getDni().isBlank() || cliente.getDni().length() != 8) {
			errors.put("dni", "DNI cannot be empty and it has 8 digits");
		}
		if (cliente.getPhoneNumber() == null || cliente.getPhoneNumber().isBlank()) {
			errors.put("phonenumber", "Phone number cannot be empty");
		}	
		if (cliente.getEmail() == null || cliente.getEmail().isBlank()) {
			errors.put("email", "Email cannot be empty");
		}	
		return errors;
	}
}
