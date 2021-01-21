package com.soaint.shoppingcar.services;

import com.soaint.shoppingcar.models.entity.Producto;
import com.soaint.shoppingcar.responses.MyApiResponse;
import com.soaint.shoppingcar.responses.MyListApiResponse;

public interface IProductoService {

	MyListApiResponse<Producto> findAll(int page, int pageSize, String orderBy);
	MyApiResponse<Producto> findById(Long id);
	MyApiResponse<Producto> save(Producto producto);
	void delete(Long id);
}
