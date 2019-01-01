package gradingTools.comp790Colab.assignment1.testcases;

import java.util.HashMap;
import java.util.Map;

import util.tags.DistributedTags;
import framework.grading.testing.BasicTestCase;
import grader.basics.execution.BasicProjectExecution;
import grader.basics.execution.NotRunnableException;
import grader.basics.execution.RunningProject;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import gradingTools.utils.RunningProjectUtils;

public class CollaborativeInputPromptTestCase extends BasicTestCase {
	protected boolean client1HasInitialIntPrompt;
	protected boolean client2HasInitialDoublePrompt ;
    protected boolean client1HasInitialDoublePrompt ;
    protected boolean client2HasInitialIntPrompt ;
    protected StringBuffer client1NoInputOutput, client2NoInputOutput;
    protected RunningProject noInputRunningProject;
    protected String noInputPrompt;
	
	public CollaborativeInputPromptTestCase() {
		super("Prompt printer test case");
	}

	private TestCaseResult testForIntegerPrompt(StringBuffer output) {
		
		if (output != null && 
				output.toString().trim().toLowerCase().contains("int"))
			return pass();
		else
			return fail("Program does not contain prompt for integer");
	}
	private boolean hasIntegerPrompt(StringBuffer output) {
		boolean retVal = output != null && 
				output.toString().trim().toLowerCase().contains("int");
		if (!retVal) {
			System.out.println(output + " does not have int");
		}
		return retVal;
	}
	private boolean hasDoublePrompt(StringBuffer output) {
		// converting twice to lower case!
		return (output != null && 
				(output.toString().toLowerCase().contains("decimal") || output.toString().toLowerCase().contains("double")));
			
	}	
			

	private TestCaseResult testForDoublePrompt(StringBuffer output) {
		// converting twice to lower case!
		if (output != null && 
				(output.toString().toLowerCase().contains("decimal") || output.toString().toLowerCase().contains("double")))
			return pass();
		else
			return fail("Program does not contain prompt for double");
	}
	
	public static RunningProject runAliceBobProject(Project project, int timeout, Integer anInteger, Double aDouble) {
		Map<String, String> processToInput = new HashMap();
		if (anInteger != null)
			processToInput.put(DistributedTags.CLIENT_1, BasicProjectExecution.toInputString(anInteger.toString()));
		if (aDouble != null)
			processToInput.put(DistributedTags.CLIENT_2, BasicProjectExecution.toInputString(aDouble.toString()));
		return RunningProjectUtils.runProject(project, timeout, processToInput);
		
	}
	public static RunningProject runAliceBobProject(Project project, int timeout,  Double aDouble) {
		return runAliceBobProject(project, timeout, null, aDouble);
		
	}
	public static RunningProject runAliceBobProject(Project project, int timeout,  Integer anInteger) {
		return runAliceBobProject(project, timeout, anInteger, null);
		
	}
	public static RunningProject runAliceBobProject(Project project, int timeout) {
		return runAliceBobProject(project, timeout, null, null);
		
	}
	
	protected void runProjectAndGatherOutputStats(Project project) {
		// Get the output when we have no input from the user
		 System.out.println("Starting  project at time:" + System.currentTimeMillis());

//		 noInputRunningProject = runAliceBobProject(project, 5);
		 noInputRunningProject = runAliceBobProject(project, 5); // dummy Input

		 noInputPrompt = noInputRunningProject.await();
	        System.out.println(("Current thread:" + Thread.currentThread()));

		 System.out.println("finished awaiting running project at time:" + System.currentTimeMillis() + " with output " + noInputPrompt);
		Map<String, StringBuffer> aProcessToOutput = noInputRunningProject.getProcessOutput();
		 System.out.println("aProcessToOutput=" + aProcessToOutput);

		 client1NoInputOutput = aProcessToOutput.get(DistributedTags.CLIENT_1);
		client2NoInputOutput = aProcessToOutput.get(DistributedTags.CLIENT_2);
//		client1HasInitialIntPrompt = testForIntegerPrompt(client1NoInputOutput).getPercentage() > 0;
		client1HasInitialIntPrompt = hasIntegerPrompt(client1NoInputOutput);

//		client2HasInitialDoublePrompt = testForDoublePrompt(client2NoInputOutput).getPercentage() > 0;
		client2HasInitialDoublePrompt = hasDoublePrompt(client2NoInputOutput);

//		client1HasInitialDoublePrompt = testForDoublePrompt(client1NoInputOutput).getPercentage() > 0;
		client1HasInitialDoublePrompt = hasDoublePrompt(client1NoInputOutput);

//		client2HasInitialIntPrompt = testForIntegerPrompt(client2NoInputOutput).getPercentage() > 0;
		client2HasInitialIntPrompt = hasIntegerPrompt(client2NoInputOutput);

	}

