package gradingTools.sharedTestCase;

import static grader.basics.config.BasicProjectExecution.restoreOutputAndGetRedirectedOutput;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import framework.execution.ARunningProject;
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
public class LoggingMethodExecutionTestCase extends BasicTestCase {
    
    public static final Object DNC = new Object();
    
    public static final MethodReturnReference M0_RET = new MethodReturnReference(0);
    public static final MethodReturnReference M1_RET = new MethodReturnReference(1);
    public static final MethodReturnReference M2_RET = new MethodReturnReference(2);
    public static final MethodReturnReference M3_RET = new MethodReturnReference(3);
    public static final PastObjectReference EX_TARGET = PastObjectReference.of(PastObjectReference.SourceType.TARGET_OBJECT);
    
    private final Constructor<?> constructor;
    private final Object[] constructorArgs;
    private final Object executionTarget;
    private final Method[] methods;
    private final Object[][] arguments;
    private final MethodEnvironment[] methodEnvirons;
    private final Object[] expectedReturnValues;
    
    
    public static Method CYCLIC_GET_PROPERTY;
    
    static {
        try {
            CYCLIC_GET_PROPERTY = LoggingMethodExecutionTestCase.class.getMethod("cyclicGetProperty", Object.class, Method[].class);
        } catch (NoSuchMethodException | SecurityException ex) {
            CYCLIC_GET_PROPERTY = null;
        }
    }
    
    public LoggingMethodExecutionTestCase(String name, Object o, MethodEnvironment me, Object retVals) {
        this(name, null, null, o, new Method[]{me.getMethod()}, new Object[][]{me.getArguments()}, new MethodEnvironment[]{me}, new Object[]{retVals});
    }
    
    public LoggingMethodExecutionTestCase(String name, Object o, Method m, Object[] args, Object retVals) {
        this(name, null, null, o, new Method[]{m}, new Object[][]{args}, null, new Object[]{retVals});
    }
    
    public LoggingMethodExecutionTestCase(String name, Object o, MethodEnvironment[] meArr, Object[] retVals) {
        this(name, null, null, o,
                Arrays.stream(meArr).map((me)->me.getMethod()).toArray(Method[]::new),
                Arrays.stream(meArr).map((me)->me.getArguments()).toArray(Object[][]::new),
                meArr,
                retVals);
    }
    
    public LoggingMethodExecutionTestCase(String name, Object o, Method[] m, Object[][] args, Object[] retVals) {
        this(name, null, null, o, m, args, null, retVals);
    }
    
    public LoggingMethodExecutionTestCase(String name, Constructor<?> c, Object[] cArgs, MethodEnvironment me, Object retVals) {
        this(name, c, cArgs, null, new Method[]{me.getMethod()}, new Object[][]{me.getArguments()}, new MethodEnvironment[]{me}, new Object[]{retVals});
    }
    
    public LoggingMethodExecutionTestCase(String name, Constructor<?> c, Object[] cArgs, Method m, Object[] args, Object retVals) {
        this(name, c, cArgs, null, new Method[]{m}, new Object[][]{args},null, new Object[]{retVals});
    }
    
    public LoggingMethodExecutionTestCase(String name, Constructor<?> c, Object[] cArgs, Method[] m, Object[][] args, Object[] retVals) {
        this(name, c, cArgs, null, m, args, null, retVals);
    }
    
    public LoggingMethodExecutionTestCase(String name, Constructor<?> c, Object[] cArgs, MethodEnvironment[] meArr, Object[] retVals) {
        this(name, c, cArgs, null,
                Arrays.stream(meArr).map((me)->me.getMethod()).toArray(Method[]::new),
                Arrays.stream(meArr).map((me)->me.getArguments()).toArray(Object[][]::new),
                meArr,
                retVals);
    }
    
    private LoggingMethodExecutionTestCase(String name, Constructor<?> c, Object[] cArgs, Object o, Method[] m, Object[][] args, MethodEnvironment[] meArr, Object[] retVals) {
        super(name);
        constructor = c;
        constructorArgs = cArgs;
        executionTarget = o;
        methods = m;
        arguments = args;
        expectedReturnValues = retVals;
        methodEnvirons = meArr;
    }

