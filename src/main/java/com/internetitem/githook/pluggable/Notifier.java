package com.internetitem.githook.pluggable;

import com.internetitem.githook.dataModel.PushNotificationData;

public interface Notifier {
	NotificationResult sendNotification(PushNotificationData push);

	public static enum NotificationResult {
		Success,
		FailedPermanently,
		FailedTemporarily
	}
}
