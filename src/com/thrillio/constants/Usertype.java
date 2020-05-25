package com.thrillio.constants;

public enum Usertype {
	USER("user"),
	EDITOR("editor"),
	CHIEF_EDITOR("chiefeditor");
	private Usertype(String value) {
		this.value=value;
	}
	private String value;
	public String getValue() {
		return value;
	}
}
