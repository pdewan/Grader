package grader.config;

import grader.basics.config.ABasicExecutionSpecification;
import grader.basics.config.BasicConfigurationManagerSelector;
import grader.basics.config.BasicStaticConfigurationUtils;
import grader.basics.execution.GradingMode;
import grader.basics.project.CurrentProjectHolder;
import util.trace.Tracer;

import static grader.basics.config.BasicStaticConfigurationUtils.DEFAULT_USE_PROJECT_CONFIGURATION;
import static grader.basics.config.BasicStaticConfigurationUtils.USE_PROJECT_CONFIGURATION;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.PropertiesConfiguration;

public class AnExecutionSpecification extends ABasicExecutionSpecification implements ExecutionSpecification {
//	protected List<String> processTeams = new ArrayList<>();
//	protected Map<String, List<String>> processTeamToProcesses = new HashMap<>();
//	protected Map<String, List<String>> processTeamToTerminatingProcesses = new HashMap<>();
//	protected Map<String, Integer> processToSleepTime = new HashMap<>();
//	protected Map<String, String> processToEntryTag = new HashMap<>();
//	protected Map<String, List<String>> processToEntryTags = new HashMap<>();
//	protected Map<String, String> processToEntryPoint = new HashMap<>();
//	protected Map<String, List<String>> processToArgs = new HashMap<>();
//	protected Map<String, List<String>> processToStartTags = new HashMap<>();
	
