package com.thrillio.managers;

import java.util.List;

import com.thrillio.constants.Gender;
import com.thrillio.constants.Usertype;
import com.thrillio.dao.UserDao;
import com.thrillio.entities.User;

public class UserManager {
	private static UserManager instance = new UserManager();
	private static UserDao dao = new UserDao();
	
	private UserManager() {
	};

	public static UserManager getInstance() {
		return instance;
	}

	public User createUser(long id, String email, String password, String firstName, String lastName, Gender gender,
			Usertype userType) {
		User user = new User();
		user.setId(id);
		user.setEmail(email);
		user.setPassword(password);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setUserType(userType);
		user.setGender(gender);
		return user;
	}
	public List<User> getUsers() {
		return dao.getUsers();
	}

	public User getUser(int userId) {
		return dao.getUser(userId);
		
	}

	public long authenticate(String email, String password) {
		return dao.authenticate(email, password);
	}
}
