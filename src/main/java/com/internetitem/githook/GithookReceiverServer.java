package com.internetitem.githook;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration
public class GithookReceiverServer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(GithookReceiverServer.class);
	}

	public static void main(String[] args) {
		new GithookReceiverServer().configure(new SpringApplicationBuilder(GithookReceiverServer.class)).run(args);
	}

}
