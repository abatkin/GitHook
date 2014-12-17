package com.internetitem.githook.pluggable;

public interface TemplateProvider {

	public static final String TEMPLATE_PUSHED = "pushed";

	String getTemplate(String templateName);
}
