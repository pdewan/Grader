package gradingTools.comp401f15.assignment4.testcases;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import framework.grading.testing.BasicTestCase;
import grader.basics.execution.BasicProjectExecution;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.BasicProjectIntrospection;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.util.ProjectExecution;

public class ScannerHasErrorPropertyTestCase extends BasicTestCase {

    public ScannerHasErrorPropertyTestCase() {
        super("Scanner Has Error Property");
    }

    String[] scannerDescriptions = {null, "ScannerBean", ".*Bean.*", ".*Bean.*"};
    Class[] constructorArgTypes2 = {String.class};
    Class[] constructorArgTypes1 = {};
    String[] constructorArgs2 = {""};
    String[] constructorArgs1 = {};

    String inputEndingSpaces() {
//        return " { word 9+2 ";
        return "{ word @# 9+2 7654 @@ \"987 "; 
    }

    String input() {
        return "{ word @# 9+2 7654 @@ \"987";
    }

    String[] outputPropertyNames = {};

    public TestCaseResult test(Project aProject, Class[] aConstructorArgTypes, Object[] aConstructorArgs, String aScannedString) throws NotAutomatableException, NotGradableException {
        Class aClass = BasicProjectIntrospection.findClass(aProject,
                scannerDescriptions[0],
                scannerDescriptions[1],
                scannerDescriptions[2],
                scannerDescriptions[3]);
        if (aClass == null) {
            return fail("No scanner bean found");
        }
        Method errorPropertyGetter = null;
        for (Method m : aClass.getMethods()) {
            if (m.getName().matches("get.*[eE]rr.*")) {

//            if (m.getName().matches("get.*[eE]rror.*")) {
                errorPropertyGetter = m;
                break;
            }
        }
        if (errorPropertyGetter == null) {
            return fail("No scanner error getter");
        }

        Map<String, Object> anInputs = new HashMap<>();
        anInputs.put("ScannedString", aScannedString);
        String errorPropertyName = errorPropertyGetter.getName().substring(3);
        outputPropertyNames = new String[]{errorPropertyName};
        Map<String, Object> anActualOutputs = ProjectExecution.testBean(getCheckable().getName(), getName(), aProject, scannerDescriptions, aConstructorArgTypes, aConstructorArgs, anInputs, outputPropertyNames);
        if (anActualOutputs.get(BasicProjectExecution.MISSING_CLASS) != null) { // only output, no object
            return fail("Could not find scanner bean");
        }
        if (errorPropertyGetter.getReturnType().isArray()) {
            Object[] errors = (Object[]) anActualOutputs.get(errorPropertyName);
            if (errors != null && errors.length > 0) {
                return pass();
            } else {
                return partialPass(0.3, "Errors not logged");
            }
        } else if (errorPropertyGetter.getReturnType().equals(String.class)) {
            String errors = (String) anActualOutputs.get(errorPropertyName);
            if (errors != null && !errors.isEmpty()) {
                return pass();
            } else {
                return partialPass(0.3, "Errors not logged");
            }
        } else {
            return partialPass(0.2, "Error getter does not return Array or String");
        }
    }

    @Override
    public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
        if (project.getClassesManager().isEmpty()) {
            throw new NotGradableException();
        }

        TestCaseResult result = test(project, constructorArgTypes1, constructorArgs1, inputEndingSpaces());
        if (result.getNotes().contains("Could not find scanner bean")) {
            return result;
        }
        if (result.getPercentage() < 0.7) {
            result = test(project, constructorArgTypes2, constructorArgs2, inputEndingSpaces());
            if (result.getPercentage() < 0.7) {
                result = test(project, constructorArgTypes1, constructorArgs1, input());
                if (result.getPercentage() < 0.7) {
                    result = test(project, constructorArgTypes2, constructorArgs2, input());
                }
            }
        }
        return result;
    }
}
