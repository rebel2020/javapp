package com.thrillio;

import java.util.ArrayList;
import java.util.List;

import com.thrillio.bgjobs.DownloadWeblinks;
import com.thrillio.entities.Bookmark;
import com.thrillio.entities.User;
import com.thrillio.managers.BookmarkManager;
import com.thrillio.managers.UserManager;

public class Launch {
	private static List<User> users = new ArrayList();
	private static List<List<Bookmark> > bookmarks = new ArrayList<>();
//	private static User[] users;
//	private static Bookmark[][] bookmarks;
	private static void loadData() {
		System.out.println("Loading data...");
		DataStore.loadData();
		users=UserManager.getInstance().getUsers();
		bookmarks = BookmarkManager.getInstance().getBookmarks();
//		System.out.println("Printing data...");
		printUserData();
		printBookmarkData();
	}
	private static void printUserData() {
		for(User user:users) {
			System.out.println(user);
		}
	}
	private static void printBookmarkData() {
		for(List<Bookmark> bookmarkList: bookmarks) {
			for(Bookmark bookmark:bookmarkList) {
				System.out.println(bookmark);
			}
		}
	}
	private static void start() {
		System.out.println("\n\nBookmarking...");
		for(User user:users) {
			View.browse(user, bookmarks);
		}		
	}
	private static void download() {
		DownloadWeblinks downloadWeblink = new DownloadWeblinks(true);
		(new Thread(downloadWeblink)).start();
	}

	public static void main(String[] args) {
		loadData();
		start();
/*		System.out.println("\n\n");
		download();*/
	}
}
