package com.soaint.shoppingcar.services;

import com.soaint.shoppingcar.models.entity.User;
import com.soaint.shoppingcar.responses.MyListApiResponse;

public interface IUserService {

	MyListApiResponse<User> findAll(int pagina, int cantidad, String orderBy);
	User findById(Long id);	
	User findByUsername(String username);	
	User findByUsernameNoPassword(String username);	
	User save(User user);
	void deleteById(Long id);
}
