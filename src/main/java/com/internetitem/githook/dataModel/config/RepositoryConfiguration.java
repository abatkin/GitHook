package com.internetitem.githook.dataModel.config;

import com.internetitem.githook.pluggable.Notifier;

import java.util.List;

public interface RepositoryConfiguration {
	List<Notifier> getNotifiers();
}
