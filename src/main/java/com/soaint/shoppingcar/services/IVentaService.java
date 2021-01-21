package com.soaint.shoppingcar.services;

import com.soaint.shoppingcar.models.entity.Venta;
import com.soaint.shoppingcar.responses.MyApiResponse;
import com.soaint.shoppingcar.responses.MyListApiResponse;

public interface IVentaService {

	MyListApiResponse<Venta> findAll(int page, int pageSize, String orderBy);
	MyApiResponse<Venta> findById(Long id);
	MyApiResponse<Venta> save(Venta entity);
}
