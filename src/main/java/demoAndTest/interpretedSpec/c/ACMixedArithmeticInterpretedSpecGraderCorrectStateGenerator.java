package demoAndTest.interpretedSpec.c;

import demoAndTest.GraderDemoerAndTester;
import tools.DirectoryUtils;
import util.trace.Tracer;
/*
 * This is a test not of the student programs but of the grader on Java non distributed programs
 */
public class ACMixedArithmeticInterpretedSpecGraderCorrectStateGenerator {
	public static void main (String[] anArgs) {
		GraderDemoerAndTester demoerAndTester = new ACMixedArithmeticInterpretedSpecGraderDemoerAndTester(anArgs);

		demoerAndTester.setAutoProceed(true);
		demoerAndTester.setGeneratingCorrectDir(true);
//		Tracer.showInfo(true);
//		Tracer.setKeywordPrintStatus(DirectoryUtils.class, true);
		demoerAndTester.demoAndTest();

		
	}

}
