package grader.junit;

import java.util.List;
import java.util.Map;

import framework.grading.FrameworkProjectRequirements;
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
		Map<String,List<GradableJUnitTest>> aGroupedGradables = BasicJUnitUtils.toGroupedGradables(aJUnitSuiteClass);
		//  these steps seem to be done only in grader mode
		Map<String, List<GraderTestCase>> aGroupedTestCases = JUnitUtils.toGraderTestCaseMap(aGroupedGradables);		
		addGroupedTwoLevelTestCases(aGroupedTestCases);		
	}
//	public static Map<String,List<GradableJUnitTest>> toGroupedGradables(Class<?> aJUnitSuiteClass) {
//		List<Class> aJUnitOrSuiteClasses = JUnitUtils.getTopLevelJUnitTestsAndSuites(aJUnitSuiteClass);
//		List<Class> aSuiteClasses = JUnitUtils.selectJUnitSuites(aJUnitOrSuiteClasses);
//		List<Class> aTestCases = new ArrayList(aJUnitOrSuiteClasses);
//		aTestCases.removeAll(aSuiteClasses);
//		Map<String, List<GradableJUnitTest>>  aGroupedTopLevelGradables = JUnitUtils.toTopLevelGradables(aTestCases);
//		Map<String, List<GradableJUnitTest>>  aGroupedSuiteGradables = JUnitUtils.toSuiteGradables(aSuiteClasses);
//		Map<String,List<GradableJUnitTest>> aGroupedGradables = new HashMap();
//		aGroupedGradables.putAll(aGroupedTopLevelGradables);
//		aGroupedGradables.putAll(aGroupedSuiteGradables);
//		return aGroupedSuiteGradables;
//	}
//	public static List<Class> selectJUnitSuites (List<Class> aJUnitSuiteAndTestCases) {
//		List<Class> retVal = new ArrayList();
//		for (Class aJUnitTestCase:aJUnitSuiteAndTestCases) {
//			if (isJUnitSuite(aJUnitTestCase)) {
//				retVal.add(aJUnitTestCase);
//			}
//		}
//		return retVal;
//	}
//	
////	public void addJUnitTestSuiteFlat (Class<?> aJUnitSuiteClass) {
////		List<Class> aJUnitClasses = getJUnitTestClassesDeep(aJUnitSuiteClass);
////		Map<String, List<JUnitTestToGraderTestCase>>  aGroupedTestCases = createAndCollectTopLevelTestCases(aJUnitClasses);
////		Map<String, List<JUnitTestToGraderTestCase>>  aGroupedSuiteTestCases = createAndCollectSuiteTestCases(aSuiteClasses)(aJUnitClasses);
////
////		addGroupedTwoLevelTestCases(aGroupedTestCases);
////		
////	}
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
////	public void separateAndAddtestCasesAndSuite(Map<String, List<JUnitTestToGraderTestCase>> aTestCases) {
////		for (String aGroup:aTestCases.keySet()) {
////			List<JUnitTestToGraderTestCase> aJUnitTestToGraderTestCases = aTestCases.get(aGroup);
////			double aTotalScore = computeTotalScore(aJUnitTestToGraderTestCases);
////			setPointWeights(aJUnitTestToGraderTestCases, aTotalScore);
////			JUnitTestToGraderTestCase aFirstCase = aJUnitTestToGraderTestCases.get(0);
////			
////			boolean anIsRestriction = aFirstCase.isRestriction();
////			boolean anIsExtraCredit = aFirstCase.isExtra();
////			if (anIsRestriction) {
////				addRestriction(aGroup, aTotalScore, aJUnitTestToGraderTestCases.toArray(testCasesType));
////			} else {
////				addFeature(aGroup, aTotalScore, anIsExtraCredit, aJUnitTestToGraderTestCases.toArray(testCasesType));
////			}			
////		}		
////	}
////	public void addGroupedFlatTestCases(Map<String, List<GraderTestCase>> aTestCases) {
////		for (String aGroup:aTestCases.keySet()) {
////			List<GraderTestCase> aJUnitTestToGraderTestCases = aTestCases.get(aGroup);
////			double aTotalScore = computeTotalScore(aJUnitTestToGraderTestCases);
////			setPointWeights(aJUnitTestToGraderTestCases, aTotalScore);
////			GraderTestCase aFirstCase = aJUnitTestToGraderTestCases.get(0);
////			
////			boolean anIsRestriction = aFirstCase.isRestriction();
////			boolean anIsExtraCredit = aFirstCase.isExtra();
////			if (anIsRestriction) {
////				addRestriction(aGroup, aTotalScore, aJUnitTestToGraderTestCases.toArray(testCasesType));
////			} else {
////				addFeature(aGroup, aTotalScore, anIsExtraCredit, aJUnitTestToGraderTestCases.toArray(testCasesType));
////			}			
////		}		
////	}
//	
//	
//	public static double  computeTotalScore (List<GraderTestCase> aJUnitTestToGraderTestCases) {
//		double aRetVal = 0;
//		for (GraderTestCase aJUnitTestToGraderTestCase:aJUnitTestToGraderTestCases) {
//			Double aMaxScore = aJUnitTestToGraderTestCase.getMaxScore();
//			if (aMaxScore != null)
//			aRetVal += aJUnitTestToGraderTestCase.getMaxScore();
//		}
//		return aRetVal;
//	}
//	public static void setPointWeights (List<GraderTestCase> aJUnitTestToGraderTestCases) {
//		double aTotalScore = computeTotalScore(aJUnitTestToGraderTestCases);
//		setPointWeights(aJUnitTestToGraderTestCases, aTotalScore);
//	}
//	
//	public static void setPointWeights (List<GraderTestCase> aJUnitTestToGraderTestCases, double aTotalScore) {
//		for (GraderTestCase aJUnitTestToGraderTestCase:aJUnitTestToGraderTestCases) {
//			Double aMaxScore = aJUnitTestToGraderTestCase.getMaxScore();
//			if (aMaxScore != null)
//			aJUnitTestToGraderTestCase.setPointWeight(aMaxScore/aTotalScore);
//		}
//	}
////	public  Map<String, List<GraderTestCase>> createAndCollectSuiteTestCases(List<Class> aSuiteClasses) {
////		Map<String, List<GraderTestCase>> aResult = new HashMap();
////		for (Class aSuiteClass:aSuiteClasses) {
////			GradableJUnitTest aSuiteProperties = new AGradableJUnitTest(aSuiteClass);
//////			String aFeatureName =  aProperties.getExplanation();
////			Double aFeatureScore = aSuiteProperties.getMaxScore();
////			List<Class> aLeafTestCases = getJUnitTestClassesDeep(aSuiteClass);
////			Double aTestMaxScore = null;
////			if (aSuiteProperties.getMaxScore() != null) {
////				aTestMaxScore = aSuiteProperties.getMaxScore()/aLeafTestCases.size();
////			}
////
////			List<GraderTestCase> aTestCases = new ArrayList();
////			for (Class aLeafTestCase:aLeafTestCases) {
////				GradableJUnitTest aTestCaseProperties = new AGradableJUnitTest(aLeafTestCase); 
////				if (aTestMaxScore != null) {
////					aTestCaseProperties.setMaxScore(aTestMaxScore);	
////					aTestCaseProperties.setGroup(aSuiteProperties.getExplanation());
////					aTestCaseProperties.setRestriction(aSuiteProperties.isRestriction());
////					aTestCaseProperties.setExtra(aSuiteProperties.isExtra());
////				}
////				GraderTestCase aJUnitTestToGraderTestCase =
////					 	new AGraderTestCase(aTestCaseProperties);
////				aTestCases.add(aJUnitTestToGraderTestCase);
////			}
////			
////			aResult.put(aSuiteProperties.getExplanation(), aTestCases);
////			
////		}
////		return aResult;
////	}
//	public  Map<String, List<GradableJUnitTest>> toSuiteGradables(List<Class> aSuiteClasses) {
//		Map<String, List<GradableJUnitTest>> aResult = new HashMap();
//		for (Class aSuiteClass:aSuiteClasses) {
//			GradableJUnitSuite aSuiteGradable = new AGradableJUnitTestSuite(aSuiteClass);
////			String aFeatureName =  aProperties.getExplanation();
//			Double aFeatureScore = aSuiteGradable.getMaxScore();
//			List<Class> aLeafTestCases = getJUnitTestClassesDeep(aSuiteClass);
//			Double aTestMaxScore = null;
//			if (aSuiteGradable.getMaxScore() != null) {
//				aTestMaxScore = aSuiteGradable.getMaxScore()/
//						aLeafTestCases.size();
//			}
//
//			List<GradableJUnitTest> aGradables = new ArrayList();
//			for (Class aLeafTestCase:aLeafTestCases) {
//				GradableJUnitTest aGradable = new AGradableJUnitTest(aLeafTestCase); 
//				if (aTestMaxScore != null) {
//					aGradable.setMaxScore(aTestMaxScore);	
//					aGradable.setGroup(aSuiteGradable.getExplanation());
//					aGradable.setRestriction(aSuiteGradable.isRestriction());
//					aGradable.setExtra(aSuiteGradable.isExtra());
//				}
//				
//				aGradables.add(aGradable);
//			}
//			aSuiteGradable.addAll(aGradables);
////			if (aGradables.size() > 1) { // they need a parent
//				aGradables.add(0, aSuiteGradable); // for consistency add it always
////			}
//			aResult.put(aSuiteGradable.getExplanation(), aGradables);
//			
//		}
//		return aResult;
//	}
//
//	
////	public  Map<String, List<GraderTestCase>> createAndCollectTopLevelTestCases(List<Class> aJUnitClasses) {
////		Map<String, List<GraderTestCase>> aResult = new HashMap();
////		for (Class aJUnitClass:aJUnitClasses) {
////			GraderTestCase aJUnitTestToGraderTestCase =
////					 	new AGraderTestCase(new AGradableJUnitTest(aJUnitClass));
////			String aGroup = aJUnitTestToGraderTestCase.getGroup();
////			List<GraderTestCase> aClasses = aResult.get(aGroup);
////			if (aClasses == null) {
////				aClasses = new ArrayList();
////				aResult.put(aGroup, aClasses);
////			}
////			aClasses.add(aJUnitTestToGraderTestCase);			
////		}
////		return aResult;
////	}
//	public  Map<String, List<GradableJUnitTest>> toTopLevelGradables
//	(List<Class> aJUnitClasses) {
//		Map<String, List<GradableJUnitTest>> aResult = new HashMap();
//		for (Class aJUnitClass:aJUnitClasses) {
//			GradableJUnitTest aGradableJUnitTest = new AGradableJUnitTest(aJUnitClass);			
//			String aGroup = aGradableJUnitTest.getGroup();
//			List<GradableJUnitTest> aClasses = aResult.get(aGroup);
//			GradableJUnitSuite aSuite;
//			if (aClasses == null) {
//				aClasses = new ArrayList();
//				aResult.put(aGroup, aClasses);
//				aSuite = new AGradableJUnitTestSuite(aJUnitClass);
//				aSuite.setExplanation(aGroup);
//				aClasses.add(aSuite);
//			}
//			aSuite = (GradableJUnitSuite) aClasses.get(0);
//			aClasses.add(aGradableJUnitTest);
//			aSuite.add(aGradableJUnitTest);
//		}
//		return aResult;
//	}
////	public  Map<String, List<JUnitTestToGraderTestCase>> createAndCollectTwoLevelTestCases(List<Class> aJUnitClasses) {
////		Map<String, List<JUnitTestToGraderTestCase>> aResult = new HashMap();
////		for (Class aJUnitClass:aJUnitClasses) {
////			JUnitTestToGraderTestCase aJUnitTestToGraderTestCase =
////					 	new AJUnitTestToGraderTestCase(aJUnitClass, new AJUnitTestToGraderProperties(aJUnitClass));
////			String aGroup = aJUnitTestToGraderTestCase.getGroup();
////			List<JUnitTestToGraderTestCase> aClasses = aResult.get(aGroup);
////			if (aClasses == null) {
////				aClasses = new ArrayList();
////				aResult.put(aGroup, aClasses);
////			}
////			aClasses.add(aJUnitTestToGraderTestCase);			
////		}
////		return aResult;
////	}
//	
//	public static List<Class> getJUnitTestClassesDeep (Class<?> aJUnitSuiteClass) {
//		Suite.SuiteClasses aSuiteClassAnnotation = aJUnitSuiteClass.getAnnotation(Suite.SuiteClasses.class);
//		if (aSuiteClassAnnotation == null)
//			return null;
//		Class[] aTestClasses = aSuiteClassAnnotation.value();
//		List<Class> retVal = new ArrayList();
//		for (Class aTestClass: aTestClasses) {
//			List<Class> aSubList = getJUnitTestClassesDeep(aTestClass);
//			if (aSubList == null) {
//				retVal.add(aTestClass);
//			} else {
//				retVal.addAll(aSubList);
//			}
//		}
//		return retVal;
//	}
//	public static boolean isJUnitSuite (Class<?> aClass) {
//		return aClass.getAnnotation(Suite.SuiteClasses.class) != null;
//	}
//	public static List<Class> getTopLevelJUnitTestsAndSuites (Class<?> aJUnitSuiteClass) {
//		Suite.SuiteClasses aSuiteClassAnnotation = aJUnitSuiteClass.getAnnotation(Suite.SuiteClasses.class);
//		if (aSuiteClassAnnotation == null)
//			return null;
//		Class[] aTestClasses = aSuiteClassAnnotation.value();
//		return Arrays.asList(aTestClasses);
//	}
	
//	public static void main (String[] args) {
//		JUnitProjectRequirements aJUnitProjectRequirements = new AJUnitProjectRequirements();
//		aJUnitProjectRequirements.addJUnitTestSuite(ReflectiveCartesianPointSuite.class);
//		aJUnitProjectRequirements.checkFeatures(null);
////		addJUnitTestSuite (CartesianPointSuite.class);
//		
//	}

}
