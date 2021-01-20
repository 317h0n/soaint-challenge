package com.soaint.shoppingcar.models.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.soaint.shoppingcar.models.entity.User;

public interface UserDao extends PagingAndSortingRepository<User, Long> {

	@Query(value="select u from User u "
			+ "left join fetch u.roles r ",
			countQuery = "select count(u) from User u")
	Page<User> findAll(Pageable pageable);
	
	@Query("select u from User u "
			+ "left join fetch u.roles r "
			+ "where u.username = ?1")
	Optional<User> findByUsername(String username);
}
