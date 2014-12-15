package com.internetitem.githook;

import ch.qos.logback.classic.ViewStatusMessagesServlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.internetitem.githook.html.HtmlProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ImportResource({"classpath:META-INF/cxf/cxf.xml"})
public class CxfConfiguration {

	private static final Log logger = LogFactory.getLog(CxfConfiguration.class);

	@Autowired
	private ApplicationContext ctx;

	@Bean
	public ServletRegistrationBean getCxfServletRegistrationBean() {
		return new ServletRegistrationBean(new CXFServlet(), "/services/*");
	}

	@Bean
	public ServletRegistrationBean getLogbackServletRegistrationBean() {
		return new ServletRegistrationBean(new ViewStatusMessagesServlet(), "/logback");
	}

	@Bean
	public Server jaxRsServer() {
		// Find all beans annotated with @Path
		List<Object> serviceBeans = new ArrayList<>(ctx.getBeansWithAnnotation(Path.class).values());
		logger.info("Registering service beans: " + serviceBeans);

		// Find all beans annotated with @Providers
		List<Object> providers = new ArrayList<>(ctx.getBeansWithAnnotation(Provider.class).values());
		logger.info("Registering providers: " + providers);

		JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
		factory.setBus(ctx.getBean(SpringBus.class));
		factory.setAddress("/");
		factory.setServiceBeans(serviceBeans);
		factory.setProviders(providers);
		Server server = factory.create();

		if (ctx.getEnvironment().getProperty("logRequests", Boolean.class, Boolean.FALSE).booleanValue()) {
			server.getEndpoint().getInInterceptors().add(new LoggingInInterceptor());
		}

		return server;
	}

	@Bean
	public HtmlProvider htmlProvider() {
		return new HtmlProvider();
	}

	@Bean
	public JacksonJaxbJsonProvider jsonProvider() {
		JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
		provider.setMapper(mapper());
		return provider;
	}

	@Bean
	public ObjectMapper mapper() {
		return new ObjectMapper();
	}

}
