package com.internetitem.githook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.internetitem.githook.html.HtmlProvider;
import freemarker.log.Logger;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;
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

	public static final Version FREEMARKER_VERSION = new Version(2, 3, 21);

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
		factory.setProviders(Arrays.asList(jsonProvider(), htmlProvider()));
		factory.setResourceProviders(resourceProviders);
		return factory.create();
	}

	@Bean
	public Object htmlProvider() {
		return new HtmlProvider();
	}

	@Bean
	public freemarker.template.Configuration freemarkerConfiguration() {
		try {
			Logger.selectLoggerLibrary(Logger.LIBRARY_SLF4J);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Error setting up logging library: " + e.getMessage(), e);
		}
		freemarker.template.Configuration fmConfig = new freemarker.template.Configuration(FREEMARKER_VERSION);
		fmConfig.setClassForTemplateLoading(CxfConfiguration.class, "/templates");
		fmConfig.setObjectWrapper(new DefaultObjectWrapperBuilder(FREEMARKER_VERSION).build());
		fmConfig.setDefaultEncoding("UTF-8");
		fmConfig.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		return fmConfig;
	}

	@Bean
	public JacksonJaxbJsonProvider jsonProvider(){
		JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
		provider.setMapper(mapper());
		return provider;
	}

	@Bean
	public ObjectMapper mapper() {
		ObjectMapper mapper = new ObjectMapper();
		return mapper;
	}

}
