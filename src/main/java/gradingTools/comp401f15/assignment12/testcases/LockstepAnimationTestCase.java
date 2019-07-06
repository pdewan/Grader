package gradingTools.comp401f15.assignment12.testcases;

import grader.basics.config.BasicProjectExecution;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import gradingTools.comp401f15.assignment11.testcases.CommandInterpreterAnimationTestCase;

import java.lang.reflect.Method;

import util.misc.ThreadSupport;

public class LockstepAnimationTestCase extends CommandInterpreterAnimationTestCase{
//	Method waitMethod2;
	Project project;
	String coordinatedMethod;
	public LockstepAnimationTestCase(String aProceedMethod, String aCoordinatedMethod) {
		super(aProceedMethod);
		coordinatedMethod = aCoordinatedMethod;
		
	}
	protected void waitForThreads() {
		ThreadSupport.sleep(2000);
	}
	
	public TestCaseResult test(Project aProject, boolean autoGrade) throws NotAutomatableException, NotGradableException {
		project = aProject;
		return super.test(aProject, autoGrade);
	}
	
	protected boolean didNotHaveTwoThreads = false;
//	protected Method waitMethod;
//	public static final String COORDINATED_METHOD = "lockstepArthur";
	static Object[] emptyObjects = {};

	protected void callAsynchronousMethods() throws Throwable {
		Method waitMethod = getUniqueParameterlessMethod(coordinatedMethod);
		if (waitMethod == null) {
			System.out.println("Did not find coordinated method:" + coordinatedMethod);
			return ;
		}
		
		// put the waiting method in wait state
		// start the wait method
		Object retVal = BasicProjectExecution.timedInvoke(commandInterpreter, waitMethod, emptyObjects);
		//sleep so it goes into wait
		waitForThreads();
		initThreadState();
		// now start lockstep method and let them do the lockstep
		retVal = BasicProjectExecution.timedInvoke(commandInterpreter, foundMethod, emptyObjects);
//		waitForThreads();
		

		
	}
	protected synchronized  TestCaseResult computeResult() {
		if (coordinatedMethod == null) {
			return fail ("No method:" + coordinatedMethod);
		}
		if (numChild1Events == 0 && childThread1 == null) 
			return partialPass (0.1, "controller thread did not send property event");
		if (numChild2Events == 0 && childThread2 == null) {
			System.out.println("Child thread null" + childThread2);
			return partialPass (0.2, "controlled thread did not send property event");
		}
		if (!child1AfterChild2 || !child2AfterChild1) {
			return partialPass(0.3, "notifying and coordinated threads did not interleave");
		}
//		if (numChild1Events <=  0 || numChild2Events <= 0 ) {
//			return partialPass (0.2, "only one step in lockstep animation");
//		}
		if (numChild1Sleeps <=  1 || numChild2Sleeps <= 1 ) {
			return partialPass (1.0, "only one step in lockstep animation, probably a bug");
		}
//		if (child2SleepsAfterChild1) {
//			return partialPass (0.8, "controlled thread slept: " + coordinatedMethod);
//		}
		return pass();
			
			

	}

}
