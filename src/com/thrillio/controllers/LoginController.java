package com.thrillio.controllers;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.thrillio.dao.UserDao;
import com.thrillio.managers.UserManager;

@WebServlet(urlPatterns = { "/login", "/register", "/logout", "/validate" })
public class LoginController extends HttpServlet {
	public LoginController() {
	};

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		RequestDispatcher dispatcher = null;
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		if (request.getServletPath().contains("login")) {
			dispatcher = request.getRequestDispatcher("/login.jsp");
		} else if (request.getServletPath().contains("register")) {
			dispatcher = request.getRequestDispatcher("/register.jsp");
		} else if (request.getServletPath().contains("logout")) {
			dispatcher = request.getRequestDispatcher("/logout.jsp");
		} else if (request.getServletPath().contains("validate")) {
			long userId = authenticate(email, password);
			if (userId != -1) {
				dispatcher = request.getRequestDispatcher("/mybooks.jsp");
				session.setAttribute("userId", userId);
			} else {
				dispatcher = request.getRequestDispatcher("login");
			}
		}
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		RequestDispatcher dispatcher = null;
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		if (request.getServletPath().contains("login")) {
			long userId = authenticate(email, password);
			if (userId != -1) {
				BookmarkController bookmarkController = new BookmarkController();
				dispatcher = request.getRequestDispatcher("bookmark");
				session.setAttribute("userId", userId);
//				bookmarkController.doGet(request, response);
	//			return;
			} else {
				dispatcher = request.getRequestDispatcher("/login.jsp");
			}
		}
		dispatcher.forward(request, response);
	}

	public long authenticate(String email, String password) {
		return UserManager.getInstance().authenticate(email, password);
	}
}
