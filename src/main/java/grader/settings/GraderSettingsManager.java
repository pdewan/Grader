package grader.settings;

import grader.navigation.NavigationKind;

public interface GraderSettingsManager {
	
	public String getEditor();
	public void setEditor(String newValue);
	
    public String getStartingOnyen(String aModule);
	
	public void setStartingOnyen(String aModule, String aStartOnyen) ;
	
    public String getDownloadPath(String aModule);
	
	public void setDownloadPath(String aModule, String aNewValue) ;
	
	
	public String getEndingOnyen(String aModule) ;
	
	public void setEndingOnyen(String aModule, String anEndOnyen);
	
	public NavigationKind getNavigationKind(String aModule) ;
	
	public void setNavigationKind(String aModule, NavigationKind aNavigationKind);
	
	public Boolean getAnimateGrades(String aModule);
	
	public void setAnimateGrades(String aModule, boolean newVal);
	
	public Integer getAnimationPauseTime(String aModule) ;
	
	String getModule();
	void setModule(String newValue);
	void save();
	String getProblem(String aModule);
	void setProblem(String aModule, String aNewValue);
//	void init(ModuleProblemManager initValue);
	String replaceModuleProblemVars(String original);
	String getNormalizedProblem(String aModule);
	void setAnimationPauseTime(String aModule, Integer newVal);
//	NavigationFilter getNavigationFilter(String aModule);
//	void setNavigationFilter(String aModule, NavigationFilter aNavigationFilter);
//	
	String getNavigationFilter(String aModule);
	void setNavigationFilter(String aModule, String aNavigationFilter);
	String getNavigationFilterOption(String aModule,
			String aNavigationFilter);
	void setNavigationFilterOption(String aModule,
			String aNavigationFilter, Object anOption);
//	Object getNavigationFilterOption(String aModule,
//			NavigationFilter aNavigationFilter);
//	void setNavigationFilterOption(String aModule,
//			NavigationFilter aNavigationFilter, Object anOption);
	String getWordPath();
	void setWordPath(String aNewValue);
	String getCCompilerPath();
	void setCCompilerPath(String aNewValue);
	String getDiff();
	void setDiff(String newValue);
	String getPythonInterpreterPath();
	void setPythonInterpreterPath(String aNewValue);
	void init();
	String getTestProjectSrc();
	void setTestProjectSrc(String newValue);
	void setNormalizedProblem(String aModule, String aNewValue);
	void setNormalizedModule(String newValue);
	

}
