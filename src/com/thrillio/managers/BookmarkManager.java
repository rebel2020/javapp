package com.thrillio.managers;

import java.util.Collection;
import java.util.List;

import com.thrillio.constants.BookGenre;
import com.thrillio.constants.KidFriendlyStatus;
import com.thrillio.constants.MovieGenre;
import com.thrillio.dao.BookmarkDao;
import com.thrillio.entities.Book;
import com.thrillio.entities.Bookmark;
import com.thrillio.entities.Movie;
import com.thrillio.entities.User;
import com.thrillio.entities.UserBookmark;
import com.thrillio.entities.Weblink;

public class BookmarkManager {
	private static BookmarkManager instance = new BookmarkManager();
	private static BookmarkDao dao = new BookmarkDao();

	private BookmarkManager() {
	};

	public static BookmarkManager getInstance() {
		return instance;
	}

	public Movie createMovie(long id, String title, String profileUrl, int releaseYear, String[] cast,
			String[] directors, MovieGenre genre, double imdbRating) {
		Movie movie = new Movie();
		movie.setId(id);
		movie.setTitle(title);
		movie.setProfileUrl(profileUrl);
		movie.setReleaseYear(releaseYear);
		movie.setCast(cast);
		movie.setDirectors(directors);
		movie.setGenre(genre);
		movie.setImdbRating(imdbRating);
		return movie;
	}

	public Book createBook(long id, String title,String imageUrl, String profileUrl, int publicationYear, String publisher,
			String[] authors, BookGenre genre, double amazonRating) {
		Book book = new Book();
		book.setId(id);
		book.setTitle(title);
		book.setImageUrl(imageUrl);
		book.setProfileUrl(profileUrl);
		book.setPublicationYear(publicationYear);
		book.setPublisher(publisher);
		book.setAuthors(authors);
		book.setGenre(genre);
		book.setAmazonRating(amazonRating);
		return book;
	}

	public Weblink createWeblink(long id, String title, String url, String host, String profileUrl) {
		Weblink weblink = new Weblink();
		weblink.setId(id);
		weblink.setTitle(title);
		weblink.setProfileUrl(profileUrl);
		weblink.setUrl(url);
		weblink.setHost(host);
		return weblink;
	}


	public void saveUserBookmark(User user, Bookmark bookmark) {
		UserBookmark userBookmark = new UserBookmark();
		userBookmark.setUser(user);
		userBookmark.setBookmark(bookmark);

/*		if(bookmark instanceof Weblink) {
			String url = ((Weblink)bookmark).getUrl();
			if(!url.endsWith(".pdf")) {
				try {
					String webpage = HttpConnect.download(url);
					if(webpage != null) {
						IOUtil.write(webpage, bookmark.getId());
					}
				} catch (MalformedURLException | URISyntaxException e) {
					System.out.println("Error in downloading...\n\n\n");
					e.printStackTrace();
				}
			}
		}*/
		
		dao.saveUserBookmark(userBookmark);
	}

	public void setIsKidFriendlyStatus(User user,Bookmark bookmark,KidFriendlyStatus kidFriendlyStatus) {
		bookmark.setIsKidFriendlyStatus(kidFriendlyStatus);
		bookmark.setKidFriendlyMarkedBy(user);
		BookmarkDao.setIsKidFriendlyStatus(bookmark);
		System.out.println("Kid friendly status: " + kidFriendlyStatus + " +marked by " +user.getEmail()+ bookmark);

	}

	public void share(User user, Bookmark bookmark) {
		 bookmark.setSharedBy(user);
		 BookmarkDao.share(bookmark);
		System.out.println("Data to be shared...");
		if(bookmark instanceof Book) {
			System.out.println(((Book)bookmark).getItemData());
		}
		else if(bookmark instanceof Weblink) {
			System.out.println(((Weblink)bookmark).getItemData());
		}
	}

	public Collection<Bookmark> getBooks(boolean isBookmarked, long id) {
		return dao.getBooks(isBookmarked,id);
	}

	public Bookmark getBook(long bid) {
		return dao.getBook(bid);
	}
}
