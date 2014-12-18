package com.internetitem.githook.html;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

@Component
public class HtmlGenerator {

	@Autowired
	private Configuration freemarkerConfig;

	public String render(String templateName, Object obj) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		render(templateName, obj, baos);
		return baos.toString("UTF-8");
	}

	public void render(String templateName, Object obj, OutputStream out) throws IOException {
		Template template = freemarkerConfig.getTemplate(templateName);
		try {
			template.process(obj, new OutputStreamWriter(out, "UTF-8"));
		} catch (TemplateException e) {
			throw new IOException("Error processing templateName [" + templateName + "]: " + e.getMessage(), e);
		}
	}
}
