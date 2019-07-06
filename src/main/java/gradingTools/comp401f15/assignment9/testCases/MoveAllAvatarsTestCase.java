package gradingTools.comp401f15.assignment9.testCases;

import static grader.basics.config.BasicProjectExecution.restoreOutputAndGetRedirectedOutput;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javassist.Modifier;
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
import gradingTools.sharedTestCase.utils.ExecutionResultTools;

/**
 *
 * @author Andrew Vitkus
 */
public class MoveAllAvatarsTestCase extends BasicTestCase {
    private static final Method keyPressed, keyReleased, keyTyped;
    private static final Method mousePressed, mouseReleased, mouseClicked;
    private static final Method dispatchEvent;
    private static final boolean okay;
    private static final int mouseX, mouseY;
    
    static {
        Method keyPressedTemp = null, keyReleasedTemp = null, keyTypedTemp = null;
        Method mousePressedTemp = null, mouseReleasedTemp = null, mouseClickedTemp = null;
        Method dispatchEventTemp = null;
        for(Method m : KeyListener.class.getMethods()) {
            switch(m.getName()) {
                case "keyPressed":
                    keyPressedTemp = m;
                    break;
                case "keyReleased":
                    keyReleasedTemp = m;
                    break;
                case "keyTyped":
                    keyTypedTemp = m;
            }
        }
        for(Method m : MouseListener.class.getMethods()) {
            switch(m.getName()) {
                case "mousePressed":
                    mousePressedTemp = m;
                    break;
                case "mouseReleased":
                    mouseReleasedTemp = m;
                    break;
                case "mouseClicked":
                    mouseClickedTemp = m;
            }
        }
        try {
            dispatchEventTemp = Component.class.getMethod("dispatchEvent", AWTEvent.class);
        } catch (NoSuchMethodException | SecurityException ex) {
            dispatchEventTemp = null;
        }
        dispatchEvent = dispatchEventTemp;
        keyPressed = keyPressedTemp;
        keyReleased = keyReleasedTemp;
        keyTyped = keyTypedTemp;
        mousePressed = mousePressedTemp;
        mouseReleased = mouseReleasedTemp;
        mouseClicked = mouseClickedTemp;
        
        okay = keyPressed!=null && keyReleased!=null && keyTyped!=null
                && mousePressed!=null && mouseReleased!=null && mouseClicked!=null
                && dispatchEvent != null;
        
        mouseX = 12345;
        mouseY = 54321;
    }
    
    public MoveAllAvatarsTestCase() {
        super("Move all avatars test case");
    }

