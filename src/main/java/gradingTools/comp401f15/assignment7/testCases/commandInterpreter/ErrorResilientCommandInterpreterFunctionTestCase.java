package gradingTools.comp401f15.assignment7.testCases.commandInterpreter;

import static grader.basics.config.BasicProjectExecution.restoreOutputAndGetRedirectedOutput;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import util.annotations.Tags;
import framework.execution.ARunningProject;
import framework.grading.testing.BasicTestCase;
import grader.basics.config.BasicProjectExecution;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.util.ProjectIntrospection;
import gradingTools.sharedTestCase.MethodExecutionTestCase;
import gradingTools.sharedTestCase.MethodExecutionTestCase.MethodEnvironment;

/**
 *
 * @author Andrew Vitkus
 */
public class ErrorResilientCommandInterpreterFunctionTestCase extends BasicTestCase {

    private static final String TEST_COMMAND_1 = "move Arthur 10 10 ";
    private static final String TEST_COMMAND_2 = "move hello world ";
    private static final String TEST_COMMAND_3 = "say 1 2 ";
        
    public ErrorResilientCommandInterpreterFunctionTestCase() {
        super("Error Resilient Command Interpreter Function Test Case");
    }

    @Override
    public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
        BasicProjectExecution.redirectOutput();
        try {
            Class<?> commandInterpreterClass = ProjectIntrospection.findClass(project, null, "CommandInterpreter", ".*[cC]ommand.*[iI]nterpreter.*", ".*[cC]ommand[iI]nterpreter.*");
            Class<?> bridgeSceneClass = ProjectIntrospection.findClass(project, null, "BridgeScene", ".*[bB]ridge.*[sS]cene.*", ".*[bB]ridge[sS]cene.*");
            Class<?> scannerBeanClass = ProjectIntrospection.findClass(project, null, "ScannerBean", ".*[sS]canner.*[bB]ean.*", ".*[sS]canner[bB]ean.*");

            Tags tags = commandInterpreterClass.getAnnotation(Tags.class);
            if (tags != null) {
                boolean isTagged = false;
                for(String tag : tags.value()) {
                    if (tag.equals("ErrorResilient")) {
                        isTagged = true;
                        break;
                    }
                }
                if (!isTagged) {
                    return fail("Not tagged 'ErrorResilient' so not checking");
                }
            }

            Constructor<?> commandInterpreterConstructor = null;
            Constructor<?> bridgeSceneConstructor;
            Constructor<?> scannerBeanConstructor;

            boolean bridgeFirst = true;
            try {
                Constructor<?>[] commandInterpreterConstructors = commandInterpreterClass.getConstructors();
                for(Constructor<?> c : commandInterpreterConstructors) {
                    Object[] params = c.getParameterTypes();
                    if (params.length != 2) {
                        continue;
                    }
                    if (((Class<?>)params[0]).isAssignableFrom(bridgeSceneClass)
                            && ((Class<?>)params[1]).isAssignableFrom(scannerBeanClass)) {
                        commandInterpreterConstructor = c;
                        bridgeFirst = true;
                        break;
                    } else if (((Class<?>)params[0]).isAssignableFrom(scannerBeanClass)
                            && ((Class<?>)params[1]).isAssignableFrom(bridgeSceneClass)) {
                        commandInterpreterConstructor = c;
                        bridgeFirst = false;
                        break;
                    }
                }
                Objects.requireNonNull(commandInterpreterConstructor);
                bridgeSceneConstructor = bridgeSceneClass.getConstructor();
                scannerBeanConstructor = scannerBeanClass.getConstructor();
            } catch(Exception e) {
                e.printStackTrace(System.out);
                return fail("Couldn't find correct constructor for CommandInterpreter, BridgeScene, or ScannerBean");
            }

            Method setCommand = null;
            Method getError = null;

            try {
                setCommand = ProjectIntrospection.getOrFindMethodList(project, this, commandInterpreterClass, "Command").stream().filter((Method m) -> m.getName().contains("set")).collect(Collectors.toList()).get(0);
                getError = (Method)getCheckable().getRequirements().getUserObject(ErrorResilientCommandInterpreterDefinedTestCase.COMMAND_INTERPETER_ERROR_METHOD);
                Objects.requireNonNull(getError);
            } catch (Exception e) {
                e.printStackTrace(System.out);
                return fail("Either the command setter or error getter cannot be found");
            }

            boolean[] results = checkInterpretSay(commandInterpreterConstructor, bridgeSceneConstructor, scannerBeanConstructor, bridgeFirst, setCommand, getError);

            if (results.length == 1) {
                return fail("Failed to instantiate CommandInterpreter");
            } else {
                int correct = count(results, true);
                int possible = results.length;
                if (correct == 0) {
                    return fail("Incorrect or no fail");
                } else if (correct == possible) {
                    return pass();
                } else {
                    double score = ((double)correct) / possible;
                    String message = buildMessage(results);
                    return partialPass(score, message);
                }   
            }
        } finally {
            String anOutput = restoreOutputAndGetRedirectedOutput();
            if (anOutput != null && !anOutput.isEmpty()) {
             	System.out.println(anOutput);
             	ARunningProject.appendToTranscriptFile(project, getCheckable().getName(), anOutput);
            }
        }
    }
        
    private static boolean[] checkInterpretSay(Constructor<?> commandInterpreterConstructor, Constructor<?> bridgeSceneConstructor, Constructor<?> scannerBeanConstructor, boolean bridgeFirst, Method setCommand, Method getError) {
        boolean[] ret = new boolean[10];
        Object scannerBeanInstance = BasicProjectExecution.timedInvoke(scannerBeanConstructor, new Object[]{});
        Object bridgeSceneInstance = BasicProjectExecution.timedInvoke(bridgeSceneConstructor, new Object[]{});
        
        
        Method conditionalTransform = null;
        try {
            conditionalTransform = ErrorResilientCommandInterpreterFunctionTestCase.class.getMethod("conditionalTransformation", Object.class, Predicate.class, UnaryOperator.class);
        } catch (NoSuchMethodException | SecurityException ex) { }
        Predicate<Object> isListCheck = (o) -> List.class.isAssignableFrom(o.getClass());
        UnaryOperator<Object> transformation = (o) -> Arrays.asList(((List)o).toArray());
        MethodExecutionTestCase.MethodEnvironment[] methods;
        if (conditionalTransform == null || !List.class.isAssignableFrom(getError.getReturnType())) {
            methods = new MethodExecutionTestCase.MethodEnvironment[]{
                MethodEnvironment.get(getError),                    // 0
                MethodEnvironment.get(setCommand, TEST_COMMAND_1),  // 1
                MethodEnvironment.get(getError),                    // 2
                MethodEnvironment.get(setCommand, TEST_COMMAND_2),  // 3
                MethodEnvironment.get(getError),                    // 4
                MethodEnvironment.get(setCommand, TEST_COMMAND_3),  // 5
                MethodEnvironment.get(getError),                    // 6
                MethodEnvironment.get(setCommand, TEST_COMMAND_1),  // 7
                MethodEnvironment.get(getError),                    // 8
            };
        } else {
            methods = new MethodExecutionTestCase.MethodEnvironment[]{
                MethodEnvironment.get(getError),                    // 0
                MethodEnvironment.get(conditionalTransform, MethodExecutionTestCase.M0_RET, isListCheck, transformation),
                MethodEnvironment.get(setCommand, TEST_COMMAND_1),  // 1
                MethodEnvironment.get(conditionalTransform, MethodExecutionTestCase.M0_RET, isListCheck, transformation),
                MethodEnvironment.get(setCommand, TEST_COMMAND_2),  // 3
                MethodEnvironment.get(conditionalTransform, MethodExecutionTestCase.M0_RET, isListCheck, transformation),
                MethodEnvironment.get(setCommand, TEST_COMMAND_3),  // 5
                MethodEnvironment.get(conditionalTransform, MethodExecutionTestCase.M0_RET, isListCheck, transformation),
                MethodEnvironment.get(setCommand, TEST_COMMAND_1),  // 7
                MethodEnvironment.get(conditionalTransform, MethodExecutionTestCase.M0_RET, isListCheck, transformation),
            };
        }
        
        Object[] exData;
        if (bridgeFirst) {
            exData = MethodExecutionTestCase.invoke(commandInterpreterConstructor,
                new Object[]{bridgeSceneInstance, scannerBeanInstance}, methods);
        } else {
            exData = MethodExecutionTestCase.invoke(commandInterpreterConstructor,
                new Object[]{scannerBeanInstance, bridgeSceneInstance}, methods);
        }
        System.out.println(Arrays.toString(exData));
        
        if (exData.length == 1) {
            return new boolean[]{false};
        }
        if (conditionalTransform != null && List.class.isAssignableFrom(getError.getReturnType())) {
            exData = Arrays.copyOfRange(exData, 1, exData.length);
        }
        
        if (List.class.isAssignableFrom(getError.getReturnType())) {
            System.out.println("Error is List");
            List<String> a = getStringListOrElse(exData, 0, null);//, ()->exData[0] instanceof CharSequence);
            List<String> b = getStringListOrElse(exData, 2, null);//, ()->exData[2] instanceof CharSequence);
            List<String> c = getStringListOrElse(exData, 4, null);//, ()->exData[4] instanceof CharSequence);
            List<String> d = getStringListOrElse(exData, 6, null);//, ()->exData[6] instanceof CharSequence);
            List<String> e = getStringListOrElse(exData, 8, null);//, ()->exData[8] instanceof CharSequence);
            
            ret[0] = checkNotIntance(exData, 0, Exception.class);
            ret[1] = checkEqual(exData, 0, null) || (a != null && a.isEmpty());
            ret[2] = checkNotIntance(exData, 1, Exception.class);
            ret[3] = checkEqual(exData, 2, null) || (b != null && b.isEmpty());
            ret[4] = checkNotIntance(exData, 3, Exception.class);
            ret[5] = c != null && c.size() > 0;
            ret[6] = checkNotIntance(exData, 5, Exception.class);
            ret[7] = d != null && d.size() > 0;
            ret[8] = (c == null || d == null) ? false : (Collections.indexOfSubList(d, c) != -1 ? c.equals(d) : true);
            ret[9] = checkEqual(exData, 8, null) || (e != null && e.isEmpty());
        } else if (getError.getReturnType().isArray()) {
            System.out.println("Error is Array");
            String[] a = getStringArrayOrElse(exData, 0, null);//, ()->exData[0] instanceof CharSequence);
            String[] b = getStringArrayOrElse(exData, 2, null);//, ()->exData[2] instanceof CharSequence);
            String[] c = getStringArrayOrElse(exData, 4, null);//, ()->exData[4] instanceof CharSequence);
            String[] d = getStringArrayOrElse(exData, 6, null);//, ()->exData[6] instanceof CharSequence);
            String[] e = getStringArrayOrElse(exData, 8, null);//, ()->exData[8] instanceof CharSequence);

            ret[0] = checkNotIntance(exData, 0, Exception.class);
            ret[1] = checkEqual(exData, 0, null) || (a != null && a.length == 0);
            ret[2] = checkNotIntance(exData, 1, Exception.class);
            ret[3] = checkEqual(exData, 2, null) || (b != null && b.length == 0);
            ret[4] = checkNotIntance(exData, 3, Exception.class);
            ret[5] = c != null && c.length > 0;
            ret[6] = checkNotIntance(exData, 5, Exception.class);
            ret[7] = d != null && d.length > 0;
            ret[8] = (c == null || d == null) ? false : (stringArrayStartsWith(d, c) ? Arrays.equals(c, d) : true);
            ret[9] = checkEqual(exData, 8, null) || (e != null && e.length == 0);
        } else {
            System.out.println("Error is String");
            String a = toStringOrElse(exData, 0, null);//, ()->exData[0] instanceof CharSequence);
            String b = toStringOrElse(exData, 2, null);//, ()->exData[2] instanceof CharSequence);
            String c = toStringOrElse(exData, 4, null);//, ()->exData[4] instanceof CharSequence);
            String d = toStringOrElse(exData, 6, null);//, ()->exData[6] instanceof CharSequence);
            String e = toStringOrElse(exData, 8, null);//, ()->exData[8] instanceof CharSequence);
            
            ret[0] = checkNotIntance(exData, 0, Exception.class);
            ret[1] = checkEqual(exData, 0, null) || (a != null && a.isEmpty());
            ret[2] = checkNotIntance(exData, 1, Exception.class);
            ret[3] = checkEqual(exData, 2, null) || (b != null && b.isEmpty());
            ret[4] = checkNotIntance(exData, 3, Exception.class);
            ret[5] = c != null && c.length() > 0;
            ret[6] = checkNotIntance(exData, 5, Exception.class);
            ret[7] = d != null && d.length() > 0;
            ret[8] = (c == null || d == null) ? false : (d.startsWith(c) ? c.equals(d) : true);
            ret[9] = checkEqual(exData, 8, null) || (e != null && e.isEmpty());
        }
        
        System.out.println(Arrays.toString(ret));
        
        return ret;
    }
    
    private static List<String> getStringListOrElse(Object[] results, int a, List<String> fallback) {
        if (a >= results.length) {
            return fallback;
        }
        if (!(results[a] instanceof List)) {
            return fallback;
        }
        return ((List<Object>)results[a]).stream().map((o)->o.toString()).collect(Collectors.toList());
    }
    
    private static boolean stringListStartsWith(List<String> a, List<String> b) {
        for(int i = 0; i < Math.min(a.size(), b.size()); i ++) {
            if (!a.get(i).equals(b.get(i))) {
                return false;
            }
        }
        if (a.size() != b.size()) {
            return false;
        }
        return true;
    }
    
    private static boolean stringArrayStartsWith(String[] a, String[] b) {
        for(int i = 0; i < Math.min(a.length, b.length); i ++) {
            if (!b[i].equals(a[i])) {
                return false;
            }
        }
        return true;
    }
    
    private static String[] getStringArrayOrElse(Object[] results, int a, String[] fallback) {
        if (a >= results.length) {
            return fallback;
        } else {
            if (results[a].getClass().isArray()) {
                return toStringArray((Object[])results[a]);
            } else {
                return fallback;
            }
        }
    }
    
    private static String[] toStringArray(Object[] oArr) {
        return Arrays.stream(oArr).map((o)->o.toString()).toArray(String[]::new);
    }
    
    private static Object getOrElse(Object[] results, int a, Object fallback) {
        if (a >= results.length) {
            return fallback;
        }
        return results[a];
    }
    
    private static Object getOrElse(Object[] results, int a, Object fallback, boolean nullValid) {
        return getOrElse(results, a, fallback, true, ()->true);
    }
    
    private static Object getOrElse(Object[] results, int a, Object fallback, BooleanSupplier isValid) {
        return getOrElse(results, a, fallback, true, isValid);
    }
    
    private static Object getOrElse(Object[] results, int a, Object fallback, boolean nullValid, BooleanSupplier isValid) {
        if (a >= results.length || !isValid.getAsBoolean()) {
            return fallback;
        }
        Object o = results[a];
        if (!nullValid && o == null) {
            return fallback;
        }
        return results[a];
    }
    
    private static String toStringOrElse(Object[] results, int a, String fallback) {
        Object o = getOrElse(results, a, fallback, false);
        if (o == null) {
            return null;
        } else {
            return o.toString();
        }
    }
    
    private static String toStringOrElse(Object[] results, int a, String fallback, BooleanSupplier isValid) {
        Object o = getOrElse(results, a, fallback, false, isValid);
        if (o == null) {
            return null;
        } else {
            return o.toString();
        }
    }
    
    private static boolean checkNEqual(Object[] results, int a, Object value) {
        if (a >= results.length) {
            return false;
        }
        
        return !Objects.equals(results[a], value);
    }
    
    private static boolean checkEqual(Object[] results, int a, Object value) {
        if (a >= results.length) {
            return false;
        }
        
        return Objects.equals(results[a], value);
    }
    
    private static boolean checkNotIntance(Object[] results, int a, Class<?> c) {
        return a < results.length && !c.isInstance(results[a]);
    }
    
    private static String buildMessage(boolean[] notes) {
        StringBuilder ret = new StringBuilder();
        if (notes[0] == false) {
            ret.append("Exception getting error before setting command\n");
        }
        if (notes[1] == false) {
            ret.append("Non-empty error message before setting command\n");
        }
        if (notes[2] == false) {
            ret.append("Exception getting error after setting valid command\n");
        }
        if (notes[3] == false) {
            ret.append("Non-empty error message after valid command\n");
        }
        if (notes[4] == false) {
            ret.append("Exception getting error after setting invalid command\n");
        }
        if (notes[5] == false) {
            ret.append("Empty error message after invalid command\n");
        }
        if (notes[6] == false) {
            ret.append("Exception getting error after setting 2 invalid commands\n");
        }
        if (notes[7] == false) {
            ret.append("Empty error message after invalid command\n");
        }
        if (notes[8] == false) {
            ret.append("Previous error messages appear to be retained with new invalid commands\n");
        }
        if (notes[9] == false) {
            ret.append("Errors not cleared when setting valid command\n");
        }
        return ret.toString();
    }
    
    private static int count(boolean[] arr, boolean value) {
        int count = 0;
        for(boolean bool : arr) {
            if (bool == value) {
                count ++;
            }
        }
        return count;
    }
    
    public static Object conditionalTransformation(Object target, Predicate<Object> condition, UnaryOperator<Object> transform) {
        if (condition.test(target)) {
            return transform.apply(target);
        } else {
            return target;
        }
    }
}
