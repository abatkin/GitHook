package com.internetitem.githook;

import com.internetitem.githook.config.MailConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;

@ComponentScan(basePackageClasses = {MailSenderAutoConfiguration.class, GithookReceiverServer.class})
@EnableAutoConfiguration
public class GithookReceiverServer extends SpringBootServletInitializer {

	@Value("${RecipientConfig}")
	private String recipientConfig;

	@Value("${SleepTime:10000}")
	private long sleepTime;

	@Bean
	public MailConfiguration getMailConfiguration() throws IOException {
		if (sleepTime < 5000) {
			sleepTime = 5000;
		}
		return MailConfiguration.load(recipientConfig, sleepTime);
	}

	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(5);
		return executor;
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(GithookReceiverServer.class);
	}

	public static void main(String[] args) {
		new GithookReceiverServer().configure(new SpringApplicationBuilder(GithookReceiverServer.class)).run(args);
	}

}