	@Override
	public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException,
			NotGradableException {
		try {
			runProjectAndGatherOutputStats(project);

//			// Get the output when we have no input from the user
//			 noInputRunningProject = runAliceBobProject(project, 1);
//			String noInputPrompt = noInputRunningProject.await();
//			Map<String, StringBuffer> aProcessToOutput = noInputRunningProject.getProcessOutput();
//			 client1NoInputOutput = aProcessToOutput.get(DistributedTags.CLIENT_1);
//			client2NoInputOutput = aProcessToOutput.get(DistributedTags.CLIENT_2);
//			client1HasInitialIntPrompt = testForIntegerPrompt(client1NoInputOutput).getPercentage() > 0;
//			client2HasInitialDoublePrompt = testForDoublePrompt(client2NoInputOutput).getPercentage() > 0;
//			client1HasInitialDoublePrompt = testForDoublePrompt(client1NoInputOutput).getPercentage() > 0;
//			client2HasInitialIntPrompt = testForIntegerPrompt(client2NoInputOutput).getPercentage() > 0;
			boolean samePromptForBoth = (client1HasInitialIntPrompt && client1HasInitialDoublePrompt) ||
					(client2HasInitialIntPrompt && client2HasInitialDoublePrompt);
			boolean hasInitialIntPrompt = client1HasInitialIntPrompt || client2HasInitialIntPrompt;
			boolean hasInitialDoublePrompt = client1HasInitialDoublePrompt || client2HasInitialDoublePrompt;
			// If we have not seen prompts for ints or doubles, check if they
			// show up after giving input	
			// forget this,too tedious, and no penalty for prompts appearing late in Jacob's test
			if (!client1HasInitialIntPrompt && !client2HasInitialDoublePrompt && !client1HasInitialDoublePrompt && !client2HasInitialIntPrompt) {
				
			}
			System.out.println("Initialized booleans");
			
//
//			// Get the output when we have integer input from the user
//			RunningProject integerInputRunningProject = runAliceBobProject(project, 1,
//					1);
//			String integerInputPrompt = integerInputRunningProject.await();
//			integerInputPrompt = integerInputPrompt.substring(noInputPrompt.length());
//
//			// Get the output when we have double input from the user
//			RunningProject doubleInputRunningProject = runAliceBobProject(project, 1,
//					1.4);
//			String doubleInputPrompt = doubleInputRunningProject.await();
//			doubleInputPrompt = doubleInputPrompt.substring(noInputPrompt.length());
//
//			// See if the initial prompt is an int or double prompt
//			boolean hasInitialIntPrompt = testForIntegerPrompt(noInputPrompt).getPercentage() > 0;
//			boolean hasInitialDoublePrompt = testForDoublePrompt(noInputPrompt).getPercentage() > 0;
//			boolean samePromptForBoth = hasInitialIntPrompt && hasInitialDoublePrompt;
//
//			// If we have not seen prompts for ints or doubles, check if they
//			// show up after giving input
//			if (!hasInitialIntPrompt) {
//				hasInitialIntPrompt = testForIntegerPrompt(doubleInputPrompt).getPercentage() > 0;
//			}
//			if (!hasInitialDoublePrompt) {
//				hasInitialDoublePrompt = testForDoublePrompt(integerInputPrompt).getPercentage() > 0;
//			}

			// Create an error message based on our findings
			String errorMessage = "";
			double credit = 1.0;
			if (!hasInitialIntPrompt) {
				errorMessage += "Program does not prompt for integer inputs\n";
				credit = 0.5;
			}
			if (!hasInitialDoublePrompt) {
				errorMessage += "Program does not prompt for double inputs\n";
				if (credit == 0.5) {
					credit = 0;
				} else {
					credit = 0.5;
				}
			}
			if (samePromptForBoth) {
				errorMessage = "Program does not prompt separately for int and double inputs\n";
				credit = 0.5;
			}

			if (credit == 1.0) {
				return pass();
			} else if (noInputPrompt.length() == 0) {
					
				throw new NotAutomatableException();
			} else {
				return partialPass(credit, errorMessage);
			}
		}

		catch (NotRunnableException e) {
			throw new NotGradableException();
		}
	}
}
