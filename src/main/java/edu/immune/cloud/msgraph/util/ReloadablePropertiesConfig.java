package edu.immune.cloud.msgraph.util;

import java.io.File;
import java.util.Properties;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * Reloadable Properties Setup Configuration Handler
 * @author Lalit Mehra
 *
 */
@Component
public class ReloadablePropertiesConfig {

	@Bean
	@ConditionalOnProperty(name = "spring.config.location", matchIfMissing = false)
	public PropertiesConfiguration propertiesConfiguration(@Value("${spring.config.location}") String path,
			@Value("${spring.properties.refreshDelay}") long refreshDelay) throws Exception {
		String filePath = path.substring("file:".length());
		PropertiesConfiguration configuration = new PropertiesConfiguration(new File(filePath).getAbsolutePath());
		FileChangedReloadingStrategy fileChangedReloadingStrategy = new FileChangedReloadingStrategy();
		fileChangedReloadingStrategy.setRefreshDelay(refreshDelay);
		configuration.setReloadingStrategy(fileChangedReloadingStrategy);
		return configuration;
	}

	@Bean
	@ConditionalOnBean(PropertiesConfiguration.class)
	@Primary
	public Properties properties(PropertiesConfiguration propertiesConfiguration) throws Exception {
		ReloadableProperties properties = new ReloadableProperties(propertiesConfiguration);
		return properties;
	}
	
}
