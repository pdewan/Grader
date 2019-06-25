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
import grader.sakai.project.SakaiProject;

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
   
}
