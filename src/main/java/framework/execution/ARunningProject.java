package framework.execution;

import grader.basics.execution.BasicRunningProject;
import grader.basics.project.Project;
import grader.config.StaticConfigurationUtils;
import grader.sakai.project.SakaiProject;
import grader.trace.overall_transcript.OverallTranscriptSaved;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.models.ALocalGlobalTranscriptManager;
import util.models.LocalGlobalTranscriptManager;
import util.pipe.InputGenerator;
import util.pipe.ProcessInputListener;
import util.trace.Tracer;
import util.trace.console.ConsoleInput;
import wrappers.framework.project.ProjectWrapper;

/**
 * This is a wrapper for a running project independent of the method of
 * execution. This provides support for synchronization via semaphores and
 * output manipulation.
 */
public class ARunningProject extends BasicRunningProject implements ProcessInputListener {

	protected Map<String, LocalGlobalTranscriptManager> processToTranscriptManager = new HashMap<>();
	protected ProjectWrapper projectWrapper;
	protected SakaiProject project;

	public ARunningProject(Project aProject, InputGenerator anOutputBasedInputGenerator, List<String> aProcesses,
			Map<String, String> aProcessToInput) {
		super(aProject, anOutputBasedInputGenerator, aProcesses, aProcessToInput);
	}

	protected void maybeProcessProjectWrappper(Project aProject) {
		if (aProject != null && aProject instanceof ProjectWrapper) {
			projectWrapper = (ProjectWrapper) aProject;
			project = projectWrapper.getProject();
			outputFileName = project.getOutputFileName();
			projectOutput = project.getCurrentOutput();
			// input.append(project.getCurrentInput());
		}
	}

	protected void maybeProcessTrace(String aProcess, int aProcessNumber) {
		if (StaticConfigurationUtils.getTrace()) {
			LocalGlobalTranscriptManager aTranscriptManager = new ALocalGlobalTranscriptManager();
			processToTranscriptManager.put(aProcess, aTranscriptManager);
			aTranscriptManager.setIndexAndLogDirectory(aProcessNumber,
					project.getStudentAssignment().getFeedbackFolder().getAbsoluteName());
			aTranscriptManager.setProcessName(aProcess);
		}
	}

	public ARunningProject(Project aProject, InputGenerator anOutputBasedInputGenerator, String anInput) {
		super(aProject, anOutputBasedInputGenerator, anInput);
	}

	public String createFeatureTranscript() {
		transcript.setLength(0);
		if (project == null || project.getCurrentGradingFeature() == null) {
			return "";
		}
		String featureName = project.getCurrentGradingFeature().getName();

		transcript.append(featureHeader(featureName) + "\n");
		// transcript.append("*****************************(");
		// String featureName = project.getCurrentGradingFeature().getName();
		// transcript.append(featureName);
		// transcript.append(")*****************************\n");
		// String anInput = project.getCurrentInput(); // this changes with process
		// team, there can be multiple inputs and they can be given incrementally
		String anInput = input.toString();
		if (!anInput.isEmpty()) {
			transcript.append("INPUT(" + featureName + ")\n");
			transcript.append(anInput + "\n");
		}
		String[] args = project.getCurrentArgs();

		if (args != null && args.length > 0) {
			transcript.append("MAIN ARGS(" + featureName + ")\n");
			transcript.append("[");
			for (int i = 0; i < args.length; i++) {
				if (i != 0) {
					transcript.append(",");
				}
				transcript.append(args[i]);
			}
			transcript.append("[");

		}
		if (output == null) {
			// Tracer.error("Null output!");
			output = "No output from program";
			// return "";
			// return transcript.toString() + ;
		}
		if (!output.isEmpty()) {
			transcript.append("OUTPUT(" + featureName + ")\n");
			transcript.append(output + "\n");
		}
		if (!errorOutput.isEmpty()) {
			transcript.append("ERRORS(" + featureName + ")\n");
			transcript.append(errorOutput + "\n");
		}
		return transcript.toString();

	}

	//
	public void appendCumulativeOutput() {
		if (projectOutput == null) {
			return;
		}
		String transcript = createFeatureTranscript();
		projectOutput.append(transcript);
		if (outputFileName == null) {
			return;
		}
		appendToTranscriptFile(project, transcript);
		// try {
		// FileWriter fileWriter = new FileWriter(outputFileName, true);
		// fileWriter.append(transcript);
		// OverallTranscriptSaved.newCase(null, null, project, outputFileName,
		// transcript, this);
		//// if (project.getCurrentGradingFeature() != null)
		//// FeatureTranscriptSaved.newCase(null, null, project,
		// project.getCurrentGradingFeature()., outputFileName, transcript, this);;
		// fileWriter.close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	//
	protected void maybeSetCurrentProjectIO() {
		if (project != null) {
			project.setCurrentOutput(new StringBuffer(output));
			project.setCurrentInput(input.toString());
		}
	}

	public static void appendToTranscriptFile(SakaiProject aProject, String aText) {
		try {
			String anOutputFileName = aProject.getOutputFileName();
			FileWriter fileWriter = new FileWriter(anOutputFileName, true);
			fileWriter.append(aText);
			OverallTranscriptSaved.newCase(null, null, aProject, anOutputFileName, aText, ARunningProject.class);
			// if (project.getCurrentGradingFeature() != null)
			// FeatureTranscriptSaved.newCase(null, null, project,
			// project.getCurrentGradingFeature()., outputFileName, transcript, this);;
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void maybeTraceInput(String anInput, String aProcessName) {
		if (Tracer.isInfo(anInput)) {
			return;
		}
		if (!StaticConfigurationUtils.getTrace()) {
			return;
		}

		ConsoleInput consoleInput = ConsoleInput.newCase(anInput, this);
		String infoString = Tracer.toInfo(consoleInput, consoleInput.getMessage());
		if (infoString != null) {
			appendProcessOutput(aProcessName, infoString);
		}
	}

	protected void maybeAppendToProjectInput(String anInput) {
		project.appendCurrentInput(anInput);// this should go,

	}

	//
	public static void appendToTranscriptFile(SakaiProject aProject, String aFeatureName, String aText) {
		appendToTranscriptFile(aProject, featureHeader(aFeatureName) + "\n" + aText);
	}

	public static void appendToTranscriptFile(Project aProject, String aFeatureName, String aText) {
		appendToTranscriptFile(((ProjectWrapper) aProject).getProject(), featureHeader(aFeatureName) + "\n" + aText);
	}

	public void appendOutputAndErrorsToTranscriptFile(SakaiProject aProject) {
		appendToTranscriptFile(aProject, getOutputAndErrors());
	}

	public SakaiProject getProject() {
		return project;
	}

}
