package framework.execution;

import grader.basics.execution.BasicProcessRunner;
import grader.basics.execution.NotRunnableException;
import grader.basics.execution.Runner;
import grader.basics.execution.RunningProject;
import grader.basics.project.Project;
import grader.basics.settings.BasicGradingEnvironment;
import grader.basics.util.TimedProcess;
import grader.config.StaticConfigurationUtils;
import grader.language.LanguageDependencyManager;
import grader.trace.execution.MainClassFound;
import grader.trace.execution.MainClassNotFound;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Scanner;

import util.misc.Common;
import util.pipe.InputGenerator;

/**
 * This runs the program in a new process.
 */
public class InteractiveConsoleProcessRunner implements Runner {

    private Map<String, String> entryPoints;
    private File folder;
    Project project;
    Thread outputThread; // can we share this also?
    static Thread inputThread;
    static Thread errorThread;
    StringBuffer input;
    

    public InteractiveConsoleProcessRunner(Project aProject) throws NotRunnableException {
        try {
//            entryPoint = getEntryPoint(aProject);
//            entryPoint = JavaMainClassFinderSelector.getMainClassFinder().getEntryPoint(aProject);
            entryPoints = LanguageDependencyManager.getMainClassFinder().getEntryPoints(aProject, StaticConfigurationUtils.getPotentialMainEntryPointNames());

            folder = aProject.getBuildFolder(entryPoints.get(BasicProcessRunner.MAIN_ENTRY_POINT));
            project = aProject;
            MainClassFound.newCase(entryPoints.get(BasicProcessRunner.MAIN_ENTRY_POINT), project.getSourceFolder().getName(), this);
        } catch (Exception e) {
        	MainClassNotFound.newCase(entryPoints.get(BasicProcessRunner.MAIN_ENTRY_POINT), project.getSourceFolder().getName(), this);
            throw new NotRunnableException();
        }
    }

//    /**
//     * This figures out what class is the "entry point", or, what class has main(args)
//     * @param project The project to run
//     * @return The class canonical name. i.e. "foo.bar.SomeClass"
//     * @throws framework.execution.NotRunnableException
//     * @see grader.project.AMainClassFinder which repeats this code (sigh)
//     */
//    private String getEntryPoint(Project project) throws NotRunnableException {
//        if (project.getClassesManager().isEmpty())
//            throw new NotRunnableException();
//
//        ClassesManager manager = project.getClassesManager().get();
//        for (ClassDescription description : manager.getClassDescriptions()) {
//            try {
//                description.getJavaClass().getMethod("main", String[].class);
//                return description.getJavaClass().getCanonicalName();
//            } catch (NoSuchMethodException e) {
//            }
//        }
//        throw new NotRunnableException();
//    }

    /**
     * This runs the project with no arguments
     * @param input The input is ignored.
     * @return A RunningProject object which you can use for synchronization and acquiring output
     * @throws grader.basics.execution.NotRunnableException
     */
    @Override
    public ARunningProject run(String input) throws NotRunnableException {
        return run(input, -1);
    }
    public ARunningProject run(String input, String[] anArgs) throws NotRunnableException {
        return run(input, anArgs, -1);
    }

    /**
     * This runs the project with no arguments with a timeout
     * @param input The input is ignored.
     * @param timeout The timeout, in seconds. Set to -1 for no timeout
     * @return A RunningProject object which you can use for synchronization and acquiring output
     * @throws grader.basics.execution.NotRunnableException
     */
    @Override
    public ARunningProject run(String input, int timeout) throws NotRunnableException {
        return run(input, new String[]{}, timeout);
    }

