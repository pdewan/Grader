package gradingTools.comp401f15.assignment10.testCases;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import tools.CompilationNavigation;
import wrappers.framework.project.ProjectWrapper;

//import com.github.antlrjavaparser.api.body.ClassOrInterfaceDeclaration;
//import com.github.antlrjavaparser.api.body.MethodDeclaration;

import framework.grading.testing.BasicTestCase;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.project.flexible.FlexibleClassDescription;
import grader.sakai.project.SakaiProject;

public class CommandListObjectTestCase extends BasicTestCase {
	
	public CommandListObjectTestCase () {
		super ("Command List Object Test case");
		
	}
	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {
		return pass();
//		ProjectWrapper aProjectWrapper = (ProjectWrapper) project;
//		SakaiProject aSakaiProject = aProjectWrapper.getProject();
//		Set<FlexibleClassDescription> descriptions = aSakaiProject.getClassesManager().tagToClassDescriptions("CommandList");
//		if (descriptions == null || descriptions.isEmpty()) {
//			descriptions = aSakaiProject.getClassesManager().tagToClassDescriptions("Command List");
//		}
//		if (descriptions == null || descriptions.isEmpty()) {
//			return fail ("Command list object not found");
//		}
//		boolean foundRunnable = false;
//		FlexibleClassDescription description = null;
//		for (FlexibleClassDescription aClassDescription:descriptions) {
//			foundRunnable = Runnable.class.isAssignableFrom(aClassDescription.getJavaClass());
//			if (foundRunnable) {
//				description = aClassDescription;
//				break;
//			}
//		}
//		if (!foundRunnable) {
//			return fail ("Command list not a runnable");
//		}
//		// Check that it loops and runs the command within the loop
//        try {
//        	
//            ClassOrInterfaceDeclaration classDef = CompilationNavigation.getClassDef(description.getCompilationUnit());
//            MethodDeclaration method = CompilationNavigation.getMethod(classDef, "run");
//            String code = method.getBody().toString();
//            StringBuilder result = new StringBuilder();
//            
//            // Look for a loop
//            double passes = 0;
//            Class<?> _class = description.getJavaClass();
//            for (Field field : _class.getDeclaredFields()) {
//                if (List.class.isAssignableFrom(field.getType()) || Vector.class.isAssignableFrom(field.getType())) {
//                    passes++;
//                    break;
//                }
//            }
//            if (passes == 0)
//                result.append("There is no List or Vector containing runnables.");
//            if (code.contains("for") || code.contains("while"))
//                passes++;
//            else
//                result.append("Couldn't find a loop.");
//
//            // Look for run
//            if (code.contains(".run();"))
//                passes++;
//            else
//                result.append("Couldn't find a run invocation.");
////            result.setScore(this.getCheckable().get * (passes / 3.0));
//            if (passes == 3)
//            	return pass();
//            return partialPass(passes/3.0, result.toString());
//        } catch (IOException e) {
//            return null;
//        }
    
		

		
		
	}
}
