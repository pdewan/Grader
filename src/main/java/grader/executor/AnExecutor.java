package grader.executor;

import grader.basics.execution.BasicExecutionSpecification;
import grader.basics.execution.BasicExecutionSpecificationSelector;
import grader.basics.settings.BasicGradingEnvironment;
import grader.compilation.c.CFilesCompilerSelector;
import grader.config.StaticConfigurationUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnExecutor implements Executor {
	String executorDirectory;
	public static final String EXECUTOR_FILE = "executor";
	public  final List<File> sourceFiles;
	public AnExecutor() {
		sourceFiles = new ArrayList();
		sourceFiles.add(new File(EXECUTOR_FILE + ".c"));
		
	}
	
	/* (non-Javadoc)
	 * @see grader.executor.Executor#compile()
	 */
	@Override
	public void compile() {
		
		try {
			CFilesCompilerSelector.getClassFilesCompiler().compile(new File(executorDirectory + "/src") , 
					new File (executorDirectory + "/bin"), sourceFiles);
			if (BasicGradingEnvironment.get().isNotWindows()) {
				String fileNames = executorDirectory + "/bin/" + EXECUTOR_FILE + "*";
				Runtime.getRuntime().exec("setuid nobody " + fileNames);
			}
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
	/* (non-Javadoc)
	 * @see grader.executor.Executor#execute(java.lang.String[])
	 */
	@Override
	public String[] maybeToExecutorCommand(String[] aCommand) {
//		if (!StaticConfigurationUtils.getInheritedBooleanModuleProblemProperty(StaticConfigurationUtils.USE_EXECEUTOR, false))
//			return aCommand;
		if (!BasicExecutionSpecificationSelector.getBasicExecutionSpecification().isUseExecutor())
			return aCommand;
		String[] retVal = new String[aCommand.length+1];
		retVal[0] = executorDirectory + "/bin/" + EXECUTOR_FILE;
		for (int index = 0; index < aCommand.length; index++) {
			retVal[index + 1] = StaticConfigurationUtils.quotePath(aCommand[index]);
		}
		return retVal;
		
	}
	/* (non-Javadoc)
	 * @see grader.executor.Executor#setExecutorDirectory(java.lang.String)
	 */
	@Override
	public void setExecutorDirectory(String newVal) {
		executorDirectory = newVal;
//		File f = new File(newVal);
//		System.out.println ("Exists:" + f.exists());
	}

}