    /**
     * This runs the project providing input and arguments
     * @param input The input string is ignored. Instead the console input is used.
     * @param args The arguments to pass in
     * @param timeout The timeout, in seconds. Set to -1 for no timeout
     * @return A RunningProject object which you can use for synchronization and acquiring output
     * @throws grader.basics.execution.NotRunnableException
     */
    @Override
	public ARunningProject run(InputGenerator aDynamicInputProvider, String anEntryPoint, String input,
			String[] args, int timeout) throws NotRunnableException {
    	String[] command = StaticConfigurationUtils.getExecutionCommand(project, folder, anEntryPoint, args);
    	return run(null, command, input, args, timeout);
	}
    @Override
    public ARunningProject run(String input, String[] args, int timeout) throws NotRunnableException {
    	return run (null, entryPoints.get(BasicProcessRunner.MAIN_ENTRY_POINT), input, args, timeout);
//    	String[] command = StaticConfigurationUtils.getExecutionCommand(folder, entryPoints.get(MainClassFinder.MAIN_ENTRY_POINT));
//    	return run(command, input, args, timeout);
    	

//        final RunningProject runner = new RunningProject(project);
//
//        try {
////            runner.start();
//        	
//        	String[] command = StaticConfigurationUtils.getExecutionCommand(entryPoint);
//        	ProcessBuilder builder;
//        	if (command.length == 0)
//
//            // Prepare to run the process
////            ProcessBuilder builder = new ProcessBuilder("java", "-cp", GradingEnvironment.get().getClasspath(), entryPoint);
//             builder = new ProcessBuilder("java", "-cp", GradingEnvironment.get().getClasspath(), entryPoint);
//        	else
//        		builder = new ProcessBuilder(command);
//
//        	builder.directory(folder);
//
//            // Start the process
//            final TimedProcess process = new TimedProcess(builder, timeout);
//            process.start();
//
//            // Print output to the console
//            InputStreamReader isr = new InputStreamReader(process.getInputStream());
//            final BufferedReader br = new BufferedReader(isr);
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        String line = null;
//                        while ((line = br.readLine()) != null)
//                            System.out.println(line);
//                    } catch (IOException e) {
//                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                    }
//                }
//            }).start();
//
//            // Feed console input to the process
//            OutputStreamWriter osw = new OutputStreamWriter(process.getOutputStream());
//            final BufferedWriter bw = new BufferedWriter(osw);
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    boolean loop = true;
//                    Scanner scanner = new Scanner(System.in);
//                    while (loop) {
//                        try {
//                            process.getProcess().exitValue();
//                            loop = false;
//                        } catch (IllegalThreadStateException e) {
//
//                            try {
//                                if (scanner.hasNextLine()) {
//                                    bw.write(scanner.nextLine());
//                                    bw.newLine();
//                                    bw.flush();
//                                }
//                                Thread.sleep(50);
//                            } catch (Exception e1) {
//                                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                            }
//                        }
//                    }
//                }
//            }).start();
//
//        } catch (Exception e) {
////            runner.error();
////            runner.end();
//        }
//        return runner;
    }
    static OutputStreamWriter osw;
    static BufferedWriter bw;
//    static OutputStreamWriter esw; 
//    static BufferedWriter bew;

    
    
