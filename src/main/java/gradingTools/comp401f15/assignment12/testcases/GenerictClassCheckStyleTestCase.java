package gradingTools.comp401f15.assignment12.testcases;

import grader.basics.junit.TestCaseResult;
import grader.basics.project.Project;
import grader.sakai.project.SakaiProject;
import gradingTools.sharedTestCase.checkstyle.CheckStyleTestCase;

import java.util.List;

public class GenerictClassCheckStyleTestCase extends CheckStyleTestCase {
	public GenerictClassCheckStyleTestCase (String aTypeTag) {
		super (aTypeTag, aTypeTag + " is Generic Test Case");
	}

//	@Override
//	public TestCaseResult test(Project project, boolean autoGrade)
//			throws NotAutomatableException, NotGradableException {
//		Class<?> clazz = IntrospectionUtil.getOrFindClass(project, this, tag);
//		if (clazz == null) {
//			return fail(aTypeTag + " not found.", autoGrade);
//		}
//		
////		ParameterizedType foo = (ParameterizedType) clazz.getGenericSuperclass();
//		Class bar = clazz.asSubclass(clazz);
//		Type super2 = bar.getGenericSuperclass();
//
//		Object anObject;
//		try {
//			anObject = clazz.newInstance();
//			
//		} catch (InstantiationException | IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			return fail ("table class does not have empty constructor");
//		}
//		Class aConcreteClass = anObject.getClass();
//		
//		ParameterizedType aGenericSuperClass = (ParameterizedType) aConcreteClass.getGenericSuperclass();
//	
//		
//		if (Modifier.isAbstract(clazz.getModifiers()))
//            return pass(autoGrade);
//        return fail(tag + " is not abstract.", autoGrade);
//	}

	@Override
	public String regexLineFilter() {
		// TODO Auto-generated method stub
		return "(.*)" + getActualType() + "(.*)" + "should be generic" + "(.*)";
	}

	@Override
	public String failMessageSpecifier(List<String> aFailedLines) {
		// TODO Auto-generated method stub
		return typeTag + " should be generic";
	}
	 protected TestCaseResult computeResult (Project aProject, String[] aCheckStyleLines, List<String> aFailedLines, boolean autoGrade) {
		 return singleMatchScore(aProject, aCheckStyleLines, aFailedLines, autoGrade);
//		 if (aResult.getPercentage() != 1.0) {
//			 if (aProject.getEntryPoints() == null || aProject.getEntryPoints().get(MainClassFinder.MAIN_ENTRY_POINT) == null)
//				 return aResult;
//			 String aMainClassUsed = aProject.getEntryPoints().get(MainClassFinder.MAIN_ENTRY_POINT);
//			 if (aMainClassUsed.contains("main.") || aMainClassUsed.contains("Main.") ) {
//				 return partialPass(0.5, aResult.getNotes() + " but main package defined or main package has wrong case");
//			 }
//		 }
//		 return aResult;
	    	
	}

}
