package com.internetitem.githook.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internetitem.githook.GithookReceiverServer;
import com.internetitem.githook.dataModel.ws.GitHubCommit;
import com.internetitem.githook.dataModel.ws.GitHubPush;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GithookReceiverServer.class)
public class DataModelTests {

	@Autowired
	private ObjectMapper mapper;

	@Test
	public void testParsing() throws IOException {
		InputStream istream = new DataModelTests().getClass().getResourceAsStream("/test.json");
		assertNotNull(istream);

		GitHubPush push = mapper.readValue(istream, GitHubPush.class);
		assertNotNull(push);

		List<GitHubCommit> commits = push.getCommits();
		assertNotNull(commits);
		assertEquals(1, commits.size());
		GitHubCommit commit = commits.get(0);
		assertNotNull(commit.getAuthor());
		assertNotNull(commit.getCommitter());

		assertEquals("https://github.com/abatkin/TestRepository/compare/89f0c9fa414a...d2a9afefce41", push.getCompare());
	}
}
