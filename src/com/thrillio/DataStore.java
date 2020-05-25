package com.thrillio;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.thrillio.constants.BookGenre;
import com.thrillio.constants.Gender;
import com.thrillio.constants.KidFriendlyStatus;
import com.thrillio.constants.MovieGenre;
import com.thrillio.constants.Usertype;
import com.thrillio.controllers.BookmarkController;
import com.thrillio.entities.Bookmark;
import com.thrillio.entities.User;
import com.thrillio.entities.UserBookmark;
import com.thrillio.managers.BookmarkManager;
import com.thrillio.managers.UserManager;
import com.thrillio.utils.IOUtil;

public class DataStore {
	/*
	 * public static final int USER_BOOKMARK_LIMIT = 5; public static final int
	 * BOOKMARK_COUNT_PER_TYPE = 5; public static final int BOOKMARK_TYPE_COUNT = 3;
	 * public static final int TOTAL_USER_COUNT = 5;
	 * 
	 * private static int bookmarkIndex = 0;
	 */
	private static List<User> users = new ArrayList();
	private static List<List<Bookmark>> bookmarks = new ArrayList<>();
//	private static User[] users = new User[TOTAL_USER_COUNT];
//	private static Bookmark[][] bookmarks = new Bookmark[BOOKMARK_TYPE_COUNT][BOOKMARK_COUNT_PER_TYPE];
	private static List<UserBookmark> userBookmarks = new ArrayList<>();

	public static List<User> getUsers() {
		return users;
	}

	public static List<List<Bookmark>> getBookmarks() {
		return bookmarks;
	}

