package com.soaint.shoppingcar.models.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.soaint.shoppingcar.models.entity.Cliente;

public interface ClienteDao extends PagingAndSortingRepository<Cliente, Long>  {

	@Query(value="select c from Cliente c "
			+ "left join fetch c.ventas v "
			+ "left join fetch v.items vi "
			+ "left join fetch vi.producto p ",
			countQuery = "select count(c) from Cliente c")
	Page<Cliente> findAll(Pageable pageable);
}
