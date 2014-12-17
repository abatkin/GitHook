package com.internetitem.githook.dataModel;

public class PushNotificationData {

	private String htmlMessage;
	private String description;

	public PushNotificationData(String htmlMessage, String description) {
		this.htmlMessage = htmlMessage;
		this.description = description;
	}

	public String getHtmlMessage() {
		return htmlMessage;
	}

	public String getDescription() {
		return description;
	}
}
