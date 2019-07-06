package gradingTools.comp401f15.assignment7.testCases.commandInterpreter;

import static grader.basics.config.BasicProjectExecution.restoreOutputAndGetRedirectedOutput;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
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
public class SayCommandInterpretedTestCase extends BasicTestCase {

    private static final String TEST_COMMAND = "say \"What is your name?\" ";
    private static final String EXPECTED_STRING = "What is your name?";
        
    public SayCommandInterpretedTestCase() {
        super("Say command test case");
    }

    @Override
    public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
        BasicProjectExecution.redirectOutput();
        try {
            Class<?> commandInterpreterClass = ProjectIntrospection.findClass(project, null, "CommandInterpreter", ".*[cC]ommand.*[iI]nterpreter.*", ".*[cC]ommand[iI]nterpreter.*");
            Class<?> bridgeSceneClass = ProjectIntrospection.findClass(project, null, "BridgeScene", ".*[bB]ridge.*[sS]cene.*", ".*[bB]ridge[sS]cene.*");
            Class<?> scannerBeanClass = ProjectIntrospection.findClass(project, null, "ScannerBean", ".*[sS]canner.*[bB]ean.*", ".*[sS]canner[bB]ean.*");

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
                    } else if (((Class<?>)params[0]).isAssignableFrom(scannerBeanClass)
                            && ((Class<?>)params[1]).isAssignableFrom(bridgeSceneClass)) {
                        commandInterpreterConstructor = c;
                        bridgeFirst = false;
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
            Method getScannedString = null;
            Method approach = null;
            Method getArthur = null;
            Method getGuard = null;
            Method getStringShape = null;
            Method getText = null;

            try {
                setCommand = ProjectIntrospection.getOrFindMethodList(project, this, commandInterpreterClass, "Command").stream().filter((Method m) -> m.getName().contains("set")).collect(Collectors.toList()).get(0);
                getScannedString = ProjectIntrospection.getOrFindMethodList(project, this, scannerBeanClass, "ScannedString").stream().filter((Method m) -> m.getName().contains("get")).collect(Collectors.toList()).get(0);
                approach = ProjectIntrospection.getOrFindMethodList(project, this, bridgeSceneClass, "approach").get(0);
                getArthur = ProjectIntrospection.getOrFindMethodList(project, this, bridgeSceneClass, "Arthur").get(0);
                getGuard = ProjectIntrospection.getOrFindMethodList(project, this, bridgeSceneClass, "Guard").stream().filter((Method m) -> !m.getName().matches(".*[aA]r[ae].*")).collect(Collectors.toList()).get(0);
                for(Method m : getArthur.getReturnType().getMethods()) {
                    boolean doPick = false;
                    Class<?> retType = m.getReturnType();
                    StructurePattern structurePattern = m.getReturnType().getAnnotation(StructurePattern.class);
                    if (structurePattern == null) {
                        if (retType.isInterface()) {
                            for(Class<?> clazz : ProjectIntrospection.getClassesForInterface(project, retType)) {
                                structurePattern = clazz.getAnnotation(StructurePattern.class);
                                if (structurePattern != null 
                                        && StructurePatternNames.STRING_PATTERN.equals(structurePattern.value())) {
                                    doPick = true;
                                    break;
                                }
                            }
                        }
                    } else if (StructurePatternNames.STRING_PATTERN.equals(structurePattern.value())) {
                        doPick = true;
                    }
                    if (doPick){
                        getStringShape = m;
                        break;
                    }
                }
                List<Method> lm = ProjectIntrospection.getOrFindMethodList(project, this, getStringShape.getReturnType(), "Text");
                lm = lm.stream().filter((s)->s.getName().contains("get")&&CharSequence.class.isAssignableFrom(s.getReturnType())).collect(Collectors.toList());
                if (lm.isEmpty()) {
                    lm = ProjectIntrospection.getOrFindMethodList(project, this, getStringShape.getReturnType(), "String");
                    lm = lm.stream().filter((s)->s.getName().contains("get")&&CharSequence.class.isAssignableFrom(s.getReturnType())).collect(Collectors.toList());
                }
                getText = lm.get(0);
            } catch (Exception e) {
                e.printStackTrace(System.out);
                return fail("At least one of the following can't be found: setCommand, getScannedString, approach, getArthur, getGuard, avatar speech getter, String_Pattern getText/getString");
            }

            boolean[] results = checkInterpretSay(commandInterpreterConstructor, bridgeSceneConstructor, scannerBeanConstructor, bridgeFirst, setCommand, getScannedString, approach, getArthur, getGuard, getStringShape, getText);

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
        
    private static boolean[] checkInterpretSay(Constructor<?> commandInterpreterConstructor, Constructor<?> bridgeSceneConstructor, Constructor<?> scannerBeanConstructor, boolean bridgeFirst, Method setCommand, Method getScannedString, Method approach, Method getArthur, Method getGuard, Method getStringShape, Method getText) {
        boolean[] ret = new boolean[3];
        Object scannerBeanInstance = BasicProjectExecution.timedInvoke(scannerBeanConstructor, new Object[]{});
        Object bridgeSceneInstance = BasicProjectExecution.timedInvoke(bridgeSceneConstructor, new Object[]{});
        
        
        MethodExecutionTestCase.MethodEnvironment[] methods = new MethodExecutionTestCase.MethodEnvironment[]{
            MethodEnvironment.get(bridgeSceneInstance, getArthur),                                  // 0
            MethodEnvironment.get(bridgeSceneInstance, getGuard),                                   // 1
            MethodEnvironment.get(MethodExecutionTestCase.M1_RET, getStringShape),                  // 2
            MethodEnvironment.get(bridgeSceneInstance, approach, MethodExecutionTestCase.M0_RET),   // 3
            MethodEnvironment.get(setCommand, TEST_COMMAND),                                        // 4
            MethodEnvironment.get(scannerBeanInstance, getScannedString),                           // 5
            MethodEnvironment.get(MethodExecutionTestCase.M2_RET, getText)                          // 6
        };
        
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
        
        ret[0] = checkNotIntance(exData, 4, Exception.class);
        ret[1] = checkEqual(exData, 5, TEST_COMMAND);
        ret[2] = checkEqual(exData, 6, EXPECTED_STRING);
        
        System.out.println(Arrays.toString(ret));
        
        return ret;
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
            ret.append("Exception when setting command\n");
        }
        if (notes[1] == false) {
            ret.append("The ScannerBean's ScannedString is not set properly\n");
        }
        if (notes[2] == false) {
            ret.append("Does not call say (or error in approach making say untestable)\n");
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
}
