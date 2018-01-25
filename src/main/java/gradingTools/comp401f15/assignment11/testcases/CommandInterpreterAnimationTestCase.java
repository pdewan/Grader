package gradingTools.comp401f15.assignment11.testcases;

import grader.basics.execution.BasicProjectExecution;
import grader.basics.junit.TestCaseResult;
import util.misc.ThreadSupport;
import util.trace.TraceableBus;
import util.trace.TraceableListener;
import bus.uigen.ObjectEditor;
import bus.uigen.oadapters.ObjectAdapter;
import util.trace.uigen.ObjectAdapterReceivedPropertyChangeEvent;

public  class CommandInterpreterAnimationTestCase extends CommandIntrepreterMethodCallTestCase  implements TraceableListener{
	public CommandInterpreterAnimationTestCase(String methodTag) {
		super("CommandInterpreter", methodTag, Void.TYPE);
	}


	protected String threadName1;
	protected String threadName2;

	protected Thread parentThread;
	protected int numThreadsBeforeExecution;
	protected int numThreadsAfterExecution;
	protected Thread childThread1;
	protected Thread childThread2;
	protected boolean child2AfterChild1;
	protected boolean child1AfterChild2;
	protected boolean eventInParentThread;
	protected long lastChild1Time;
	protected long lastChild2Time;
	protected boolean child1SleepsAfterChild2;
	protected boolean child2SleepsAfterChild1;
	protected int numChild1Sleeps;
	protected int numChild2Sleeps;
//	protected List<Integer> child1Sleeps = new ArrayList();
//	protected List<Integer> child2Sleeps = new ArrayList();
	protected int numChild1Events;
	protected int numChild2Events;
	protected  int minEventTime;
	protected int maxEventTime;
	protected long lastEventTime;

	public static final long MIN_SLEEP_TIME = 50;
	@Override
	public void newEvent(Exception aTraceable) {
		if (aTraceable instanceof ObjectAdapterReceivedPropertyChangeEvent) {
//		if (aTraceable instanceof PropertyChangeEventInfo) {
//			   System.out.println("property:" + aTraceable);

			processPropertyChange();
		}		
	}
	protected boolean isParentThread(Thread aThread) {
		return (aThread == parentThread) || aThread.getName().contains("ool");
	}
   protected synchronized void processPropertyChange() {
	   Thread currentThread = Thread.currentThread();
 		numThreadsAfterExecution = Math.max(Thread.activeCount(),numThreadsAfterExecution) ;

	   long currentTime =  System.currentTimeMillis();
	   int anInterval = (int) (currentTime - lastEventTime);

	   if (lastEventTime != 0) {
		   maxEventTime = Math.max(maxEventTime, anInterval);
		   minEventTime = Math.min(minEventTime,  anInterval);
	   } 
		  
	    lastEventTime = currentTime;
		 
	  
		if (isParentThread(currentThread) && !eventInParentThread) {	// do not really know the future thread			
			System.out.println("event in parent thread");
			eventInParentThread = true;
			return;

		} else if (childThread1 == null ) {
//	  		numThreadsAfterExecution = Math.max(Thread.activeCount(),numThreadsAfterExecution) ;

			childThread1 = currentThread;
			threadName1 = childThread1.getName();
			System.out.println("child 1 starts:" + threadName1);
	  		System.out.println("Num threads after execution:" + numThreadsAfterExecution );

		} else if (childThread1 != currentThread && childThread2 == null){
//	  		numThreadsAfterExecution = Math.max(Thread.activeCount(),numThreadsAfterExecution) ;
			childThread2 = currentThread;
			threadName2 = childThread2.getName();
			System.out.println("child 2 starts:" + threadName2);
	  		System.out.println("Num threads after execution:" + numThreadsAfterExecution );

		} else if (!child2AfterChild1 && childThread1 != null && childThread2 != null && childThread2 == currentThread) {
			child2AfterChild1 = true;
			System.out.println("child 2 executes after child 1");

		} else if  (!child1AfterChild2 && childThread1 != null && childThread2 != null && childThread1 == currentThread) {
			child1AfterChild2 = true;
			System.out.println("child 1 executes after child 2");
		}	
		if (currentThread == childThread1)  {
			numChild1Events++;
			if ((currentTime - lastChild1Time) > MIN_SLEEP_TIME) {
				numChild1Sleeps++;
//				System.out.println("Child 1 sleeps");

//				child1Sleeps.add(1);
			}
			lastChild1Time = currentTime;
			if ((currentTime - lastChild2Time) > MIN_SLEEP_TIME) {
				
				child1SleepsAfterChild2 = true;
			}
		}
		if (currentThread == childThread2)  {
			numChild2Events++;
			if ((currentTime - lastChild2Time) > MIN_SLEEP_TIME) {
				numChild2Sleeps++;
//				System.out.println("Child 2 sleeps");
//				child2Sleeps.add(1);
			}
			lastChild2Time = currentTime;
			if ((currentTime - lastChild1Time) > MIN_SLEEP_TIME) {
				child2SleepsAfterChild1 = true;
			}
		}
		
	}
   
