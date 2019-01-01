package framework.execution;

import grader.basics.execution.BasicProcessRunnerFactory;
import grader.basics.execution.Runner;
import grader.basics.execution.RunnerFactory;
import grader.basics.project.Project;

public class ProcessRunnerFactory extends BasicProcessRunnerFactory implements RunnerFactory{
	
//	Runner processRunner;
//	
//	public void setProcessRunner(Runner newVal) {
//		processRunner = newVal;
//	}

	@Override
	public Runner getOrCreateProcessRunner(Project aProject,
			String aSpecifiedProxyMainClass) {
		if (processRunner == null)
			processRunner = new ProcessRunner(aProject, aSpecifiedProxyMainClass);
		return processRunner;
	}

}
