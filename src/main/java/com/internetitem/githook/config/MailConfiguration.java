package com.internetitem.githook.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MailConfiguration {

	public static final Logger logger = LoggerFactory.getLogger(MailConfiguration.class);

	private Map<String, Set<String>> recipientMap;

	public MailConfiguration() {
		this.recipientMap = new HashMap<>();
	}

	public Set<String> getRecipients(String repository) {
		return recipientMap.get(repository);
	}

	public boolean addRecipient(String repository, String mail) {
		Set<String> recipients = recipientMap.get(repository);
		boolean newRepository = false;
		if (recipients == null) {
			recipients = new HashSet<>();
			recipientMap.put(repository, recipients);
			newRepository = true;
		}
		logger.debug("Adding recipient [" + mail + "] to repository [" + repository + "]");
		recipients.add(mail);
		return newRepository;
	}
}
