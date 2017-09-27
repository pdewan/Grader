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

//    private Semaphore runningState = new Semaphore(1);
////	protected Map<String, String> processToInput = new HashMap();
//    protected Map<String, String> processToErrors = new HashMap();
//    protected Map<String, StringBuffer> processToOutput = new HashMap();
    protected Map<String, LocalGlobalTranscriptManager> processToTranscriptManager = new HashMap();
//    // duplicates the mapping in Process Runner
//    protected Map<String, RunnerInputStreamProcessor> processToIn = new HashMap();
//    protected Map<String, TimedProcess> nameToProcess = new HashMap();
//    protected TimedProcess currentProcess;
//    protected boolean destroyed;
//
//   
//
//	
//	protected Map<String, String> processToOutputAndErrors = new HashMap();
//
//    private String output = ""; // why is this a string and not a string buffer?
//    private String errorOutput = "";
//    String outputAndErrors = "";
//    private NotRunnableException exception;
////	Project project;
      protected ProjectWrapper projectWrapper;
//    String outputFileName;
//    StringBuffer projectOutput;
      protected SakaiProject project;
//    Set<Thread> dependentThreads = new HashSet();
//    Set<Closeable> dependentCloseables = new HashSet();
//
//    InputGenerator outputBasedInputGenerator; // actuall all we need is an output consumer
////	RunnerInputStreamProcessor processIn;
//    StringBuffer input = new StringBuffer();
//    protected Map<String, StringBuffer> processToInput = new HashMap();
//    protected Map<String, RunnerErrorOrOutStreamProcessor> processToOut = new HashMap();
//    protected Map<String, RunnerErrorOrOutStreamProcessor> processToErr = new HashMap();
//    List<String> processes;

    public ARunningProject(Project aProject, InputGenerator anOutputBasedInputGenerator, List<String> aProcesses, Map<String, String> aProcessToInput) {
    	super(aProject, anOutputBasedInputGenerator, aProcesses, aProcessToInput);
//        exception = null;
//        output = null;
//        processes = aProcesses;
//        if (aProject != null && aProject instanceof ProjectWrapper) {
//            projectWrapper = (ProjectWrapper) aProject;
//            project = projectWrapper.getProject();
//            outputFileName = project.getOutputFileName();
//            projectOutput = project.getCurrentOutput();
////			input.append(project.getCurrentInput());
//        }
//        outputBasedInputGenerator = anOutputBasedInputGenerator;
//        if (outputBasedInputGenerator != null) {
//            outputBasedInputGenerator.addProcessInputListener(this); // maybe this should be in another class
//        }
////		processToInput = aProcessToInput;
//        if (aProcessToInput != null) {
//            for (String aProcess : aProcessToInput.keySet()) {
//                String anInput = aProcessToInput.get(aProcess);
//                // if (anInput != null)
//                processToInput.put(aProcess, new StringBuffer(anInput));
////				processToOutput.put(aProcess, new StringBuffer());
//                input.append(anInput);
//            }
//        }
//        if (aProcesses != null) {
//            for (int i = 0; i < aProcesses.size(); i++) {
//                String aProcess = aProcesses.get(i);
//                if (StaticConfigurationUtils.getTrace()) {
//                    LocalGlobalTranscriptManager aTranscriptManager = new ALocalGlobalTranscriptManager();
//                    processToTranscriptManager.put(aProcess, aTranscriptManager);
//                    aTranscriptManager.setIndexAndLogDirectory(i, project.getStudentAssignment().getFeedbackFolder().getAbsoluteName());
//                    aTranscriptManager.setProcessName(aProcess);
//                }
//                if (outputBasedInputGenerator != null) {
//                    outputBasedInputGenerator.addProcessName(aProcess);
//
//                }
//            }
//            if (outputBasedInputGenerator != null) {
//                outputBasedInputGenerator.processNamesAdded();
//            }
//
//        }
//
    }
    protected void maybeProcessProjectWrappper(Project aProject) {
    	if (aProject != null && aProject instanceof ProjectWrapper) {
            projectWrapper = (ProjectWrapper) aProject;
            project = projectWrapper.getProject();
            outputFileName = project.getOutputFileName();
            projectOutput = project.getCurrentOutput();
//			input.append(project.getCurrentInput());
        }
    }
    protected void maybeProcessTrace(String aProcess, int aProcessNumber) {
   	 if (StaticConfigurationUtils.getTrace()) {
            LocalGlobalTranscriptManager aTranscriptManager = new ALocalGlobalTranscriptManager();
            processToTranscriptManager.put(aProcess, aTranscriptManager);
            aTranscriptManager.setIndexAndLogDirectory(aProcessNumber, project.getStudentAssignment().getFeedbackFolder().getAbsoluteName());
            aTranscriptManager.setProcessName(aProcess);
        }
   }
