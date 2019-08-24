package gradingTools.comp401f15.assignment4.testcases;

import static gradingTools.comp401f15.assignment4.testcases.ScannerBeanReturnsTokenInterfaceArrayTestCase.TOKEN_METHOD;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
import gradingTools.comp401f15.assignment3.testcases.ExtendedTokenDefinitions;
import gradingTools.comp401f15.assignment4.testcases.commands.CommandTokenDefinitions;

/**
 *
 * @author Andrew Vitkus
 */
public class MultiRunScannerBeanTestCase extends BasicTestCase {

    String[] scannerDescriptions = {null, "ScannerBean", ".*Bean.*", ".*Bean.*"};
    
    String[] scannerTestValues = new String[]{"define greet { say \"Hello\" } ",
                                              "rotateRightArm Galahad 6 ",
                                              "repeat call greet 2 ",
                                              "thread { rotatRightArm Robin 9 }"};
    
    String[] scannerTestExpectedTags = new String[]{"define Word Start say Quote End",
                                                    "rotateRightArm Word Number",
                                                    "repeat call Word Number",
//                                                    "thread Begin rotatRightArm Word Minus Number End"};
    												"thread Start rotateRightArm Word Number End"};

	static Object[] emptyObjects = {};

    @Override
    public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
        Class scannerClass = BasicProjectIntrospection.findClass(project, 
                                scannerDescriptions[0],
                                scannerDescriptions[1],
                                scannerDescriptions[2],
                                scannerDescriptions[3]);
        if (scannerClass == null) {
            return fail("Can't find scanner bean");
        }
        Object scanner;
        try {
            Constructor defaultScannerConstructor;
            defaultScannerConstructor = scannerClass.getConstructor();
            if (defaultScannerConstructor == null) {
                return fail("Can't find default scanner bean constructor");
            }
            scanner = defaultScannerConstructor.newInstance();
            if (scanner == null) {
                return fail("Couldn't to instaniate scanner bean. Is it nonstandard?");
            }
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            return fail("Problems finding and instantiating scanner bean");
        }
        Method tokenMethod = (Method)getCheckable().getRequirements().getUserObject(TOKEN_METHOD);
        Method stringSetterMethod;
        try {
            stringSetterMethod = scannerClass.getMethod("setScannedString", String.class);
        } catch (NoSuchMethodException | SecurityException ex) {
            stringSetterMethod = null;
        }
        if (stringSetterMethod == null) {
            for(Method m : scannerClass.getMethods()) {
                if (m.getName().matches(".*get.*[Ss]canned[Ss]tring.*")) {
                    stringSetterMethod = m;
                    break;
                }
            }
        }
        if (stringSetterMethod == null) {
            return fail("Can't find setter for scanner bean's ScannedString");
        }
        
        int[] tests = new int[]{4, 4, 4, 4}; // 4=correct, 2=old not cleared, 1=same as first, 0=wrong
        
        for(int i = 0; i < 4; i ++) {
            try {
                String testing = scannerTestValues[i];
                String[] expected = scannerTestExpectedTags[i].split(" ");
                Object[] args = {testing};
                BasicProjectExecution.timedInvoke(scanner, stringSetterMethod, new Object[] {testing});
//                stringSetterMethod.invoke(scanner, testing);
                Object[] tokens = (Object[]) tokenMethod.invoke(scanner);
                Map<String, Class> tokenClassMap = new HashMap<>();
                tokenClassMap.putAll(ExtendedTokenDefinitions.extendedTokenClassMap);
                tokenClassMap.putAll(CommandTokenDefinitions.commandTokenClassMap);
                
                if (tokens.length == expected.length) {
                    for(int j = 0; j < tokens.length; j ++) {
                        if(!tokenClassMap.containsKey(expected[j]) || 
                        		!tokenClassMap.get(expected[j]).isInstance(tokens[j])) {
//                            tests[i] = 0;
//                            break;
                            continue;
                        }
                    }
                } else if (i > 0) {
                    String conglomerateExpectedStr = scannerTestExpectedTags[0];
                    for(int j = 1; j <= i; j++) {
                        conglomerateExpectedStr += " " + scannerTestExpectedTags[j];
                    }
                    expected = conglomerateExpectedStr.split(" ");
                    if (tokens.length == expected.length) {
                        tests[i] = 2;
                        for(int j = 0; j < tokens.length; j ++) {
                            if(!tokenClassMap.containsKey(expected[j]) || !tokenClassMap.get(expected[j]).isInstance(tokens[j])) {
                                tests[i] = 0;
                                break;
                            }
                        }
                    } else {
                        String firstExpected = scannerTestExpectedTags[0];
                        expected = firstExpected.split(" ");
                        if (tokens.length == expected.length) {
                            tests[i] = 1;
                            for(int j = 0; j < tokens.length; j ++) {
                                if(!tokenClassMap.containsKey(expected[j]) || !tokenClassMap.get(expected[j]).isInstance(tokens[j])) {
                                    tests[i] = 0;
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    tests[i] = 0;
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                tests[i] = 0;
            } catch (Throwable e) {
            	tests[i] = 0;
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        int sum = 0;
        for(int test : tests) {
            sum += test;
        }
//        if (sum == 12) {

        if (sum == 16) {
            return pass();
        } else {
            sum -= 4;
            String message = buildMessage(tests);
            if (sum <= 0) {
                return fail(message);
            } else {
                return partialPass( (sum/16.0), message);
            }
        }
    }
    
    private static String buildMessage(int[] tests) {
        StringBuilder message = new StringBuilder();
        for(int i = 0; i < tests.length; i ++) {
            message.append("Pass ").append(i);
            switch(tests[i]) {
                case 4:
                    message.append(": Correct tokens");
                case 2:
                    message.append(": Tokens from previous runs were not removed\n");
                    break;
                case 1:
                    message.append(": Tokens were unchanged from first run despite new input\n");
                    break;
                case 0:
                    message.append(": Scanner returned incorrect tokens");
            }
        }
        return message.toString();
    }
    
    public MultiRunScannerBeanTestCase() {
        super("Multiple-Run Scanner Bean Test Case");
    }
}