    static Scanner scanner = new Scanner(System.in);
    static ARunningProject runner; // need the runner to change for single input thread
    // only one of these should be executing at one time as static vars are accessed
//    protected RunningProject getRunningProject() {
//    	return runner;
//    }
	@Override
	public ARunningProject run(InputGenerator anOutputBasedInputGenerator, String[] command, String input,
			String[] args, int timeout) throws NotRunnableException {
//		 final RunningProject runner = new RunningProject(project, anOutputBasedInputGenerator,  input);
		runner = new ARunningProject(project, anOutputBasedInputGenerator,  input);

	        try {
//	            runner.start();
	        	
	        	ProcessBuilder builder;
	        	if (command.length == 0)

	            // Prepare to run the process
//	            ProcessBuilder builder = new ProcessBuilder("java", "-cp", GradingEnvironment.get().getClasspath(), entryPoint);
	             builder = new ProcessBuilder("java", "-cp", BasicGradingEnvironment.get().getClassPath(), entryPoints.get(BasicProcessRunner.MAIN_ENTRY_POINT));
	        	else {
	        		builder = new ProcessBuilder(command);
	        		System.out.println("Running command:"
							+ Common.toString(command, " "));
	        	}

	        	builder.directory(folder);

	            // Start the process
	            final TimedProcess process = new TimedProcess(builder, timeout);
	            runner.setCurrentTimeProcess(process);
	            process.start();

	            // Print output to the console
	            InputStreamReader isr = new InputStreamReader(process.getInputStream());
	            final BufferedReader br = new BufferedReader(isr);
	            runner.addDependentCloseable(br);
	            // printing on System.out whatever the process is outputtng
	            outputThread = new Thread(new Runnable() {
	                @Override
	                public void run() {
	                    try {
	                        String line = null;
	                        while ((line = br.readLine()) != null && !runner.isDestroyed()) {
	                            System.out.println(line);
	                            runner.appendCumulativeOutput(line + "\n");
	                        }
	                    } catch (IOException e) {
	                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
	                    }
	                }
	            });
	            outputThread.setName ("Output Thread");
	            runner.addDependentThread(outputThread);
	            outputThread.start();
//	            new Thread(new Runnable() {
//	                @Override
//	                public void run() {
//	                    try {
//	                        String line = null;
//	                        while ((line = br.readLine()) != null)
//	                            System.out.println(line);
//	                    } catch (IOException e) {
//	                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//	                    }
//	                }
//	            }).start();

	            // Feed console input to the process
//	            OutputStreamWriter osw = new OutputStreamWriter(process.getOutputStream());
	            
	            // reset static variables to current process
	            osw = new OutputStreamWriter(process.getOutputStream());
	            bw = new BufferedWriter(osw);
//	            new Thread(new Runnable() {
//	                @Override
//	                public void run() {
//	                    boolean loop = true;
//	                    Scanner scanner = new Scanner(System.in);
//	                    while (loop) {
//	                        try {
//	                            process.getProcess().exitValue();
//	                            loop = false;
//	                        } catch (IllegalThreadStateException e) {
//
//	                            try {
//	                                if (scanner.hasNextLine()) {
//	                                    bw.write(scanner.nextLine());
//	                                    bw.newLine();
//	                                    bw.flush();
//	                                }
//	                                Thread.sleep(50);
//	                            } catch (Exception e1) {
//	                                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//	                            }
//	                        }
//	                    }
//	                }
//	            }).start();
	            
	            // start error thread
	            // Print output to the console
	            InputStreamReader esr = new InputStreamReader(process.getErrorStream());
	            final BufferedReader ebr = new BufferedReader(esr);
	            runner.addDependentCloseable(ebr);
	            errorThread = new Thread(new Runnable() {
	                @Override
	                public void run() {
	                    try {
	                        String line = null;
	                        while ((line = ebr.readLine()) != null && !runner.isDestroyed()) {
	                            System.err.println(line);
	                            runner.appendErrorOutput(line + "\n");
	                        }
	                    } catch (IOException e) {
	                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
	                    }
	                }
	            });
	            errorThread.setName ("Error Thread");
	            runner.addDependentThread(errorThread);
	            errorThread.start();
//	            new Thread(new Runnable() {
//	                @Override
//	                public void run() {
//	                    try {
//	                        String line = null;
//	                        while ((line = br.readLine()) != null)
//	                            System.out.println(line);
//	                    } catch (IOException e) {
//	                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//	                    }
//	                }
//	            }).start();

	            // Feed console input to the process
//	            OutputStreamWriter osw = new OutputStreamWriter(process.getOutputStream());
	            
	            // reset static variables to current process
//	            esw = new OutputStreamWriter(process.getOutputStream());
//	            bew = new BufferedWriter(esw);
//	            new Thread(new Runnable() {
//	                @Override
//	                public void run() {
//	                    boolean loop = true;
//	                    Scanner scanner = new Scanner(System.in);
//	                    while (loop) {
//	                        try {
//	                            process.getProcess().exitValue();
//	                            loop = false;
//	                        } catch (IllegalThreadStateException e) {
//
//	                            try {
//	                                if (scanner.hasNextLine()) {
//	                                    bw.write(scanner.nextLine());
//	                                    bw.newLine();
//	                                    bw.flush();
//	                                }
//	                                Thread.sleep(50);
//	                            } catch (Exception e1) {
//	                                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//	                            }
//	                        }
//	                    }
//	                }
//	            }).start();
	            // end error thread
	            
	            if (inputThread == null) {
	           inputThread =  new Thread(new Runnable() {
	                @Override
	                public void run() {
	                	Thread myThread = Thread.currentThread();
	                    boolean loop = true;
	                    if (scanner == null) {
	                    scanner = new Scanner(System.in);
	                    }
//	    	            runner.addDependentCloseable(scanner);

	                    while (loop) {
//	                        try {
//	                            process.getProcess().exitValue();
//	                            loop = false;
//	                        } catch (IllegalThreadStateException e) {

	                            try {
	                                if (scanner.hasNextLine()) {
	                                	String nextLine = scanner.nextLine();
	                                	runner.newInputLine(null, nextLine);
	                                    bw.write(nextLine);
//	                                    bw.write(scanner.nextLine());
	                                    bw.newLine();
	                                    bw.flush();
	                                }
//	                                Thread.sleep(50);
//	                                if (myThread.isInterrupted() || runner.isDestroyed())
//	                                	loop = false;
	                            } catch (Exception e1) {
	                            	System.out.println("Providing input to non existing process");
//	                                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
	                            }
	                        }
	                    }
//	                }
	            });
	           inputThread.setName("Input Thread");
	           inputThread.start();
	            }
//	           runner.addDependentThread(inputThread);


	        } catch (Exception e) {
//	            runner.error();
//	            runner.end();
	        }
	        return runner;
	}
	
