package com.soaint.shoppingcar.models.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.soaint.shoppingcar.models.entity.Cliente;

public interface ClienteDao extends PagingAndSortingRepository<Cliente, Long>  {

	Page<Cliente> findAll(Pageable pageable);
}
