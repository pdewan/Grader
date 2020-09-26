package framework.project;

import framework.execution.ARunningProject;
import framework.execution.InteractiveConsoleProcessRunner;
import framework.execution.ProcessRunner;
import framework.execution.ReflectionRunner;
import grader.basics.execution.NotRunnableException;
import grader.basics.execution.RunningProject;
import grader.basics.project.BasicProject;
import grader.basics.project.ClassesManager;
import grader.basics.project.CurrentProjectHolder;
import grader.basics.util.Option;
import grader.sakai.project.ASakaiProjectDatabase;
import grader.sakai.project.SakaiProject;
import grader.settings.GraderSettingsManager;
import grader.settings.GraderSettingsManagerSelector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import util.pipe.InputGenerator;

//import scala.Option;


/**
 * A "standard" project. That is, an IDE-based java project.
 */
public class StandardProject extends BasicProject implements FatProject {
    
//
//    private File directory;
//    private File sourceFolder;
//    private Option<ClassesManager> classesManager;
//    private TraceableLog traceableLog;
//    boolean noSrc;
    protected SakaiProject project;
    protected void setProject (Object aProject) {
    	project = (SakaiProject) aProject;
    }
    protected Option<ClassesManager> createClassesManager(File buildFolder) throws ClassNotFoundException, IOException {
//      classesManager = Option.apply((ClassesManager) new ProjectClassesManager(project, buildFolder, sourceFolder));
     CurrentProjectHolder.setProject(this);
     CurrentProjectHolder.setProjectLocation(this.projectFolder);
     // so that classes manager can find it
     return Option.apply((ClassesManager) new ProjectClassesManager(this, project, buildFolder, sourceFolder, sourceFilePattern));

  }
    /**
     * Basic constructor
     *
     * @param aDirectory The location of the project
     * @param name      The name of the project, such as "Assignment1"
     * @throws FileNotFoundException
     */

    // rewriting Josh's code
    public StandardProject(SakaiProject aProject, File aDirectory, String name, String aFilePattern) throws FileNotFoundException {
        super(aProject, aDirectory, name, aFilePattern);
        project = aProject; 

    }
    


    @Override
    public ARunningProject start(String input) throws NotRunnableException {
        return new ReflectionRunner(this).run(input);
    }

    @Override
    public RunningProject launch(String input) throws NotRunnableException {
        return new ProcessRunner(this).run(input);
    }

    @Override
    public RunningProject start(String input, int timeout) throws NotRunnableException {
        return new ReflectionRunner(this).run(input, timeout);
    }

    @Override
    public RunningProject launch(InputGenerator anOutputBasedInputGenerator, String input, int timeout) throws NotRunnableException {
        return new ProcessRunner(this).run(anOutputBasedInputGenerator, input, timeout);
    }
    @Override
    public RunningProject launch(InputGenerator anOutputBasedInputGenerator, Map<String, String> aProcessToInput, int timeout) throws NotRunnableException {
        return new ProcessRunner(this).run(anOutputBasedInputGenerator, aProcessToInput, timeout);
    }
    
    @Override
    public RunningProject launch( String input, int timeout) throws NotRunnableException {
        return new ProcessRunner(this).run(input, timeout);
    }
    @Override
    public RunningProject launch( String input, String[] anArgs, int timeout) throws NotRunnableException {
        return new ProcessRunner(this).run(input, anArgs, timeout);
    }

