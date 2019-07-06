package gradingTools.comp110.assignment3.testCases;



import framework.grading.testing.BasicTestCase;
import grader.basics.config.BasicProjectExecution;
import grader.basics.execution.NotRunnableException;
import grader.basics.execution.ResultingOutErr;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;

public class PromptStringTestCase extends BasicTestCase {
	public PromptStringTestCase() {
		super("Prompt printer test case");
	}

	private TestCaseResult testForStringPrompt(String output) throws NotGradableException {
		String lowercaseOutput = output.trim().toLowerCase();
		boolean containsString = lowercaseOutput.contains("string")
				|| lowercaseOutput.contains("word")|| lowercaseOutput.contains("text");
		boolean containsLength = lowercaseOutput.contains("length")||lowercaseOutput.contains("size")
				||lowercaseOutput.contains("line");
		boolean containsComputation = lowercaseOutput.contains("computation")||lowercaseOutput.contains("do");
		if (containsString && containsLength && containsComputation) {
			return pass();
		} else {
			String message = "";
			double score = 1.0;
			if (!containsString) {
				message += "Does not ask the user for a String\n";
				score -= 0.3;
			}
			if (!containsLength) {
				message += "Does not mention anything about the String length\n";
				score -= 0.3;
			}
			if(!containsComputation){
				message += "Does not ask the user for a computation\n";
				score -= 0.4;
			}
			if (score > 0) {
				return partialPass(score, message);
			} else {
				throw new NotGradableException();
			}
		}
	}

	@Override
	public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException,
			NotGradableException {
		try {

			// Get the output after inputting one string, as both prompts should now be up
//			RunningProject oneInputRunningProject = RunningProjectUtils.runProject(project, 1, "hello");
//			String oneInputPrompt = oneInputRunningProject.await();
			ResultingOutErr aResult = BasicProjectExecution.callMain("hello");
//			String oneInputPrompt = ProjectExecution.callMain("hello").out;
			String oneInputPrompt = aResult.out;
			String oneInputErr = aResult.err;
			if (!oneInputErr.isEmpty()) {
				System.out.println ("One input prompt has error");
			}


			// Check the prompt
			return testForStringPrompt(oneInputPrompt);

		} catch (NotRunnableException e) {
			throw new NotGradableException();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new NotGradableException();

		}
	}
}