	public static void loadData() {
		/*
		 * loadUser(); loadWeblinks(); loadMovies(); loadBooks();
		 */
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?useSSL=false",
				"root", "Rebel@123")) {
			Statement smt = con.createStatement();
			loadBooks(smt);
			loadUsers(smt);
			loadWeblinks(smt);
			loadMovies(smt);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void loadMovies(Statement smt) {
		String query = "select m.id,m.title,m.release_year,group_concat(a.name separator ',') as cast,group_concat(d.name separator ','),"
				+ "m.movie_genre_id,m.imdb_rating from Movie m,Director d,Actor a,Movie_Actor ma,Movie_Director md "
				+ "where ma.movie_id=m.id and ma.actor_id=a.id and md.director_id=d.id and md.movie_id=m.id group by m.id;";
		try {
			ResultSet rs = smt.executeQuery(query);
			List<Bookmark> bookmarkList = new ArrayList<>();
			while (rs.next()) {
				long id = rs.getLong(1);
				String title = rs.getString(2);
				int releaseYear = rs.getInt(3);
				String[] cast = rs.getString(4).split(",");
				String[] directors = rs.getString(5).split(",");
				int movieGenreId = rs.getInt(6);
				MovieGenre genre = MovieGenre.values()[movieGenreId];
				double imdbRating = rs.getDouble(7);
				Bookmark bookmark = BookmarkManager.getInstance().createMovie(id, title, "", releaseYear, cast, directors, genre, imdbRating);
				bookmarkList.add(bookmark);
			}
			bookmarks.add(bookmarkList);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void loadWeblinks(Statement smt) {
		String query = "select * from Weblink";
		try {
			ResultSet rs = smt.executeQuery(query);
			List<Bookmark> bookmarkList = new ArrayList<>();
			while (rs.next()) {
				long id = rs.getLong(1);
				String title = rs.getString(2);
				String url = rs.getString(3);
				String host = rs.getString(4);
				int kidFriendlyStatusId = rs.getInt(5);
				KidFriendlyStatus kidFriendlyStatus = KidFriendlyStatus.values()[kidFriendlyStatusId];
				Date createdDate = rs.getDate("created_date");
				Bookmark bookmark = BookmarkManager.getInstance().createWeblink(id, title, url, host, "");
				bookmarkList.add(bookmark);
			}
			bookmarks.add(bookmarkList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void loadBooks(Statement smt) {
		String query = "select b.id,b.title,b.publication_year,p.name as publisher,group_concat(a.name separator ',') as authors,"
				+ "b.book_genre_id,b.amazon_rating,b.created_date from Book b,Author a,Publisher p,Book_Author ba "
				+ "where b.publisher_id=p.id and ba.book_id=b.id and ba.author_id=a.id group by b.id;";
		try {
			ResultSet rs = smt.executeQuery(query);
			List<Bookmark> bookmarkList = new ArrayList<>();
			while (rs.next()) {
				long id = rs.getLong("id");
				String title = rs.getString("title");
				int publicationYear = rs.getInt("publication_year");
				String publisher = rs.getString("publisher");
				String[] authors = rs.getString("authors").split(",");
				int genreId = rs.getInt("book_genre_id");
				BookGenre genre = BookGenre.values()[genreId];
				double amazonRating = rs.getDouble("amazon_rating");
				Date createdDate = rs.getDate("created_date");

				Bookmark bookmark = BookmarkManager.getInstance().createBook(id, title, "", publicationYear, publisher,
						authors, genre, amazonRating);
				bookmarkList.add(bookmark);
			}
			bookmarks.add(bookmarkList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void loadUsers(Statement smt) {
		String query = "select * from User";
		try {
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
				User user = UserManager.getInstance().createUser(id, email, password, firstName, lastName, gender,
						userType);
				users.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void loadUser() {
		List<String> data = new ArrayList<>();
//		String[] data = new String[TOTAL_USER_COUNT];
		String fileName = "User.txt";
		IOUtil.read(data, fileName);
		for (String row : data) {
			String[] values = row.split("\t");
			Gender gender = Gender.MALE;
			if (values[5].equals("F"))
				gender = Gender.FEMALE;
			else if (values[5].equals("T"))
				gender = Gender.TRANSGENDER;
			User user = UserManager.getInstance().createUser(Long.parseLong(values[0]), values[1], values[2], values[3],
					values[4], gender, Usertype.valueOf(values[6]));
			users.add(user);
		}
		/*
		 * users[0]=UserManager.getInstance().createUser(1000,"user0@semanticsquare.com"
		 * ,"test","John","M",Gender.MALE,Usertype.USER);
		 * users[1]=UserManager.getInstance().createUser(1001,"user1@semanticsquare.com"
		 * ,"test","Sam","M",Gender.MALE,Usertype.USER);
		 * users[2]=UserManager.getInstance().createUser(1002,"user2@semanticsquare.com"
		 * ,"test","Anita","M",Gender.MALE,Usertype.EDITOR);
		 * users[3]=UserManager.getInstance().createUser(1003,"user3@semanticsquare.com"
		 * ,"test","Sara","M",Gender.FEMALE,Usertype.EDITOR);
		 * users[4]=UserManager.getInstance().createUser(1004,"user4@semanticsquare.com"
		 * ,"test","Dheeru","M",Gender.MALE,Usertype.CHIEF_EDITOR);
		 */
	}

	public static void loadWeblinks() {
//		String[] data=new String[BOOKMARK_COUNT_PER_TYPE];
		List<String> data = new ArrayList<>();
		String fileName = "WebLink.txt";
		IOUtil.read(data, fileName);
		List<Bookmark> bookmarkList = new ArrayList();
		for (String row : data) {
			String[] values = row.split("\t");
			Bookmark bookmark = BookmarkManager.getInstance().createWeblink(Long.parseLong(values[0]), values[1],
					values[2], values[3], values[4]);
			bookmarkList.add(bookmark);
		}
		bookmarks.add(bookmarkList);
		/*
		 * bookmarks[0][0]=BookmarkManager.getInstance().createWeblink(
		 * 2000,"Taming Tiger, Part 2"
		 * ,"http://www.javaworld.com/article/2072759/core-java/taming-tiger--part-2.html"
		 * ,"http://www.javaworld.com","unknown");
		 * bookmarks[0][1]=BookmarkManager.getInstance().createWeblink(
		 * 2001,"How do I import a pre-existing Java project into Eclipse and get up and running?"
		 * ,"http://stackoverflow.com/questions/142863/how-do-i-import-a-pre-existing-java-project-into-eclipse-and-get-up-and-running"
		 * ,"http://www.stackoverflow.com","unknown");
		 * bookmarks[0][2]=BookmarkManager.getInstance().createWeblink(
		 * 2002,"Interface vs Abstract Class"
		 * ,"http://mindprod.com/jgloss/interfacevsabstract.html","http://mindprod.com",
		 * "unknown"); bookmarks[0][3]=BookmarkManager.getInstance().createWeblink(
		 * 2003,"NIO tutorial by Greg Travis"
		 * ,"http://cs.brown.edu/courses/cs161/papers/j-nio-ltr.pdf",
		 * "http://cs.brown.edu","unknown");
		 * bookmarks[0][4]=BookmarkManager.getInstance().createWeblink(
		 * 2004,"Virtual Hosting and Tomcat"
		 * ,"http://tomcat.apache.org/tomcat-6.0-doc/virtual-hosting-howto.html",
		 * "http://tomcat.apache.org","unknown");
		 */
	}

	public static void loadMovies() {
//		String[] data=new String[BOOKMARK_COUNT_PER_TYPE];
		List<String> data = new ArrayList<>();
		String fileName = "Movie.txt";
		IOUtil.read(data, fileName);
		List<Bookmark> bookmarkList = new ArrayList();
		for (String row : data) {
			String[] values = row.split("\t");
			Bookmark bookmark = BookmarkManager.getInstance().createMovie(Long.parseLong(values[0]), values[1], "",
					Integer.parseInt(values[2]), values[3].split(","), values[4].split(","),
					MovieGenre.valueOf(values[5]), Double.parseDouble(values[6]));
			bookmarkList.add(bookmark);
		}
		bookmarks.add(bookmarkList);
		/*
		 * bookmarks[1][0]=BookmarkManager.getInstance().createMovie(3000,"Citizen Kane"
		 * ,"",1941,new String[] {"Orson Welles","Joseph Cotten"},new String[]
		 * {"Orson Welles"},MovieGenre.CLASSICS,8.5);
		 * bookmarks[1][1]=BookmarkManager.getInstance().createMovie(
		 * 3001,"The Grapes of Wrath","",1940,new
		 * String[]{"Henry Fonda","Jane Darwell"},new String[]
		 * {"John Ford"},MovieGenre.CLASSICS,8.2);
		 * bookmarks[1][2]=BookmarkManager.getInstance().createMovie(
		 * 3002,"A Touch of Greatness","",2004,new String[] {"Albert Cullum"},new
		 * String[] {"Leslie Sullivan"},MovieGenre.DOCUMENTORIES,7.3);
		 * bookmarks[1][3]=BookmarkManager.getInstance().createMovie(
		 * 3003,"The Big Bang Theory","",2007,new String[]
		 * {"Kaley Cuoco","Jim Parsons"},new String[] {"Chuck Lorre","Bill Prady"},
		 * MovieGenre.TELIVISION,8.7);
		 * bookmarks[1][4]=BookmarkManager.getInstance().createMovie(3004,"Ikiru","",
		 * 1952,new String[] {"Takashi Shimura","Minoru Chiaki"},new String[]
		 * {"Akira Kurosawa"},MovieGenre.FOREIGN_MOVIES,8.4);
		 */
	}

	public static void loadBooks() {

//		String[] data=new String[BOOKMARK_COUNT_PER_TYPE];
		List<String> data = new ArrayList<>();
		String fileName = "Book.txt";
		IOUtil.read(data, fileName);
		List<Bookmark> bookmarkList = new ArrayList();
		for (String row : data) {
			String[] values = row.split("\t");
			Bookmark bookmark = BookmarkManager.getInstance().createBook(Long.parseLong(values[0]), values[1], "",
					Integer.parseInt(values[2]), values[3], values[4].split(","), BookGenre.valueOf(values[5]),
					Double.parseDouble(values[6]));
			bookmarkList.add(bookmark);
		}
		bookmarks.add(bookmarkList);

		/*
		 * bookmarks[2][0]=BookmarkManager.getInstance().createBook(4000,"Walden","",
		 * 1854,"Wilder Publications",new String[]
		 * {"Henry David Thoreau"},BookGenre.PHILOSOPHY,4.3);
		 * bookmarks[2][1]=BookmarkManager.getInstance().createBook(
		 * 4001,"Self-Reliance and Other Essays","",1993,"Dover Publications",new
		 * String[] {"Ralph Waldo Emerson"},BookGenre.PHILOSOPHY,4.5);
		 * bookmarks[2][2]=BookmarkManager.getInstance().createBook(
		 * 4002,"Light From Many Lamps","",1988,"Touchstone",new String[]
		 * {"Lillian Eichler Watson"}, BookGenre.PHILOSOPHY,5.0);
		 * bookmarks[2][3]=BookmarkManager.getInstance().createBook(
		 * 4003,"Head First Design Patterns","",2004,"O'Reilly Media",new String[]
		 * {"Eric Freeman","Bert Bates","Kathy Sierra","Elisabeth Robson"},BookGenre.
		 * TECHNICAL,4.5); bookmarks[2][4]=BookmarkManager.getInstance().createBook(
		 * 4004,"Effective Java Programming Language Guide","",2007,"Prentice Hall",new
		 * String[] {"Joshua Bloch"},BookGenre.TECHNICAL,4.9);
		 */
	}

	public static void add(UserBookmark userBookmark) {
		userBookmarks.add(userBookmark);
	}
}
