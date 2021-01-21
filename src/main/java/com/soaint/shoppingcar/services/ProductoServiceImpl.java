package com.soaint.shoppingcar.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soaint.shoppingcar.models.dao.ProductoDao;
import com.soaint.shoppingcar.models.entity.Producto;
import com.soaint.shoppingcar.responses.MyApiResponse;
import com.soaint.shoppingcar.responses.MyListApiResponse;

@Service
public class ProductoServiceImpl implements IProductoService {

	@Autowired
	private ProductoDao dao;
	
	private Logger log = LoggerFactory.getLogger(ProductoServiceImpl.class);
	
	@Override
	public MyListApiResponse<Producto> findAll(int page, int pageSize, String orderBy) {
		orderBy = orderBy == null ? "code" : orderBy;
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
		MyListApiResponse<Producto> response = new MyListApiResponse<Producto>();
		try {
			Pageable paging = PageRequest.of(page, pageSize, Sort.by(direction, orderBy));
			Page<Producto> result = dao.findAll(paging);
			response.setError(false);
			response.setElements(result.getTotalElements());
			response.setPages(result.getTotalPages());
			response.setPage(result.getNumber() + 1);
			response.setContent(result.getContent());
		} catch (Exception e) {
			response.setError(true);
			if (e.getMessage().contains("could not resolve property")) {
				log.error("Sort value not exists.");
				response.setMessage("Sort value not exists.");
			} else {
				log.error("Sort problem.");
				response.setMessage("Sort problem.");
			}
			response.setBackendMessage(e.getMessage());
		}
		return response;
	}
	
	@Override
	public MyApiResponse<Producto> findById(Long id) {
		Producto producto = dao.findById(id).orElse(null);
		MyApiResponse<Producto> response = new MyApiResponse<>();
		response.setContent(producto);
		response.setMessage(null);
		response.setError(false);;
		if (producto == null) {
			response.setMessage("Producto " + id + " not found");
			response.setError(true);
		}
		return response;
	}

	@Override
	@Transactional
	public MyApiResponse<Producto> save(Producto entity) {
		MyApiResponse<Producto> response = new MyApiResponse<>();
		try {
			Producto producto = dao.save(entity);
			response.setContent(producto);
			response.setMessage("Producto " + entity.getName() + " was saved successfully.");
			response.setError(false);
			if (producto == null) {
				response.setMessage("There was a problem trying to save the producto " + entity.getName());
				response.setError(true);
			}
		} catch (Exception e) {
			response.setBackendMessage(e.getMessage());	
			response.setMessage("There was an error");
			response.setError(true);		
		}
		return response;
	}

	@Override
	public void delete(Long id) {
		dao.deleteById(id);
	}

}
