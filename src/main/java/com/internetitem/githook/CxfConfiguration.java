package com.internetitem.githook;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.ResourceProvider;
import org.apache.cxf.jaxrs.spring.SpringResourceFactory;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import javax.ws.rs.Path;
import java.util.Arrays;
import java.util.LinkedList;

@Configuration
@ImportResource({"classpath:META-INF/cxf/cxf.xml"})
public class CxfConfiguration {

	@Autowired
	private ApplicationContext ctx;


	@Bean
	public ServletRegistrationBean getServletRegistrationBean() {
		return new ServletRegistrationBean(new CXFServlet(), "/services/*");
	}

	@Bean
	public Server jaxRsServer() {
		LinkedList<ResourceProvider> resourceProviders = new LinkedList<>();
		for (String beanName : ctx.getBeanDefinitionNames()) {
			if (ctx.findAnnotationOnBean(beanName, Path.class) != null) {
				SpringResourceFactory factory = new SpringResourceFactory(beanName);
				factory.setApplicationContext(ctx);
				resourceProviders.add(factory);
			}
		}

		JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
		factory.setBus(ctx.getBean(SpringBus.class));
		factory.setProviders(Arrays.asList(jsonProvider()));
		factory.setResourceProviders(resourceProviders);
		return factory.create();
	}

	@Bean
	public JacksonJsonProvider jsonProvider(){
		return new JacksonJsonProvider();
	}

}
