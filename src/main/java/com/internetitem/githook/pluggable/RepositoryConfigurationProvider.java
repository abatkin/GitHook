package com.internetitem.githook.pluggable;

import com.internetitem.githook.dataModel.config.RepositoryConfiguration;

public interface RepositoryConfigurationProvider {
	RepositoryConfiguration getRepositoryConfiguration(String repositoryUrl);
}
