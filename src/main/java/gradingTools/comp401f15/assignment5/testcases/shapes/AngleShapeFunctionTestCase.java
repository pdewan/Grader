package gradingTools.comp401f15.assignment5.testcases.shapes;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import framework.grading.testing.BasicTestCase;
import grader.basics.config.BasicProjectExecution;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.BasicProjectIntrospection;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;

/**
 *
 * @author Andrew Vitkus
 */
public class AngleShapeFunctionTestCase extends BasicTestCase {
    private static final String TAG = "Angle";
    private static final String LINE_TAG = "RotatingLine";
    
    private static final String[] classDescriptions = new String[]{null, TAG, ".*"+TAG+".*", ".*"+TAG+".*"};
    private static final String[] lineDescriptions = new String[]{null, LINE_TAG, ".*"+LINE_TAG+".*", ".*"+LINE_TAG+".*"};
    
    public static final int CONSTRUCTOR_TIMEOUT = 10;
    public static final int EXECUTION_TIMEOUT = 100;

    @Override
    public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
        Class angleShapeClass = BasicProjectIntrospection.findClass(project, 
                                                            classDescriptions[0],
                                                            classDescriptions[1],
                                                            classDescriptions[2],
                                                            classDescriptions[3]);
        Class lineClass = BasicProjectIntrospection.findClass(project, 
                                                            lineDescriptions[0],
                                                            lineDescriptions[1],
                                                            lineDescriptions[2],
                                                            lineDescriptions[3]);
        
        if (angleShapeClass == null) {
            return fail("Cannot find angle shape class");
        } else {
            Constructor emptyConstructor = null;
            Constructor lineParamConstructor = null;
            Constructor[] constructors = angleShapeClass.getConstructors();
            for(Constructor c : constructors) {
                if (c.getParameterCount() == 0) { // empty constructor
                    emptyConstructor = c;
                    break;
                } else if (c.getParameterCount() == 2) { // line param constructor?
                    if (lineClass != null) {
                        boolean correctConstructorParams = true;
                        for(Class param : c.getParameterTypes()) { // check the constructors params
                            if (!param.isAssignableFrom(lineClass)) { // if the line class can't be cast them, I don't want it
                                correctConstructorParams = false;
                                break;
                            }
                        }
                        if (correctConstructorParams) {
                            lineParamConstructor = c;
                        }
                    }
                }
            }
            if (emptyConstructor != null) {
                return testEmptyConstructor(emptyConstructor, angleShapeClass);
            } else if (lineParamConstructor != null) {
                return testLineConstructor(lineParamConstructor, angleShapeClass, lineClass);
            }
            return fail("Can't find expected an constructor for Angle Shape class");
        }
    }
    
    private TestCaseResult testEmptyConstructor(Constructor emptyConstructor, Class angleShapeClass) {
        BasicProjectExecution.timedInvoke(emptyConstructor, null, CONSTRUCTOR_TIMEOUT);
        return pass();
    }
    
    private TestCaseResult testLineConstructor(Constructor lineParamConstructor, Class angleShapeClass, Class lineClass) {
        if (lineClass == null) {
            return fail("Can't find empty constructor\nCan't find line class to test fallback line parameter constructor");
        }
        Object line1 = BasicProjectExecution.timedInvoke(lineParamConstructor, null, CONSTRUCTOR_TIMEOUT);
        Object line2 = BasicProjectExecution.timedInvoke(lineParamConstructor, null, CONSTRUCTOR_TIMEOUT);
        if (line1 == null || line2 == null) {
            return fail("Can't find empty constructor\nFailed to instantiate lines to test fallback line parameter constructor");
        }
        Object angleShape = BasicProjectExecution.timedInvoke(lineParamConstructor, new Object[]{line1, line2}, CONSTRUCTOR_TIMEOUT);
        if (angleShape == null) {
            return fail("Can't find empty constructor\nFailed to instantiate line parameter constructor");
        }
        return partialPass(0.5, "Can't find empty constructor");
    }
    
    private Method[] getAngleShapeMethods(Class angleShapeClass) {
        Method[] methods = new Method[3];
        if (angleShapeClass != null) {
            for(Method m : angleShapeClass.getMethods()) {
                
            }
        }
        return methods;
    }
    
    private Method[] getLineMethods(Class lineClass) {
        Method[] methods = new Method[2];
        if (lineClass != null) {
            for(Method m : lineClass.getMethods()) {
                String methodName = m.getName();
                switch(methodName) {
                    case "getX":
                        methods[0] = m;
                        break;
                    case "getY":
                        methods[1] = m;
                        break;
                    default:
                        if (methodName.matches("^((?![sS]et).)*([gG]et)+[xX].*")) {
                            methods[0] = m;
                        } else if (methodName.matches("^((?![sS]et).)*([gG]et)+[yY].*")) {
                            methods[1] = m;
                        }
                }
            }
        }
        return methods;
    }
    
    public AngleShapeFunctionTestCase() {
        super("Angle Shape Class Functionality Test Case");
    }
}
