package com.internetitem.githook;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath("/services")
public class JerseyConfiguration extends ResourceConfig {
	public JerseyConfiguration() {
		register(Githook.class);
		register(JacksonJaxbJsonProvider.class);
	}
}