    @Override
    public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
        BasicProjectExecution.redirectOutput();
        String anOutput = "";
        System.out.println("Testcase: " + name);
        Object[] details;
        if (constructor == null) {
            System.out.println("Using provided object (" + executionTarget.getClass().getTypeName() + "): " + executionTarget);
            anOutput += restoreOutputAndGetRedirectedOutput();
            if (methodEnvirons != null) {
                details = invoke(executionTarget, methodEnvirons);
            } else {
                details = invoke(executionTarget, methods, arguments);
            }
        } else {
            System.out.print("Constructing object using constructor " + constructor.toGenericString() + " and arguments (");
            for(int i = 0; i < constructorArgs.length; i ++) {
                if (i > 0) {
                    System.out.print(", ");
                }
                System.out.print(constructorArgs[i]);
            }
            System.out.println(")");
            anOutput += restoreOutputAndGetRedirectedOutput();
            if (methodEnvirons != null) {
                details = invoke(constructor, constructorArgs, methodEnvirons);
            } else {
                details = invoke(constructor, constructorArgs, methods, arguments);
               
            }
        }
        if (details == null || details.length == 0) {
            return fail("Couldn't grade or find problems, something is very wrong"); 
        }
        BasicProjectExecution.redirectOutput();
        int passed = 0;
        int dnc = 0;
        String errors = "";
        for(int i = 0; i < details.length; i ++) {
            System.out.print("Invoked " + methods[i].toGenericString());
            if (arguments[i].length == 0) {
                System.out.println(" with no arguments");
            } else {
                System.out.print(" with arguments (");
                for(int j = 0; j < arguments[i].length; j ++) {
                    if (j > 0) {
                        System.out.print(", ");
                    }
                    System.out.print(arguments[i][j]);
                }
                System.out.print(")\n");
            }
            System.out.println("Result (" + details[i].getClass().getTypeName() + "): " + details[i]);
            if (DNC.equals(expectedReturnValues[i])) {
                System.out.println("Expected: DO NOT CARE");
            } else {
                System.out.println("Expected (" + expectedReturnValues[i].getClass().getTypeName() + "): " + expectedReturnValues[i]);
            }
            if (details[i] instanceof Exception) {
                System.out.println("Error");
                errors += errorToString(constructor, methods[i], (Exception)details[i]) + "\n";
            } else {
                if (expectedReturnValues[i] == null) {
                    if (details[i] == null) {
                        passed ++;
                        System.out.println("Match!");
                    } else {
                        System.out.println("Wrong");
                        errors += errorToString(constructor, methods[i], null) + "\n";
                    }
                } else if (DNC.equals(expectedReturnValues[i])) {
                    dnc ++;
                    System.out.println("Ignored");
                }else if (expectedReturnValues[i].equals(details[i])) {
                    passed ++;
                    System.out.println("Match!");
                } else {
                    System.out.println("Wrong");
                    errors += errorToString(constructor, methods[i], null) + "\n";
                }
            }
        }
        anOutput += restoreOutputAndGetRedirectedOutput();
        if (anOutput != null && !anOutput.isEmpty()) {
            ARunningProject.appendToTranscriptFile(project, getCheckable().getName(), anOutput);
        }
        int total = methods.length - dnc;
        if (passed == total) {
            return pass();
        } else if (passed == 0) {
            return fail(errors);
        } else {
            return partialPass(((double)passed) / total, errors);
        }
    }
    
    public static Object[] invoke(Constructor<?> c, Object[] cArgs, MethodEnvironment[] meArr) {
        Object o;
        try {
            o = BasicProjectExecution.timedInvokeWithExceptions(c, cArgs);
            return invoke(o, meArr);
        } catch (InstantiationException ex) {
            return new Object[]{ex};
        } catch (Exception e) {
            return new Object[]{new ExecutionFailureException(e)};
        }
    }
    
    public static Object[] invoke(Object o, MethodEnvironment[] meArr) {
        //Method[] mArr = Arrays.stream(meArr).map((me)->me.getMethod()).toArray(Method[]::new);
        //Object[][] pArr = Arrays.stream(meArr).map((me)->me.getArguments()).toArray(Object[][]::new);
        //return invoke(o, mArr, pArr);
        Object[] ret;
        ret = new Object[meArr.length];
        for(int i = 0; i < meArr.length; i ++) {
            MethodEnvironment me = meArr[i];
            boolean error = false;
            Object[] pArr = me.getArguments();
            for(int j = 0; j < pArr.length; j ++) {
                if (pArr[j] instanceof PastObjectReference) {
                    PastObjectReference ref = (PastObjectReference)pArr[j];
                    switch(ref.type) {
                        case CONSTRUCTOR_ARGUMENT:
                            ret[i] = new IllegalArgumentException("Constructor argument refrenced when unused");
                            error = true;
                            break;
                        case TARGET_OBJECT:
                            if (o != null) {
                                pArr[j] = o;
                            } else {
                                ret[i] = new IllegalArgumentException("Target object refrenced when null");
                                error = true;
                            }
                            break;
                        case METHOD_PARAMETER:
                            if (ref.method > i || ref.method < 0) {
                                ret[i] = new IllegalArgumentException("Requested method out of range");
                                error = true;
                            } else {
                                Object[] args = meArr[ref.method].getArguments();
                                if (ref.paramNumber >= args.length || ref.paramNumber < 0) {
                                    ret[i] = new IllegalArgumentException("Requested parameter out of range");
                                    error = true;
                                } else {
                                    pArr[j] = args[ref.paramNumber];
                                }
                            }
                            break;
                        case METHOD_RETURN:
                            if (ref.method >= i || ref.method < 0) {
                                ret[i] = new IllegalArgumentException("Requested method out of range");
                                error = true;
                            } else {
                                if (ret[ref.method] instanceof Exception) {
                                    ret[i] = new IllegalArgumentException("Requested return was exception, can't evaluate");
                                    error = true;
                                } else {
                                    pArr[j] = ret[ref.method];
                                }
                            }
                            break;
                    }
                }
            }
            Object target = me.getTarget() == null ? o : me.getTarget();
            if (target instanceof PastObjectReference) {
                PastObjectReference ref = (PastObjectReference)target;
                switch(ref.type) {
                    case TARGET_OBJECT:
                        target = o;
                        break;
                    case METHOD_RETURN:
                        if (ref.method >= i || ref.method < 0) {
                            ret[i] = new IllegalArgumentException("Requested method out of range");
                            error = true;
                        } else {
                            if (ret[ref.method] instanceof Exception) {
                                ret[i] = new IllegalArgumentException("Requested return was exception, can't evaluate");
                                error = true;
                            } else {
                                target = ret[ref.method];
                            }
                        }
                }
            }
            
            if (!error) {
                ret[i] = invoke(target, me.getMethod(), pArr);
            }
        }
        return ret;
    }
    
    public static Object[] invoke(Constructor<?> c, Object[] cArgs, Method m[], Object[]... arguments) {
        Object o;
        try {
            o = BasicProjectExecution.timedInvokeWithExceptions(c, cArgs);
            return invoke(o, m, arguments);
        } catch (InstantiationException ex) {
            return new Object[]{ex};
        } catch (Exception e) {
            return new Object[]{new ExecutionFailureException(e)};
        }
    }
    
    public static Object[] invoke(Object o, Method m[], Object[]... arguments) {
        Object[] ret;
        if (m.length == arguments.length) {
            ret = new Object[m.length];
            for(int i = 0; i < m.length; i ++) {
                ret[i] = invoke(o, m[i], arguments[i]);
            }
        } else {
            ret = new Object[0];
        }
        return ret;
    }
    
    public static Object invoke(Constructor<?> c, Object[] cArgs, MethodEnvironment me) {
        return invoke(c, cArgs, me.getMethod(), me.getArguments());
    }
    
    public static Object invoke(Object o, MethodEnvironment me) {
        return invoke(o, me.getMethod(), me.getArguments());
    }
    
    public static Object invoke(Constructor<?> c, Object[] cArgs, Method m, Object... arguments) {
        Object o;
        try {
            o = BasicProjectExecution.timedInvokeWithExceptions(c, cArgs);
            return invoke(o, m, arguments);
        } catch (InstantiationException ex) {
            return ex;
        } catch (Exception e) {
            return new ExecutionFailureException(e);
        }
    }
    
    public static Object invoke(Object o, Method m, Object... arguments) {
        try {
            return BasicProjectExecution.timedInvokeWithExceptions(o, m, arguments);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            return ex;
        } catch (Throwable e) {
            return new ExecutionFailureException(e);
        }
    }
    
    private static String errorToString(Constructor<?> c, Method m, Exception e) {
        String message = "Error running method '" + m.toGenericString()
                + "' from class '" + m.getDeclaringClass().getTypeName() + "': ";
        if (e == null) {
            message += "Wrong output";
        } else {
            if (e instanceof ClassNotFoundException) {
                message += "Can't find class";
            } else if (e instanceof NoSuchMethodException) {
                message += "Can't find method";
            } else if (e instanceof IllegalAccessException) {
                message += "Can't access method";
            } else if (e instanceof IllegalArgumentException) {
                message += "Argument type missmatch";
            } else if (e instanceof InvocationTargetException) {
                message += "Can't invoke method";
            } else if (e instanceof InstantiationException) {
                message += "Can't execute constructor '" + c.toGenericString() + "'";
            } else if (e instanceof ExecutionFailureException) {
                message += "Method threw exception - " + e.getCause().getLocalizedMessage();
            } else {
                message += "Unexpected exception - " + e.getLocalizedMessage();
            }
        }
        return message;
    }
    
    public static Object cyclicGetProperty(Object o, Method... methods) throws Exception {
        for (Method method : methods) {
            o = method.invoke(o);
        }
        return o;
    }
    
    public static Method[] recursiveFindMethod(Class<?> root, String name, String tag) {
        return recursiveFindMethod(root, name, tag, ".*" + name + ".*", ".*" + tag + ".*");
    }
    public static Method[] recursiveFindMethod(Class<?> root, String name, String tag, String nameRegex, String tagRegex) {
        return recursiveFindMethod(root, name, tag, nameRegex, tagRegex, 3);
    }
    
    public static Method[] recursiveFindMethod(Class<?> root, String name, String tag, String nameRegex, String tagRegex, int maxDepth) {
        List<Method> methodList = BasicProjectIntrospection.findMethod(root, name, tag, nameRegex, tagRegex);
        if (methodList.isEmpty()) {
            if (maxDepth == 0) {
                return null;
            } else {
                for (Method m : root.getMethods()) {
                    Method[] methods = recursiveFindMethod(root, name, tag, nameRegex, tagRegex, maxDepth - 1);
                    if (methods != null) {
                        List<Method> ret = new ArrayList<>();
                        ret.add(m);
                        ret.addAll(Arrays.asList(methods));
                        return ret.toArray(new Method[ret.size()]);
                    }
                }
                return null;
            }
        } else {
            return new Method[]{methodList.get(0)};
        }
    }
    
    public static class MethodEnvironment {
        private final Method m;
        private final Object[] arguments;
        private final Object target;

        private MethodEnvironment(Object target, Method m, Object[] arguments) {
            this.m = m;
            this.arguments = arguments;
            this.target = target;
        }
        
        public static MethodEnvironment get(Object target, Method m, Object... arguments) {
            return new MethodEnvironment(target, m, arguments);
        }
        
        public static MethodEnvironment get(Method m, Object... arguments) {
            return new MethodEnvironment(null, m, arguments);
        }

        public Method getMethod() {
            return m;
        }

        public Object[] getArguments() {
            return arguments;
        }
        
        public Object getTarget() {
            return target;
        }
    }
    
    public static class ExecutionFailureException extends Exception {

        private ExecutionFailureException(Throwable e) {
            super(e);
        }
        
    }
    
    public static class PastObjectReference {
        private final SourceType type;
        public int method, paramNumber;
        
        private PastObjectReference(SourceType type) {
            this.type = type;
            method = -1;
            paramNumber = -1;
        }
        
        @Override
        public String toString() {
            return "Data from other Object, target is " + type;
        }
        
        public static PastObjectReference of(SourceType type) {
            return new PastObjectReference(type);
        }
        
        public static enum SourceType {
            CONSTRUCTOR_ARGUMENT, TARGET_OBJECT, METHOD_PARAMETER, METHOD_RETURN
        }
    }
    
    public static class MethodParameterReference extends PastObjectReference {
        public MethodParameterReference(int method, int paramNumber) {
            super(SourceType.METHOD_PARAMETER);
            this.method = method;
            this.paramNumber = paramNumber;
        }
        
        @Override
        public String toString() {
            return "Method " + method + " parameter " + paramNumber;
        }
    }
    
    public static class MethodReturnReference extends PastObjectReference {
        public MethodReturnReference(int method) {
            super(SourceType.METHOD_RETURN);
            this.method = method;
        }
        
        @Override
        public String toString() {
            return "Method " + method + " result";
        }
    }
}
