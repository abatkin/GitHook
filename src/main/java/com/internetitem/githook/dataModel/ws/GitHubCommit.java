package com.internetitem.githook.dataModel.ws;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubCommit {

	private List<String> modified;
	private List<String> removed;
	private List<String> added;
	private String id;
	private String message;
	private String url;
	private GitHubCommitter author;
	private GitHubCommitter committer;

	public List<String> getModified() {
		return modified;
	}

	public void setModified(List<String> modified) {
		this.modified = modified;
	}

	public List<String> getRemoved() {
		return removed;
	}

	public void setRemoved(List<String> removed) {
		this.removed = removed;
	}

	public List<String> getAdded() {
		return added;
	}

	public void setAdded(List<String> added) {
		this.added = added;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public GitHubCommitter getAuthor() {
		return author;
	}

	public void setAuthor(GitHubCommitter author) {
		this.author = author;
	}

	public GitHubCommitter getCommitter() {
		return committer;
	}

	public void setCommitter(GitHubCommitter committer) {
		this.committer = committer;
	}
}
