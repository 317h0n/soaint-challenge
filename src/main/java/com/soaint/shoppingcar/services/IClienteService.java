package com.soaint.shoppingcar.services;

import com.soaint.shoppingcar.models.entity.Cliente;
import com.soaint.shoppingcar.responses.MyApiResponse;
import com.soaint.shoppingcar.responses.MyListApiResponse;

public interface IClienteService {

	MyListApiResponse<Cliente> findAll(int page, int pageSize, String orderBy);
	MyApiResponse<Cliente> findById(Long id);
	MyApiResponse<Cliente> save(Cliente entity);
}
