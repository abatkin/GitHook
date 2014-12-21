package com.internetitem.githook.config;

import com.internetitem.githook.config.yaml.NotificationConfig;
import com.internetitem.githook.config.yaml.NotificationEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

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
					Yaml yaml = new Yaml(new Representer());
					NotificationConfig loadedConfig = yaml.loadAs(reader, NotificationConfig.class);
					for (NotificationEntry ne : loadedConfig.getNotifications()) {
						List<String> repositories = ne.getRepositories();
						if (repositories == null || repositories.isEmpty()) {
							continue;
						}
						List<String> emails = ne.getEmails();
						if (emails == null || emails.isEmpty()) {
							continue;
						}

						for (String repository : repositories) {
							Set<String> repositoryEmails = recipientMap.get(repository);
							if (repositoryEmails == null) {
								repositoryEmails = new HashSet<>();
								recipientMap.put(repository, repositoryEmails);
								numRepositories++;
							}
							numRecipients += emails.size();
							repositoryEmails.addAll(emails);
						}
					}
				}
			}

			logger.info("Added " + numRecipients + " recipients to " + numRepositories + " repositories");
			return recipientMap;
		}
	}

	public static MailConfiguration load(String filename, long sleepTime) throws IOException {
		MailConfiguration config = new MailConfiguration(filename, sleepTime);
		config.preload();
		config.scan();
		return config;
	}


}