//
    public ARunningProject(Project aProject, InputGenerator anOutputBasedInputGenerator, String anInput) {
    	super(aProject, anOutputBasedInputGenerator, anInput);
//        this(aProject, anOutputBasedInputGenerator, null, (Map) null);
////		exception = null;
////		output = null;
////		if (aProject != null && aProject instanceof ProjectWrapper) {
////			projectWrapper = (ProjectWrapper) aProject;
////			project = projectWrapper.getProject();
////			outputFileName = project.getOutputFileName();
////			projectOutput = project.getCurrentOutput();
//////			input.append(project.getCurrentInput());
////		}
////		outputBasedInputGeneraor = anOutputBasedInputGenerator;
////		if (outputBasedInputGeneraor != null) {
////			outputBasedInputGeneraor.addProcessInputListener(this); // maybe this should be in another class
//
//        input.setLength(0);
//        input.append(anInput);
//
    }
//
//    public void start() throws InterruptedException {
//        runningState.acquire();
//    }
//
//    public void end() {
//        runningState.release();
//    }
//
//    public void appendCumulativeOutput(String newVal) {
//        if (this.output == null && newVal != null) {
//            this.output = "";
//        }
//        this.output += newVal;
////		if (outputBasedInputGeneraor != null) {
////			outputBasedInputGeneraor.newOutputLine(null, newVal);
////		}
//
//        outputAndErrors += newVal;
//
//    }
//
//    public Map<String, StringBuffer> getProcessOutput() {
//        return processToOutput;
//    }
//
//    public void appendProcessOutput(String aProcess, String newVal) {
////		if (aProcess == null) { // it will never be null
////			return;
////		}
//    	System.out.println ("New output " + newVal + " aprocess " + aProcess);
//        // wonder why did not have this before
//        if (newVal == null) {
//            return;
//        }
//        StringBuffer aProcessOutput = processToOutput.get(aProcess);
////		boolean newProcess = processToTranscriptManager.get(aProcess) == null;
////		if (processOutput == null && newVal != null) {
//        if (aProcessOutput == null && newVal != null) {
//            aProcessOutput = new StringBuffer();
//            processToOutput.put(aProcess, aProcessOutput);
//        }
//
//        aProcessOutput.append(newVal);
////		processToOutput.put(aProcess, aProcessOutput);
//        if (outputBasedInputGenerator != null) {
//            outputBasedInputGenerator.newOutputLine(aProcess, newVal);
////			if (newProcess) {
////				outputBasedInputGenerator.addProcessName(aProcess);
////			}
//        }
//
////		if (newProcess) {
////			LocalGlobalTranscriptManager aTranscriptManager = new ALocalGlobalTranscriptManager();
////			processToTranscriptManager.put(aProcess, aTranscriptManager );
////			aTranscriptManager.setProcessName(aProcess);
////			
////		}
//        appendErrorAndOutput(aProcess, newVal);
//
//    }
//
//    public void appendErrorOutput(String aProcess, String newVal) {
//        String processErrors = processToErrors.get(aProcess);
//        if (processErrors == null && newVal != null) {
//            processErrors = "";
//        }
//        processErrors += newVal;
//
//        processToErrors.put(aProcess, processErrors);
//        appendErrorAndOutput(aProcess, newVal);
//    }
//
//    public void appendErrorAndOutput(String aProcess, String newVal) {
//        String processOutputAndErrors = processToOutputAndErrors.get(aProcess);
//        processOutputAndErrors += newVal;
//
//        processToOutputAndErrors.put(aProcess, processOutputAndErrors);
//
//    }
//
//    public void setOutput(String output) {
//        this.output = output;
//    }
//
//    public String getOutput() {
//        return output;
//    }
//
//    public String getOutputAndErrors() {
//        return outputAndErrors;
//    }
//
//    public void appendErrorOutput(String anErrorOutput) {
//        if (this.errorOutput == null && anErrorOutput != null) {
//            this.errorOutput = "";
//        }
//        this.errorOutput += anErrorOutput;
//        outputAndErrors += anErrorOutput;
//
//    }
//
//    public void setErrorOutput(String errorOutput) {
//        this.errorOutput = errorOutput;
//    }
//
//    public String getErrorOutput() {
//        return errorOutput;
//    }
//
//    public void error() {
//        this.exception = new NotRunnableException();
//        exception.announce();
//    }
//
//    public static final String FEATURE_HEADER_PREFIX = "*****************************(";
//    public static final String FEATURE_HEADER_SUFFIX = ")*****************************";
//
//    public static String featureHeader(String aFeatureName) {
//        return FEATURE_HEADER_PREFIX + aFeatureName + FEATURE_HEADER_SUFFIX;
//    }
//
//    public static String extractFeatureTranscript(String aFeatureName, String allOutput) {
//        int startIndex = allOutput.indexOf(featureHeader(aFeatureName));
//        if (startIndex == -1) {
//            return "";
//        }
//        int endIndex;
//        int prevIndex = startIndex;
//        int nextIndex;
//        while (true) {
//            nextIndex = allOutput.indexOf(aFeatureName, prevIndex + 1);
//            if (nextIndex < 0) {
//                endIndex = allOutput.indexOf(FEATURE_HEADER_PREFIX, prevIndex + 1);
//                if (endIndex == -1) {
//                    endIndex = allOutput.length();
//                }
//                break;
//            } else {
//                prevIndex = nextIndex;
//            }
//
//        }
//        return allOutput.substring(startIndex, endIndex);
//    }
//
//    StringBuffer transcript = new StringBuffer(); // reusing the buffer
//
    public String createFeatureTranscript() {
        transcript.setLength(0);
        if (project == null || project.getCurrentGradingFeature() == null) {
            return "";
        }
        String featureName = project.getCurrentGradingFeature().getName();

        transcript.append(featureHeader(featureName) + "\n");
//		transcript.append("*****************************(");
//		String featureName = project.getCurrentGradingFeature().getName();
//		transcript.append(featureName);
//		transcript.append(")*****************************\n");
//		String anInput = project.getCurrentInput(); // this changes with process team, there can be multiple inputs and they can be given incrementally
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
//            Tracer.error("Null output!");
            output = "No output from program";
//            return "";
//            return transcript.toString() + ;
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
//		try {
//			FileWriter fileWriter = new FileWriter(outputFileName, true);
//			fileWriter.append(transcript);
//			OverallTranscriptSaved.newCase(null, null, project,  outputFileName, transcript, this);
////			if (project.getCurrentGradingFeature() != null)
////			FeatureTranscriptSaved.newCase(null, null, project,  project.getCurrentGradingFeature()., outputFileName, transcript, this);;
//			fileWriter.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

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
//			if (project.getCurrentGradingFeature() != null)
//			FeatureTranscriptSaved.newCase(null, null, project,  project.getCurrentGradingFeature()., outputFileName, transcript, this);;
            fileWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
   protected void maybeTraceInput (String anInput, String aProcessName) {
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

//    public String await() throws NotRunnableException {
//        if (exception != null) {
//            throw exception;
//        }
//        try {
//            runningState.acquire();
//        } catch (InterruptedException e) {
//            throw new NotRunnableException();
//        }
//        appendCumulativeOutput();
//        if (project != null) {
//            project.setCurrentOutput(new StringBuffer(output));
//            project.setCurrentInput(input.toString());
//        }
//        return output;
//    }
//
//    @Override
//    public void newInputLine(String aProcessName, String anInput) {
//    	System.out.println("New input " + anInput + "for " + aProcessName );
//    	if (aProcessName != null)
//        processToIn.get(aProcessName).newInput(anInput + "\n");
//        project.appendCurrentInput(anInput);// this should go, 
//        if (aProcessName != null && processToInput != null) {
//            StringBuffer aProcessStringBuffer = processToInput.get(aProcessName);
//            if (aProcessStringBuffer != null) {
//                aProcessStringBuffer.append(anInput);
//            }
//        }
////        input.append(anInput);
//       input.append(anInput + "\n"); // need to put new line to it
//
//        // why would this be info ever?
//        if (Tracer.isInfo(anInput)) {
//            return;
//        }
//        if (!StaticConfigurationUtils.getTrace()) {
//            return;
//        }
//
//        ConsoleInput consoleInput = ConsoleInput.newCase(anInput, this);
//        String infoString = Tracer.toInfo(consoleInput, consoleInput.getMessage());
//        if (infoString != null) {
//            appendProcessOutput(aProcessName, infoString);
//        }
//
//    }
//
//    public void terminateTeam() {
//        Set<String> aProcesses = nameToProcess.keySet();
//        for (String aProcess : aProcesses) {
//            terminateProcess(aProcess);
////			try {
////				processToOut.get(aProcess).getSemaphore().acquire();
////			} catch (InterruptedException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////			try {
////				processToErr.get(aProcess).getSemaphore().acquire();
////			} catch (InterruptedException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////			TimedProcess timedProcess = nameToProcess.get(aProcess);
////			timedProcess.getProcess().destroy();
//        }
//        terminateRunner();
//		// try {
//        // // Wait for the output to finish
//        // outputSemaphore.acquire();
//        // errorSemaphore.acquire();
//        // runner.end();
//        // } catch (Exception e) {
//        // e.printStackTrace();
//        // Tracer.error(e.getMessage());
//        // runner.error();
//        // runner.end();
//        // }
//    }
//
//    void terminateRunner() {
//        try {
//            // Wait for the output to finish
////			acquireIOLocks();
//            releaseTeamLocks();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Tracer.error(e.getMessage());
//            error();
//            releaseTeamLocks();
////			runner.end();
//        }
//    }
//
//    void releaseTeamLocks() {
////		try {
////			outputSemaphore.release(); // share once for all processes
////			errorSemaphore.release();
////		Set<String> aProcesses = processToOut.keySet();
////		for (String aProcess:aProcesses) {
////			processToOut.get(aProcess).getSemaphore().release();;
////			processToErr.get(aProcess).getSemaphore().release();
////			
////		}
//        end();
////		} catch (InterruptedException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//    }
//
//    @Override
//    public void inputTerminated(String aProcessName) {
////		System.out.println("Terminating input");
//        terminateProcess(aProcessName);
////		processToIn.get(aProcessName).terminateInput();
//
//    }
//
//    public void terminateProcess(String aProcess) {
//        System.out.println("Terminating:" + aProcess);
////
//        try {
//            processToOut.get(aProcess).getSemaphore().acquire(); // this is deadlocking, need to debug
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        try {
//            processToErr.get(aProcess).getSemaphore().acquire();
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        TimedProcess timedProcess = nameToProcess.get(aProcess);
//        timedProcess.getProcess().destroy();
//    }
//
//    public RunnerInputStreamProcessor getProcessIn(String aProcessName) {
//        return processToIn.get(aProcessName);
//    }
//
//    // the mapping could be passed to this object rather than the individual processIn's
//
//    public void setProcessIn(String aProcess, RunnerInputStreamProcessor processIn) {
////		this.processIn = processIn;
//        processToIn.put(aProcess, processIn);
//    }
//
//    public RunnerErrorOrOutStreamProcessor getProcessOut(String aProcessName) {
//        return processToOut.get(aProcessName);
//    }
//
//    // the mapping could be passed to this object rather than the individual processIn's
//
//    public void setProcessOut(String aProcess, RunnerErrorOrOutStreamProcessor newVal) {
////		this.processIn = processIn;
//        processToOut.put(aProcess, newVal);
//    }
//
//    public RunnerErrorOrOutStreamProcessor getProcessErr(String aProcessName) {
//        return processToErr.get(aProcessName);
//    }
//
//    // the mapping could be passed to this object rather than the individual processIn's
//
//    public void setProcessErr(String aProcess, RunnerErrorOrOutStreamProcessor newVal) {
////		this.processIn = processIn;
//        processToErr.put(aProcess, newVal);
//    }
//
//    public TimedProcess getProcess(String aProcessName) {
//        return nameToProcess.get(aProcessName);
//    }
//
//    public void setProcess(String aProcessName, TimedProcess aTimedProcess) {
////		this.processIn = processIn;
//        nameToProcess.put(aProcessName, aTimedProcess);
//    }
//
    public SakaiProject getProject() {
        return project;
    }
//    public boolean isDestroyed() {
//		return destroyed;
//	}
//
//	public void setDestroyed(boolean destroyed) {
//		this.destroyed = destroyed;
//	}
//	
//	public void addDependentThread(Thread aThread) {
//		dependentThreads.add(aThread);
//	}
//	
//	public void addDependentCloseable(Closeable aCloseable) {
//		dependentCloseables.add(aCloseable);
//	}
//
//    public void destroy() {
//    	setDestroyed(true);
//    	currentProcess.destroy();
//    	for (Thread dependentThread:dependentThreads) {
//    		dependentThread.interrupt();
//    	}
//    	for (Closeable closeable:dependentCloseables) {
//    		try {
//				closeable.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//    	}
//    	end();
//    }
//    public TimedProcess getCurrentTimedProcess() {
//		return currentProcess;
//	}
//
//	public void setCurrentTimeProcess(TimedProcess currentProcess) {
//		this.currentProcess = currentProcess;
//	}

}
