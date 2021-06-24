package grader.execution;

import framework.execution.ProcessRunnerFactory;
import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.config.BasicStaticConfigurationUtils;
import grader.basics.execution.BasicProjectExecution;
import grader.basics.execution.GradingMode;
import grader.basics.execution.JavaMainClassFinderSelector;
import grader.basics.execution.RunnerSelector;
import grader.config.ExecutionSpecificationSelector;

public class GradingModeConfigurer {
	public static void configureGradingMode() {
		GradingMode.setGraderRun(true);
		boolean aManualGradingOnly = 
				ExecutionSpecificationSelector.getExecutionSpecification().getNavigationKind().equals(BasicStaticConfigurationUtils.MANUAL_GRADING);
		GradingMode.setManualGradingOnly(aManualGradingOnly);
		BasicProjectExecution.setReRunInfiniteProcesses(false);
		BasicExecutionSpecificationSelector.setBasicExecutionSpecification(
				ExecutionSpecificationSelector.getExecutionSpecification());
		RunnerSelector.setFactory(new ProcessRunnerFactory());
		/*
		 * Should this be set like this? Can someone override the finder later?
		 * This is being done to override the basic main class finder. I suppose
		 * after the system is up, one can change the class finder
		 */
		JavaMainClassFinderSelector
				.setMainClassFinder(new AFlexibleMainClassFinder());
	}

}
