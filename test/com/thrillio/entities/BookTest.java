package com.thrillio.entities;

import static org.junit.Assert.assertFalse;

import org.junit.jupiter.api.Test;

import com.thrillio.constants.BookGenre;
import com.thrillio.managers.BookmarkManager;

class BookTest {

	@Test
	void testIsKidFriendkyEligibleGenreIsPholosophy() {
		//BookGenre is PHYLOSOPHY
		Book book = BookmarkManager.getInstance().createBook(4000,"Walden","google.com","",1854,"Wilder Publications",new String[] {"Henry David Thoreau"},BookGenre.PHILOSOPHY,4.3);
		Boolean isKidFriendlyEligible = book.isKidFriendkyEligible();
		assertFalse("Genre is Philosophy- must return false",isKidFriendlyEligible);
	}
	@Test
	void testIsKidFriendkyEligibleGenreIsSelfHelp() {
		//BookGenre is SELF_HELP
		Book book = BookmarkManager.getInstance().createBook(4000,"Walden","google.com","",1854,"Wilder Publications",new String[] {"Henry David Thoreau"},BookGenre.SELF_HELP,4.3);
		Boolean isKidFriendlyEligible = book.isKidFriendkyEligible();
		assertFalse("Genre is Selp help- must return false",isKidFriendlyEligible);
	}

}
