package gradingTools.comp790ColabInc.assignment1.testcases;

import grader.basics.project.Project;
import gradingTools.comp790Colab.assignment1.testcases.CollaborativeInputPromptTestCase;
import gradingTools.utils.RunningProjectUtils;

import java.util.Map;

import util.tags.DistributedTags;

public class CollaborativeIncrementalInputPromptTestCase extends CollaborativeInputPromptTestCase {
	public CollaborativeIncrementalInputPromptTestCase() {
//		super("Prompt printer test case");
		super();
	}
	
//	public static boolean hasIntegerPrompt(String output) {
//		return output.trim().toLowerCase().contains("int");
//	}
//	
//	public static boolean hasDoublePrompt(String output) {
//		return output.toLowerCase().contains("decimal") || output.toLowerCase().contains("double");
//	}

//	private TestCaseResult testForIntegerPrompt(OutputBasedMixedArithmeticInputGenerator anOutputBasedInputGenerator) {
////		if (output.trim().toLowerCase().contains("int"))
//		if (anOutputBasedInputGenerator.foundIntPrompt())
//			return pass();
//		else
//			return fail("Program does not contain prompt for integer");
//	}
//
//	private TestCaseResult testForDoublePrompt(OutputBasedMixedArithmeticInputGenerator anOutputBasedInputGenerator) {
//		if (anOutputBasedInputGenerator.foundDoublePrompt())
//			return pass();
//		else
//			return fail("Program does not contain prompt for double");
//	}
//	private TestCaseResult testForIntegerPrompt(String output) {
//		if (output.trim().toLowerCase().contains("int"))
//			return pass();
//		else
//			return fail("Program does not contain prompt for integer");
//	}
//
//	private TestCaseResult testForDoublePrompt(String output) {
//		if (output.toLowerCase().contains("decimal") || output.toLowerCase().contains("double"))
//			return pass();
//		else
//			return fail("Program does not contain prompt for double");
//	}
	protected void runProjectAndGatherOutputStats(Project project) {
		// Get the output when we have no input from the user
		CollaborativeOutputBasedMixedArithmeticInputGenerator anOutputBasedInputGenerator = new ACollaborativeOutputBasedMixedArithmeticInputGenerator(null, null);
		 System.out.println("Starting  project at time:" + System.currentTimeMillis());

		noInputRunningProject = RunningProjectUtils.runProject(project, 5,
				anOutputBasedInputGenerator);
		noInputPrompt = noInputRunningProject.await();
		 System.out.println("finished awaiting running project at time:" + System.currentTimeMillis());

//		if (anOutputBasedInputGenerator.foundDoublePrompt() && anOutputBasedInputGenerator.foundIntPrompt())
//			return pass();
//		else
//			return 	super.test(project, autoGrade);
//		
//		
//		
//		 noInputRunningProject = runAliceBobProject(project, 1);
//		 noInputPrompt = noInputRunningProject.await();
		Map<String, StringBuffer> aProcessToOutput = noInputRunningProject.getProcessOutput();
		 client1NoInputOutput = aProcessToOutput.get(DistributedTags.CLIENT_1);
		client2NoInputOutput = aProcessToOutput.get(DistributedTags.CLIENT_2);
		client1HasInitialIntPrompt = anOutputBasedInputGenerator.foundIntPrompt(DistributedTags.CLIENT_1);
		client2HasInitialDoublePrompt = anOutputBasedInputGenerator.foundDoublePrompt(DistributedTags.CLIENT_2);
		client1HasInitialDoublePrompt = anOutputBasedInputGenerator.foundDoublePrompt(DistributedTags.CLIENT_1);
		client2HasInitialIntPrompt = anOutputBasedInputGenerator.foundIntPrompt(DistributedTags.CLIENT_2);
		System.out.println("Gathered output stats");
//		client1HasInitialIntPrompt = testForIntegerPrompt(client1NoInputOutput).getPercentage() > 0;
//		client2HasInitialDoublePrompt = testForDoublePrompt(client2NoInputOutput).getPercentage() > 0;
//		client1HasInitialDoublePrompt = testForDoublePrompt(client1NoInputOutput).getPercentage() > 0;
//		client2HasInitialIntPrompt = testForIntegerPrompt(client2NoInputOutput).getPercentage() > 0;
	}

//	@Override
//	public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException,
//			NotGradableException {
//		try {
//
//			// Get the output when we have no input from the user
////			RunningProject noInputRunningProject = RunningProjectUtils.runProject(project, 1);
//			OutputBasedMixedArithmeticInputGenerator anOutputBasedInputGenerator = new ACollaborativeOutputBasedMixedArithmeticInputGenerator(1, 1.4);
//			RunningProject interactiveInputProject = RunningProjectUtils.runProject(project, 1,
//					anOutputBasedInputGenerator);
//			String incOutput = interactiveInputProject.await();
//			if (anOutputBasedInputGenerator.foundDoublePrompt() && anOutputBasedInputGenerator.foundIntPrompt())
//				return pass();
//			else
//				return 	super.test(project, autoGrade);
////			String noInputPrompt = noInputRunningProject.await();
////
////			// Get the output when we have integer input from the user
////			RunningProject integerInputRunningProject = RunningProjectUtils.runProject(project, 1,
////					"1");
////			String integerInputPrompt = integerInputRunningProject.await();
////			integerInputPrompt = integerInputPrompt.substring(noInputPrompt.length());
////
////			// Get the output when we have double input from the user
////			RunningProject doubleInputRunningProject = RunningProjectUtils.runProject(project, 1,
////					"1.4");
////			String doubleInputPrompt = doubleInputRunningProject.await();
////			doubleInputPrompt = doubleInputPrompt.substring(noInputPrompt.length());
//
//			// See if the initial prompt is an int or double prompt
////			boolean hasIntPrompt = testForIntegerPrompt(anOutputBasedInputGenerator).getPercentage() > 0;
////			boolean hasDoublePrompt = testForDoublePrompt(anOutputBasedInputGenerator).getPercentage() > 0;
//////			boolean samePromptForBoth = hasIntPrompt && hasDoublePrompt;
////			String noInputPrompt = "";
////			String integerInputPrompt = "";
////			String doubleInputPrompt = "";
////			// If we have not seen prompts for ints or doubles, check if they
////			// show up after giving input
////			if (!hasIntPrompt || !hasDoublePrompt) {
////				// Get the output when we have no input from the user
////				RunningProject noInputRunningProject = RunningProjectUtils.runProject(project, 1);
////				 noInputPrompt = noInputRunningProject.await();
////			}
////			
////			if (!hasIntPrompt) {
////				// Get the output when we have double input from the user
////				RunningProject doubleInputRunningProject = RunningProjectUtils.runProject(project, 1,
////						"1.4");
////				 doubleInputPrompt = doubleInputRunningProject.await();
////				doubleInputPrompt = doubleInputPrompt.substring(noInputPrompt.length());
////
////				hasIntPrompt = testForIntegerPrompt(doubleInputPrompt).getPercentage() > 0;
////			}
////			if (!hasDoublePrompt) {
////				// Get the output when we have integer input from the user
////				RunningProject integerInputRunningProject = RunningProjectUtils.runProject(project, 1,
////						"1");
////				 integerInputPrompt = integerInputRunningProject.await();
////				integerInputPrompt = integerInputPrompt.substring(noInputPrompt.length());
////				hasDoublePrompt = testForDoublePrompt(integerInputPrompt).getPercentage() > 0;
////
////			}
////			
////
////			// Create an error message based on our findings
////			String errorMessage = "";
////			double credit = 1.0;
////			if (!hasIntPrompt) {
////				errorMessage += "Program does not prompt for integer inputs\n";
////				credit = 0.5;
////			}
////			if (!hasDoublePrompt) {
////				errorMessage += "Program does not prompt for double inputs\n";
////				if (credit == 0.5) {
////					credit = 0;
////				} else {
////					credit = 0.5;
////				}
////			}
////			if (samePromptForBoth) {
////				errorMessage = "Program does not prompt separately for int and double inputs\n";
////				credit = 0.5;
////			}
////
////			if (credit == 1.0) {
////				return pass();
//////			} else if ((noInputPrompt.length()) > 0
//////					&& ((integerInputPrompt.length() > 0) || (doubleInputPrompt.length() > 0))) {
//////				throw new NotAutomatableException();
//////			} else {
////			} else if ((noInputPrompt.length() <= 0 && integerInputPrompt.length() <= 0 && doubleInputPrompt.length() <=0) ) {
////				throw new NotAutomatableException();
////			} else {
////				return partialPass(credit, errorMessage);
////			}
////		}
////
//		} catch (NotRunnableException e) {
//			throw new NotGradableException();
//		}
//	}
}
