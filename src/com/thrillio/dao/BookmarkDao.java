package com.thrillio.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.thrillio.constants.BookGenre;
import com.thrillio.entities.Book;
import com.thrillio.entities.Bookmark;
import com.thrillio.entities.Movie;
import com.thrillio.entities.UserBookmark;
import com.thrillio.entities.Weblink;
import com.thrillio.managers.BookmarkManager;

public class BookmarkDao {

	public void saveUserBookmark(UserBookmark userBookmark) {
		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?useSSL=false",
				"root", "Rebel@123")) {
			Statement smt = con.createStatement();
			if (userBookmark.getBookmark() instanceof Book) {
				saveUserBook(userBookmark, smt);
			} else if (userBookmark.getBookmark() instanceof Weblink) {
				saveUserWeblink(userBookmark, smt);
			} else {
				saveUserMovie(userBookmark, smt);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void saveUserMovie(UserBookmark userBookmark, Statement smt) {
		String query = "insert into User_Movie (user_id,movie_id) values (" + userBookmark.getUser().getId() + ","
				+ userBookmark.getBookmark().getId() + ");";
		try {
			smt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void saveUserWeblink(UserBookmark userBookmark, Statement smt) {
		String query = "insert into User_Weblink (user_id,weblink_id) values (" + userBookmark.getUser().getId() + ","
				+ userBookmark.getBookmark().getId() + ");";
		try {
			smt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void saveUserBook(UserBookmark userBookmark, Statement smt) {
		String query = "insert into User_Book (user_id,book_id) values (" + userBookmark.getUser().getId() + ","
				+ userBookmark.getBookmark().getId() + ");";
		try {
			smt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public static void setIsKidFriendlyStatus(Bookmark bookmark) {
		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?useSSL=false",
				"root", "Rebel@123")) {
			Statement smt = con.createStatement();
			String tableToUpdate = "Book";
			if (bookmark instanceof Movie) {
				tableToUpdate = "Movie";
			} else if (bookmark instanceof Weblink) {
				tableToUpdate = "Weblink";
			}

			String sql = "update " + tableToUpdate + " set kid_friendly_status="
					+ bookmark.getIsKidFriendlyStatus().ordinal() + ", kid_friendly_marked_by="
					+ bookmark.getKidFriendlyMarkedBy().getId() + " where id=" + bookmark.getId() + ";";
			smt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void share(Bookmark bookmark) {
		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?useSSL=false",
				"root", "Rebel@123")) {
			Statement smt = con.createStatement();
			String tableToUpdate = "Book";
			if (bookmark instanceof Weblink) {
				tableToUpdate = "Weblink";
			}
			String sql = "update " + tableToUpdate + " set shared_by=" + bookmark.getSharedBy().getId() + " where id="
					+ bookmark.getId() + ";";
			smt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public Collection<Bookmark> getBooks(boolean isBookmarked, long userId) {
		List<Bookmark> result = new ArrayList<>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try (Connection con = DriverManager.getConnection(
				"jdbc:mysql://localhost/jid_thrillio?allowPublicKeyRetrieval=true&useSSL=false", "root",
				"Rebel@123")) {
			Statement stmt = con.createStatement();
			String query = "";
			if (!isBookmarked) {
				query = "Select b.id, title, image_url, publication_year, GROUP_CONCAT(a.name SEPARATOR ',') AS authors, book_genre_id, "
						+ "amazon_rating from Book b, Author a, Book_Author ba where b.id = ba.book_id and ba.author_id = a.id and "
						+ "b.id NOT IN (select ub.book_id from User u, User_Book ub where u.id = " + userId
						+ " and u.id = ub.user_id) group by b.id";
			} else {
				query = "Select b.id, title, image_url, publication_year, GROUP_CONCAT(a.name SEPARATOR ',') AS authors, book_genre_id, "
						+ "amazon_rating from Book b, Author a, Book_Author ba where b.id = ba.book_id and ba.author_id = a.id and "
						+ "b.id in (select ub.book_id from User u, User_Book ub where u.id = " + userId
						+ " and u.id = ub.user_id) group by b.id";
			}
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				long id = rs.getLong("id");
				String title = rs.getString("title");
				String imageUrl = rs.getString("image_url");
				int publicationYear = rs.getInt("publication_year");
				// String publisher = rs.getString("name");
				String[] authors = rs.getString("authors").split(",");
				int genre_id = rs.getInt("book_genre_id");
				BookGenre genre = BookGenre.values()[genre_id];
				double amazonRating = rs.getDouble("amazon_rating");

//				System.out.println("id: " + id + ", title: " + title + ", publication year: " + publicationYear + ", authors: " + String.join(", ", authors) + ", genre: " + genre + ", amazonRating: " + amazonRating);

				Bookmark bookmark = BookmarkManager.getInstance().createBook(id, title, imageUrl, "", publicationYear,
						null, authors, genre, amazonRating);
				result.add(bookmark);
			}
		} catch (SQLException e) {
			System.out.println("\n\n\n\n\n");
			e.printStackTrace();
		}
		return result;
	}

	public Bookmark getBook(long bid) {
		Bookmark bookmark = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?useSSL=false",
				"root", "Rebel@123")) {
			Statement smt = con.createStatement();
			String query = "select b.id,b.image_url,b.title,b.publication_year,p.name as publisher,group_concat(a.name separator ',') as authors,"
					+ "b.book_genre_id,b.amazon_rating,b.created_date from Book b,Author a,Publisher p,Book_Author ba "
					+ "where b.publisher_id=p.id and ba.book_id=b.id and ba.author_id=a.id and b.id="+bid +" group by b.id;";
			ResultSet rs = smt.executeQuery(query);
			List<Bookmark> bookmarkList = new ArrayList<>();
			while (rs.next()) {
				long id = rs.getLong("id");
				String title = rs.getString("title");
				int publicationYear = rs.getInt("publication_year");
				String imageUrl = rs.getString("image_url");
				String publisher = rs.getString("publisher");
				String[] authors = rs.getString("authors").split(",");
				int genreId = rs.getInt("book_genre_id");
				BookGenre genre = BookGenre.values()[genreId];
				double amazonRating = rs.getDouble("amazon_rating");
				Date createdDate = rs.getDate("created_date");

				bookmark = BookmarkManager.getInstance().createBook(id, title, imageUrl, "", publicationYear, publisher,
						authors, genre, amazonRating);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bookmark;
	}
}
