package grader.trace;

import framework.execution.ProcessRunner;
import grader.execution.AnExecutionSpecification;
import util.trace.ImplicitKeywordKind;
import util.trace.Tracer;

public class GraderTraceUtility {
	static boolean turnOn = true;
	public static boolean isTurnOn() {
		return turnOn;
	}
	public static void setTurnOn(boolean turnOn) {
		GraderTraceUtility.turnOn = turnOn;
	}
	public static void setTracing() {
		Tracer.setImplicitPrintKeywordKind(ImplicitKeywordKind.OBJECT_PACKAGE_NAME);	
		if (isTurnOn()) {
			Tracer.setKeywordPrintStatus(ProcessRunner.class, true);
			Tracer.setKeywordPrintStatus(AnExecutionSpecification.class, true);
		}

	}
}
