package grader.executor;

import grader.basics.project.Project;

public class ExecutorSelector {
	static Executor singleton;
	public static Executor getExecutor() {
		if (singleton == null) {
			singleton = new AnExecutor();
		}
		return singleton;
	}

}
