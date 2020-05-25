package com.thrillio.constants;

public enum BookGenre {
	ART("Art"),
	BIOGRAPHY("Biography"),
	CHILDREN("Children"),
	FICTION("FICTION"),
	HISTORY("History"),
	MISTORY("Mistory"),
	PHILOSOPHY("Phylosophy"),
	RELIGION("Religion"),
	ROMANCE("Romance"),
	SELF_HELP("Self Help"),
	TECHNICAL("Techinical");
	private BookGenre(String name) {
		this.name=name;
	}
	private String name;
	public String getName() {
		return name;
	}

}
