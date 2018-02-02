package framework.execution;

import grader.basics.execution.ARunnerErrorStreamProcessor;
import grader.basics.execution.ARunnerInputStreamProcessor;
import grader.basics.execution.BasicProcessRunner;
import grader.basics.execution.NotRunnableException;
import grader.basics.execution.Runner;
import grader.basics.execution.RunnerErrorOrOutStreamProcessor;
import grader.basics.execution.RunnerInputStreamProcessor;
import grader.basics.execution.RunningProject;
import grader.basics.project.BasicProjectIntrospection;
import grader.basics.project.Project;
import grader.basics.settings.BasicGradingEnvironment;
import grader.config.StaticConfigurationUtils;
import grader.execution.EntryPointNotFound;
import grader.execution.ExecutionSpecification;
import grader.execution.ExecutionSpecificationSelector;
import grader.execution.TagNotFound;
import grader.executor.ExecutorSelector;
import grader.language.LanguageDependencyManager;
import grader.permissions.java.JavaProjectToPermissionFile;
import grader.sakai.project.SakaiProject;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import util.pipe.InputGenerator;
import wrappers.framework.project.ProjectWrapper;

/**
 * This runs the program in a new process.
 */
public class ProcessRunner extends BasicProcessRunner implements Runner {
	ExecutionSpecification executionSpecification;

	protected void initializeExecutionState() {
		executionSpecification = ExecutionSpecificationSelector.getExecutionSpecification();
	}

	public ProcessRunner(Project aProject, String aSpecifiedMainClass) throws NotRunnableException {
		super(aProject, aSpecifiedMainClass);
	}

	public ProcessRunner(Project aProject) throws NotRunnableException {
		super(aProject);
	}

	// use it without project
	public ProcessRunner() throws NotRunnableException {

	}

	public Map<String, String> getEntryPoints() {
		if (entryPoints == null) {
			entryPoints = LanguageDependencyManager.getMainClassFinder().getEntryPoints(project, specifiedMainClass);

		}
		return entryPoints;
	}

	public File getFolder() {
		return getFolder(getEntryPoints().get(BasicProcessRunner.MAIN_ENTRY_POINT));
	}

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

	/**
	 * This figures out what class is the "entry point", or, what class has
	 * main(args)
	 *
	 * @param project
	 *            The project to run
	 * @return The class canonical name. i.e. "foo.bar.SomeClass"
	 * @throws NotRunnableException
	 */
	protected String getMainEntryPoint() {
		return getEntryPoints().get(BasicProcessRunner.MAIN_ENTRY_POINT);
	}

	protected List<String> getProcessTeams() {
		return executionSpecification.getProcessTeams();
	}

	protected List<String> getProcesses(String firstTeam) {
		return executionSpecification.getProcesses(firstTeam);
	}

	protected List<String> getTerminatingProcesses(String firstTeam) {
		return executionSpecification.getTerminatingProcesses(firstTeam);
	}

	public String classWithEntryTagTarget(String anEntryTag) {
		if (anEntryTag == null)
			return "";
		Class aClass = BasicProjectIntrospection.findUniqueClassByTag(project, anEntryTag);
		if (aClass != null) {
			String aRetVal = aClass.getName();
		}
		if (project instanceof ProjectWrapper) {
			grader.project.flexible.FlexibleProject graderProject = ((ProjectWrapper) project).getProject();
			grader.project.flexible.FlexibleClassDescription aClassDescription = graderProject.getClassesManager()
					.tagToUniqueClassDescription(anEntryTag); // looks like we should use IntrpspectUtil
			return aClassDescription.getClassName();
		}
		return null; // this should never be executed
	}

	public String classWithEntryTagsTarget(List<String> anEntryTags) {
		if (anEntryTags == null)
			return "";
		Class aClass = BasicProjectIntrospection.findClassByTags(project, anEntryTags.toArray(emptyStringArray));
		if (aClass != null) {
			String aRetVal = aClass.getName();
			return aRetVal; // added this
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

	protected List<String> getStartTags(String aProcess) {
		return executionSpecification.getStartTags(aProcess);
	}

	// need an equivalent of this for basicprocessrunner
	protected String searchForEntryPoint(String aProcess) {
		List<String> basicCommand = StaticConfigurationUtils.getBasicCommand(aProcess);
		String anEntryPoint = null;
		if (StaticConfigurationUtils.hasEntryPoint(basicCommand))

		{
			anEntryPoint = executionSpecification.getEntryPoint(aProcess);
			if (anEntryPoint == null)
				throw EntryPointNotFound.newCase(this);
		}
		if (anEntryPoint != null && !anEntryPoint.isEmpty()) {
			getFolder(anEntryPoint);
		}
		return anEntryPoint;
	}

	protected String searchForEntryTag(String aProcess) {
		List<String> basicCommand = StaticConfigurationUtils.getBasicCommand(aProcess);

		// if (anEntryPoint != null && !anEntryPoint.isEmpty()) {
		// getFolder(anEntryPoint);
		// }
		String anEntryTag = null;
		List<String> anEntryTags = null;
		if (StaticConfigurationUtils.hasEntryTags(basicCommand))
			anEntryTags = executionSpecification.getEntryTags(aProcess);
		else if (StaticConfigurationUtils.hasEntryTag(basicCommand))
			anEntryTag = executionSpecification.getEntryTag(aProcess); // this will match entryTag also, fix at some pt

		// if (anEntryTag != null ) {
		// getFolder(anEntryTag);
		// }
		String aClassWithEntryTag = null;
		if (anEntryTag != null) {
			aClassWithEntryTag = classWithEntryTagTarget(anEntryTag);
			if (aClassWithEntryTag == null)
				throw TagNotFound.newCase(anEntryTag, this);
		} else if (anEntryTags != null) {
			aClassWithEntryTag = classWithEntryTagsTarget(anEntryTags);
			if (aClassWithEntryTag == null)
				throw TagNotFound.newCase(anEntryTags, this);
		}
		if (aClassWithEntryTag != null && folder == null) {

			getFolder(aClassWithEntryTag);
		}
		return aClassWithEntryTag;
	}

	protected String[] getArgs(String aProcess) {
		return executionSpecification.getArgs(aProcess).toArray(new String[0]);
	}

	protected int getSleepTime(String aProcess) {
		return executionSpecification.getSleepTime(aProcess);
	}

	protected String[] getCommand(String aProcess, String anEntryPoint, String anEntryTag, String[] anArgs) {
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
