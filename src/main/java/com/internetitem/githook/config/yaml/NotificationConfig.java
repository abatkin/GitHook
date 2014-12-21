package com.internetitem.githook.config.yaml;

import java.util.List;

public class NotificationConfig {

	private List<NotificationEntry> notifications;

	public List<NotificationEntry> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<NotificationEntry> notifications) {
		this.notifications = notifications;
	}
}
