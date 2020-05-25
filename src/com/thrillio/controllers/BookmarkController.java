package com.thrillio.controllers;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thrillio.constants.KidFriendlyStatus;
import com.thrillio.entities.Bookmark;
import com.thrillio.entities.User;
import com.thrillio.managers.BookmarkManager;
import com.thrillio.managers.UserManager;

@WebServlet(urlPatterns = {"/bookmark","/bookmark/save","/bookmark/mybooks"})
public class BookmarkController extends HttpServlet {
	/*
	 * private static BookmarkController instance = new BookmarkController();
	 * 
	 * private BookmarkController() { };
	 * 
	 * public static BookmarkController getInstance() { return instance; }
	 */
	public BookmarkController() {
	};

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher=null;
		if(request.getServletPath().contains("save")) {
			User user=UserManager.getInstance().getUser(5);
			String bid=request.getParameter("bid");
			Bookmark bookmark = BookmarkManager.getInstance().getBook(Long.parseLong(bid));
			BookmarkManager.getInstance().saveUserBookmark(user, bookmark);
			Collection<Bookmark> list=BookmarkManager.getInstance().getBooks(true,5);
			request.setAttribute("books", list);
			dispatcher = request.getRequestDispatcher("/mybooks.jsp");			
		}
		else if(request.getServletPath().contains("mybooks")) {
			Collection<Bookmark> list=BookmarkManager.getInstance().getBooks(true,5);
			request.setAttribute("books", list);
			dispatcher = request.getRequestDispatcher("/mybooks.jsp");			
			
		} else {
			Collection<Bookmark> list=BookmarkManager.getInstance().getBooks(false,5);
			request.setAttribute("books", list);
			dispatcher = request.getRequestDispatcher("/browse.jsp");			
		}
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if(request.getServletPath().contains("bookmark")) {
			doGet(request, response);
		}
	}

	public void saveUserBookmark(User user, Bookmark bookmark) {
		BookmarkManager.getInstance().saveUserBookmark(user, bookmark);

	}

	public void setIsKidFriendlyStatus(User user, Bookmark bookmark, KidFriendlyStatus kidFriendlyStatus) {
		BookmarkManager.getInstance().setIsKidFriendlyStatus(user, bookmark, kidFriendlyStatus);

	}

	public void share(User user, Bookmark bookmark) {
		BookmarkManager.getInstance().share(user, bookmark);
	}
}
