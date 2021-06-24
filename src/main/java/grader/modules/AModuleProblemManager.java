package grader.modules;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.config.BasicStaticConfigurationUtils;
import grader.config.ConfigurationManagerSelector;
import grader.config.ExecutionSpecificationSelector;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class AModuleProblemManager implements ModuleProblemManager{
//	public static final String MODULES = "modules";
//	public static final String GENERIC_COURSE = "GenericCourse";
	PropertiesConfiguration configuration, dynamicConfiguration;
	PropertiesConfiguration courseConfiguration, dynamicModuleConfiguration;
//	GraderSettingsManager graderSettingsManager = GraderSettingsManagerSelector.getGraderSettingsManager();
	List<String> modules;
	public AModuleProblemManager() {
		init();
		
				
//				GraderSettingsManagerSelector.getGraderSettingsManager();
//		configuration = ConfigurationManagerSelector.getConfigurationManager().getStaticConfiguration();
//		dynamicConfiguration = ConfigurationManagerSelector.getConfigurationManager().getDynamicConfiguration();
//		courseConfiguration = ConfigurationManagerSelector.getConfigurationManager().getCourseConfiguration();
//		dynamicModuleConfiguration = ConfigurationManagerSelector.getConfigurationManager().getDynamicModuleConfiguration();
		
	}
	// this may be executed twice
	@Override
	public void init() {
		configuration = ConfigurationManagerSelector.getConfigurationManager().getStaticConfiguration();
		dynamicConfiguration = ConfigurationManagerSelector.getConfigurationManager().getDynamicConfiguration();
		courseConfiguration = ConfigurationManagerSelector.getConfigurationManager().getCourseConfiguration();
		dynamicModuleConfiguration = ConfigurationManagerSelector.getConfigurationManager().getDynamicModuleConfiguration();
	}
//	@Override
//	public void init(GraderSettingsManager aGraderSettingsManager) {
//		graderSettingsManager = aGraderSettingsManager;
//	}
	
	public void saveModules() {
		try {
			if (dynamicModuleConfiguration == null) {
				System.err.println("null dynamic module config");
				return;
			}
			dynamicModuleConfiguration.setProperty(BasicStaticConfigurationUtils.MODULES, getModules());
	        boolean aSaveSettings = ExecutionSpecificationSelector.getExecutionSpecification().getSaveInteractiveSettings();
	        if (aSaveSettings) {
			dynamicModuleConfiguration.save();
	        }
        } catch (ConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
	}
//	// side effect, reading modules will save them
//	public List<String> getModules() {
//		if (modules != null) return modules;
//		List objectModules = null;
//		if (courseConfiguration != null) {
//			 objectModules = courseConfiguration.getList(MODULES);
//		 }
//		
//		if (objectModules != null && objectModules.isEmpty()) {
//			System.err.println("No modules found in modules.properties, using the ones in config");
//		}
//		
//		if (objectModules == null || objectModules.isEmpty()) {
//		
////		 List objectModules = configuration.getList("modules");
////		 List objectModules = configuration.getList(MODULES);
//			if (configuration != null) {
//		objectModules = configuration.getList(MODULES);
//			} else {
//				System.err.println("Null configuration");
//				return new ArrayList(); // when we are rnning code without the grader running such as a C compiler of checkstyle
//			}
//		}
//		
//
////		 List<String> 	modules = objectModules;
//		modules = objectModules;
//
//			if (modules == null || objectModules.size() == 0) {
//				modules = new ArrayList();
////				Tracer.error("No modules specified in configuration file!");
////				modules.add("GenericCourse");
//				modules.add(GENERIC_COURSE);
////				Tracer.error("No modules specified in configuration file!");
////				System.exit(-1);
//			}
//			saveModules();
//			
//		return modules;
//		
//	}
	// side effect, reading modules will save them
	public List<String> getModules() {
		if (modules != null) return modules;
		List objectModules = null;
		objectModules = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getModules();
		if (objectModules == null) {
			objectModules = new ArrayList();
		}
		if (objectModules.isEmpty()) {
			objectModules.add(BasicStaticConfigurationUtils.DEFAULT_MODULE);
		}
		modules = objectModules;
		return objectModules;
//		if (courseConfiguration != null) {
//			 objectModules = courseConfiguration.getList(MODULES);
//		 }
//		
//		if (objectModules != null && objectModules.isEmpty()) {
//			System.err.println("No modules found in modules.properties, using the ones in config");
//		}
//		
//		if (objectModules == null || objectModules.isEmpty()) {
//		
////		 List objectModules = configuration.getList("modules");
////		 List objectModules = configuration.getList(MODULES);
//			if (configuration != null) {
//		objectModules = configuration.getList(MODULES);
//			} else {
//				System.err.println("Null configuration");
//				return new ArrayList(); // when we are rnning code without the grader running such as a C compiler of checkstyle
//			}
//		}
		

////		 List<String> 	modules = objectModules;
//		modules = objectModules;
//
//			if (modules == null || objectModules.size() == 0) {
//				modules = new ArrayList();
////				Tracer.error("No modules specified in configuration file!");
////				modules.add("GenericCourse");
//				modules.add(GENERIC_COURSE);
////				Tracer.error("No modules specified in configuration file!");
////				System.exit(-1);
//			}
//			saveModules();
//			
//		return modules;
		
	}
	
//	@Override
//	public String replaceModuleProblemVars(String  original) {
//		String moduleName = graderSettingsManager.getModule();
//		String problemName = graderSettingsManager.getProblem(moduleName);
//		String retVal = original;
////		String problemName = dynamicConfiguration.getString(AGraderSettingsModel.MODULE + "." + AGraderSettingsModel.MODULE);
//		retVal = retVal.replace("{moduleName}", moduleName);
//		retVal = retVal.replace("{ModuleName}", moduleName);
//		retVal = retVal.replace("{modulename}", moduleName.toLowerCase());
//		
//		retVal = retVal.replace("{problemName}", problemName);
//		retVal = retVal.replace("{ProblemName}", problemName);
//		retVal = retVal.replace("{problemname}", problemName.toLowerCase());
//		return retVal;
//		
//	}
    public String getModulePrefix(String aModule) {
    	String retVal =  configuration.getString(aModule + ".problems.prefix")	;

		if (retVal == null)
			retVal = configuration.getString("default.problems.prefix", "Assignment");
		return retVal;
	}
    
//    public String getCurrentModule() {
//    	List<String> modules = getModules();
//    	return  dynamicConfiguration.getString("currentModule", modules.get(0));
//    	
//    }

//	public String getProblemsAndCurrentProblem(String aModule, String downloadPath, List<String> problems) {
//		problems.clear();
////		List<String> problems = new ArrayList();
//		String currentModulePrefix =getModulePrefix(aModule);
//		problems.clear();
//		if (downloadPath != null) {
//			File folder = new File(downloadPath);
//			if (!folder.exists()) {
//				Tracer.error("No folder found for:" + downloadPath);				
//			} else {
//				File gradesFile = new File(downloadPath + "/grades.csv"); // is this a sakai assignment folder
//				if (gradesFile.exists()) 
//					folder = folder.getParentFile();
//				File[] children = folder.listFiles();
//				for (File child:children) {
//					if (child.getName().startsWith(currentModulePrefix)) {
//						problems.add(child.getName());
//					}
//				}
//			}
//		}
//		if (problems.size() > 0)
//			return problems.get(problems.size() - 1);
//		else
//			return null;
////		return problems;
//	}
	
	
	
	
//	public void setProblems(String aModule, String aProblemDirectory, List<String> aProblems) {
//		
//		
//	}
//	public String getStartingOnyen(String aModule, String aProblem) {
//		return null;
//	}
//	
//	public void setStartingOnyen(String aModule, String aProblem, String aStartOnyen) {
//		;
//	}
//	
//	public String getEndingOnyen(String aModule, String aProblem) {
//		return null;
//	}
//	
//	public void setEndingOnyen(String aModule, String aProblem, String anEndOnyen) {
//		;
//	}
//	
//	public NavigationKind getNavigationKind(String aModule, String aProblem) {
//		return null;
//	}
//	
//	public void setNavigationKind(String aModule, String aProblem, NavigationKind aNavigationKind) {
//		;
//	}
//	
//	public Boolean getAnimateGrades(String aModule, String aProblem) {
//		return null;
//	}
//	
//	public void setAnimateGrades(String aModule, String aProblem, boolean newVal) {
//		;
//	}
//	
//	public Integer getAnimationPauseTime(String aModule, String aProblem) {
//		return null;
//	}
//	
//	public NavigationFilter getAnimatePauseTime(String aModule, String aProblem) {
//		return null;
//	}
//	
//	public Object getFilterOption(String aModule, String aProblem) {
//		return null;
//	}
//
//	public void setFilterOption(String aModule, String aProblem, Object newVal ) {
//		;
//	}


}
