package com.soaint.shoppingcar.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.soaint.shoppingcar.models.dao.VentaDao;
import com.soaint.shoppingcar.models.entity.Venta;
import com.soaint.shoppingcar.responses.MyApiResponse;
import com.soaint.shoppingcar.responses.MyListApiResponse;

@Service
public class VentaServiceImpl implements IVentaService {

	@Autowired
	private VentaDao dao;
	
	@Override
	public MyListApiResponse<Venta> findAll(int page, int pageSize, String orderBy) {
		orderBy = orderBy == null ? "fecha" : orderBy;
		Sort.Direction direction;
		if (orderBy.startsWith("-")) {
			direction = Sort.Direction.DESC;
			orderBy = orderBy.replaceAll("-", "");
		} else if (orderBy.startsWith("+")) {
			direction = Sort.Direction.ASC;
			orderBy = orderBy.replaceAll("+", "");
		} else {
			direction = Sort.Direction.ASC;
		}
		MyListApiResponse<Venta> response = new MyListApiResponse<Venta>();
		try {
			Pageable paging = PageRequest.of(page, pageSize, Sort.by(direction, orderBy));
			Page<Venta> result = dao.findAll(paging);
			response.setError(false);
			response.setElements(result.getTotalElements());
			response.setPages(result.getTotalPages());
			response.setPage(result.getNumber() + 1);
			response.setContent(result.getContent());
		} catch (Exception e) {
			response.setError(true);
			if (e.getMessage().contains("could not resolve property")) {
				response.setMessage("Sort value "+orderBy+" not exists.");
			} else {
				response.setMessage("Sort problems.");
			}
			response.setBackendMessage(e.getMessage());
		}
		return response;
	}
	
	

	@Override
	public MyApiResponse<Venta> findById(Long id) {
		Venta venta = dao.findById(id).orElse(null);
		MyApiResponse<Venta> response = new MyApiResponse<>();
		response.setContent(venta);
		response.setMessage(null);
		response.setError(false);
		if (venta == null) {
			response.setMessage("Venta " + id + " not found");
			response.setError(true);
		}
		return response;
	}

	@Override
	public MyApiResponse<Venta> save(Venta entity) {
		MyApiResponse<Venta> response = new MyApiResponse<>();
		try {
			Venta venta = dao.save(entity);
			response.setContent(venta);
			response.setMessage("Venta was saved successfully.");
			response.setError(false);
			if (venta == null) {
				response.setMessage("There was a problem trying to save the venta");
				response.setError(true);
			}
		} catch (Exception e) {
			response.setBackendMessage(e.getMessage());
			response.setMessage("There was an error");
			response.setError(true);
		}
		return response;
	}

}
