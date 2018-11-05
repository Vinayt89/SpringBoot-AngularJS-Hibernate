package com.example.service;

import java.util.List;

import com.example.model.User;

public interface UserService {
	
	User findById(long id);
	User findByName(String name);
	void saveOrUpdateUser(User user);
	void deleteUserById(long id);
	List<User> findAllUsers(); 
	void deleteAllUsers();
	public boolean isUserExist(User user);
}
