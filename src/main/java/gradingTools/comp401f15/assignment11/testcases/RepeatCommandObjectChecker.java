package gradingTools.comp401f15.assignment11.testcases;

import grader.checkers.ACheckResult;
import grader.checkers.AnAbstractFeatureChecker;
import grader.checkers.CheckResult;
import grader.project.flexible.FlexibleClassDescription;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import tools.CompilationNavigation;

//import com.github.antlrjavaparser.api.body.ClassOrInterfaceDeclaration;
//import com.github.antlrjavaparser.api.body.MethodDeclaration;

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 11/19/13
 * Time: 2:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class RepeatCommandObjectChecker extends AnAbstractFeatureChecker {

    @Override
    public CheckResult check() {
        CheckResult result = new ACheckResult();
        return result;
//        List<ClassDescription> aClasses = project.getClassesManager().get().findClass("Repeat", "Repeat", "Repeat", "Repeat");
//
//        aClasses = project.getClassesManager().get().findClass("Repeat", "Repeat", "Repeat", "Repeat");

        // Get the class that is tagged as "Command List"
//        Set<FlexibleClassDescription> descriptions = project.getClassesManager().tagToClassDescriptions("Repeat");
//        if (descriptions == null || descriptions.isEmpty()) {
//            result.setScore(0);
//            result.getLog().add("No classed tagged \"Repeat\"");
//            return result;
//        }
//        FlexibleClassDescription description = new ArrayList<FlexibleClassDescription>(descriptions).get(0);

        // Check that it loops and runs the command within the loop
//        try {
//            ClassOrInterfaceDeclaration classDef = CompilationNavigation.getClassDef(description.getCompilationUnit());
//            MethodDeclaration method = CompilationNavigation.getMethod(classDef, "run");
//            String code = method.getBody().toString();
//
//            // Look for a loop
//            double passes = 1;
//            if (code.contains("for") || code.contains("while"))
//                passes++;
//            else
//                result.getLog().add("Couldn't find a loop.");
//
//            // Look for run
//            if (code.contains(".run();"))
//                passes++;
//            else
//                result.getLog().add("Couldn't find a run invocation.");
//            result.setScore(feature.getMax() * (passes / 3.0));
//            return result;
//        } catch (IOException e) {
//            return null;
//        }
    }
}
