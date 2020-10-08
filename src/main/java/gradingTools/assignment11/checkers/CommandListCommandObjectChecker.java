package gradingTools.assignment11.checkers;

import grader.checkers.ACheckResult;
import grader.checkers.AnAbstractFeatureChecker;
import grader.checkers.CheckResult;
import grader.project.flexible.FlexibleClassDescription;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import tools.CompilationNavigation;

//import com.github.antlrjavaparser.api.body.ClassOrInterfaceDeclaration;
//import com.github.antlrjavaparser.api.body.MethodDeclaration;

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 11/19/13
 * Time: 1:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommandListCommandObjectChecker extends AnAbstractFeatureChecker {
    @Override
    public CheckResult check() {
        CheckResult result = new ACheckResult();

        // Get the class that is tagged as "Command List"
        Set<FlexibleClassDescription> descriptions = project.getClassesManager().tagToClassDescriptions("Command List");
        if (descriptions.isEmpty()) {
            result.setScore(0);
            result.getLog().add("No classed tagged \"Command List\"");
            return result;
        }
        FlexibleClassDescription description = new ArrayList<FlexibleClassDescription>(descriptions).get(0);
        double passedTests = 1;

        // Check for the List of Vector
        Class<?> _class = description.getJavaClass();
        for (Field field : _class.getDeclaredFields()) {
            if (List.class.isAssignableFrom(field.getType()) || Vector.class.isAssignableFrom(field.getType())) {
                passedTests++;
                break;
            }
        }
        if (passedTests == 1)
            result.getLog().add("There is no List or Vector containing runnables.");

        // Check that it runs all of the commands
//        try {
//            ClassOrInterfaceDeclaration classDef = CompilationNavigation.getClassDef(description.getCompilationUnit());
//            MethodDeclaration method = CompilationNavigation.getMethod(classDef, "run");
//            String code = method.getBody().toString();
//            if (code.contains(".run();"))
//                passedTests++;
//            else
//                result.getLog().add("Command list should run other commands.");
//        } catch (IOException e) {
//            return null;
//        }

        result.setScore((passedTests / 3.0) * feature.getMax());
        return result;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
