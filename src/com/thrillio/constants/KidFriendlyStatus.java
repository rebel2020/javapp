package com.thrillio.constants;

public enum KidFriendlyStatus {
	UNKNOWN("unknown"),
	APPROVED("approved"),
	REJECTED("rejected");
	private KidFriendlyStatus(String value) {
		this.value = value;
	}
	private String value;
	public String getValue() {
		return value;
	}
}
