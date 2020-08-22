package grader.config;

import grader.basics.config.BasicConfigurationManagerSelector;

public class ConfigurationManagerSelector {
	static ConfigurationManager configurationManager;
	//= new AConfigurationManager();

	public static ConfigurationManager getConfigurationManager() {
		return configurationManager;
	}

	public static void setConfigurationManager(
			ConfigurationManager configurationManager) {
		ConfigurationManagerSelector.configurationManager = configurationManager;
	}
	
	static {
		configurationManager = new AConfigurationManager();
		BasicConfigurationManagerSelector.setConfigurationManager(configurationManager);
	}
	

}