	public AnExecutionSpecification() {
		
	}
	
//	protected void loadEntryTag(String aProcess) {
//		String entryTag = StaticConfigurationUtils.getEntryTag(aProcess);
//		if (entryTag != null)
//			graderProcessToEntryTag.put(aProcess, StaticConfigurationUtils.getEntryTag(aProcess));
//	}
//	
//	protected void loadStartTags(String aProcess) {
//		List<String> startTags = StaticConfigurationUtils.getProcessStartTags(aProcess);
//		if (startTags != null)
//			graderProcessToStartTags.put(aProcess, startTags);
//	}
//	
//	protected void loadEntryTags(String aProcess) {
//		List<String> entryTags = StaticConfigurationUtils.getEntryTags(aProcess);
//		if (entryTags  != null)
//			graderProcessToEntryTags.put(aProcess, entryTags);
//	}
//	protected void loadEntryPoint(String aProcess) {
//		String anEntryPoint = StaticConfigurationUtils.getEntryPoint(aProcess);
//		if (anEntryPoint  != null)
//			graderProcessToEntryPoint.put(aProcess, anEntryPoint);
//	}
//	protected void loadSleepTime(String aProcess) {
//		Integer sleepTime = StaticConfigurationUtils.getSleepTime(aProcess);
//		if (sleepTime != null)
//			graderProcessToSleepTime.put(aProcess, sleepTime);
//	}
//	
//	protected void loadArgs(String aProcess) {
//		List<String> args = StaticConfigurationUtils.getProcessArgs(aProcess);
//		if (args != null)
//			graderProcessToArgs.put(aProcess, args);
//	}
//	
//	/* (non-Javadoc)
//	 * @see grader.execution.ExecutionSpecification#loadFromConfiguration()
//	 */
	@Override
	/**
	 * Not sure we need this method but let us keep it for now
	 */
	public void loadFromConfiguration() {
//		processTeams = StaticConfigurationUtils.getProcessTeams();
//
//		for (String aProcessTeam:processTeams) {
//			List<String> aProcesses =  StaticConfigurationUtils.getProcesses(aProcessTeam);
//			graderProcessTeamToProcesses.put(aProcessTeam, aProcesses);
//			List<String> aTerminatingProcesses =  StaticConfigurationUtils.getTerminatingProcesses(aProcessTeam);
//			graderProcessTeamToTerminatingProcesses.put(aProcessTeam, aTerminatingProcesses);
//
//			for (String aProcess:aProcesses) {
//				loadSleepTime(aProcess);
//				loadEntryTag(aProcess);
//				loadEntryTags(aProcess);
//				loadEntryPoint(aProcessTeam);
//				loadStartTags(aProcess);
//				loadArgs(aProcess);
////				Integer sleepTime = StaticConfigurationUtils.getSleepTime(aProcess);
////				if (sleepTime != null)
////					processToSleepTime.put(aProcess, sleepTime);
//				
//				
////				
////				String entryTag = StaticConfigurationUtils.getEntryTag(aProcess);
////				if (entryTag != null)
////					processToEntryTag.put(aProcess, StaticConfigurationUtils.getEntryTag(aProcess));
//				
////				List<String> args = StaticConfigurationUtils.getProcessArgs(aProcess);
////				if (args != null)
////					processToArgs.put(aProcess, args);
////				List<String> startTags = StaticConfigurationUtils.getProcessStartTags(aProcess);
////				if (startTags != null)
////					processToStartTags.put(aProcess, args);
//				
//			}
//		}
		
	}
//	@Override
//    public String getStringProperty(String aProperty, String aDefault) {
//    	String retVal = super.getStringProperty(aProperty, null);
//    	if (retVal != null) {
//    		return retVal;
//    	}
//    	retVal = StaticConfigurationUtils.getInheritedStringModuleProblemProperty(aProperty, null);
//    	if (retVal != null)
//    		return retVal;
//    	return aDefault;
//    	
//    }
	 PropertiesConfiguration dynamicConfiguration;
	 PropertiesConfiguration dynamicConfiguration() {
		 if (dynamicConfiguration == null) {
			 dynamicConfiguration = ConfigurationManagerSelector.getConfigurationManager().getDynamicConfiguration();
		 }
		 return dynamicConfiguration;
	 }
	 @Override
	 public  String getInheritedStringModuleProblemProperty(
				
				String aProperty, String defaultValue) {
//		 String retVal = dynamicConfiguration().getString(aProperty, null);
		 String retVal = StaticConfigurationUtils.getDynamicInheritedStringModuleProblemProperty(aProperty, null);

		 if (retVal != null) {
			 return retVal;
		 }
		  retVal = super.getInheritedStringModuleProblemProperty(aProperty, null);
		 if (retVal != null) {
			 return retVal;
		 }
		 return StaticConfigurationUtils.getInheritedStringModuleProblemProperty(aProperty, defaultValue);

//	    	if (!BasicStaticConfigurationUtils.isUseProjectConfiguration()) {
//	    		return defaultValue;
//	    	}
//			return BasicStaticConfigurationUtils.getInheritedStringModuleProblemProperty(BasicConfigurationManagerSelector.getConfigurationManager().getOrCreateProjectConfiguration(), BasicStaticConfigurationUtils.getModule(), BasicStaticConfigurationUtils.getProblem(), aProperty, defaultValue);

			
		}
	 @Override
	 public  Integer getInheritedIntegerModuleProblemProperty(
				
				String aProperty, Integer defaultValue) {
		 Integer retVal = StaticConfigurationUtils.getDynamicInheritedIntegerModuleProblemProperty(aProperty, null);
		 if (retVal != null) {
			 return retVal;
		 }
		 retVal = super.getInheritedIntegerModuleProblemProperty(aProperty, null);
		 if (retVal != null) {
			 return retVal;
		 }
		 return StaticConfigurationUtils.getInheritedIntegerModuleProblemProperty(aProperty, defaultValue);

//	    	if (!BasicStaticConfigurationUtils.isUseProjectConfiguration()) {
//	    		return defaultValue;
//	    	}
//			return BasicStaticConfigurationUtils.getInheritedStringModuleProblemProperty(BasicConfigurationManagerSelector.getConfigurationManager().getOrCreateProjectConfiguration(), BasicStaticConfigurationUtils.getModule(), BasicStaticConfigurationUtils.getProblem(), aProperty, defaultValue);

			
		}
	 @Override
	 public  Boolean getInheritedBooleanModuleProblemProperty(
				
				String aProperty, Boolean defaultValue) {
		 Boolean retVal = StaticConfigurationUtils.getDynamicInheritedIBooleanModuleProblemProperty(aProperty, null);
		 if (retVal != null) {
			 return retVal;
		 }
		 retVal = super.getInheritedBooleanModuleProblemProperty(aProperty, null);
		 if (retVal != null) {
			 return retVal;
		 }
		 return StaticConfigurationUtils.getInheritedBooleanModuleProblemProperty(aProperty, defaultValue);

//	    	if (!BasicStaticConfigurationUtils.isUseProjectConfiguration()) {
//	    		return defaultValue;
//	    	}
//			return BasicStaticConfigurationUtils.getInheritedStringModuleProblemProperty(BasicConfigurationManagerSelector.getConfigurationManager().getOrCreateProjectConfiguration(), BasicStaticConfigurationUtils.getModule(), BasicStaticConfigurationUtils.getProblem(), aProperty, defaultValue);

			
		}
	 @Override
	 public  List getInheritedListModuleProblemProperty(
				
				String aProperty, List<String> defaultValue) {
//		 List retVal = dynamicConfiguration().getList(aProperty, null);
//		 if (retVal != null) {
//			 return retVal;
//		 }
		 List retVal = super.getInheritedListModuleProblemProperty(aProperty, null);
		 if (retVal != null && !retVal.isEmpty()) {
			 return retVal;
		 }
		 return StaticConfigurationUtils.getInheritedListModuleProblemProperty(aProperty, defaultValue);

//	    	if (!BasicStaticConfigurationUtils.isUseProjectConfiguration()) {
//	    		return defaultValue;
//	    	}
//			return BasicStaticConfigurationUtils.getInheritedStringModuleProblemProperty(BasicConfigurationManagerSelector.getConfigurationManager().getOrCreateProjectConfiguration(), BasicStaticConfigurationUtils.getModule(), BasicStaticConfigurationUtils.getProblem(), aProperty, defaultValue);

			
		}
	 protected String getConfigurationDirectString(String aProperty, String aDefault) {
		 String retVal = dynamicConfiguration().getString(aProperty, null);
		 if (retVal != null) {
			 return retVal;
		 }
		   retVal = super.getConfigurationDirectString(aProperty, null);
		    if (retVal != null) {
		    	return retVal;
		    }
	    	return StaticConfigurationUtils.getCourseOrStaticString(aProperty, aDefault);
	  }
	 protected List getConfigurationDirectList(String aProperty, List aDefault) {
//		 List retVal = dynamicConfiguration().getList(aProperty, null);
//		 if (retVal != null) {
//			 return retVal;
//		 }
		   List  retVal = super.getConfigurationDirectList(aProperty, null);
		    if (retVal != null) {
		    	return retVal;
		    }
	    	return StaticConfigurationUtils.getCourseOrStaticList(aProperty, aDefault);
	  }
	@Override
	public String getDynamicExecutionFileName() {
//		return StaticConfigurationUtils.getInheritedStringModuleProblemProperty(AConfigurationManager.DYNAMIC_CONFIG_PROPERTY, AConfigurationManager.DYNAMIC_CONFIGURATION_FILE_NAME);
		return StaticConfigurationUtils.getDynamicExecutionFileName();
	}
	@Override
    public String getStartOnyen() {
    	return getStringProperty(StaticConfigurationUtils.START_ONYEN, "");
    }
	@Override
    public String getEndOnyen() {
    	return getStringProperty(StaticConfigurationUtils.END_ONYEN, "");
    }
	@Override
	public void setGraderStartOnyen(String newVal) {
		setGraderStringProperty(StaticConfigurationUtils.START_ONYEN, newVal);
	}
	
