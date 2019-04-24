package grader.junit;

import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Set;

import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import util.annotations.Explanation;
import util.annotations.Group;
import util.annotations.IsExtra;
import util.annotations.IsRestriction;
import util.annotations.MaxValue;
import framework.grading.testing.BasicTestCase;
import grader.basics.junit.AJUnitTestResult;
import grader.basics.junit.GradableJUnitSuite;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
//import grader.junit.test.directreference.ACartesianPointJUnitTester;
import grader.basics.testcase.JUnitTestCase;

public class AnOriginalJUnitTestToGraderTestCase extends BasicTestCase implements GraderTestCase{
	public static int DEFAULT_SCORE = 10;	
	int defaultScore = DEFAULT_SCORE;
	Class jUnitClass;
	boolean isExtra;
	boolean isRestriction;
	double maxScore;
	String explanation;
	String group = "";
	RunNotifier aRunNotifier = new RunNotifier();
	AJUnitTestResult runListener = new AJUnitTestResult();
	
	public AnOriginalJUnitTestToGraderTestCase (Class aJUnitClass) {
		init();
		setJUnitClass(aJUnitClass);	
	}
	
//	public AJUnitTestToGraderTestCase () {
//		init();
//	}
	public void init() {
		aRunNotifier.addListener(runListener);
	}
	
	public Class getJUnitClass() {
		return jUnitClass;
	}
	
	public void setDefaultScore(int aDefaultScore) {
		defaultScore = aDefaultScore;
	}
	
	public int getDefaultScore() {
		return defaultScore;
	}
	
	public void setMaxScore (Class aJUnitClass) {
		if (aJUnitClass.isAnnotationPresent(MaxValue.class)) {
			MaxValue aMaxValue =  (MaxValue) aJUnitClass.getAnnotation(MaxValue.class);
			maxScore = aMaxValue.value();
		} else {
			maxScore = defaultScore;
		}
	}
	public void setIsRestriction (Class aJUnitClass) {
		if (aJUnitClass.isAnnotationPresent(IsRestriction.class)) {
			IsRestriction anIsRestriction =  (IsRestriction) aJUnitClass.getAnnotation(IsRestriction.class);
			isRestriction = anIsRestriction.value();
		} else {
			isRestriction = false;
		}
	}
	public void setIsExtra (Class aJUnitClass) {
		if (aJUnitClass.isAnnotationPresent(IsExtra.class)) {
			IsExtra anIsExtra =  (IsExtra) aJUnitClass.getAnnotation(IsExtra.class);
			isExtra = anIsExtra.value();
		} else {
			isExtra = false;
		}
	}
	
	public void setExplanation (Class aJUnitClass) {
		if (aJUnitClass.isAnnotationPresent(Explanation.class)) {
			Explanation anExplanation =  (Explanation) aJUnitClass.getAnnotation(Explanation.class);
			explanation = anExplanation.value();
		} else {
			explanation = aJUnitClass.getSimpleName();
		}
		setName(explanation);
	}	

	public void setGroup (Class aJUnitClass) {
		if (aJUnitClass.isAnnotationPresent(Group.class)) {
			Group aGroup =  (Group) aJUnitClass.getAnnotation(Group.class);
			group = aGroup.value();
		} else {
			group = explanation;
		}
	}
	@Override
	public String getGroup() {
		return group;
	}
	public void setJUnitClass(Class aJUnitClass) {
		jUnitClass = aJUnitClass;
		setExplanation(aJUnitClass);
		setMaxScore(aJUnitClass);
		setIsRestriction(aJUnitClass);
		setIsExtra(aJUnitClass);
		setGroup(aJUnitClass);
//		this.jUnitClass = aJUnitClass;
//		if (aJUnitClass.isAnnotationPresent(MaxValue.class)) {
//			MaxValue aMaxValue =  (MaxValue) aJUnitClass.getAnnotation(MaxValue.class);
//			maxScore = aMaxValue.value();
//		} else {
//			maxScore = DEFAULT_SCORE;
//		}
		
	}
	public boolean isRestriction() {
		return isRestriction;
	}
	public boolean isExtra() {
		return isExtra;
	}
	public Double getMaxScore() {
		return  maxScore;
	}	
	public String getExplanation() {
		return explanation;
	}	
	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {
		try {
			Class aJUnitClass = getJUnitClass();
			runListener.setJUnitName(aJUnitClass.getName());
			Runner aRunner = new BlockJUnit4ClassRunner(aJUnitClass);
			aRunner.run(aRunNotifier);
			return runListener.getTestCaseResult();

			
		} catch (InitializationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return fail(e.getMessage());
		}
		// InitializationError
//		Runner aRunner = new BlockJUnit4ClassRunner(ACartesianPointParametrizedJUnitTester.class);
//		Runner aRunner = new BlockJUnit4ClassRunner(ASinglePointBeforeClassJUnitMultiTester.class);
		// IniitializationError
//		Runner aRunner = new BlockJUnit4ClassRunner(ACartesianPointParametrizedJUnitMultiTester.class);
//		return null;
	}
//	public static void main (String[] args) {
//		AnOriginalJUnitTestToGraderTestCase foo = new AnOriginalJUnitTestToGraderTestCase(ACartesianPointJUnitTester.class);
////		foo.setJUnitClass(ACartesianPointJUnitTester.class);
//		System.out.println (foo);
//	}

	@Override
	public void setMaxScore(double aMaxScore) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGroup(String newVal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRestriction(boolean newVal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setExtra(boolean newVal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TestCaseResult test() {
			return null;
		
	}

	@Override
	public void setExplanation(String newVal) {
		
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int numExecutions() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addPropertyChangeListenerRecursive(PropertyChangeListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getFractionComplete() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Double> getPercentages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getMessages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TestCaseResult> getTestCaseResults() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getUnroundedScore() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public double getComputedMaxScore() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int numLeafNodeDescendents() {
		return 0;
	}

	@Override
	public int numInternalNodeDescendents() {
		return 0;
	}

	@Override
	public Set<Class> getLeafClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Class> getPassClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Class> getPartialPassClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Class> getFailClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Class> getUntestedClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getDisplayedScore() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public JUnitTestCase getJUnitTestCase() {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public void setTopLevelSuite(GradableJUnitSuite newVal) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public GradableJUnitSuite getTopLevelSuite() {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	@Override
//	public Class[] getUntestedClasses() {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	@Override
//	public void setWriteToConsole(boolean newVal) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public boolean isWriteToConsole() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean isWriteToFile() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public void setWriteToFile(boolean writeToFile) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public boolean isWriteToServer() {
//		// TODO Auto-generated method stub
//		return false;
//	}

	
}
