package framework.execution;

import grader.basics.execution.NotRunnableException;
import grader.basics.execution.Runner;
import grader.basics.execution.RunningProject;
import grader.basics.project.ClassDescription;
import grader.basics.project.ClassesManager;
import grader.basics.project.Project;
import grader.basics.util.TimedProcess;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import util.misc.TeePrintStream;
import util.pipe.InputGenerator;

/**
 * Runs a project in the same JVM using reflection to invoke the main method.
 * @see {@link grader.execution.AReflectionBasedProjectRunner}
 */
public class ReflectionRunner implements Runner {

    private Project project;

    public ReflectionRunner(Project project) {
        this.project = project;
    }

    /**
     * Runs the project with no arguments
     * @param input The input to provide
     * @return A RunningProject object which you can use for synchronization and acquiring output
     * @throws NotRunnableException
     */
    @Override
    public ARunningProject run(String input) throws NotRunnableException {
        return run(input, new String[]{}, -1);
    }

    @Override
    public ARunningProject run(String input, int timeout) throws NotRunnableException {
        // TODO: Add timeout
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * This runs the project with no arguments
     * @param input Input to use as System.in
     * @param args Arguments for the program
     * @return A RunningProject object which you can use for synchronization and acquiring output
     * @throws NotRunnableException
     */
    @Override
    public ARunningProject run(final String input, final String[] args, int timeout) throws NotRunnableException {
        final ARunningProject runner = new ARunningProject(project, null, input);
        try {
            runner.start();
        } catch (InterruptedException e) {
            throw new NotRunnableException();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream systemIn = System.in;
                PrintStream systemOut = new PrintStream(System.out);

                try {

                    // Create the input/output streams
                    InputStream stdin = new ByteArrayInputStream(input.getBytes());
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    PrintStream stdout = new TeePrintStream(outputStream, System.out);
                    System.setIn(stdin);
                    System.setOut(stdout);

                    // Get the main method and invoke it
                    Class mainClass = getMainClass();
                    Object[] mainArgs = new Object[]{args};
                    Method mainMethod = getMainMethod(mainClass);
                    mainMethod.invoke(mainClass, mainArgs);

                    // Close the Tee stream
                    stdin.close();
                    stdout.close();

                    // Do something with the output
                    runner.setOutput(outputStream.toString());
                    runner.end();
                } catch (Exception e) {
                    runner.error();
                    runner.end();
                } finally {
                    System.setIn(systemIn);
                    System.setOut(systemOut);
                }
            }
        }).start();
        return runner;
    }

    /**
     * Finds which class has the main method. This finds the first class with main
     * @return The Class containing main
     * @throws NotRunnableException
     */
    private Class<?> getMainClass() throws NotRunnableException {
        if (project.getClassesManager().isDefined()) {
            ClassesManager manager = (ClassesManager) project.getClassesManager().get();
            for (ClassDescription classDescription : manager.getClassDescriptions()) {
                Class<?> _class = classDescription.getJavaClass();
                try {
                    Method method = _class.getMethod("main", String[].class);
                    if (Modifier.isStatic(method.getModifiers()))
                        return _class;
                } catch (NoSuchMethodException e) {
                }
            }
            throw new NotRunnableException();
        } else
            throw new NotRunnableException();
    }

    /**
     * This gets the main method giving a class. This also transforms the exception to a NotRunnableException
     * @param mainClass The class containing main
     * @return The main {@link Method}
     * @throws NotRunnableException
     */
    private Method getMainMethod(Class<?> mainClass) throws NotRunnableException {
        try {
            return mainClass.getMethod("main", String[].class);
        } catch (NoSuchMethodException e) {
            throw new NotRunnableException();
        }
    }

	@Override
	public ARunningProject run(InputGenerator anOutputBasedInputGenerator, String[] command, String input,
			String[] args, int timeout) throws NotRunnableException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ARunningProject run(InputGenerator aDynamicInputProvider, String anEntryPoint, String input,
			String[] args, int timeout) throws NotRunnableException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TimedProcess run(RunningProject aRunner, InputGenerator anOutputBasedInputGenerator,
			String[] command, String input, String[] args, int timeout, String aProcess, boolean wait)
			throws NotRunnableException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ARunningProject run(InputGenerator aDynamicInputProvider, String input, String[] args,
			int timeout)
			throws NotRunnableException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ARunningProject run(
			InputGenerator anOutputBasedInputGenerator,
			String input, int timeout) throws NotRunnableException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void terminateProcess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSpecifiedMainClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSpecifiedMainClass(String specifiedMainClass) {
		// TODO Auto-generated method stub
		
	}

}
