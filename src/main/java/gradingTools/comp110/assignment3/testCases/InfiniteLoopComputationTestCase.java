package gradingTools.comp110.assignment3.testCases;

import framework.grading.testing.BasicTestCase;
import grader.basics.execution.BasicProjectExecution;
import grader.basics.execution.NotRunnableException;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;

public class InfiniteLoopComputationTestCase extends BasicTestCase {

	String input1;
	String choice1;
	String input2;
	String choice2;

	public InfiniteLoopComputationTestCase(String input1, String choice1, String input2,
			String choice2) {
		super("Test repeated request of inputs and choices");
		this.input1 = input1;
		this.choice1 = choice1;
		this.input2 = input2;
		this.choice2 = choice2;
	}

	@Override
	public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException,
			NotGradableException {

		try {
//			RunningProject runningProject = RunningProjectUtils.runProject(project, 1, input1,
//					choice1);
//			String output1 = runningProject.await();
			String output1 = BasicProjectExecution.callMain(input1, choice1).out;
//			if (output1.endsWith("\n")) {
//				output1 = output1.substring(0, output1.length() - 1);
//			}
//
//			RunningProject runningProject2 = RunningProjectUtils.runProject(project, 2, input1,
//					choice1, input2, choice2);
//			String output2 = runningProject2.await();
			String output2 = BasicProjectExecution.callMain(input1, choice1, input2, choice2).out;
			if (output2.startsWith(output1)) {
				if (output2.length() > output1.length() + 1) {
					return pass();
				} else {
					return fail("Does not continually ask for new Strings and calculations to run");
				}
			} else {
				throw new NotAutomatableException();
			}

		} catch (NotRunnableException e) {
			throw new NotAutomatableException();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new NotAutomatableException();
		}
	}
}