	@Override
	public void setGraderEndOnyen(String newVal) {
		setGraderStringProperty(StaticConfigurationUtils.END_ONYEN, newVal);
	}
	@Override
    public String getProblemDownloadPath() {
    	return getStringProperty(StaticConfigurationUtils.PROBLEM_PATH, null);
    }
	@Override
	public void setGraderProblemDownloadPath(String newVal) {
		setGraderStringProperty(StaticConfigurationUtils.PROBLEM_PATH, newVal);
	}
	@Override
	public String getAssignmentsDataFolder() {
		return getDirectStringProperty(StaticConfigurationUtils.ASSIGNMENTS_DATA_FOLDER, StaticConfigurationUtils.DEFAULT_ASSIGNMENTS_DATA_FOLDER);
	}
	@Override
	public String getInteractionLogDirectory() {
		return getDirectStringProperty(StaticConfigurationUtils.INTERACTION_LOG_DIRECTORY, StaticConfigurationUtils.INTERACTION_LOG_DIRECTORY);
	}
	@Override
	public String getRequirementsFormat() {
		return getStringProperty(StaticConfigurationUtils.REQUIREMENTS, StaticConfigurationUtils.DEFAULT_REQUIREMENTS);
	}
	@Override
	public List<String> getAutoVisitActions() {
		return getListProperty(StaticConfigurationUtils.VISIT_ACTIONS, StaticConfigurationUtils.DEFAULT_VISIT_ACTIONS);
	}
	
