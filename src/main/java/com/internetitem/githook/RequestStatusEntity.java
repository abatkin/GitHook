package com.internetitem.githook;

public class RequestStatusEntity {

	private String message;
	private boolean success;

	public RequestStatusEntity(String message, boolean success) {
		this.message = message;
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public boolean getSuccess() {
		return success;
	}
}
