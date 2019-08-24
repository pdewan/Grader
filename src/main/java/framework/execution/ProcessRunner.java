package framework.execution;

import grader.basics.config.BasicExecutionSpecification;
import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.config.BasicStaticConfigurationUtils;
import grader.basics.execution.ARunnerErrorStreamProcessor;
import grader.basics.execution.ARunnerInputStreamProcessor;
import grader.basics.execution.BasicProcessRunner;
import grader.basics.execution.EntryPointNotFound;
import grader.basics.execution.NotRunnableException;
import grader.basics.execution.Runner;
import grader.basics.execution.RunnerErrorOrOutStreamProcessor;
import grader.basics.execution.RunnerInputStreamProcessor;
import grader.basics.execution.RunningProject;
import grader.basics.execution.TagNotFound;
import grader.basics.project.BasicProjectIntrospection;
import grader.basics.project.Project;
import grader.basics.settings.BasicGradingEnvironment;
import grader.config.ExecutionSpecification;
import grader.config.ExecutionSpecificationSelector;
import grader.config.StaticConfigurationUtils;
import grader.executor.ExecutorSelector;
import grader.language.LanguageDependencyManager;
import grader.permissions.java.JavaProjectToPermissionFile;
import grader.sakai.project.SakaiProject;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import framework.project.StandardProject;
import util.pipe.InputGenerator;
import wrappers.framework.project.ProjectWrapper;

/**
 * This runs the program in a new process.
 * we should really not have any code here as BasicProcessRunner should do everything
 */
public class ProcessRunner extends BasicProcessRunner implements Runner {
//	BasicExecutionSpecification executionSpecification;
	public ProcessRunner(Project aProject, String aSpecifiedProxyMainClass) throws NotRunnableException {
		super(aProject, aSpecifiedProxyMainClass);

		
	}

	public ProcessRunner(Project aProject) throws NotRunnableException {
		super(aProject);
	}

	// This overrides by calling LanguageDependencyManager instead of BasicLanguageDepdendencyManager
	//LanguageDependencyManager calls the method in BasicLanguageDepenencyManager
	//Let us not make them different
//	public Map<String, String> getEntryPoints() {
//		if (entryPoints == null) {
//			entryPoints = LanguageDependencyManager.getMainClassFinder().getEntryPoints(project, specifiedMainClass);
//
//		}
//		return entryPoints;
//	}



	// use it without project
	public ProcessRunner(File aFolder) throws NotRunnableException {
		// folder = aFolder;
		super(aFolder);

	}

	/**
	 * This runs the project with no arguments
	 * 
	 * @param input
	 *            The input to use as standard in for the process
	 * @return A RunningProject object which you can use for synchronization and
	 *         acquiring output
	 * @throws NotRunnableException
	 */

	/**
	 * This runs the project with no arguments with a timeout
	 * 
	 * @param input
	 *            The input to use as standard in for the process
	 * @param timeout
	 *            The timeout, in seconds. Set to -1 for no timeout
	 * @return A RunningProject object which you can use for synchronization and
	 *         acquiring output
	 * @throws NotRunnableException
	 */



	public String classWithEntryTagTarget(String anEntryTag) {

		String aRetVal = super.classWithEntryTagTarget(anEntryTag);
		if (aRetVal != null) {
			return aRetVal;
		}
		// should the following code really be executed?
		if (project instanceof ProjectWrapper) {
			grader.project.flexible.FlexibleProject graderProject = ((ProjectWrapper) project).getProject();
			grader.project.flexible.FlexibleClassDescription aClassDescription = graderProject.getClassesManager()
					.tagToUniqueClassDescription(anEntryTag); // looks like we should use IntrpspectUtil
			return aClassDescription.getClassName();
		}
		return null; // this should never be executed
	}