	protected void waitForThreads() {
		ThreadSupport.sleep(2500);
	}
	static Object[] emptyObjects = {};
	protected void callAsynchronousMethods() throws Throwable {
//		Object retVal = BasicProjectExecution.timedInvoke(commandInterpreter, foundMethod);
		
			Object retVal = BasicProjectExecution.timedInvoke(commandInterpreter, foundMethod, emptyObjects);
		

	//		retVal = ExecutionUtil.timedInvoke(commandInterpreter, foundMethod);
		
	}
	
	protected  synchronized TestCaseResult computeResult() {
		if (childThread1 == null) {
			if (eventInParentThread) {
				return fail ("Command not executed in separate thread");
			}
			return fail ("No property notification from thread");
		}
//		if (childThread1 == parentThread) {
//		if (eventInParentThread) {
//
//			return fail ("Command not executed in separate thread");
//		}
		return pass();
	}
//	protected TestCaseResult computeResult() {
//		if (childThread1 == null) {
//			return fail ("No property notification");
//		}
//		if (childThread1 == parentThread) {
//			return fail ("Command not executed in separate thread");
//		}
//		if (child1AfterChild2) {
//			return fail ("Interleaved threads");
//		}
//		return pass();
//	}
	protected synchronized void stopThread(Thread aThread) {
		System.out.println ("Stopping thread:" + aThread);
		aThread.interrupt();
//		aThread.suspend();
//		aThread.stop();
	}
	protected synchronized void initThreadState() {
//		child1Sleeps.clear();
//		child2Sleeps.clear();
		lastEventTime = 0;
		threadName1 = null;
		threadName2 = null;
//		parentThread = null;
		numThreadsAfterExecution = 0;
		numThreadsBeforeExecution =  Thread.activeCount();
		
//		int numThreadsBeforeAnimation;
		childThread1 = null;
		childThread2 = null;
		child2AfterChild1 = false;
		child1AfterChild2 = false;
		eventInParentThread = false;
		parentThread = Thread.currentThread();
		lastChild1Time = System.currentTimeMillis();
		lastChild2Time = lastChild1Time;
		numChild1Sleeps = 0;
		numChild2Sleeps = 0;
		child1SleepsAfterChild2 = false;
		child2SleepsAfterChild1 = false;
		numChild1Events  = 0;
		numChild2Events = 0;
		  minEventTime = Integer.MAX_VALUE;
		   maxEventTime = 0;
	}
	@Override
	protected TestCaseResult callMethods() throws Throwable {
		try {
		System.out.println("calling methods");
//		parentThread = null;
//		childThread1 = null;
//		childThread2 = null;
//		child2AfterChild1 = false;
//		child1AfterChild2 = false;
//		eventInParentThread = false;
//		parentThread = Thread.currentThread();
//		lastChild1Time = System.currentTimeMillis();
//		lastChild2Time = lastChild1Time;
//		numChild1Sleeps = 0;
//		numChild2Sleeps = 0;
//		child1SleepsAfterChild2 = false;
//		child2SleepsAfterChild1 = false;
		initThreadState();
  		ObjectAdapter anAdapter = ObjectEditor.toObjectAdapter(bridgeSceneInstance);
		System.out.println ("Adding traceable listener");
  		TraceableBus.addTraceableListener(this);
  		callAsynchronousMethods();
  		System.out.println ("waiting for threads");
//		computeResult();
		waitForThreads();
  		System.out.println ("Finished waiting for threads");
//  		numThreadsAfterExecution = Math.max(Thread.activeCount(),numThreadsAfterExecution) ;
  		if (eventInParentThread || numThreadsAfterExecution <= numThreadsBeforeExecution) {
  			System.out.println ("No threads created");
  		}
//  		TestCaseResult aReturnValue = computeResult();
		if (childThread1 != null) {
			stopThread(childThread1);
		}
		if (childThread2 != null) {
			stopThread(childThread2);
//			childThread1.interrupt();
//			childThread1.suspend();
//			childThread2.stop();
		}
  		TestCaseResult aReturnValue = computeResult();

		// TODO Auto-generated method stub
//		return computeResult();

		return aReturnValue;
		} finally {
			processFinally();
		}
	}
	
	protected void processFinally() {
		System.out.println ("Removing traceable listener");
		TraceableBus.removeTraceableListener(this);

		// TODO Auto-generated method stub
		
	}
}
