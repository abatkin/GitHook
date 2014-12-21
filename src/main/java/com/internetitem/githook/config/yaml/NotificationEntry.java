package com.internetitem.githook.config.yaml;

import java.util.List;

public class NotificationEntry {
	private List<String> repositories;
	private List<String> emails;

	public List<String> getRepositories() {
		return repositories;
	}

	public void setRepositories(List<String> repositories) {
		this.repositories = repositories;
	}

	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}
}