    @Override
    public RunningProject launchInteractive() throws NotRunnableException {
    	RunningProject retVal = new InteractiveConsoleProcessRunner(this).run("");
//    	retVal.createFeatureTranscript();
    	return retVal;
//        return new InteractiveConsoleProcessRunner(this).run("");
    }
    @Override
    public RunningProject launchInteractive(String[] args) throws NotRunnableException {
    	RunningProject retVal = new InteractiveConsoleProcessRunner(this).run("", args);
//    	retVal.createFeatureTranscript();
    	return retVal;
//        return new InteractiveConsoleProcessRunner(this).run("");
    }
    public static File getCheckstyleOutFolder(SakaiProject aProject) {
    	return new File (aProject.getOutputFolder());
    }
    public String createLocalCheckStyleOutputFileName() {

		return defaultCheckstyleOutputfileName();
	}
    public static String defaultCheckstyleOutputfileName() {
    	return DEFAULT_CHECK_STYLE_FILE_PREFIX + DEFAULT_CHECK_STYLE_FILE_SUFFIX;
    }
    public String getAssignmentDataFolderName() {
      return ASakaiProjectDatabase.getCurrentSakaiProjectDatabase().getAssignmentDataFolder().getMixedCaseAbsoluteName();
      
    }
    public File getCheckstyleOutFolder() {
    	return getCheckstyleOutFolder(project);
//    	return new File (project.getOutputFolder());
    }
  @Override
  protected File getCheckStyleConfigurationDefaultFolder() {
    GraderSettingsManager graderSettingsManager = GraderSettingsManagerSelector
      .getGraderSettingsManager();
  String aModule = graderSettingsManager.getModule();
  if (aModule == null) {
    System.err.println("NUll module! Internal error");
  }
//  String aFileName = getAssignmentDataFolderName() + aModule + "/checkstyle/";
//  String aFileName = getAssignmentDataFolderName(); 
//
//  File aReturnValue = new File(aFileName);
//  if (!aReturnValue.exists()) {
//    try {
//      System.err.println("Creating:" + aReturnValue);
//      aReturnValue.createNewFile();
//    } catch (IOException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
//  }
//  return aReturnValue;
  return new File("config" + "/checkstyle/" + aModule);
//  String aProblem = graderSettingsManager.getNormalizedProblem(aModule);
//  return new File( ProjectDatabaseWrapper.getCurrentSakaiProjectDatabase().getAssignmentDataFolder().getAbsoluteName());
}
    
//    protected File getCheckStyleConfigurationDefaultFolder() {
//		return new File("config" + "/checkstyle/");
//	}
    public String getCheckstyleText() {
    	String retVal = super.getCheckstyleText();
    	project.setCheckstyleText(retVal);
    	return retVal;
    	
    }
//    public File getCheckstyleOutFolder() {
//      if (checkstyleOutFolder == null) {
//        checkstyleOutFolder = new File(project.getOutputFolder());
//        if (!checkstyleOutFolder.exists()) {
//          try {
//            System.err.println("Creating folder:" + checkstyleOutFolder);
//            checkstyleOutFolder.createNewFile();
//          } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//          }
//        }     
//      }
//      return checkstyleOutFolder;
//
//    }
     
//      @Override
//      public String createFullCheckStyleOutputFileName() {
  //
//      String aParentFolder = findCheckstyleOutputParentFolder();
//      if (aParentFolder == null) {
//        return null;
//      }
////      String aLogFileName = aParentFolder + "/" + "CheckStyle_All.csv";
////      File aLogFile = new File(aLogFileName);
////      if (!aLogFile.exists()) {
////        return null;
////      }
//////      return createLocalCheckStyleFileName();
  //
//      return aParentFolder + "/" + createLocalCheckStyleOutputFileName();
//    }
//      @Override
//      protected File getCheckStyleConfigurationDefaultFolder() {
//        GraderSettingsManager graderSettingsManager = GraderSettingsManagerSelector
//          .getGraderSettingsManager();
//      String aModule = graderSettingsManager.getModule();
//      if (aModule == null) {
//        System.err.println("NUll module! Internal error");
//      }
//      return new File("config" + "/checkstyle/" + aModule);
////      String aProblem = graderSettingsManager.getNormalizedProblem(aModule);
////      return new File( ProjectDatabaseWrapper.getCurrentSakaiProjectDatabase().getAssignmentDataFolder().getAbsoluteName());
//    }
}
