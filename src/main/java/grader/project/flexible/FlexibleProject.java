package grader.project.flexible;

import framework.grading.testing.Checkable;
import framework.grading.testing.TestCase;
import grader.basics.execution.CommandGenerator;
import grader.basics.execution.RunningProject;
import grader.execution.ProxyBasedClassesManager;
import grader.execution.ProxyClassLoader;
import grader.project.folder.RootCodeFolder;
import grader.project.source.ClassesTextManager;
import grader.project.view.ClassViewManager;

import java.util.List;
import java.util.Map;

import wrappers.framework.project.ProjectWrapper;

import com.thoughtworks.qdox.JavaDocBuilder;

public interface FlexibleProject {

    public FlexibleClassesManager getClassesManager();

    public void setClassesManager(ProxyBasedClassesManager aClassesManager);

    public ClassViewManager getClassViewManager();

    public void setClassViewManager(ClassViewManager aClassViewManager);

    public ClassesTextManager getClassesTextManager();

    public void setClassesTextManager(ClassesTextManager aClassesTextManager);

    public void setProjectFolder(String aProjectFolder);

    public String getProjectFolderName();

    public String getSourceProjectFolderName();

    public String getBinaryProjectFolderName();

    public List<Class> getImplicitlyLoadedClasses();

    public RootCodeFolder getRootCodeFolder();

    public ProxyClassLoader getClassLoader();

    public Thread run(String mainClassName, String[][] args, String[] anInputFiles, String[] anOutputFiles);

    String getOutputFolder();

    void setOutputFolder(String outputFolder);

    public void maybeMakeClassDescriptions();

    public void setHasBeenRun(boolean newVal);

    public void setCanBeRun(boolean newVal);

    public boolean canBeRun();

    public boolean hasBeenRun();
    
    public boolean hasBeenCompiled();
    
    public boolean canBeCompiled();
    
    public void setHasBeenCompiled(boolean newVal);    
    
    public void setCanBeCompiled(boolean newVal);

    public String getOutputFileName();

    String getSourceFileName();
    
    String getCheckStyleFileName();

    boolean runChecked();

    public boolean setRunParameters(String aMainClassName, String anArgs[][], String[] anInputFiles, String[] anOutputFiles, CommandGenerator aMainClassFinder);

    Thread runProject();

    JavaDocBuilder getJavaDocBuilder();

	StringBuffer getCurrentOutput();

	void clearOutput();

	void setCurrentOutput(StringBuffer currentOutput);

	Checkable getCurrentGradingFeature();

	void setCurrentGradingFeature(Checkable newVal);
	TestCase getCurrentTestCase();

	void setCurrentTestCase(TestCase newVal);

	String getCurrentInput();

	void setCurrentInput(String currentInput);

	String[] getCurrentArgs();

	void setCurrentArgs(String[] currentArgs);

	String getSourceSuffix();

	boolean isNoProjectFolder();

	void setNoProjectFolder(boolean noProjectFolder);
	
	boolean hasUnCompiledClasses();
	public List<String> getNonCompiledClasses();
	public void addNonCompiledClass(String newVal);
	public boolean hasCompiledClasses() ;

	public List<String> getCompiledClasses() ;

	public void addCompiledClass(String newVal) ;

	void appendCurrentInput(String aCurrentInput);

	void setNewClassLoader();
	public boolean hasBeenLoaded();

	public void setHasBeenLoaded(boolean hasBeenLoaded) ;

	public boolean canBeLoaded() ;

	public void setCanBeLoaded(boolean canBeLoaded) ;

	boolean isFilesCompiled();

	void setFilesCompiled(boolean filesCompiled);

	boolean isFilesUnzipped();

	void setFilesUnzipped(boolean filesUnzipped);

	String getCheckstyleText();
	public Map<String, String> getEntryPoints() ;
	public void setEntryPoints(Map<String, String> entryPoints) ;

	RunningProject getCurrentRunningProject();
	void setCurrentRunningProject(RunningProject currentRunningProject);
	ProjectWrapper getWrapper();
	
		void setWrapper(ProjectWrapper newValue);

}
