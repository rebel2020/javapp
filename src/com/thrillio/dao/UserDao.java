package com.thrillio.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.thrillio.constants.Gender;
import com.thrillio.constants.Usertype;
import com.thrillio.entities.User;
import com.thrillio.managers.UserManager;

public class UserDao {
	public List<User> getUsers() {
		return new ArrayList<User>();
	}

	public User getUser(int userId) {
		User user=null;
		String query = "select * from User where id=" + userId + ";";
		try {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?useSSL=false",
					"root", "Rebel@123")) {
				Statement smt = con.createStatement();
				ResultSet rs = smt.executeQuery(query);
				while (rs.next()) {
					long id = rs.getLong(1);
					String email = rs.getString(2);
					String password = rs.getString(3);
					String firstName = rs.getString(4);
					String lastName = rs.getString(5);
					int genderId = rs.getInt(6);
					Gender gender = Gender.values()[genderId];
					int userTypeId = rs.getInt(7);
					Usertype userType = Usertype.values()[userTypeId];
					Date createdDate = rs.getDate("created_date");
					user = UserManager.getInstance().createUser(id, email, password, firstName, lastName, gender,
							userType);
					}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public long authenticate(String email, String password) {
		String query = "select * from User where email='" + email + "' and password='"+password+"';";
		try {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?useSSL=false",
					"root", "Rebel@123")) {
				Statement smt = con.createStatement();
				ResultSet rs = smt.executeQuery(query);
				while (rs.next()) {
					return rs.getLong("id");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;		
	}
}
