package grader.execution;

import framework.execution.ProcessRunnerFactory;
import grader.basics.config.BasicStaticConfigurationUtils;
import grader.basics.execution.BasicExecutionSpecificationSelector;
import grader.basics.execution.BasicProjectExecution;
import grader.basics.execution.GradingMode;
import grader.basics.execution.JavaMainClassFinderSelector;
import grader.basics.execution.RunnerSelector;

public class GradingModeConfigurer {
	public static void configureGradingMode() {
		GradingMode.setGraderRun(true);
		BasicProjectExecution.setReRunInfiniteProcesses(false);
		BasicExecutionSpecificationSelector.setBasicExecutionSpecification(
				ExecutionSpecificationSelector.getExecutionSpecification());
		RunnerSelector.setFactory(new ProcessRunnerFactory());
		JavaMainClassFinderSelector
				.setMainClassFinder(new AFlexibleMainClassFinder());
	}

}
