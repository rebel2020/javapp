package com.thrillio.entities;

import static org.junit.Assert.*;

import org.junit.Test;

import com.thrillio.entities.Weblink;
import com.thrillio.managers.BookmarkManager;

public class WeblinkTest {

	@Test
	public void testIsKidFriendkyEligiblePornInUrl() {
		// Test 1- porn in url-- false
		Weblink weblink = BookmarkManager.getInstance().createWeblink(2000, "Taming Tiger, Part 2",
				"http://www.javaworld.com/article/2072759/core-java/taming-porn--part-2.html",
				"http://www.javaworld.com", "unknown");
		boolean isKidFriendkyEligible = weblink.isKidFriendkyEligible();

		assertFalse("Porn in url- must return false", isKidFriendkyEligible);

	}

	@Test
	public void testIsKidFriendkyEligiblePornInTitle() {
		// Test 1- porn in title-- false
		Weblink weblink = BookmarkManager.getInstance().createWeblink(2000, "Taming porn, Part 2",
				"http://www.javaworld.com/article/2072759/core-java/taming-tiger--part-2.html",
				"http://www.javaworld.com", "unknown");
		boolean isKidFriendkyEligible = weblink.isKidFriendkyEligible();

		assertFalse("Porn in title- must return false", isKidFriendkyEligible);

	}

	@Test
	public void testIsKidFriendkyEligibleAdultInHost() {
		// Test- adult in host-- false
		Weblink weblink = BookmarkManager.getInstance().createWeblink(2000, "Taming Tiger, Part 2",
				"http://www.javaworld.com/article/2072759/core-java/taming-tiger--part-2.html", "http://www.adult.com",
				"unknown");
		boolean isKidFriendkyEligible = weblink.isKidFriendkyEligible();

		assertFalse("adult in host- must return false", isKidFriendkyEligible);

	}

	@Test
	public void testIsKidFriendkyEligibleAdultInURLButNotInHost() {
		// Test 4- adult in url but not in host-- true
		Weblink weblink = BookmarkManager.getInstance().createWeblink(2000, "Taming Tiger, Part 2",
				"http://www.javaworld.com/article/2072759/core-java/taming-adult--part-2.html",
				"http://www.javaworld.com", "unknown");
		boolean isKidFriendkyEligible = weblink.isKidFriendkyEligible();

		assertTrue("adult in url- must return true", isKidFriendkyEligible);
	}

	@Test
	public void testIsKidFriendkyEligibleAdultInTitleOnly() {
		// Test 1- adult in title only-- true
		Weblink weblink = BookmarkManager.getInstance().createWeblink(2000, "Taming adult, Part 2",
				"http://www.javaworld.com/article/2072759/core-java/taming-tiger--part-2.html",
				"http://www.javaworld.com", "unknown");
		boolean isKidFriendkyEligible = weblink.isKidFriendkyEligible();

		assertTrue("adult in title- must return true", isKidFriendkyEligible);

	}

}
