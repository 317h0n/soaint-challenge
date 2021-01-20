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

import com.soaint.shoppingcar.models.dao.UserDao;
import com.soaint.shoppingcar.models.entity.User;
import com.soaint.shoppingcar.responses.MyListApiResponse;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserDao dao;
	
	private Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public MyListApiResponse<User> findAll(int page, int total, String orderBy) {
		orderBy = orderBy == null ? "username" : orderBy;
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
		MyListApiResponse<User> response = new MyListApiResponse<User>();
		try {
			Pageable paging = PageRequest.of(page, total, Sort.by(direction, orderBy));
			Page<User> result = dao.findAll(paging);
			response.setError(false);
			response.setElements(result.getTotalElements());
			response.setPages(result.getTotalPages());
			response.setPage(result.getNumber() + 1);
			response.setContent(result.getContent());
		} catch (Exception e) {
			if (e.getMessage().contains("could not resolve property")) {
				log.error("Sort value not exists.");
				response.setMessage("Sort value not exists.");
			} else {
				log.error("No sort problem.");
				response.setMessage(e.getMessage());
			}
		}
		return response;
	}

	@Override
	public User findById(Long id) {
		return dao.findById(id).orElse(null);
	}

	@Override
	public User findByUsername(String username) {
		return dao.findByUsername(username).orElse(null);
	}

	@Override
	@Transactional
	public User save(User user) {
		return dao.save(user);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		dao.deleteById(id);
	}

	@Override
	public User findByUsernameNoPassword(String username) {
		User user = dao.findByUsername(username).orElse(null);
		if (user != null) {
			user.setPassword("****************");
		}
		return user;
	}

}