	@Override
	public boolean isForceCompile() {
		return getBooleanProperty(StaticConfigurationUtils.FORCE_COMPILE_CLASSES, StaticConfigurationUtils.DEFAULT_FORCE_COMPILE_CLASSES);
	}
	@Override
	public void setGraderForceCompile(boolean newVal) {
		runtimeGraderBooleanProperties.put(StaticConfigurationUtils.FORCE_COMPILE_CLASSES, newVal);
	}
	@Override
	public boolean isPreCompileMissingClasses() {
		return getBooleanProperty(StaticConfigurationUtils.PRE_COMPILE_MISSING_CLASSES, StaticConfigurationUtils.DEFAULT_PRE_COMPILE_CLASSES);
	}
	@Override
	public void setGraderPreCompileMissingClasses(boolean newVal) {
		runtimeGraderBooleanProperties.put(StaticConfigurationUtils.PRE_COMPILE_MISSING_CLASSES, newVal);
	}
	@Override
	public boolean isPrivacy() {
		return getBooleanProperty(StaticConfigurationUtils.PRIVACY, StaticConfigurationUtils.DEFAULT_PRIVACY);
	}
	@Override
	public void setPrivacy(boolean newVal) {
		runtimeGraderBooleanProperties.put(StaticConfigurationUtils.PRIVACY, newVal);

	}
	@Override
	public String getLoggers() {
		return getStringProperty(StaticConfigurationUtils.LOGGERS, StaticConfigurationUtils.DEFAULT_LOGGERS);

	}
	@Override
	public void setLoggers(String newVal) {
		runtimeGraderStringProperties.put(StaticConfigurationUtils.LOGGERS, newVal);


	}
	@Override
	public boolean isCompileMissingClasses() {
		return getBooleanProperty(StaticConfigurationUtils.COMPILE_MISSING_CLASSES, StaticConfigurationUtils.DEFAULT_COMPILE_MISSING_CLASSES);
	}
	@Override
	public void setGraderCompileMissingClasses(boolean newVal) {
		runtimeGraderBooleanProperties.put(StaticConfigurationUtils.COMPILE_MISSING_CLASSES, newVal);
	}
	@Override
	public boolean isLoadClasses() {
		return getBooleanProperty(StaticConfigurationUtils.LOAD_CLASSES, StaticConfigurationUtils.DEFAULT_LOAD_CLASSES);
	}
	@Override
	public void setGraderLoadClasses(boolean newVal) {
		runtimeGraderBooleanProperties.put(StaticConfigurationUtils.LOAD_CLASSES, StaticConfigurationUtils.DEFAULT_LOAD_CLASSES);
	}
	@Override
	public boolean isUnzipFiles() {
		return getBooleanProperty(StaticConfigurationUtils.UNZIP_FILES, StaticConfigurationUtils.DEFAULT_UNZIP_FILES);
	}
	@Override
	public void setGraderUnzipFiles(boolean newVal) {
		runtimeGraderBooleanProperties.put(StaticConfigurationUtils.UNZIP_FILES, newVal);
	}
	@Override
	public boolean isCheckStyle() {
		return getBooleanProperty(StaticConfigurationUtils.CHECK_STYLE, StaticConfigurationUtils.DEFAULT_CHECK_STYLE);
	}
	@Override
	public void setCheckStyle(boolean newVal) {
		runtimeGraderBooleanProperties.put(StaticConfigurationUtils.CHECK_STYLE, newVal);
	}
	public  boolean isUseProjectConfiguration() {
		if (GradingMode.getGraderRun() // acctually will definitely be true as this class is being used
			&& CurrentProjectHolder.getCurrentProject() == null) {
			return false;
		}
		return super.isUseProjectConfiguration();
	}
	@Override
	public String getCCompiler() {
		return getStringProperty(
				StaticConfigurationUtils.C_COMPILER_PATH, StaticConfigurationUtils.DEFAULT_C_COMPILER_PATH );
		
//		return graderProcessToEntryTag.get(aProcess);
		
	}
	
	
//	 
//
//	@Override
//	public String getCObjSuffix() {
//		return StaticConfigurationUtils.getCourseOrStaticString(StaticConfigurationUtils.C_OBJ, StaticConfigurationUtils.DEFAULT_C_OBJ);
////		return  StaticConfigurationUtils.getInheritedStringModuleProblemProperty(StaticConfigurationUtils.C_OBJ, StaticConfigurationUtils.DEFAULT_C_OBJ);
//	}
//
//	@Override
//	public String getExecutorDirectory() {
//		return StaticConfigurationUtils.getCourseOrStaticString(StaticConfigurationUtils.EXECUTOR, StaticConfigurationUtils.DEFAULT_EXECUTOR);
//	}
	
//	/* (non-Javadoc)
//	 * @see grader.execution.ExecutionSpecification#getProcessTeams()
//	 */
//	@Override
//	public List<String> getProcessTeams() {
//		return processTeams;
//	}
//	
//	/* (non-Javadoc)
//	 * @see grader.execution.ExecutionSpecification#setProcessTeams(java.util.List)
//	 */
//	@Override
//	public void setProcessTeams(List<String> aProcessTeamNames) {
//		Tracer.info(this, "Setting process team: " + aProcessTeamNames);
//		processTeams = aProcessTeamNames;
//	}
//	
//	/* (non-Javadoc)
//	 * @see grader.execution.ExecutionSpecification#getProcesses(java.lang.String)
//	 */
//	@Override
//	public List<String> getProcesses(String aProcessTeam) {
//		return processTeamToProcesses.get(aProcessTeam);
//	}
//	
//	/* (non-Javadoc)
//	 * @see grader.execution.ExecutionSpecification#setProcesses(java.lang.String, java.util.List)
//	 */
//	@Override
//	public void setProcesses(String aProcessTeam, List<String> aProcesses) {
//		Tracer.info(this, "Setting processes: " + aProcessTeam + " = " + aProcesses);
//		processTeamToProcesses.put(aProcessTeam, aProcesses);
//	}
//	
//	@Override
//	public List<String> getTerminatingProcesses(String aProcessTeam) {
//		return processTeamToTerminatingProcesses.get(aProcessTeam);
//	}
//	
//	/* (non-Javadoc)
//	 * @see grader.execution.ExecutionSpecification#setProcesses(java.lang.String, java.util.List)
//	 */
//	@Override
//	public void setTerminatingProcesses(String aProcessTeam, List<String> aProcesses) {
//		Tracer.info(this, "Setting terminating processes: " + aProcessTeam + " - " + aProcesses);
//		processTeamToTerminatingProcesses.put(aProcessTeam, aProcesses);
//	}
//	/* (non-Javadoc)
//	 * @see grader.execution.ExecutionSpecification#getSleepTime(java.lang.String)
//	 */
//	@Override
//	public Integer getSleepTime(String aProcess) {
//		return processToSleepTime.get(aProcess);
//	}
//	
//	/* (non-Javadoc)
//	 * @see grader.execution.ExecutionSpecification#setSleepTime(java.lang.String, int)
//	 */
//	@Override
//	public void setSleepTime(String aProcess, int aSleepTime) {
//		Tracer.info(this, "Setting sleep time: " + aSleepTime);
//		 processToSleepTime.put(aProcess, aSleepTime);
//	}
//	
//	/* (non-Javadoc)
//	 * @see grader.execution.ExecutionSpecification#getEntrytag(java.lang.String)
//	 */
//	@Override
//	public String getEntryTag(String aProcess) {
//		return processToEntryTag.get(aProcess);
//	}
//	/* (non-Javadoc)
//	 * @see grader.execution.ExecutionSpecification#setEntryTag(java.lang.String, java.lang.String)
//	 */
//	@Override
//	public void setEntryTag(String aProcess, String anEntryTag) {
//		Tracer.info(this, "Setting entry tag processes: " + aProcess + " - " + anEntryTag);
//		 processToEntryTag.put(aProcess, anEntryTag);
//	}
//	/* (non-Javadoc)
//	 * @see grader.execution.ExecutionSpecification#getArgs(java.lang.String)
//	 */
//	@Override
//	public List<String> getArgs(String aProcess) {
//		return processToArgs.get(aProcess);
//	}
//	/* (non-Javadoc)
//	 * @see grader.execution.ExecutionSpecification#setArgs(java.lang.String, java.util.List)
//	 */
//	@Override
//	public void setArgs(String aProcess, List<String> anEntryArgs) {
//		Tracer.info(this, "Setting processes args: " + aProcess + " - " + anEntryArgs);
//		 processToArgs.put(aProcess, anEntryArgs);
//	}
//	
//	@Override
//	public List<String> getStartTags(String aProcess) {
//		return processToStartTags.get(aProcess);
//	}
//	/* (non-Javadoc)
//	 * @see grader.execution.ExecutionSpecification#setArgs(java.lang.String, java.util.List)
//	 */
//	@Override
//	public void setStartTags(String aProcess, List<String> aStartTags) {
//		Tracer.info(this, "Setting start tag processes: " + aProcess + " - " + aStartTags);
//		processToStartTags.put(aProcess, aStartTags);
//	}
//
//	@Override
//	public String getEntryPoint(String aProcess) {
//		return processToEntryPoint.get(aProcess);
//	}
//
//	@Override
//	public void setEntryPoint(String aProcess, String anEntryPoint) {
//		Tracer.info(this, "Setting entry point processes: " + aProcess + " - " + anEntryPoint);
//		processToEntryPoint.put(aProcess, anEntryPoint);
//	}
//
//	@Override
//	public List<String> getEntryTags(String aProcess) {
//		return processToEntryTags.get(aProcess);
//	}
//
//	@Override
//	public void setEntryTags(String aProcess, List<String> anEntryTags) {
//		Tracer.info(this, "Setting entry tags processes: " + aProcess + " - " + anEntryTags);
//		processToEntryTags.put(aProcess, anEntryTags);
//		
//	}

}