    @Override
    public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
        BasicProjectExecution.redirectOutput();
        try {
            if (GraphicsEnvironment.isHeadless()) {
                throw new NotGradableException("Cannon run GUI on headless system");
            }
            if (!okay) {
                throw new NotGradableException("Failed to initalize test case");
            }

            Constructor<?> bridgeSceneControllerConstructor = null;
            Constructor<?> scenePainterConstructor = null;
            Constructor<?> bridgeSceneConstructor = null;

            Class<?> bridgeSceneControllerClass = ProjectIntrospection.findClass(project, null, "BridgeSceneController", ".*[bB]ridge[sS]cene[cC]ontroller.*", ".*[bB]ridge[sS]cene[cC]ontroller.*");
            Class<?> bridgeSceneClass = ProjectIntrospection.findClass(project, null, "BridgeScene", ".*[bB]ridge.*[sS]cene.*", ".*[bB]ridge[sS]cene.*");
            Class<?> bridgeScenePainter = ProjectIntrospection.findClass(project, null, "ObservableBridgeScenePainter", ".*[oO]bservable[bB]ridge[sS]cene[pP]ainter.*", ".*[oO]bservable[bB]ridge[sS]cene[pP]ainter.*");
            if (bridgeScenePainter == null) {
                bridgeScenePainter = ProjectIntrospection.findClass(project, null, "InheritingBridgeScenePainter", "[iI]nheriting[bB]ridge[sS]cene[pP]ainter.*", ".*[iI]nheriting[bB]ridge[sS]cene[pP]ainter.*");
            }

            boolean controllerTakesComponent = false;
            boolean controllerIsListener = MouseListener.class.isAssignableFrom(bridgeSceneControllerClass)
                    && KeyListener.class.isAssignableFrom(bridgeSceneControllerClass);

            try {
                for(Class<?> bridgeSceneInterface : bridgeSceneClass.getInterfaces()) {
                    if (bridgeSceneControllerConstructor == null) {
                        for(Constructor<?> constructor : bridgeSceneControllerClass.getConstructors()) {
                            if (bridgeSceneInterface.isAssignableFrom(constructor.getParameterTypes()[0])) {
                                Class<?> p2 = constructor.getParameterTypes()[1];
                                if (p2.isInterface()) {
                                    for(Class<?> p2Class : ProjectIntrospection.getClassesForInterface(project, p2)) {
                                        if (Component.class.isAssignableFrom(p2Class)) {
                                            bridgeSceneControllerConstructor = constructor;
                                            controllerTakesComponent = true;
                                            break;
                                        }
                                    }
                                } else {
                                    if (Component.class.isAssignableFrom(p2)) {
                                        bridgeSceneControllerConstructor = constructor;
                                        controllerTakesComponent = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (scenePainterConstructor == null) {
                        for(Constructor<?> constructor : bridgeScenePainter.getConstructors()) {
                            if (constructor.getParameterCount() == 1) {
                                if (bridgeSceneInterface.isAssignableFrom(constructor.getParameterTypes()[0])) {
                                    scenePainterConstructor = constructor;
                                    break;
                                }
                            }
                        }
                    }
                }
                Objects.requireNonNull(bridgeSceneControllerConstructor);
                Objects.requireNonNull(scenePainterConstructor);
                bridgeSceneConstructor = bridgeSceneClass.getConstructor();
            } catch(Exception e) {
                e.printStackTrace(System.out);
                return fail("Couldn't find correct constructor for BridgeSceneController, (Inheriting/Observable)BridgeScenePainter, or BridgeScene");
            }

            Method getArthur = null;
            Method getGalahad = null;
            Method getLancelot = null;
            Method getRobin = null;
            Method[] getX = new Method[2];
            Method[] getY = new Method[2];
            try {
                getArthur = ProjectIntrospection.getOrFindMethodList(project, this, bridgeSceneClass, "Arthur").get(0);
                getGalahad = ProjectIntrospection.getOrFindMethodList(project, this, bridgeSceneClass, "Galahad").get(0);
                getLancelot = ProjectIntrospection.getOrFindMethodList(project, this, bridgeSceneClass, "Lancelot").get(0);
                getRobin = ProjectIntrospection.getOrFindMethodList(project, this, bridgeSceneClass, "Robin").get(0);
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
                return fail("At least one of the following can't be found: getArthur, getGalahad, getLancelot, getRobin , Avatar.getHead, Head.getX, Head.getY");   
            }

            boolean[] results = checkMovement(bridgeSceneControllerConstructor, scenePainterConstructor, bridgeSceneConstructor, getArthur, getGalahad, getLancelot, getRobin, getX, getY, controllerIsListener, controllerTakesComponent);

            int correct = ExecutionResultTools.countBoolean(results, true);

            if (correct == results.length) {
                return pass();
            } else {
                String message = buildMessage(results);
                if (correct == 0) {
                    return fail(message);
                } else {
                    return partialPass(((double)correct) / results.length, message);
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
    
    private boolean[] checkMovement(Constructor<?> bridgeSceneControllerConstructor, Constructor<?> scenePainterConstructor, Constructor<?> bridgeSceneConstructor, Method getArthur, Method getGalahad, Method getLancelot, Method getRobin, Method[] getX, Method[] getY, boolean controllerIsListener, boolean controllerTakesComponent) {
        Object bridgeSceneInstance = BasicProjectExecution.timedInvoke(bridgeSceneConstructor, new Object[]{});
        Object scenePainter = BasicProjectExecution.timedInvoke(scenePainterConstructor, new Object[]{bridgeSceneInstance});
        
        Object bridgeSceneController;
        Component component = null;
        
        if (controllerTakesComponent) {
            if (scenePainter instanceof Component) {
                component = (Component)scenePainter;
                try {
                    bridgeSceneController = BasicProjectExecution.timedInvokeWithExceptions(bridgeSceneControllerConstructor, new Object[]{bridgeSceneInstance, component});
                } catch (Exception ex) {
                    return new boolean[]{false};
                }
            } else {
                return new boolean[]{false};
            }
        } else {
            try {
                bridgeSceneController = BasicProjectExecution.timedInvokeWithExceptions(bridgeSceneControllerConstructor, new Object[]{bridgeSceneInstance});
            } catch (Exception ex) {
                return new boolean[]{false};
            }
        }
        if (component == null && bridgeSceneController != null) {
            for(Field f : bridgeSceneControllerConstructor.getDeclaringClass().getDeclaredFields()) {
                try {
                    if (!Modifier.isPublic(f.getModifiers())) {
                        f.setAccessible(true);
                    }
                    if (Component.class.isAssignableFrom(f.getType())) {
                            component = (Component)f.get(bridgeSceneController);
                            break;
                    }
                } catch (IllegalArgumentException | IllegalAccessException | SecurityException ex) { }
            }
        }
        MethodEnvironment[] methods;
        if (controllerIsListener) {
            System.out.println("Controller is listener");
            methods = new MethodExecutionTestCase.MethodEnvironment[]{
                MethodExecutionTestCase.MethodEnvironment.get(bridgeSceneInstance, getArthur),                                                          // 0
                MethodExecutionTestCase.MethodEnvironment.get(bridgeSceneInstance, getGalahad),                                                         // 1
                MethodExecutionTestCase.MethodEnvironment.get(bridgeSceneInstance, getLancelot),                                                        // 2
                MethodExecutionTestCase.MethodEnvironment.get(bridgeSceneInstance, getRobin),                                                           // 3
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getX),       // 4
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getY),       // 5
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M1_RET, getX),       // 6
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M1_RET, getY),       // 7
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M2_RET, getX),       // 8
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M2_RET, getY),       // 9
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M3_RET, getX),       // 10
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M3_RET, getY),       // 11
                MethodExecutionTestCase.MethodEnvironment.get(mousePressed, buildMouseEvent(0, 0, MouseEvent.MOUSE_PRESSED, component)),                // 12
                MethodExecutionTestCase.MethodEnvironment.get(mouseReleased, buildMouseEvent(0, 0, MouseEvent.MOUSE_RELEASED, component)),              // 13
                MethodExecutionTestCase.MethodEnvironment.get(mouseClicked, buildMouseEvent(0, 0, MouseEvent.MOUSE_CLICKED, component)),                // 14
                MethodExecutionTestCase.MethodEnvironment.get(keyPressed, buildKeyEvent('a', KeyEvent.VK_A, KeyEvent.KEY_PRESSED, component)),          // 15
                MethodExecutionTestCase.MethodEnvironment.get(keyTyped, buildKeyEvent('a', KeyEvent.VK_UNDEFINED, KeyEvent.KEY_TYPED, component)),      // 16
                MethodExecutionTestCase.MethodEnvironment.get(keyReleased, buildKeyEvent('a', KeyEvent.VK_A, KeyEvent.KEY_RELEASED, component)),        // 17
                MethodExecutionTestCase.MethodEnvironment.get(keyPressed, buildKeyEvent('g', KeyEvent.VK_G, KeyEvent.KEY_PRESSED, component)),          // 18
                MethodExecutionTestCase.MethodEnvironment.get(keyTyped, buildKeyEvent('g', KeyEvent.VK_UNDEFINED, KeyEvent.KEY_TYPED, component)),      // 19
                MethodExecutionTestCase.MethodEnvironment.get(keyReleased, buildKeyEvent('g', KeyEvent.VK_G, KeyEvent.KEY_RELEASED, component)),        // 20
                MethodExecutionTestCase.MethodEnvironment.get(keyPressed, buildKeyEvent('l', KeyEvent.VK_L, KeyEvent.KEY_PRESSED, component)),          // 21
                MethodExecutionTestCase.MethodEnvironment.get(keyTyped, buildKeyEvent('l', KeyEvent.VK_UNDEFINED, KeyEvent.KEY_TYPED, component)),      // 22
                MethodExecutionTestCase.MethodEnvironment.get(keyReleased, buildKeyEvent('l', KeyEvent.VK_L, KeyEvent.KEY_RELEASED, component)),        // 23
                MethodExecutionTestCase.MethodEnvironment.get(keyPressed, buildKeyEvent('r', KeyEvent.VK_R, KeyEvent.KEY_PRESSED, component)),          // 24
                MethodExecutionTestCase.MethodEnvironment.get(keyTyped, buildKeyEvent('r', KeyEvent.VK_UNDEFINED, KeyEvent.KEY_TYPED, component)),      // 25
                MethodExecutionTestCase.MethodEnvironment.get(keyReleased, buildKeyEvent('r', KeyEvent.VK_R, KeyEvent.KEY_RELEASED, component)),        // 26
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getX),       // 27
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getY),       // 28
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M1_RET, getX),       // 29
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M1_RET, getY),       // 30
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M2_RET, getX),       // 31
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M2_RET, getY),       // 32
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M3_RET, getX),       // 33
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M3_RET, getY),       // 34
                MethodExecutionTestCase.MethodEnvironment.get(mousePressed, buildMouseEvent(mouseX, mouseY, MouseEvent.MOUSE_PRESSED, component)),      // 35
                MethodExecutionTestCase.MethodEnvironment.get(mouseReleased, buildMouseEvent(mouseX, mouseY, MouseEvent.MOUSE_RELEASED, component)),    // 36
                MethodExecutionTestCase.MethodEnvironment.get(mouseClicked, buildMouseEvent(mouseX, mouseY, MouseEvent.MOUSE_CLICKED, component)),      // 37
                MethodExecutionTestCase.MethodEnvironment.get(keyPressed, buildKeyEvent('a', KeyEvent.VK_A, KeyEvent.KEY_PRESSED, component)),          // 38
                MethodExecutionTestCase.MethodEnvironment.get(keyTyped, buildKeyEvent('a', KeyEvent.VK_UNDEFINED, KeyEvent.KEY_TYPED, component)),      // 39
                MethodExecutionTestCase.MethodEnvironment.get(keyReleased, buildKeyEvent('a', KeyEvent.VK_A, KeyEvent.KEY_RELEASED, component)),        // 40
                MethodExecutionTestCase.MethodEnvironment.get(keyPressed, buildKeyEvent('g', KeyEvent.VK_G, KeyEvent.KEY_PRESSED, component)),          // 41
                MethodExecutionTestCase.MethodEnvironment.get(keyTyped, buildKeyEvent('g', KeyEvent.VK_UNDEFINED, KeyEvent.KEY_TYPED, component)),      // 42
                MethodExecutionTestCase.MethodEnvironment.get(keyReleased, buildKeyEvent('g', KeyEvent.VK_G, KeyEvent.KEY_RELEASED, component)),        // 43
                MethodExecutionTestCase.MethodEnvironment.get(keyPressed, buildKeyEvent('l', KeyEvent.VK_L, KeyEvent.KEY_PRESSED, component)),          // 44
                MethodExecutionTestCase.MethodEnvironment.get(keyTyped, buildKeyEvent('l', KeyEvent.VK_UNDEFINED, KeyEvent.KEY_TYPED, component)),      // 45
                MethodExecutionTestCase.MethodEnvironment.get(keyReleased, buildKeyEvent('l', KeyEvent.VK_L, KeyEvent.KEY_RELEASED, component)),        // 46
                MethodExecutionTestCase.MethodEnvironment.get(keyPressed, buildKeyEvent('r', KeyEvent.VK_R, KeyEvent.KEY_PRESSED, component)),          // 47
                MethodExecutionTestCase.MethodEnvironment.get(keyTyped, buildKeyEvent('r', KeyEvent.VK_UNDEFINED, KeyEvent.KEY_TYPED, component)),      // 48
                MethodExecutionTestCase.MethodEnvironment.get(keyReleased, buildKeyEvent('r', KeyEvent.VK_R, KeyEvent.KEY_RELEASED, component)),        // 49
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getX),       // 50
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getY),       // 51
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M1_RET, getX),       // 52
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M1_RET, getY),       // 53
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M2_RET, getX),       // 54
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M2_RET, getY),       // 55
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M3_RET, getX),       // 56
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M3_RET, getY),       // 57
            };
        } else if (component != null) {
            System.out.println("Sending events to " + component);
            methods = new MethodExecutionTestCase.MethodEnvironment[]{
                MethodExecutionTestCase.MethodEnvironment.get(bridgeSceneInstance, getArthur),                                                          // 0
                MethodExecutionTestCase.MethodEnvironment.get(bridgeSceneInstance, getGalahad),                                                         // 1
                MethodExecutionTestCase.MethodEnvironment.get(bridgeSceneInstance, getLancelot),                                                        // 2
                MethodExecutionTestCase.MethodEnvironment.get(bridgeSceneInstance, getRobin),                                                           // 3
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getX),       // 4
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getY),       // 5
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M1_RET, getX),       // 6
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M1_RET, getY),       // 7
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M2_RET, getX),       // 8
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M2_RET, getY),       // 9
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M3_RET, getX),       // 10
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M3_RET, getY),       // 11
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildMouseEvent(0, 0, MouseEvent.MOUSE_PRESSED, component)),                // 12
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildMouseEvent(0, 0, MouseEvent.MOUSE_RELEASED, component)),              // 13
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildMouseEvent(0, 0, MouseEvent.MOUSE_CLICKED, component)),                // 14
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent('a', KeyEvent.VK_A, KeyEvent.KEY_PRESSED, component)),          // 15
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent('a', KeyEvent.VK_UNDEFINED, KeyEvent.KEY_TYPED, component)),      // 16
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent('a', KeyEvent.VK_A, KeyEvent.KEY_RELEASED, component)),        // 17
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent('g', KeyEvent.VK_G, KeyEvent.KEY_PRESSED, component)),          // 18
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent('g', KeyEvent.VK_UNDEFINED, KeyEvent.KEY_TYPED, component)),      // 19
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent('g', KeyEvent.VK_G, KeyEvent.KEY_RELEASED, component)),        // 20
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent('l', KeyEvent.VK_L, KeyEvent.KEY_PRESSED, component)),          // 21
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent('l', KeyEvent.VK_UNDEFINED, KeyEvent.KEY_TYPED, component)),      // 22
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent('l', KeyEvent.VK_L, KeyEvent.KEY_RELEASED, component)),        // 23
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent('r', KeyEvent.VK_R, KeyEvent.KEY_PRESSED, component)),          // 24
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent('r', KeyEvent.VK_UNDEFINED, KeyEvent.KEY_TYPED, component)),      // 25
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent('r', KeyEvent.VK_R, KeyEvent.KEY_RELEASED, component)),        // 26
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getX),       // 27
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getY),       // 28
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M1_RET, getX),       // 29
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M1_RET, getY),       // 30
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M2_RET, getX),       // 31
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M2_RET, getY),       // 32
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M3_RET, getX),       // 33
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M3_RET, getY),       // 34
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildMouseEvent(mouseX, mouseY, MouseEvent.MOUSE_PRESSED, component)),      // 35
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildMouseEvent(mouseX, mouseY, MouseEvent.MOUSE_RELEASED, component)),    // 36
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildMouseEvent(mouseX, mouseY, MouseEvent.MOUSE_CLICKED, component)),      // 37
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent('a', KeyEvent.VK_A, KeyEvent.KEY_PRESSED, component)),          // 38
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent('a', KeyEvent.VK_UNDEFINED, KeyEvent.KEY_TYPED, component)),      // 39
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent('a', KeyEvent.VK_A, KeyEvent.KEY_RELEASED, component)),        // 40
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent('g', KeyEvent.VK_G, KeyEvent.KEY_PRESSED, component)),          // 41
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent('g', KeyEvent.VK_UNDEFINED, KeyEvent.KEY_TYPED, component)),      // 42
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent('g', KeyEvent.VK_G, KeyEvent.KEY_RELEASED, component)),        // 43
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent('l', KeyEvent.VK_L, KeyEvent.KEY_PRESSED, component)),          // 44
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent('l', KeyEvent.VK_UNDEFINED, KeyEvent.KEY_TYPED, component)),      // 45
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent('l', KeyEvent.VK_L, KeyEvent.KEY_RELEASED, component)),        // 46
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent('r', KeyEvent.VK_R, KeyEvent.KEY_PRESSED, component)),          // 47
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent('r', KeyEvent.VK_UNDEFINED, KeyEvent.KEY_TYPED, component)),      // 48
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent('r', KeyEvent.VK_R, KeyEvent.KEY_RELEASED, component)),        // 49
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getX),       // 50
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getY),       // 51
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M1_RET, getX),       // 52
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M1_RET, getY),       // 53
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M2_RET, getX),       // 54
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M2_RET, getY),       // 55
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M3_RET, getX),       // 56
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M3_RET, getY),       // 57              // 18
            };
        } else {
            System.out.println("Couldn't find where to send events");
            return new boolean[]{false};
        }
        
        ExecutionData[] exData = MethodExecutionTestCase.invokeGetEnvironment(bridgeSceneController, methods);
        
        for(ExecutionData ed : exData) {
            System.out.println(ed.getRetVal());
        }
        
        boolean[] retVal = new boolean[4];
        
        retVal[0] = ExecutionResultTools.checkNotException(exData, 12)
                && ExecutionResultTools.checkNotException(exData, 13)
                && ExecutionResultTools.checkNotException(exData, 14);
        
        retVal[1] = ExecutionResultTools.checkNotException(exData, 15)
                && ExecutionResultTools.checkNotException(exData, 16)
                && ExecutionResultTools.checkNotException(exData, 17)
                && ExecutionResultTools.checkNotException(exData, 18)
                && ExecutionResultTools.checkNotException(exData, 19)
                && ExecutionResultTools.checkNotException(exData, 20)
                && ExecutionResultTools.checkNotException(exData, 21)
                && ExecutionResultTools.checkNotException(exData, 22)
                && ExecutionResultTools.checkNotException(exData, 23)
                && ExecutionResultTools.checkNotException(exData, 24)
                && ExecutionResultTools.checkNotException(exData, 25)
                && ExecutionResultTools.checkNotException(exData, 26);
        
        
        int moved = 0;
        int movedCorrect = 0;
        int startX, startY, endX, endY;
        for(int i = 0; i < 8; i += 2) {
            if (ExecutionResultTools.checkNEqual(exData, 4+i, 27+i)
                || ExecutionResultTools.checkNEqual(exData, 5+i, 28+i)) {
                moved ++;
            }
            Optional<Integer> value = ExecutionResultTools.getIntValue(exData, 27+i);
            if (value.isPresent()) {
                startX = value.get();
                value = ExecutionResultTools.getIntValue(exData, 28+i);
                if (value.isPresent()) {
                    startY = value.get();
                    value = ExecutionResultTools.getIntValue(exData, 50+i);
                    if (value.isPresent()) {
                        endX = value.get();
                        value = ExecutionResultTools.getIntValue(exData, 51+i);
                        if (value.isPresent()) {
                            endY = value.get();
                            if (endX - startX == mouseX && endY - startY == mouseY) {
                                movedCorrect ++;
                            }
                        }
                    }
                }
            }
        }
        
        retVal[2] = moved == 4;
        retVal[3] = movedCorrect == 4;
        
        System.out.println(Arrays.toString(retVal));
        
        return retVal;
    }
 
    public static MouseEvent buildMouseEvent(int x, int y, int type, Component origin) {
        return new MouseEvent(origin, type, System.currentTimeMillis(), 0, x, y, type == MouseEvent.MOUSE_CLICKED ? 1:0, false);
    }
    
    public static KeyEvent buildKeyEvent(char c, int keyCode, int type, Component origin) {
        int location;
        if (type == KeyEvent.KEY_TYPED) {
            keyCode = KeyEvent.VK_UNDEFINED;
            location = KeyEvent.KEY_LOCATION_UNKNOWN;
        } else {
            location = KeyEvent.KEY_LOCATION_STANDARD;
        }
        
        return new KeyEvent(origin, type, System.currentTimeMillis(), 0, keyCode, c, location);
    }
    
    private static String buildMessage(boolean[] notes) {
        StringBuilder ret = new StringBuilder();
        if (notes[0] == false) {
            ret.append("Exception handling MouseEvent\n");
        }
        if (notes[1] == false) {
            ret.append("Exception handling KeyEvents\n");
        }
        if (notes[2] == false) {
            ret.append("At least one avatar does not move\n");
        }
        if (notes[3] == false) {
            ret.append("At least one avatar does not move to click\n");
        }
        return ret.toString();
    }
}
