package com.internetitem.githook.dataModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubPush {
	// sender
	// pusher
	private GitHubRepository repository;
	// head_commit
	private List<GitHubCommit> commits;
	// ref
	private String before;
	private String after;
	// created
	// deleted
	private boolean forced;
	// base_ref
	private String compare;

	public String getBefore() {
		return before;
	}

	public void setBefore(String before) {
		this.before = before;
	}

	public String getAfter() {
		return after;
	}

	public void setAfter(String after) {
		this.after = after;
	}

	public GitHubRepository getRepository() {
		return repository;
	}

	public void setRepository(GitHubRepository repository) {
		this.repository = repository;
	}

	public List<GitHubCommit> getCommits() {
		return commits;
	}

	public void setCommits(List<GitHubCommit> commits) {
		this.commits = commits;
	}

	public boolean isForced() {
		return forced;
	}

	public void setForced(boolean forced) {
		this.forced = forced;
	}

	public String getCompare() {
		return compare;
	}

	public void setCompare(String compare) {
		this.compare = compare;
	}
}
