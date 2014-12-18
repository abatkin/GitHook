package com.internetitem.githook.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class MailConfigurationLoader {

	public static final Logger logger = LoggerFactory.getLogger(MailConfigurationLoader.class);

	public MailConfiguration load(String filename) throws IOException {
		MailConfiguration config = new MailConfiguration();

		int numRecipients = 0;
		int numRepositories = 0;

		try (FileInputStream istream = new FileInputStream(filename)) {
			try (InputStreamReader reader = new InputStreamReader(istream, "UTF-8")) {
				try (BufferedReader in = new BufferedReader(reader)) {
					String line;
					int lineNumber = 0;
					while ((line = in.readLine()) != null) {
						lineNumber++;
						line = line.trim();
						if (line.isEmpty() || line.startsWith("#")) {
							continue;
						}
						String[] parts = line.split("=", 2);
						if (parts.length != 2) {
							throw new IOException("Malformed input on line " + lineNumber);
						}
						String repository = parts[0].trim();
						String mail = parts[1].trim();
						if (config.addRecipient(repository, mail)) {
							numRepositories++;
						}
						numRecipients++;
					}
				}
			}
		}

		logger.info("Added " + numRecipients + " recipients to " + numRepositories + " repositories");

		return config;
	}

}
