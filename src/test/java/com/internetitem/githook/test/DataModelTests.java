package com.internetitem.githook.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internetitem.githook.GithookReceiverServer;
import com.internetitem.githook.dataModel.GithubPush;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.InputStream;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GithookReceiverServer.class)
public class DataModelTests {

	@Autowired
	private ObjectMapper mapper;

	@Test
	public void testParsing() throws IOException {
		InputStream istream = new DataModelTests().getClass().getResourceAsStream("/test.json");
		Assert.assertNotNull(istream);

		System.err.println("mapper: " + mapper);
		GithubPush push = mapper.readValue(istream, GithubPush.class);
		Assert.assertNotNull(push);
	}
}
