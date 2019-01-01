package demoAndTest.colabIncrementalInput;

import grader.basics.util.DirectoryUtils;
import util.trace.Tracer;
import demoAndTest.GraderDemoerAndTester;
/*
 * This is a test not of the student programs but of the grader on Java non distributed programs
 */
public class ACollaborativeMixedArithmeticIncrementalInputGraderTester {
	public static void main (String[] anArgs) {
		GraderDemoerAndTester demoerAndTester = new ACollaborativeMixedArithmeticIncrementalInputGraderDemoerAndTester(anArgs);

		demoerAndTester.setAutoProceed(true);
		demoerAndTester.setGeneratingCorrectDir(false);
		Tracer.showInfo(true);
		Tracer.setKeywordPrintStatus(DirectoryUtils.class, true);
		demoerAndTester.demoAndTest();

		
	}

}
