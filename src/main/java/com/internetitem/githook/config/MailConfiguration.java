package com.internetitem.githook.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MailConfiguration {

	public static final Logger logger = LoggerFactory.getLogger(MailConfiguration.class);

	private Map<String, Set<String>> recipientMap;
	private Thread loaderThread;
	private MailLoaderThread mailLoaderThread;

	public MailConfiguration(String filename, long sleepTime) {
		this.recipientMap = new HashMap<>();
		this.mailLoaderThread = new MailLoaderThread(filename, sleepTime);

		this.loaderThread = new Thread(mailLoaderThread);
		loaderThread.setDaemon(true);
		loaderThread.setName("RecipientReloaderThread");
	}

	public Set<String> getRecipients(String repository) {
		return recipientMap.get(repository);
	}

	private void scan() {
		loaderThread.start();
	}

	private void preload() throws IOException {
		this.recipientMap = mailLoaderThread.doLoad();
	}

	private class MailLoaderThread implements Runnable {

		private File file;
		private long sleepTime;
		private long lastModified;

		public MailLoaderThread(String filename, long sleepTime) {
			this.file = new File(filename);
			this.sleepTime = sleepTime;
		}

		@Override
		public void run() {
			while (true) {
				try {
					long newLastModified = file.lastModified();
					if (newLastModified != lastModified) {
						logger.info("Recipient file modification detected, reloading");
						MailConfiguration.this.recipientMap = doLoad();
					}
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						logger.info("file modification thread Interrupted, stop polling");
						return;
					}
				} catch (Exception e) {
					logger.warn("Internal error while reloading recipient list: " + e.getMessage(), e);

				}
			}
		}

		public Map<String, Set<String>> doLoad() throws IOException {
			Map<String, Set<String>> recipientMap = new HashMap<>();

			int numRecipients = 0;
			int numRepositories = 0;

			try (FileInputStream istream = new FileInputStream(file)) {
				this.lastModified = file.lastModified();
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

							Set<String> allRepositories = parseList(parts[0].trim());
							Set<String> allMails = parseList(parts[1].trim());

							for (String repository : allRepositories) {
								Set<String> recipients = recipientMap.get(repository);
								if (recipients == null) {
									recipients = new HashSet<>();
									recipientMap.put(repository, recipients);
									numRepositories++;
								}
								for (String mail : allMails) {
									logger.debug("Adding recipient [" + mail + "] to repository [" + repository + "]");
									recipients.add(mail);
									numRecipients++;
								}
							}
						}
					}
				}
			}

			logger.info("Added " + numRecipients + " recipients to " + numRepositories + " repositories");
			return recipientMap;
		}
	}

	private static Set<String> parseList(String list) {
		String[] itemArray = list.split(",");
		Set<String> items = new HashSet<>();
		for (String item : itemArray) {
			item = item.trim();
			if (!item.isEmpty()) {
				items.add(item);
			}
		}
		return items;
	}

	public static MailConfiguration load(String filename, long sleepTime) throws IOException {
		MailConfiguration config = new MailConfiguration(filename, sleepTime);
		config.preload();
		config.scan();
		return config;
	}


}
