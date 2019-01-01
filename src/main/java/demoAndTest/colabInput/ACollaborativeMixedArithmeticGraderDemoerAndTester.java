package demoAndTest.colabInput;

import util.trace.Tracer;
import demoAndTest.GraderDemoerAndTester;
import demoAndTest.multiparadigm.distributed.ADistributedMixedArithmeticGraderDemoerAndTester;
//import bus.uigen.pipe.DemoerAndTester;
/*
 * 

 */
public class ACollaborativeMixedArithmeticGraderDemoerAndTester extends ADistributedMixedArithmeticGraderDemoerAndTester {
	
	//	 String[] args ;
	public final  String MULTI_TEST_DIR = "Test Data/Test 790Colab";
	public final  String MULTI_CORRECT_DIR = "Test Data/Correct 790Colab";
//	public final String COURSE_NO = "Comp411";
	public final String MULTI_COURSE_NO = "Comp790Colab";



	public ACollaborativeMixedArithmeticGraderDemoerAndTester(String[] anArgs) {
		super(anArgs);
	}
	@Override
	protected String testDir() {
		return MULTI_TEST_DIR;
	}
	@Override
	protected String correctDir() {
		return MULTI_CORRECT_DIR;
	}
	@Override
	protected String courseNo() {
		return MULTI_COURSE_NO;
	}
	
	

	public static  void main (String[] anArgs) {
//		ObjectEditor.setDefaultAttribute(AttributeNames.SHOW_SYSTEM_MENUS, false);
//		Tracer.showInfo(true);
//		Tracer.setKeywordPrintStatus(DirectoryUtils.class, true);
		GraderDemoerAndTester aDemoerAndTester = new ACollaborativeMixedArithmeticGraderDemoerAndTester(anArgs);
//		args = anArgs;
		Tracer.info(ACollaborativeMixedArithmeticGraderDemoerAndTester.class, "test");
		aDemoerAndTester.demoAndTest();
		
//		aDemoerAndTester.demoAndTest();
//
//		startFirstSession();
//
//		doSteps();

	}

	

}
