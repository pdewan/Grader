package grader.junit;

import java.util.List;
import java.util.Map;

import framework.grading.FrameworkProjectRequirements;
import grader.basics.execution.GradingMode;
import grader.basics.junit.BasicJUnitUtils;
import grader.basics.junit.GradableJUnitTest;
//import gradingTools.comp999junit.assignment1.testcases.reflection.ReflectiveCartesianPointSuite;


public class AJUnitProjectRequirements extends FrameworkProjectRequirements implements JUnitProjectRequirements{
//	public void addJUnitTestSuiteOriginal (Class<?> aJUnitSuiteClass) {
//		List<Class> aJUnitOrSuiteClasses = getTopLevelJUnitTestClassesAndSuites(aJUnitSuiteClass);
//		List<Class> aSuiteClasses = selectJUnitSuites(aJUnitOrSuiteClasses);
//		List<Class> aTestCases = new ArrayList(aJUnitOrSuiteClasses);
//		aTestCases.removeAll(aSuiteClasses);
//		Map<String, List<JUnitTestToGraderTestCase>>  aGroupedTopLevelTestCases = createAndCollectTopLevelTestCases(aTestCases);
//		Map<String, List<JUnitTestToGraderTestCase>>  aGroupedSuiteTestCases = createAndCollectSuiteTestCases(aSuiteClasses);
//		Map<String,List<JUnitTestToGraderTestCase>> aGroupedTestCases = new HashMap();
//		aGroupedTestCases.putAll(aGroupedTopLevelTestCases);
//		aGroupedTestCases.putAll(aGroupedSuiteTestCases);		
//		addGroupedTwoLevelTestCases(aGroupedTestCases);		
//	}
//	public static  Map<String,List<GraderTestCase>>  toGraderTestCaseMap (Map<String,List<GradableJUnitTest>> aGradableJUnitTestCaseMap) {
//		Map<String,List<GraderTestCase>> retVal = new HashMap();
//		for (String aGroup:aGradableJUnitTestCaseMap.keySet()) {
//			retVal.put(aGroup, JUnitUtils.toGraderTestCaseList(aGradableJUnitTestCaseMap.get(aGroup)));
//		}
//		return retVal;
//	}
//	public static  List<GradableJUnitTest> toGradableTree (Map<String,List<GradableJUnitTest>> aGradableJUnitTestCaseMap) {
//		List<GradableJUnitTest> retVal = new ArrayList();
//		for (String aGroup:aGradableJUnitTestCaseMap.keySet()) {
//			List<GradableJUnitTest> aGradables = aGradableJUnitTestCaseMap.get(aGroup);
//			if (aGradables.size() == 2) { // an ungrouped test
//				retVal.add(aGradables.get(1));
//			} else {
//				retVal.add(aGradables.get(0)); // will also have the children
//			}
////			retVal.put(aGroup, toGraderTestCaseList(aGradableJUnitTestCaseMap.get(aGroup)));
//		}
//		return retVal;
//	}
//	public static GraderTestCase toGraderTestCase(GradableJUnitTest aGradableJUnitCase){
//		return new AGraderTestCase(aGradableJUnitCase);
//	}
//	public static  List<GraderTestCase> toGraderTestCaseList(List<GradableJUnitTest> aGradableJUnitCaseList){
//		List<GraderTestCase> retVal = new ArrayList();
//		for (GradableJUnitTest aGradableJUnitTestCase:aGradableJUnitCaseList) {
//			if (aGradableJUnitTestCase instanceof GradableJUnitSuite)  
//				continue; // that is just for display and hierarchy purposes
//			retVal.add(new AGraderTestCase(aGradableJUnitTestCase));
//		}
//		return retVal;
//	}
	@Override
	public void addJUnitTestSuite (Class<?> aJUnitSuiteClass) {
		// This step is also called in localchecks mode
		if (GradingMode.isManualGradingOnly() && GradingMode.getGraderRun()) {
			return;
		}
		
		try {
			aJUnitSuiteClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // force its loading
		Map<String,List<GradableJUnitTest>> aGroupedGradables = BasicJUnitUtils.toGradableTree(aJUnitSuiteClass).groupedGradables;
//		Map<String,List<GradableJUnitTest>> aGroupedGradables = BasicJUnitUtils.toGroupedGradables(aJUnitSuiteClass);
		//  these steps seem to be done only in grader mode
		Map<String, List<GraderTestCase>> aGroupedTestCases = JUnitUtils.toGraderTestCaseMap(aGroupedGradables);		
		addGroupedTwoLevelTestCases(aGroupedTestCases);		
	}
	
	static GraderTestCase[] testCasesType = {};
//	
	public void addGroupedTwoLevelTestCases(Map<String, List<GraderTestCase>> aTestCases) {
		for (String aGroup:aTestCases.keySet()) {
			List<GraderTestCase> aJUnitTestToGraderTestCases = aTestCases.get(aGroup);
			double aTotalScore = JUnitUtils.computeTotalScore(aJUnitTestToGraderTestCases);
			JUnitUtils.setPointWeights(aJUnitTestToGraderTestCases, aTotalScore);
			GraderTestCase aFirstCase = aJUnitTestToGraderTestCases.get(0);
			
			boolean anIsRestriction = aFirstCase.isRestriction();
			boolean anIsExtraCredit = aFirstCase.isExtra();
			if (anIsRestriction) {
				addRestriction(aGroup, aTotalScore, aJUnitTestToGraderTestCases.toArray(testCasesType));
			} else {
				addFeature(aGroup, aTotalScore, anIsExtraCredit, aJUnitTestToGraderTestCases.toArray(testCasesType));
			}			
		}		
	}


}
