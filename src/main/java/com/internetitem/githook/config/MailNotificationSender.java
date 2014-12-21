package com.internetitem.githook.config;

import com.internetitem.githook.dataModel.ws.GitHubPush;
import com.internetitem.githook.dataModel.ws.GitHubRepository;
import com.internetitem.githook.html.HtmlGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Set;

@Component
public class MailNotificationSender {

	public static final Logger logger = LoggerFactory.getLogger(MailNotificationSender.class);

	@Autowired
	private HtmlGenerator htmlGenerator;

	@Autowired
	private JavaMailSender mailSender;

	@Value("${FromAddress}")
	private String fromAddress;

	@Value("${TestOnly:false}")
	private boolean testOnly;

	@Autowired
	private MailConfiguration mailConfiguration;

	@Autowired
	private TaskExecutor taskExecutor;

	public void pushAsync(GitHubPush push) {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				push(push);
			}
		});
	}

	public void push(GitHubPush push) {
		try {
			GitHubRepository gitHubRepository = push.getRepository();
			if (gitHubRepository == null) {
				logger.warn("Push with no repository section");
				return;
			}
			String url = gitHubRepository.getUrl();

			Set<String> recipients = mailConfiguration.getRecipients(url);
			if (recipients == null || recipients.isEmpty()) {
				logger.info("No recipients for repository " + url);
				return;
			}

			if (testOnly) {
				logger.info("TEST - Would notify about push to " + url + ": " + recipients);
				return;
			}

			MimeMessage message = mailSender.createMimeMessage();
			message.setFrom(new InternetAddress(fromAddress));

			Address[] addresses = new Address[recipients.size()];
			int i = 0;
			for (String recipient : recipients) {
				addresses[i] = new InternetAddress(recipient);
				i++;
			}

			message.setRecipients(Message.RecipientType.TO, addresses);
			String subject = buildSubject(push);
			message.setSubject(subject);

			String content = htmlGenerator.render("push.ftl", push);
			message.setContent(content, "text/html");

			mailSender.send(message);
			logger.info("Notified " + addresses.length + " about push to " + push.getRepository().getFull_name());
		} catch (Exception e) {
			logger.error("Unable to send message: " + e.getMessage(), e);
		}
	}

	private String buildSubject(GitHubPush push) {
		int numCommits = push.getCommits().size();
		String name = push.getRepository().getFull_name();
		return "GitHub Push: " + numCommits + " commits in " + name;
	}

}
