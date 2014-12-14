package com.internetitem.githook;

import com.internetitem.githook.html.TemplateName;

@TemplateName(name = "testobject.ftl")
public class TestObject {
	private String name;
	private  String age;

	public TestObject(String name, String age) {
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
}
