package gradingTools.comp401f15.assignment12.testcases;

import grader.basics.execution.BasicProjectExecution;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import gradingTools.comp401f15.assignment11.testcases.CommandInterpreterAnimationTestCase;

import java.lang.reflect.Method;

import util.misc.ThreadSupport;

public class WaitingAnimationTestCase extends CommandInterpreterAnimationTestCase{
	Method waitMethod1;
	Method waitMethod2;
//	Project project;
	public WaitingAnimationTestCase(String aProceedMethod, String aWaitMethod1, String aWaitMethod2) {
		super(aProceedMethod);
		
	}
	
	public TestCaseResult test(Project aProject, boolean autoGrade) throws NotAutomatableException, NotGradableException {
//		project = aProject;
		return super.test(aProject, autoGrade);
	}
	
//	protected Method getUniqueParameterlessMethod (String aMethodName) {
//		List<Method> methods = IntrospectionUtil.getOrFindMethodList(project, this, commandInterpreterClass, aMethodName, aMethodName);
//		if (methods.size() < 1) {
//			System.out.println ("Could not find:" + aMethodName);
//			throw new NotGradableException();
//		}
//		if (methods.get(0).getParameterTypes().length != 0) {
//			System.out.println (aMethodName + " takes more than one parameter");
//			throw new NotGradableException();
//		}
//		return methods.get(0);
//	}
	boolean proceedAllDone;
	boolean didNotWait;
	static Object[] emptyObjects = {};

	protected void callAsynchronousMethods() throws Throwable {
		Method waitMethod1 = getUniqueParameterlessMethod("waitingArthur");
		Method waitMethod2 = getUniqueParameterlessMethod("waitingLancelot");
		Object retVal = BasicProjectExecution.timedInvoke(commandInterpreter, waitMethod1, emptyObjects);
		retVal = BasicProjectExecution.timedInvoke(commandInterpreter, waitMethod2, emptyObjects);


		proceedAllDone = false;
		didNotWait = false;
		waitForThreads();
		long currentTime = System.currentTimeMillis();
		

		
		if (childThread1 != null) {
			long aWaitTime = currentTime - lastChild1Time;
			if (aWaitTime < 2000 && numChild1Sleeps > 2) { // waiting for 1500;
//			if (aWaitTime < 2000 ) { // waiting for 1500; 

				
			System.out.println("child thread started before proceed");
			didNotWait = true;
			return;
			}
			
		}
		initThreadState(); // reset data to see if we get more events
		retVal = BasicProjectExecution.timedInvoke(commandInterpreter, foundMethod, emptyObjects);

		
	}
	protected void waitForThreads() {
		ThreadSupport.sleep(3500);
	}
	protected  TestCaseResult computeResult() {
		if (didNotWait) {
			return fail ("Did not wait for startAnimation");
		}
		if (childThread1 != null && childThread2 != null) {
			return pass();			
		}
		if (childThread1 != null) {
			return partialPass(0.5, "Only one thread proceeded");
		}
		return fail ("No proeprty notification after proceedAll");
//		if (childThread1 == parentThread) {
//		if (eventInParentThread) {
//
//			return fail ("Command not executed in separate thread");
//		}

	}

}
