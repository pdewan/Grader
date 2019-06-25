package grader.execution;

import java.util.List;

import grader.basics.execution.BasicExecutionSpecification;

public interface ExecutionSpecification extends BasicExecutionSpecification {

	public abstract void loadFromConfiguration();
	String getDynamicExecutionFileName();
//	String getCObjSuffix();
//	String getExecutorDirectory();
//
//	public abstract List<String> getProcessTeams();
//
//	public abstract void setProcessTeams(List<String> aProcessTeamNames);
	String getStartOnyen();
	String getEndOnyen();
	String getProblemDownloadPath();
	void setGraderProblemDownloadPath(String newVal);
	void setGraderStartOnyen(String newVal);
	void setGraderEndOnyen(String newVal);
	String getAssignmentsDataFolder();
	String getInteractionLogDirectory();
	boolean isForceCompile();
	void setGraderForceCompile(boolean newVal);
	boolean isUnzipFiles();
	void setGraderUnzipFiles(boolean newVal);
	boolean isPreCompileMissingClasses();
	void setGraderPreCompileMissingClasses(boolean newVal);
	boolean isLoadClasses();
	void setGraderLoadClasses(boolean newVal);
	boolean isCompileMissingClasses();
	void setGraderCompileMissingClasses(boolean newVal);
	boolean isCheckStyle();
	void setCheckStyle(boolean newVal);
	String getRequirementsFormat();
	List<String> getAutoVisitActions();
	String getCCompiler();


//	public abstract List<String> getProcesses(String aProcessTeam);
//
//	public abstract void setProcesses(String aProcessTeam,
//			List<String> aProcesses);

//	public abstract Integer getSleepTime(String aProcess);
//
//	public abstract void setSleepTime(String aProcess, int aSleepTime);

//	public abstract String getEntryTag(String aProcess);
//
//	public abstract void setEntryTag(String aProcess, String anEntryTag);
//	
//	public abstract List<String> getEntryTags(String aProcess);
//
//	public abstract void setEntryTags(String aProcess, List<String> anEntryTags);
	
	
	
//	public abstract String getEntryPoint(String aProcess);
//
//	public abstract void setEntryPoint(String aProcess, String anEntryPoint);

//	public abstract List<String> getArgs(String aProcess);
//
//	public abstract void setArgs(String aProcess, List<String> anEntryArgs);
//	public List<String> getStartTags(String aProcess) ;
//	public void setStartTags(String aProcess, List<String> aStartTags) ;
//	public List<String> getTerminatingProcesses(String aProcessTeam) ;
//	public void setTerminatingProcesses(String aProcessTeam, List<String> aProcesses) ;
}