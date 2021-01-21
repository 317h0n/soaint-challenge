package com.soaint.shoppingcar.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.soaint.shoppingcar.models.dao.ClienteDao;
import com.soaint.shoppingcar.models.entity.Cliente;
import com.soaint.shoppingcar.responses.MyApiResponse;
import com.soaint.shoppingcar.responses.MyListApiResponse;

@Service
public class ClienteServiceImpl implements IClienteService {

	@Autowired
	private ClienteDao dao;
	
	@Override
	public MyListApiResponse<Cliente> findAll(int page, int pageSize, String orderBy) {
		orderBy = orderBy == null ? "lastname" : orderBy;
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
		MyListApiResponse<Cliente> response = new MyListApiResponse<Cliente>();
		try {
			Pageable paging = PageRequest.of(page, pageSize, Sort.by(direction, orderBy));
			Page<Cliente> result = dao.findAll(paging);
			response.setError(false);
			response.setElements(result.getTotalElements());
			response.setPages(result.getTotalPages());
			response.setPage(result.getNumber() + 1);
			response.setContent(result.getContent());
		} catch (Exception e) {
			response.setError(true);
			if (e.getMessage().contains("could not resolve property")) {
				response.setMessage("Sort value not exists.");
			} else {
				response.setMessage("There was an error.");
			}
			response.setBackendMessage(e.getMessage());
		}
		return response;
	}

	@Override
	public MyApiResponse<Cliente> save(Cliente entity) {
		MyApiResponse<Cliente> response = new MyApiResponse<>();
		try {
			Cliente cliente = dao.save(entity);
			response.setContent(cliente);
			response.setMessage("Cliente " + entity.getFirstname() + " " + entity.getLastname() + " was saved successfully.");
			response.setError(false);
			if (cliente == null) {
				response.setMessage("There was a problem trying to save the cliente " + entity.getFirstname() + " " + entity.getLastname());
				response.setError(true);
			}
		} catch (Exception e) {				
			response.setMessage("There was a problem trying to save the cliente " + entity.getFirstname() + " " + entity.getLastname());
			response.setError(true);
			response.setBackendMessage(e.getMessage());
		}
		return response;
	}

	@Override
	public MyApiResponse<Cliente> findById(Long id) {
		MyApiResponse<Cliente> response = new MyApiResponse<>();
		try {
			Cliente cliente = dao.findById(id).orElse(null);
			response.setContent(cliente);
			response.setMessage(null);
			response.setError(false);
			if (cliente == null) {
				response.setMessage("Cliente " + id + " not found");
				response.setError(true);
			}	
		} catch (Exception e) {
			response.setMessage("There was an error");
			response.setError(true);
			response.setBackendMessage(e.getMessage());
		}		
		return response;
	}

}
