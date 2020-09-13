package gradingTools.comp401f15.assignment1.testcases;

import grader.basics.execution.BasicProcessRunner;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.Project;
import grader.sakai.project.SakaiProject;
import gradingTools.sharedTestCase.checkstyle.CheckStyleClassDefinedTestCase;
import wrappers.framework.project.ProjectWrapper;

import java.util.List;

public class MainClassDefinedTestCase extends CheckStyleClassDefinedTestCase {

	public MainClassDefinedTestCase(String aDescriptor) {
		super(aDescriptor);
	}
//	 public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
////	        TestCaseResult aResult = super.test(project, autoGrade);
////		 TestCaseResult aResult = fail("foo");
//	        SakaiProject aProject = ((ProjectWrapper) project).getProject();
//	        String aMainClassUsed = aProject.getEntryPoints().get(MainClassFinder.MAIN_ENTRY_POINT);
//			 if (aMainClassUsed.matches(descriptor))
//				 return pass();
//			 if (aMainClassUsed.contains("main.") || aMainClassUsed.contains("Main.") ) {
//				 return partialPass(0.5, aResult.getNotes() + " but main package defined or main package has wrong case");
//			 }
//			 return fail("No  main class matching: " + descriptor);
////		 String aTag = descriptor;
////		 if (descriptor.startsWith("@"))
////			 aTag = descriptor.substring(1);
////		 else 
////			 return aResult;
////	        if (aResult.getPercentage() == 1.0) {
////	        	return aResult;
////	        }
////	        
////	        	List<ClassDescription> aClasses = IntrospectionUtil.findClassesByTag(project, aTag);
////	        	if (aClasses.size() == 1) {
////	        		return pass();
////	        	}
////	        	if (aClasses.size() > 1) {
////	        		return partialPass(0.5, "Multiple classes tagged:" + aTag + " " + aClasses);
////	        	}
////	        	return fail("No class tagged: " + aTag);
////	        
//	        
//	        
//	 }
	
	 protected TestCaseResult computeResult (Project aProject, String[] aCheckStyleLines, List<String> aFailedLines, boolean autoGrade) {
		 TestCaseResult aResult = singleMatchScore(aProject, aCheckStyleLines, aFailedLines, autoGrade);
		 if (aResult.getPercentage() != 1.0) {
			
			 SakaiProject aSakaiProject = ((ProjectWrapper) aProject).getProject();
			 if (aSakaiProject.getEntryPoints() == null || aSakaiProject.getEntryPoints().get(BasicProcessRunner.MAIN_ENTRY_POINT) == null)
				 return aResult;
			 String aMainClassUsed = aSakaiProject.getEntryPoints().get(BasicProcessRunner.MAIN_ENTRY_POINT);
			 if (aMainClassUsed.matches(descriptor))
				 return pass();
			 if (aMainClassUsed.contains("main.") || aMainClassUsed.contains("Main.") ) {
				 return partialPass(0.5, aResult.getNotes() + " but main package defined or main package has wrong case");
			 }
		 }
		 return aResult;
	    	
	}

}
