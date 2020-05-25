package com.thrillio;

import java.util.List;

import com.thrillio.constants.KidFriendlyStatus;
import com.thrillio.constants.Usertype;
import com.thrillio.controllers.BookmarkController;
import com.thrillio.entities.Bookmark;
import com.thrillio.entities.User;
import com.thrillio.partners.Sharable;

public class View {
	public static void browse(User user, List<List<Bookmark>> bookmarks) {
		System.out.println("\n" + user.getEmail() + " is browsing bookmarks...");
		int bookmarkCount = 0;
		for (List<Bookmark> bookmarkList : bookmarks) {
			for (Bookmark bookmark : bookmarkList) {
//				if (bookmarkCount < DataStore.USER_BOOKMARK_LIMIT) {
					boolean isBookmarked = getBookmarkDicision(bookmark);
					//bookmarking
					if (isBookmarked) {
						bookmarkCount++;
						BookmarkController.getInstance().saveUserBookmark(user, bookmark);
//						System.out.println("New bookmark added... " + bookmark);
					}
//				}
				if (user.getUserType().equals(Usertype.EDITOR) || user.getUserType().equals(Usertype.CHIEF_EDITOR)) {
					//set kid friendly
					if (bookmark.getIsKidFriendlyStatus().equals(KidFriendlyStatus.UNKNOWN)
							&& bookmark.isKidFriendkyEligible()) {
						KidFriendlyStatus kidFriendlyStatus = getKidFriendlyStatusDicision(bookmark);
						if (!kidFriendlyStatus.equals(KidFriendlyStatus.UNKNOWN)) {
							BookmarkController.getInstance().setIsKidFriendlyStatus(user,bookmark,kidFriendlyStatus);
						}
					}
				}
				//Sharing
				if(bookmark.getIsKidFriendlyStatus().equals(KidFriendlyStatus.APPROVED)&& bookmark instanceof Sharable) {
					boolean isShared = getShareDicision(bookmark);
					if(isShared) {
						System.out.println(user.getEmail()+" sharing,"+bookmark);
						BookmarkController.getInstance().share(user,bookmark);
					}
				}
			}
		}

	}
	
	//TODO: Below method simulate user input after IO, We take input via console
	private static boolean getShareDicision(Bookmark bookmark) {
		return Math.random()<0.4?true:false;
	}

	private static KidFriendlyStatus getKidFriendlyStatusDicision(Bookmark bookmark) {
		return Math.random() < 0.3 ? KidFriendlyStatus.APPROVED
				: (Math.random() < 0.5 ? KidFriendlyStatus.REJECTED : KidFriendlyStatus.UNKNOWN);
	}

	private static boolean getBookmarkDicision(Bookmark bookmark) {
		return Math.random() < 0.5 ? true : false;
	}

}
