package demoAndTest.colabInput;

import demoAndTest.GraderDemoerAndTester;
/*
 * This is a test not of the student programs but of the grader on Java non distributed programs
 */
public class ACollaborativeMixedArithmeticGraderCorrectStateGenerator {
	public static void main (String[] anArgs) {
		GraderDemoerAndTester demoerAndTester = new ACollaborativeMixedArithmeticGraderDemoerAndTester(anArgs);

		demoerAndTester.setAutoProceed(true);
		demoerAndTester.setGeneratingCorrectDir(true);
//		Tracer.showInfo(true);
//		Tracer.setKeywordPrintStatus(DirectoryUtils.class, true);
		demoerAndTester.demoAndTest();

		
	}

}
