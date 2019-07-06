package grader.config;

import grader.basics.execution.BasicExecutionSpecificationSelector;

public class ExecutionSpecificationSelector {
	public static ExecutionSpecification executionSpecification;

	public static ExecutionSpecification getExecutionSpecification() {
		if (executionSpecification == null) {
			executionSpecification = new AnExecutionSpecification();
			executionSpecification.loadFromConfiguration();
		}
		return executionSpecification;
	}

	public static void setExecutionSpecification(
			ExecutionSpecification executionSpecification) {
		BasicExecutionSpecificationSelector.executionSpecification = executionSpecification;
	}

	

}
