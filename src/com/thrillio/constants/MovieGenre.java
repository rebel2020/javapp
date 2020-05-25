package com.thrillio.constants;

public enum MovieGenre {
	CLASSICS("Classics"),
	DOCUMENTARIES("DocumentAries"), 
	DRAMA("Classics"),
	SCIFI_AND_FANTASY("Scifi and fantasy"),
	COMEDY("Comedy"),
	CHILDREN_AND_FAMILY("Children and family"),
	ACTION_AND_ADVENTURE("Action and adventure"),
	THRILLER("Thriller"),
	MUSIC_AND_MUSICALS("Music and musicals"),
	TELIVISION("TV Shows"),
	HORROR("Horror"),
	SPECIAL_INTEREST("Special interest"),
	INDEPENDENT("Independent"),
	SPORTS_AND_FITNESS("Sports and fitness"),
	ANIME_AND_ANIMATION("Anime and animation"),
	GAY_AND_LESBIAN("Gay and lesbian"),
	FOREIGN_MOVIES("foreign movies"),
	CLASSIC_MOVIE_MUSICALS("Classic movie musicals");
	private MovieGenre(String name) {
		this.name = name;
	}
	private String name;
	public String getName() {
		return name;
	}
}