	public String classWithEntryTagsTarget(List<String> anEntryTags) {

		String retVal = super.classWithEntryTagsTarget(anEntryTags);
		if (retVal != null) {
			return retVal;
		}
		// we should not have to do what is below
		if (project instanceof ProjectWrapper) {
			grader.project.flexible.FlexibleProject graderProject = ((ProjectWrapper) project).getProject();
			grader.project.flexible.FlexibleClassDescription aClassDescription = graderProject.getClassesManager()
					.tagsToUniqueClassDescription(anEntryTags);
			return aClassDescription.getClassName();
		}
		return null; // this should never be executed
	}


//	// need an equivalent of this for basicprocessrunner
	// we have it so get rid of it
//	protected String searchForEntryPoint(String aProcess) {
//		List<String> basicCommand = StaticConfigurationUtils.getBasicCommand(aProcess);
//		String anEntryPoint = null;
//		if (StaticConfigurationUtils.hasEntryPoint(basicCommand))
//
//		{
//			anEntryPoint = executionSpecification.getEntryPoint(aProcess);
//			if (anEntryPoint == null)
//				throw EntryPointNotFound.newCase(this);
//		}
//		if (anEntryPoint != null && !anEntryPoint.isEmpty()) {
//			getFolder(anEntryPoint);
//		}
//		return anEntryPoint;
//	}
	// Basic and non basic should not differ
//	protected String searchForEntryTag(String aProcess) {
//		return searchForEntryTag(aProcess, StaticConfigurationUtils.getExecutionCommand());
//		
//	}



	protected int getResourceReleaseTime(String aProcess) {
		Integer aRetVal = executionSpecification.getResourceReleaseTime(aProcess);
		if (aRetVal == null) {
			return 0;
		}
		return aRetVal;
//		return executionSpecification.getSleepTime(aProcess);
	}

	protected String[] getCommand(String aProcess, String anEntryPoint, String anEntryTag, String[] anArgs) {
		/*
		 * Should return this from AnExecutionSpecification
		 */
		return StaticConfigurationUtils.getExecutionCommand(project, aProcess, folder, anEntryPoint, anEntryTag,
				anArgs);
	}

	protected String[] getExecutionCommand(Project aProject, File aBuildFolder, String anEntryPoint, String[] anArgs) {
		return StaticConfigurationUtils.getExecutionCommand(aProject, aBuildFolder, anEntryPoint);
	}

	protected void maybeSetInputAndArgs(String input, String[] args) {
		if (project != null && project instanceof ProjectWrapper) {
			SakaiProject sakaiProject = ((ProjectWrapper) project).getProject();
			sakaiProject.setCurrentInput(input); // this should go or be append for subsequent input
			sakaiProject.setCurrentArgs(args);
		}
	}

	protected RunningProject createRunningProject(Project aProject, InputGenerator anOutputBasedInputGenerator,
			String anInput) {
		return new ARunningProject(aProject, anOutputBasedInputGenerator, anInput);
	}

	protected RunningProject createRunningProject(Project aProject, InputGenerator anOutputBasedInputGenerator,
			List<String> aProcesses, Map<String, String> aProcessToInput) {

		return new ARunningProject(project, anOutputBasedInputGenerator, processes, aProcessToInput);
	}

	protected String mainEntryPoint() {
		return BasicProcessRunner.MAIN_ENTRY_POINT;
	}

	protected File getPermissionFile() {
		return JavaProjectToPermissionFile.getPermissionFile(project);
	}

	protected String getClassPath() {
		return BasicGradingEnvironment.get().getClassPath();
	}

	protected String[] maybeToExecutorCommand(String[] aCommand) {
		return ExecutorSelector.getExecutor().maybeToExecutorCommand(aCommand);
	}

	protected RunnerErrorOrOutStreamProcessor createRunnerOutputStreamProcessor(InputStream aProcessErrorOut,
			RunningProject aRunner, /* Semaphore aSemaphore, */ String aProcessName, Boolean anOnlyProcess) {
		return new ARunnerOutputStreamProcessor(aProcessErrorOut, aRunner, /* outputSemaphore, */
				aProcessName, anOnlyProcess);
	}

	protected RunnerErrorOrOutStreamProcessor createRunnerErrorStreamProcessor(InputStream aProcessErrorOut,
			RunningProject aRunner,
			/* Semaphore aSemaphore, */
			String aProcessName, Boolean anOnlyProcess) {
		return new ARunnerErrorStreamProcessor(aProcessErrorOut, aRunner, /* errorSemaphore, */
				aProcessName, anOnlyProcess);
	}

	protected RunnerInputStreamProcessor createRunnerInputStreamProcessor(OutputStream anInput, RunningProject aRunner,
			String aProcessName, /* Semaphore aSemaphore, */ Boolean anOnlyProcess) {
		return new ARunnerInputStreamProcessor(anInput, aRunner, aProcessName, anOnlyProcess);
	}
}
