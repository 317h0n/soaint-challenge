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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaint.shoppingcar.models.entity.Cliente;
import com.soaint.shoppingcar.models.entity.Producto;
import com.soaint.shoppingcar.models.entity.Venta;
import com.soaint.shoppingcar.models.entity.VentaItem;
import com.soaint.shoppingcar.responses.MyApiResponse;
import com.soaint.shoppingcar.responses.MyListApiResponse;
import com.soaint.shoppingcar.services.IClienteService;
import com.soaint.shoppingcar.services.IProductoService;
import com.soaint.shoppingcar.services.IVentaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@ApiRestController
@Api(value = "Service for the Venta resource")
public class VentaController {
	
	private static final Logger logger = LogManager.getLogger(VentaController.class);
	final ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	private IVentaService service;
	
	@Autowired
	private IClienteService clienteService;
	
	@Autowired
	private IProductoService productoService;
	
	@ApiOperation(value = "Get ventas", notes = "Return all ventas" )
	@ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                value = "Results page you want to retrieve (1..N)", defaultValue = "1"),
        @ApiImplicitParam(name = "page_size", dataType = "integer", paramType = "query",
                value = "Number of records per page.", defaultValue = "10"),
        @ApiImplicitParam(name = "order_by", dataType = "string", paramType = "query",
                value = "Sorting criteria in the format property. " +
                        "Default sort order is ascending. " +
                        "For descendig sort order use a - before property", defaultValue = "fecha")
	})
	@GetMapping(value="/ventas", produces = "application/json; charset=UTF-8")
	public ResponseEntity<MyListApiResponse<Venta>> index(
			@RequestParam(name = "page", defaultValue = "1", required = false) int page
			, @RequestParam(name = "page_size", defaultValue = "10", required = false) int pageSize
			, @RequestParam(name = "order_by", defaultValue = "fecha", required = false) String orderBy){
		MyListApiResponse<Venta> response;
		try {
			page = page < 1 ? 1 : page;
			pageSize = pageSize < 1 ? 1 : pageSize;
			response = service.findAll(--page, pageSize, orderBy);			
		} catch (DataAccessException e) {
			response = new MyListApiResponse<Venta>();
			response.setError(true);
			response.setMessage("There was an error");
			response.setBackendMessage(e.getMostSpecificCause().getMessage());
			response.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
			response.setHttpStatus(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
		}
		if (!response.isError()) {
			response.setCode(String.valueOf(HttpStatus.OK.value()));
			response.setHttpStatus(String.valueOf(HttpStatus.OK.value()));
			logger.info("Code: " + response.getCode() + "\n"
					+ "HttpStatus: " + response.getHttpStatus() + "\n");
			return new ResponseEntity<>(response, HttpStatus.OK); 
		} else {
			logger.error("Code: " + response.getCode() + "\n"
					+ "HttpStatus: " + response.getHttpStatus() + "\n"
					+ "BackendMessage: " + response.getBackendMessage() + "\n");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}	
	
	@ApiOperation(value = "Get a venta with that id", notes = "Return a venta" )
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", dataType = "integer", paramType = "path",
                value = "ID of the producto", required = true)
	})	
	@GetMapping(value = "/ventas/{id}", produces = "application/json; charset=UTF-8")
	public ResponseEntity<MyApiResponse<Venta>> show(
			@PathVariable Long id) {
		MyApiResponse<Venta> response = new MyApiResponse<Venta>();
		try {
			response = service.findById(id);
		} catch (DataAccessException e) {
			response.setError(true);
			response.setMessage("There was an error");
			response.setBackendMessage(e.getMostSpecificCause().getMessage());
			response.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
			response.setHttpStatus(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
		}
		if (!response.isError()) {
			response.setCode(String.valueOf(HttpStatus.OK.value()));
			response.setHttpStatus(String.valueOf(HttpStatus.OK.value()));
			logger.info("Code: " + response.getCode() + "\n"
					+ "HttpStatus: " + response.getHttpStatus() + "\n");
			return new ResponseEntity<>(response, HttpStatus.OK); 
		} else {
			logger.error("Code: " + response.getCode() + "\n"
					+ "HttpStatus: " + response.getHttpStatus() + "\n"
					+ "BackendMessage: " + response.getBackendMessage() + "\n");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); 
		}
	}
	
	@ApiOperation(value = "Create a venta", notes = "Return created venta")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "venta", dataType = "Venta.class", value = "Sale to create", required = true),
	})
	@PostMapping("/ventas")
	public ResponseEntity<MyApiResponse<Venta>> create(@RequestBody Venta venta) {
		MyApiResponse<Venta> response;
		Map<String, String> errors = validateVenta(venta);
		if (!errors.isEmpty()) {
			response = new MyApiResponse<Venta>();
			response.setError(true);
			Map.Entry<String, String> entry = errors.entrySet().iterator().next();
			String key = entry.getKey();
			String value = entry.getValue();
			response.setMessage("There is a problem with " + key + ", " + value);
		} else {
			Cliente e = clienteService.findById(venta.getCliente().getId()).getContent();
			if (e == null) {
				response = new MyApiResponse<Venta>();
				response.setError(true);
				response.setMessage("Cliente with ID " + venta.getCliente().getId() + " not exists");
				response.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
				response.setHttpStatus(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
				logger.error("Code: " + response.getCode() + "\n"
						+ "HttpStatus: " + response.getHttpStatus() + "\n"
						+ "BackendMessage: " + response.getBackendMessage() + "\n");
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			Venta v = new Venta();
			v.setCliente(e);
			for (VentaItem vi : venta.getItems()) {
				Producto i = productoService.findById(vi.getProducto().getId()).getContent();
				if (i == null) {
					response = new MyApiResponse<Venta>();
					response.setError(true);
					response.setMessage("Producto with ID " + vi.getProducto().getId() + " not exists");
					response.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
					response.setHttpStatus(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
					logger.error("Code: " + response.getCode() + "\n"
							+ "HttpStatus: " + response.getHttpStatus() + "\n"
							+ "BackendMessage: " + response.getBackendMessage() + "\n");
					return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}
				VentaItem lineaVenta = new VentaItem();
				lineaVenta.setCantidad(vi.getCantidad());
				lineaVenta.setProducto(i);
				v.addVentaItem(lineaVenta);
			}
			response = service.save(v);			
		}
		if (!response.isError()) {
			response.setCode(String.valueOf(HttpStatus.OK.value()));
			response.setHttpStatus(String.valueOf(HttpStatus.OK.value()));
			logger.info("Code: " + response.getCode() + "\n"
					+ "HttpStatus: " + response.getHttpStatus() + "\n");
			return new ResponseEntity<>(response, HttpStatus.OK); 
		} else {
			logger.error("Code: " + response.getCode() + "\n"
					+ "HttpStatus: " + response.getHttpStatus() + "\n"
					+ "BackendMessage: " + response.getBackendMessage() + "\n");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	private Map<String, String> validateVenta(Venta venta) {
		Map<String, String> errors = new HashMap<>();		
		if (venta.getCliente() == null) {
			errors.put("cliente", "Cliente cannot be empty");
		}	
		if (venta.getItems() == null || venta.getItems().isEmpty()) {
			errors.put("items", "Items cannot be empty");
		} else {
			for (VentaItem vi : venta.getItems()) {
				if (vi.getProducto().getId() == null) {
					errors.put("items.producto.id", "Producto ID cannot be empty");
					break;
				}
				if (vi.getCantidad() == null || vi.getCantidad() <= 0) {
					errors.put("items.cantidad", "Producto Cantidad should be greater than 0");
					break;
				}
			}
		}
		return errors;
	}
}
