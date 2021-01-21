package com.soaint.shoppingcar.models.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.soaint.shoppingcar.models.entity.Venta;

public interface VentaDao extends PagingAndSortingRepository<Venta, Long>  {

	@Query(value="select v from Venta v "
			+ "join fetch v.cliente c "
			+ "join fetch v.items vi "
			+ "join fetch vi.producto p ",
			countQuery = "select count(v) from Venta v")
	Page<Venta> findAll(Pageable pageable);
	
	@Query(value="select v from Venta v "
			+ "join fetch v.cliente c "
			+ "join fetch v.items vi "
			+ "join fetch vi.producto p "
			+ "where v.id = ?1")
	Optional<Venta> findById(Long id);
}
