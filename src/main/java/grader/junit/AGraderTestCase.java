package grader.junit;

import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Set;

import framework.grading.testing.BasicTestCase;
import grader.basics.junit.AGradableJUnitTest;
import grader.basics.junit.AJUnitTestResult;
import grader.basics.junit.GradableJUnitSuite;
import grader.basics.junit.GradableJUnitTest;
import grader.basics.junit.MaxScoreAssignmentResult;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
//import grader.junit.test.directreference.ACartesianPointJUnitTester;
import grader.basics.testcase.JUnitTestCase;
/**
 * This is a transformation of a JUnit test case into a grader test ccase/
 * It keeps references to both the JUnit case and the tree node created by localchecks
 * It is not created in localchecks mode, as it has more info I suppose.
 * However, the test cases we have been creating are subclasses of grader test
 * cases, so things get confusing
 *
 */
public class AGraderTestCase extends BasicTestCase implements GraderTestCase{
	GradableJUnitTest delegateGradableJUnitTestCase;
	AJUnitTestResult runListener = new AJUnitTestResult();
	
	public AGraderTestCase (GradableJUnitTest aGraderProperties) {
	

		delegateGradableJUnitTestCase = aGraderProperties;

	}
	
//	public AJUnitTestToGraderTestCase () {
//		init();
//	}
	public void init() {
		delegateGradableJUnitTestCase.init();
	}
	
	public Class getJUnitClass() {
		return delegateGradableJUnitTestCase.getJUnitClass();
	}
	
	public void setDefaultScore(int aDefaultScore) {
		delegateGradableJUnitTestCase.setDefaultScore(aDefaultScore);
	}
	
	public int getDefaultScore() {
		return delegateGradableJUnitTestCase.getDefaultScore();
	}
	
	public void setMaxScore (Class aJUnitClass) {
//		if (aJUnitClass.isAnnotationPresent(MaxValue.class)) {
//			MaxValue aMaxValue =  (MaxValue) aJUnitClass.getAnnotation(MaxValue.class);
//			maxScore = aMaxValue.value();
//		} else {
//			maxScore = defaultScore;
//		}
		delegateGradableJUnitTestCase.setMaxScore(aJUnitClass);
	}
	public void setIsRestriction (Class aJUnitClass) {
//		if (aJUnitClass.isAnnotationPresent(IsRestriction.class)) {
//			IsRestriction anIsRestriction =  (IsRestriction) aJUnitClass.getAnnotation(IsRestriction.class);
//			isRestriction = anIsRestriction.value();
//		} else {
//			isRestriction = false;
//		}
		delegateGradableJUnitTestCase.setIsRestriction(aJUnitClass);
	}
	public void setIsExtra (Class aJUnitClass) {
//		if (aJUnitClass.isAnnotationPresent(IsExtra.class)) {
//			IsExtra anIsExtra =  (IsExtra) aJUnitClass.getAnnotation(IsExtra.class);
//			isExtra = anIsExtra.value();
//		} else {
//			isExtra = false;
//		}
		delegateGradableJUnitTestCase.setIsExtra(aJUnitClass);
	}
	
	public void setExplanation (Class aJUnitClass) {
//		if (aJUnitClass.isAnnotationPresent(Explanation.class)) {
//			Explanation anExplanation =  (Explanation) aJUnitClass.getAnnotation(Explanation.class);
//			explanation = anExplanation.value();
//		} else {
//			explanation = aJUnitClass.getSimpleName();
//		}
//		setName(explanation);
		delegateGradableJUnitTestCase.setExplanation(aJUnitClass);
	}	

	public void setGroup (Class aJUnitClass) {
//		if (aJUnitClass.isAnnotationPresent(Group.class)) {
//			Group aGroup =  (Group) aJUnitClass.getAnnotation(Group.class);
//			group = aGroup.value();
//		} else {
//			group = explanation;
//		}
		delegateGradableJUnitTestCase.setGroup(aJUnitClass);
	}
	@Override
	public String getGroup() {
//		return group;
		return delegateGradableJUnitTestCase.getGroup();
	}
	public void setJUnitClass(Class aJUnitClass) {
		delegateGradableJUnitTestCase.setJUnitClass(aJUnitClass);		
	}
	public boolean isRestriction() {
		return delegateGradableJUnitTestCase.isRestriction();
	}
	public boolean isExtra() {
		return delegateGradableJUnitTestCase.isExtra();
	}
	public Double getMaxScore() {
		return delegateGradableJUnitTestCase.getMaxScore();
	}	
	public String getExplanation() {
		return delegateGradableJUnitTestCase.getExplanation();
	}	
	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {
		// this will clear the cache
//    	CurrentProjectHolder.setProject(project); // in case some test reset the project
		return test();
//		try {
//			Class aJUnitClass = getJUnitClass();
//			runListener.setJUnitName(aJUnitClass.getName());
//			Runner aRunner = new BlockJUnit4ClassRunner(aJUnitClass);
//			aRunner.run(runNotifier);
//			return runListener.getTestCaseResult();
//
//			
//		} catch (InitializationError e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return fail(e.getMessage());
//		}

	}
//	public static void main (String[] args) {
//		AGraderTestCase foo = new AGraderTestCase( new AGradableJUnitTest(ACartesianPointJUnitTester.class));
////		foo.setJUnitClass(ACartesianPointJUnitTester.class);
//		System.out.println (foo);
//	}

