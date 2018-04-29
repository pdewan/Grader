package framework.grading.testing;

import grader.basics.execution.RunningProject;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.permissions.Permissible;
import gradingTools.shared.testcases.utils.ABufferingTestInputGenerator;

/**
 * Like AnAbstractFeatureChecker
 */
public interface TestCase extends Permissible{

    /**
     * @return A name or short description about the test case.
     */
    public String getName();
    
    public void setName(String aName);

    /**
     * This tests the project to see if somethings is the way it is supposed to be
     *
     * @param project The project to test
     * @return A {@link TestCaseResult} containing the result and any notes.
     * @throws NotAutomatableException
     */
    public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException;

   
    public void setCheckable(Checkable checkable);

	Checkable getCheckable();
	 public double getPointWeight() ;
	public void setPointWeight(double pointWeight) ;

	TestCaseResult getLastResult();

	void setLastResult(TestCaseResult lastResult);

	ABufferingTestInputGenerator getOutputBasedInputGenerator();

	RunningProject getInteractiveInputProject();
}
