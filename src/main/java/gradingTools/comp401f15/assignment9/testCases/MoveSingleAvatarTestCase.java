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
public class MoveSingleAvatarTestCase extends BasicTestCase {
    private final String avatarName;
    private static final Method keyPressed, keyReleased, keyTyped;
    private static final Method mousePressed, mouseReleased, mouseClicked;
    private static final Method dispatchEvent;
    private static final boolean staticOkay;
    private final char inputChar;
    private final int inputCode;
    private final int mouseX, mouseY;
    private final boolean localOkay;
    
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
        
        staticOkay = keyPressed!=null && keyReleased!=null && keyTyped!=null
                && mousePressed!=null && mouseReleased!=null && mouseClicked!=null
                && dispatchEvent != null;
    }
    
    public MoveSingleAvatarTestCase(String avatar) {
        super("Move " + avatar + " test case");
        avatarName = avatar;
        char inputCharTemp = ' ';
        int inputCodeTemp = KeyEvent.VK_UNDEFINED;
        int mouseXTemp = 0;
        int mouseYTemp = 0;
        boolean okayTemp = staticOkay;
        switch(avatarName) {
            case "Arthur":
                inputCharTemp = 'a';
                inputCodeTemp = KeyEvent.VK_A;
                mouseXTemp = 10000;
                mouseYTemp = 5000;
                break;
            case "Lancelot":
                inputCharTemp = 'l';
                inputCodeTemp = KeyEvent.VK_L;
                mouseXTemp = 15000;
                mouseYTemp = 7500;
                break;
            case "Galahad":
                inputCharTemp = 'g';
                inputCodeTemp = KeyEvent.VK_R;
                mouseXTemp = 9001;
                mouseYTemp = 20000;
                break;
            case "Robin":
                inputCharTemp = 'r';
                inputCodeTemp = KeyEvent.VK_G;
                mouseXTemp = 12500;
                mouseYTemp = 24500;
                break;
            default:
                okayTemp = false;
        }
        inputChar = inputCharTemp;
        inputCode = inputCodeTemp;
        mouseX = mouseXTemp;
        mouseY = mouseYTemp;
        localOkay = okayTemp;
    }

    @Override
    public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
        BasicProjectExecution.redirectOutput();
        try {
            if (GraphicsEnvironment.isHeadless()) {
                throw new NotGradableException("Cannon run GUI on headless system");
            }
            if (!localOkay) {
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
                            if (constructor.getParameterCount() == 1) {
                                if (bridgeSceneInterface.isAssignableFrom(constructor.getParameterTypes()[0])) {
                                    bridgeSceneControllerConstructor = constructor;
                                }
                            } else if (constructor.getParameterCount() == 2) {
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

            Method getAvatar = null;
            Method[] getX = new Method[2];
            Method[] getY = new Method[2];
            try {
                getAvatar = ProjectIntrospection.getOrFindMethodList(project, this, bridgeSceneClass, avatarName).get(0);
                for(Method m : getAvatar.getReturnType().getMethods()) {
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
                return fail("At least one of the following can't be found: get" + avatarName + " , Avatar.getHead, Head.getX, Head.getY");   
            }

            boolean[] results = checkMovement(bridgeSceneControllerConstructor, scenePainterConstructor, bridgeSceneConstructor, getAvatar, getX, getY, controllerIsListener, controllerTakesComponent);

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
    
    private boolean[] checkMovement(Constructor<?> bridgeSceneControllerConstructor, Constructor<?> scenePainterConstructor, Constructor<?> bridgeSceneConstructor, Method getAvatar, Method[] getX, Method[] getY, boolean controllerIsListener, boolean controllerTakesComponent) {
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
                MethodExecutionTestCase.MethodEnvironment.get(bridgeSceneInstance, getAvatar),                                                          // 0
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getX),       // 1
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getY),       // 2
                MethodExecutionTestCase.MethodEnvironment.get(mousePressed, buildMouseEvent(0, 0, MouseEvent.MOUSE_PRESSED, component)),              // 3
                MethodExecutionTestCase.MethodEnvironment.get(mouseReleased, buildMouseEvent(0, 0, MouseEvent.MOUSE_RELEASED, component)),            // 4
                MethodExecutionTestCase.MethodEnvironment.get(mouseClicked, buildMouseEvent(0, 0, MouseEvent.MOUSE_CLICKED, component)),              // 5
                MethodExecutionTestCase.MethodEnvironment.get(keyPressed, buildKeyEvent(inputChar, inputCode, KeyEvent.KEY_PRESSED, component)),        // 6
                MethodExecutionTestCase.MethodEnvironment.get(keyTyped, buildKeyEvent(inputChar, inputCode, KeyEvent.KEY_TYPED, component)),            // 7
                MethodExecutionTestCase.MethodEnvironment.get(keyReleased, buildKeyEvent(inputChar, inputCode, KeyEvent.KEY_RELEASED, component)),      // 8
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getX),       // 9
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getY),       // 10
                MethodExecutionTestCase.MethodEnvironment.get(mousePressed, buildMouseEvent(mouseX, mouseY, MouseEvent.MOUSE_PRESSED, component)),      // 11
                MethodExecutionTestCase.MethodEnvironment.get(mouseReleased, buildMouseEvent(mouseX, mouseY, MouseEvent.MOUSE_RELEASED, component)),    // 12
                MethodExecutionTestCase.MethodEnvironment.get(mouseClicked, buildMouseEvent(mouseX, mouseY, MouseEvent.MOUSE_CLICKED, component)),      // 13
                MethodExecutionTestCase.MethodEnvironment.get(keyPressed, buildKeyEvent(inputChar, inputCode, KeyEvent.KEY_PRESSED, component)),        // 14
                MethodExecutionTestCase.MethodEnvironment.get(keyTyped, buildKeyEvent(inputChar, inputCode, KeyEvent.KEY_TYPED, component)),            // 15
                MethodExecutionTestCase.MethodEnvironment.get(keyReleased, buildKeyEvent(inputChar, inputCode, KeyEvent.KEY_RELEASED, component)),      // 16
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getX),       // 17
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getY)        // 18
            };
        } else if (component != null) {
            System.out.println("Sending events to " + component);
            methods = new MethodExecutionTestCase.MethodEnvironment[]{
                MethodExecutionTestCase.MethodEnvironment.get(bridgeSceneInstance, getAvatar),                                                                  // 0
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getX),               // 1
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getY),               // 2
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildMouseEvent(0, 0, MouseEvent.MOUSE_PRESSED, component)),            // 3
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildMouseEvent(0, 0, MouseEvent.MOUSE_RELEASED, component)),           // 4
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildMouseEvent(0, 0, MouseEvent.MOUSE_CLICKED, component)),            // 5
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent(inputChar, inputCode, KeyEvent.KEY_PRESSED, component)),  // 6
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent(inputChar, inputCode, KeyEvent.KEY_TYPED, component)),    // 7
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent(inputChar, inputCode, KeyEvent.KEY_RELEASED, component)), // 8
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getX),               // 9
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getY),               // 10
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildMouseEvent(mouseX, mouseY, MouseEvent.MOUSE_PRESSED, component)),  // 11
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildMouseEvent(mouseX, mouseY, MouseEvent.MOUSE_RELEASED, component)), // 12
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildMouseEvent(mouseX, mouseY, MouseEvent.MOUSE_CLICKED, component)),  // 13
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent(inputChar, inputCode, KeyEvent.KEY_PRESSED, component)),  // 14
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent(inputChar, inputCode, KeyEvent.KEY_TYPED, component)),    // 15
                MethodExecutionTestCase.MethodEnvironment.get(component, dispatchEvent, buildKeyEvent(inputChar, inputCode, KeyEvent.KEY_RELEASED, component)), // 16
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getX),               // 17
                MethodExecutionTestCase.MethodEnvironment.get(MethodExecutionTestCase.CYCLIC_GET_PROPERTY, MethodExecutionTestCase.M0_RET, getY)                // 18
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
        
        retVal[0] = ExecutionResultTools.checkNotException(exData, 3)
                && ExecutionResultTools.checkNotException(exData, 4)
                && ExecutionResultTools.checkNotException(exData, 5);
        
        retVal[1] = ExecutionResultTools.checkNotException(exData, 6)
                && ExecutionResultTools.checkNotException(exData, 7)
                && ExecutionResultTools.checkNotException(exData, 8);
        
        retVal[2] = ExecutionResultTools.checkNEqual(exData, 9, 17)
                || ExecutionResultTools.checkNEqual(exData, 10, 18);
        
        retVal[3] = false;
        int startX, startY, endX, endY;
        Optional<Integer> value = ExecutionResultTools.getIntValue(exData, 9);
        if (value.isPresent()) {
            startX = value.get();
            value = ExecutionResultTools.getIntValue(exData, 10);
            if (value.isPresent()) {
                startY = value.get();
                value = ExecutionResultTools.getIntValue(exData, 17);
                if (value.isPresent()) {
                    endX = value.get();
                    value = ExecutionResultTools.getIntValue(exData, 18);
                    if (value.isPresent()) {
                        endY = value.get();
                        if (endX - startX == mouseX && endY - startY == mouseY) {
                            retVal[3] = true;
                        }
                    }
                }
            }
        }
        
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
            ret.append("Exception handling KeyEvent\n");
        }
        if (notes[2] == false) {
            ret.append("Avatar does not move\n");
        }
        if (notes[3] == false) {
            ret.append("Avatar does not move to click\n");
        }
        return ret.toString();
    }
}
