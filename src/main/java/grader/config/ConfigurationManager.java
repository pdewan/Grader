package grader.config;

import org.apache.commons.configuration.PropertiesConfiguration;

import grader.basics.config.BasicConfigurationManager;

public interface ConfigurationManager extends BasicConfigurationManager{
	public  PropertiesConfiguration getStaticConfiguration() ;
	public  void setStaticConfiguration(
			PropertiesConfiguration staticConfiguration) ;
	public  PropertiesConfiguration getDynamicConfiguration() ;
	public  void setDynamicConfiguration(
			PropertiesConfiguration dynamicConfiguration) ;
	void init(String[] args);
	PropertiesConfiguration getCourseConfiguration();
	void setCourseConfiguration(PropertiesConfiguration newVal);
	PropertiesConfiguration getDynamicModuleConfiguration();
	void setDynamicModuleConfiguration(PropertiesConfiguration newVal);

}
