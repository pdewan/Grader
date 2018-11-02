package grader.trace;

import framework.execution.ProcessRunner;
import grader.execution.AnExecutionSpecification;
import trace.grader.basics.GraderBasicsTraceUtility;
import util.trace.ImplicitKeywordKind;
import util.trace.Tracer;

public class GraderTraceUtility extends GraderBasicsTraceUtility {
	static boolean turnOn = true;
	@Deprecated
	public static boolean isTurnOn() {
		return turnOn;
	}
	
	@Deprecated
	public static void setTurnOn(boolean turnOn) {
		GraderTraceUtility.turnOn = turnOn;
	}
	public static void setTracing() {
		GraderBasicsTraceUtility.setTracing();
		Tracer.setImplicitPrintKeywordKind(ImplicitKeywordKind.OBJECT_PACKAGE_NAME);	
//		if (isTurnOn()) {
			Tracer.setKeywordPrintStatus(ProcessRunner.class, true);
			Tracer.setKeywordPrintStatus(AnExecutionSpecification.class, true);
//		}

	}
}