	@Override
	public void setMaxScore(double aMaxScore) {
		delegateGradableJUnitTestCase.setMaxScore(aMaxScore);
		
	}

	@Override
	public void setGroup(String newVal) {
		delegateGradableJUnitTestCase.setGroup(newVal);
		
	}

	@Override
	public void setRestriction(boolean newVal) {
		delegateGradableJUnitTestCase.setRestriction(newVal);
		
	}

	@Override
	public void setExtra(boolean newVal) {
		delegateGradableJUnitTestCase.setExtra(newVal);
		
	}

	@Override
	public TestCaseResult test() {
		return delegateGradableJUnitTestCase.test();
	}

	@Override
	public void setExplanation(String newVal) {
		delegateGradableJUnitTestCase.setExplanation(newVal);
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return delegateGradableJUnitTestCase.getMessage();
	}

	@Override
	public String getStatus() {
		return delegateGradableJUnitTestCase.getStatus();
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener arg0) {
		delegateGradableJUnitTestCase.addPropertyChangeListener(arg0);		
	}

	@Override
	public int numExecutions() {
		return delegateGradableJUnitTestCase.numExecutions();
	}

	@Override
	public void addPropertyChangeListenerRecursive(PropertyChangeListener arg0) {
		delegateGradableJUnitTestCase.addPropertyChangeListener(arg0);		
	}

	@Override
	public double getFractionComplete() {
		return delegateGradableJUnitTestCase.getFractionComplete();
	}

	@Override
	public List<Double> getPercentages() {
		// TODO Auto-generated method stub
		return delegateGradableJUnitTestCase.getPercentages();
	}

	@Override
	public List<String> getMessages() {
		// TODO Auto-generated method stub
		return delegateGradableJUnitTestCase.getMessages();
	}

	@Override
	public List<TestCaseResult> getTestCaseResults() {
		// TODO Auto-generated method stub
		return delegateGradableJUnitTestCase.getTestCaseResults();
	}

	@Override
	public double getUnroundedScore() {
		// TODO Auto-generated method stub
		return delegateGradableJUnitTestCase.getUnroundedScore();
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return delegateGradableJUnitTestCase.getText();
	}

	@Override
	public double getComputedMaxScore() {
		// TODO Auto-generated method stub
		return delegateGradableJUnitTestCase.getComputedMaxScore();
	}

	@Override
	public int numLeafNodeDescendents() {
		return delegateGradableJUnitTestCase.numLeafNodeDescendents();
	}

	@Override
	public int numInternalNodeDescendents() {
		// TODO Auto-generated method stub
		return delegateGradableJUnitTestCase.numInternalNodeDescendents();
	}

	@Override
	public Set<Class> getLeafClasses() {
		// TODO Auto-generated method stub
		return delegateGradableJUnitTestCase.getLeafClasses();
	}

	@Override
	public Set<Class>  getPassClasses() {
		// TODO Auto-generated method stub
		return delegateGradableJUnitTestCase.getLeafClasses();
	}

	@Override
	public Set<Class> getPartialPassClasses() {
		// TODO Auto-generated method stub
		return delegateGradableJUnitTestCase.getPartialPassClasses();
	}

	@Override
	public Set<Class> getFailClasses() {
		// TODO Auto-generated method stub
		return delegateGradableJUnitTestCase.getFailClasses();
	}

	@Override
	public Set<Class> getUntestedClasses() {
		// TODO Auto-generated method stub
		return delegateGradableJUnitTestCase.getUntestedClasses();
	}

	@Override
	public double getDisplayedScore() {
		// TODO Auto-generated method stub
		return delegateGradableJUnitTestCase.getDisplayedScore();
	}
	public String toString() {
		return getName() + "(" + super.toString() + ")";
	}

	@Override
	public JUnitTestCase getJUnitTestCase() {
		return delegateGradableJUnitTestCase.getJUnitTestCase();
	}

	@Override
	public boolean isDefinesMaxScore() {
		return delegateGradableJUnitTestCase.isDefinesMaxScore();
	}

	@Override
	public void setDefinesMaxScore(boolean definesMaxScore) {
		delegateGradableJUnitTestCase.setDefinesMaxScore(definesMaxScore);
	}

	@Override
	public void fillLeafNodeDescendents(List<GradableJUnitTest> retVal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MaxScoreAssignmentResult assignMaxScores() {
		// TODO Auto-generated method stub
		return null;
	}


}
