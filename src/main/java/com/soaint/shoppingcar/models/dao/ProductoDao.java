package com.soaint.shoppingcar.models.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.soaint.shoppingcar.models.entity.Producto;

public interface ProductoDao extends PagingAndSortingRepository<Producto, Long>  {

	Page<Producto> findAll(Pageable pageable);
}
