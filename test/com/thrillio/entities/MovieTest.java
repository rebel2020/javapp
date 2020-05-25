package com.thrillio.entities;

import static org.junit.Assert.assertFalse;

import org.junit.jupiter.api.Test;

import com.thrillio.constants.MovieGenre;
import com.thrillio.managers.BookmarkManager;

class MovieTest {

	@Test
	void testIsKidFriendkyEligibleGenreIsHorror() {
		// Test 1- Genre is Horror- false
		Movie movie = BookmarkManager.getInstance().createMovie(3000,"Citizen Kane","",1941,new String[] {"Orson Welles","Joseph Cotten"},new String[] {"Orson Welles"},MovieGenre.HORROR,8.5);
		boolean isKidFriendkyEligible = movie.isKidFriendkyEligible();
		assertFalse("Genre is Horror- must return false",isKidFriendkyEligible);
		
	}
	@Test
	void testIsKidFriendkyEligibleGenreIsThriller() {
		// Test- Genre is Thriller- false
		Movie movie= BookmarkManager.getInstance().createMovie(3000,"Citizen Kane","",1941,new String[] {"Orson Welles","Joseph Cotten"},new String[] {"Orson Welles"},MovieGenre.THRILLER,8.5);
		boolean isKidFriendkyEligible = movie.isKidFriendkyEligible();
		assertFalse("Genre is Thriller- must return false",isKidFriendkyEligible);
	}

}