	public ARunningProject runJosh(InputGenerator anOutputBasedInputGenerator, String[] command, String input,
			String[] args, int timeout) throws NotRunnableException {
		 final ARunningProject runner = new ARunningProject(project, anOutputBasedInputGenerator,  input);

	        try {
//	            runner.start();
	        	
	        	ProcessBuilder builder;
	        	if (command.length == 0)

	            // Prepare to run the process
//	            ProcessBuilder builder = new ProcessBuilder("java", "-cp", GradingEnvironment.get().getClasspath(), entryPoint);
	             builder = new ProcessBuilder("java", "-cp", BasicGradingEnvironment.get().getClassPath(), entryPoints.get(BasicProcessRunner.MAIN_ENTRY_POINT));
	        	else
	        		builder = new ProcessBuilder(command);

	        	builder.directory(folder);

	            // Start the process
	            final TimedProcess process = new TimedProcess(builder, timeout);
	            runner.setCurrentTimeProcess(process);
	            process.start();

	            // Print output to the console
	            InputStreamReader isr = new InputStreamReader(process.getInputStream());
	            final BufferedReader br = new BufferedReader(isr);
	            runner.addDependentCloseable(br);
	            outputThread = new Thread(new Runnable() {
	                @Override
	                public void run() {
	                    try {
	                        String line = null;
	                        while ((line = br.readLine()) != null)
	                            System.out.println(line);
	                    } catch (IOException e) {
	                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
	                    }
	                }
	            });
	            outputThread.setName ("Output Thread");
	            runner.addDependentThread(outputThread);
	            outputThread.start();
//	            new Thread(new Runnable() {
//	                @Override
//	                public void run() {
//	                    try {
//	                        String line = null;
//	                        while ((line = br.readLine()) != null)
//	                            System.out.println(line);
//	                    } catch (IOException e) {
//	                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//	                    }
//	                }
//	            }).start();

	            // Feed console input to the process
	            OutputStreamWriter osw = new OutputStreamWriter(process.getOutputStream());
	            final BufferedWriter bw = new BufferedWriter(osw);
//	            new Thread(new Runnable() {
//	                @Override
//	                public void run() {
//	                    boolean loop = true;
//	                    Scanner scanner = new Scanner(System.in);
//	                    while (loop) {
//	                        try {
//	                            process.getProcess().exitValue();
//	                            loop = false;
//	                        } catch (IllegalThreadStateException e) {
//
//	                            try {
//	                                if (scanner.hasNextLine()) {
//	                                    bw.write(scanner.nextLine());
//	                                    bw.newLine();
//	                                    bw.flush();
//	                                }
//	                                Thread.sleep(50);
//	                            } catch (Exception e1) {
//	                                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//	                            }
//	                        }
//	                    }
//	                }
//	            }).start();
	           inputThread =  new Thread(new Runnable() {
	                @Override
	                public void run() {
	                	Thread myThread = Thread.currentThread();
	                    boolean loop = true;
	                    Scanner scanner = new Scanner(System.in);
//	    	            runner.addDependentCloseable(scanner);

	                    while (loop) {
	                        try {
	                            process.getProcess().exitValue();
	                            loop = false;
	                        } catch (IllegalThreadStateException e) {

	                            try {
	                                if (scanner.hasNextLine()) {
	                                    bw.write(scanner.nextLine());
	                                    bw.newLine();
	                                    bw.flush();
	                                }
	                                Thread.sleep(50);
	                                if (myThread.isInterrupted() || runner.isDestroyed())
	                                	loop = false;
	                            } catch (Exception e1) {
	                                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
	                            }
	                        }
	                    }
	                }
	            });
	           inputThread.setName("Input Thread");
	           inputThread.start();
	           runner.addDependentThread(inputThread);


	        } catch (Exception e) {
//	            runner.error();
//	            runner.end();
	        }
	        return runner;
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
