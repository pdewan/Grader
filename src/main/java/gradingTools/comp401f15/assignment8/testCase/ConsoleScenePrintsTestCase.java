package gradingTools.comp401f15.assignment8.testCase;

import static grader.basics.config.BasicProjectExecution.restoreOutputAndGetRedirectedOutput;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import gradingTools.sharedTestCase.MethodExecutionTestCase.ExecutionData;
import gradingTools.sharedTestCase.MethodExecutionTestCase.MethodEnvironment;

/**
 *
 * @author Andrew Vitkus
 */
public class ConsoleScenePrintsTestCase extends BasicTestCase {
        
    public ConsoleScenePrintsTestCase() {
        super("Console scene view prints notifications test case");
    }

    @Override
    public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
        BasicProjectExecution.redirectOutput();
        try {
            Class<?> consoleSceneViewClass = ProjectIntrospection.findClass(project, null, "ConsoleSceneView", ".*[cC]console.*[sS]cene.*", ".*[cC]onsole[sS]cene[vV]iew.*");
            Class<?> bridgeSceneClass = ProjectIntrospection.findClass(project, null, "BridgeScene", ".*[bB]ridge.*[sS]cene.*", ".*[bB]ridge[sS]cene.*");

            Constructor<?> consoleSceneViewConstructor = null;
            Constructor<?> bridgeSceneConstructor;

            try {
                for(Class<?> bscInterface : bridgeSceneClass.getInterfaces()) {
                    for(Constructor<?> constructor : consoleSceneViewClass.getConstructors()) {
                        if (constructor.getParameterCount() == 1) {
                            if (bscInterface.isAssignableFrom(constructor.getParameterTypes()[0])) {
                                consoleSceneViewConstructor = constructor;
                            }
                        }
                    }
                }
                Objects.requireNonNull(consoleSceneViewConstructor);
                bridgeSceneConstructor = bridgeSceneClass.getConstructor();
            } catch(Exception e) {
                e.printStackTrace(System.out);
                return fail("Couldn't find correct constructor for ConsoleSceneView or BridgeScene");
            }

            Method getArthur;
            Method[] getX = new Method[2];
            Method[] getY = new Method[2];
            Method approach;
            Method fail;

            try {
                approach = ProjectIntrospection.getOrFindMethodList(project, this, bridgeSceneClass, "approach").get(0);
                fail = ProjectIntrospection.getOrFindMethodList(project, this, bridgeSceneClass, "fail", "failed").get(0);

                getArthur = ProjectIntrospection.getOrFindMethodList(project, this, bridgeSceneClass, "Arthur").get(0);
                for(Method m : getArthur.getReturnType().getMethods()) {
                    boolean doPick = false;
                    Class<?> retType = m.getReturnType();
                    StructurePattern structurePattern = m.getReturnType().getAnnotation(StructurePattern.class);
                    if (m.getName().matches(".*[hH]ead.*")) {
                        doPick = true;
                    } else if (structurePattern == null) {
                        if (retType.isInterface()) {
                            for(Class<?> clazz : ProjectIntrospection.getClassesForInterface(project, retType)) {
                                structurePattern = clazz.getAnnotation(StructurePattern.class);
                                if (structurePattern != null 
                                        && (StructurePatternNames.IMAGE_PATTERN.equals(structurePattern.value())
                                            || StructurePatternNames.LABEL_PATTERN.equals(structurePattern.value()))) {
                                    doPick = true;
                                    break;
                                }
                            }
                        }
                    } else if (StructurePatternNames.IMAGE_PATTERN.equals(structurePattern.value()) 
                               || StructurePatternNames.LABEL_PATTERN.equals(structurePattern.value())) {
                        doPick = true;
                    }
                    if (doPick){
                        getX[0] = m;
                        getY[0] = m;
                        break;
                    }
                }
                List<Method> lm = ProjectIntrospection.getOrFindMethodList(project, this, getX[0].getReturnType(), "X");
                lm = lm.stream().filter((s)->s.getName().contains("get")).collect(Collectors.toList());
                getX[1] = lm.get(0);

                lm = ProjectIntrospection.getOrFindMethodList(project, this, getY[0].getReturnType(), "Y");
                lm = lm.stream().filter((s)->s.getName().contains("get")).collect(Collectors.toList());
                getY[1] = lm.get(0);
            } catch (Exception e) {
                e.printStackTrace(System.out);
                return fail("At least one of the following can't be found: aproach, fail, getArthur, Avatar.getHead, Head.getX, Head.getY");
            }

            boolean[] results = checkConsoleScene(consoleSceneViewConstructor, bridgeSceneConstructor, approach, fail, getArthur, getX, getY);

            if (results.length == 1) {
                return fail("Failed to instantiate ConsoleSceneView");
            } else {
                int correct = count(results, true);
                int possible = results.length;
                if (correct == possible) {
                    return pass();
                } else {
                    String message = buildMessage(results);
                    if (correct == 0) {
                        return fail(message);
                    } else {
                        double score = ((double)correct) / possible;
                        return partialPass(score, message);
                    }   
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
        
    private static boolean[] checkConsoleScene(Constructor<?> consoleSceneViewConstructor, Constructor<?> bridgeSceneConstructor, Method approach, Method fail, Method getArthur, Method[] getX, Method[] getY) {
        boolean[] ret = new boolean[4];
        Object bridgeSceneInstance = BasicProjectExecution.timedInvoke(bridgeSceneConstructor, new Object[]{});
        
        MethodEnvironment[] methods = new MethodExecutionTestCase.MethodEnvironment[]{
            MethodEnvironment.get(bridgeSceneInstance, getArthur),
            MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getX),
            MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getY),
            MethodEnvironment.get(30*1000, bridgeSceneInstance, approach, MethodExecutionTestCase.M0_RET),
            MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getX),
            MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getY),
            MethodEnvironment.get(30*1000, bridgeSceneInstance, fail),
            MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getX),
            MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getY)
        };
        
        ExecutionData[] exData = MethodExecutionTestCase.invokeGetEnvironment(consoleSceneViewConstructor, new Object[]{bridgeSceneInstance}, methods);
        for(ExecutionData data : exData) {
            System.out.println("Out: " + data.getStdOut());
            System.out.println("Err: " + data.getStdErr());
        }
        System.out.println(Arrays.toString(exData));
        
        if (exData.length == 1) {
            return new boolean[]{false};
        }
        
        String[][] approachPropertyChanges = parsePropertyChanges(exData[4].getStdOut());
        String[][] failPropertyChanges = parsePropertyChanges(exData[7].getStdOut());
        
        ret[0] = Stream.concat(Arrays.stream(exData[4].getStdOut().split("\n")), Arrays.stream(exData[7].getStdOut().split("\n")))
                .parallel().unordered()
                .map((line) -> line.matches("java\\.beans\\.PropertyChangeEvent\\[propertyName=.*; oldValue=.*; newValue=.*; propagationId=.*; source=.*\\]"))
                .allMatch((b)->b==true);
        
        if (ret[0] == true) {
            long noChange = Stream.concat(Arrays.stream(approachPropertyChanges), Arrays.stream(failPropertyChanges))
                    .parallel().unordered()
                    .filter((properties) -> properties[1].equals(properties[2])) // new/old value
                    .count();
            ret[1] = noChange == 0;

            long uniqueSources = Stream.concat(Arrays.stream(approachPropertyChanges), Arrays.stream(failPropertyChanges))
                    .parallel().unordered()
                    .map((properties) -> properties[4]) // source
                    .distinct()
                    .count();
            ret[2] = uniqueSources >= 7;
            String[][] all = Arrays.copyOf(approachPropertyChanges, approachPropertyChanges.length + failPropertyChanges.length);
            System.arraycopy(failPropertyChanges, 0, all, approachPropertyChanges.length, failPropertyChanges.length);
            // check for property with same source with an newValue equal to a past oldValue
            ret[3] = false;
            for(int i = 0; !ret[3] && i < all.length; i ++) {
                String[] prop1 = all[i];
                for(int j = i; !ret[3] && j < all.length; j ++) {
                    String[] prop2 = all[j];
                    if (prop1[4].equals(prop2[4]) && prop1[0].equals(prop2[0])) {
                        if (prop1[2].equals(prop2[1])) {
                            ret[3] = true;
                        }
                    }
                }
            }
        } else {
            ret[1] = false;
            ret[2] = false;
            ret[3] = false;
        }
        
        System.out.println(Arrays.toString(ret));
        
        return ret;
    }
    
    public static String[][] parsePropertyChanges(String lines) {
        return Arrays.stream(lines.split("\n")).parallel()
                .filter((line) -> line.startsWith("java.beans.PropertyChangeEvent"))
                .map(ConsoleScenePrintsTestCase::parsePropertyChange)
                .toArray(String[][]::new);
    }
    
    public static String[] parsePropertyChange(String line) {
        line = line.substring(line.indexOf('[')+1, line.lastIndexOf(']'));
        return Arrays.stream(line.split("; ")).parallel()
                .map((s) -> s.split("=")[1]).toArray(String[]::new);
    }
    
    private static String buildMessage(boolean[] notes) {
        StringBuilder ret = new StringBuilder();
        if (notes[0] == false) {
            ret.append("println not called directly on PropertyChangeEvent objects (note: all tests fail if this does)\n");
        }
        if (notes[1] == false) {
            ret.append("Properties reported despite having no change\n");
        }
        if (notes[2] == false) {
            ret.append("Not all objects are being listened to\n");
        }
        if (notes[3] == false) {
            ret.append("New value not used as old in subsequent changes\n");
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
    
    public static void printOut(String line) {
        System.out.println(line);
    }
    
    public static void printErr(String line) {
        System.err.println(line);
    }
}
