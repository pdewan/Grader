package gradingTools.comp401f15.assignment10.testCases;

import java.beans.PropertyChangeEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import util.misc.Common;
import util.misc.ThreadSupport;
import util.trace.TraceableBus;
import util.trace.TraceableListener;
import bus.uigen.ObjectEditor;
import bus.uigen.oadapters.ObjectAdapter;
import util.trace.uigen.ObjectAdapterReceivedPropertyChangeEvent;
import util.trace.uigen.ObjectAdapterPropertyChangeEventInfo;
import framework.execution.ARunningProject;
import framework.grading.testing.BasicTestCase;
import grader.basics.config.BasicProjectExecution;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.util.ProjectIntrospection;
import gradingTools.sharedTestCase.MethodExecutionTestCase;
import gradingTools.sharedTestCase.MethodExecutionTestCase.MethodEnvironment;
import gradingTools.sharedTestCase.utils.RedirectionEnvironment;

/**
 *
 * @author Andrew
 */
public class PreconditionExecutionTestCase extends BasicTestCase implements TraceableListener {

    public PreconditionExecutionTestCase() {
        super("Precondition execution Test Case");
    }
    public Method getBridgeSceneMethod(Project project, Class bridgeSceneClass, String aTag) {
       List<Method> aMethod = ProjectIntrospection.getOrFindMethodList(project, this, bridgeSceneClass, aTag);
       if (aMethod.size() == 0) {
    	   System.out.println("Could not find method:" + aTag);
    	   return null;
       }
       return aMethod.get(0);

    }

    @Override
    public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
        Optional<RedirectionEnvironment> outRedir = RedirectionEnvironment.redirectOut(); 
        try {
//            ExecutionUtil.redirectOutput();
            Class bridgeSceneClass = ProjectIntrospection.getOrFindClass(project, this, "BridgeScene");
            List<Method> approachMList = ProjectIntrospection.getOrFindMethodList(project, this, bridgeSceneClass, "approach");
            if (approachMList == null || approachMList.isEmpty()) {
                return fail("Can't find approach method in class " + bridgeSceneClass.getTypeName());
            }
            Method approach = approachMList.get(0);
            Constructor<?> bridgeSceneConstructor;
            try {
                bridgeSceneConstructor = bridgeSceneClass.getConstructor();
            } catch (NoSuchMethodException | SecurityException ex) {
                return fail("Can't access BridgeScene constructor");
            }
            if (bridgeSceneConstructor == null) {
                return fail("Can't find empty BridgeScene constructor");
            }

            Method say = null;
            Method passed = null;
            Method failed = null;
            Method preSay = null;
            Method prePassed = null;
            Method preFailed = null;
            Method preApproach = null;
            Method getArthur = null;
            Method getLancelot = null;

            try {

//                say = IntrospectionUtil.getOrFindMethodList(project, this, bridgeSceneClass, "say").get(0);
//                getArthur = IntrospectionUtil.getOrFindMethodList(project, this, bridgeSceneClass, "Arthur").get(0);
//                getLancelot = IntrospectionUtil.getOrFindMethodList(project, this, bridgeSceneClass, "Lancelot").get(0);
//
//                passed = IntrospectionUtil.getOrFindMethodList(project, this, bridgeSceneClass, "passed").get(0);
//                failed = IntrospectionUtil.getOrFindMethodList(project, this, bridgeSceneClass, "failed").get(0);
//
//                prePassed = IntrospectionUtil.getOrFindMethodList(project, this, bridgeSceneClass, "prePassed").get(0);
//                preApproach = IntrospectionUtil.getOrFindMethodList(project, this, bridgeSceneClass, "preApproach").get(0);
//                preFailed = IntrospectionUtil.getOrFindMethodList(project, this, bridgeSceneClass, "preFailed").get(0);
//                prePassed = IntrospectionUtil.getOrFindMethodList(project, this, bridgeSceneClass, "prePassed").get(0);
//                preSay = IntrospectionUtil.getOrFindMethodList(project, this, bridgeSceneClass, "preSay").get(0);
//
//                say = IntrospectionUtil.getOrFindMethodList(project, this, bridgeSceneClass, "say").get(0);
//                getArthur = IntrospectionUtil.getOrFindMethodList(project, this, bridgeSceneClass, "Arthur").get(0);
//                getLancelot = IntrospectionUtil.getOrFindMethodList(project, this, bridgeSceneClass, "Lancelot").get(0);
                say = getBridgeSceneMethod(project, bridgeSceneClass, "say");
                getArthur = getBridgeSceneMethod(project, bridgeSceneClass, "Arthur");
                getLancelot = getBridgeSceneMethod(project, bridgeSceneClass, "Lancelot");
                passed = getBridgeSceneMethod(project, bridgeSceneClass, "passed");
                if (passed == null)
                    passed =  getBridgeSceneMethod(project, bridgeSceneClass, "pass");
                failed = getBridgeSceneMethod(project, bridgeSceneClass, "failed");
                if (failed == null)
                    failed = getBridgeSceneMethod(project, bridgeSceneClass, "fail");

                prePassed = getBridgeSceneMethod(project, bridgeSceneClass, "prePassed");
                if (prePassed == null)
                    prePassed = getBridgeSceneMethod(project, bridgeSceneClass, "prePass");
                preApproach = getBridgeSceneMethod(project, bridgeSceneClass, "preApproach");
                preFailed = getBridgeSceneMethod(project, bridgeSceneClass, "preFailed");
                if (preFailed == null)
                    preFailed = getBridgeSceneMethod(project, bridgeSceneClass, "preFail");
                preSay = getBridgeSceneMethod(project, bridgeSceneClass, "preSay");


            } catch (Exception e) {
                return fail("Can't find at least one of the following: say, preSay, preApproach, prePassed, preFailed, getArthur, getLancelot, passed, failed");
            }

//            boolean[] results = checkMovement(bridgeSceneConstructor, approach, say, passed, failed, getArthur, getLancelot);
            Object[] results = checkMovement(bridgeSceneConstructor, approach, say, passed, failed, getArthur, getLancelot, preApproach, prePassed, preFailed, preSay);
            int numReturnValuesMatched = numMatches(expectedPre, preData);
            Set<String> namesMatched = matches(expectedNames, preTags);
            double percentValuesMatched = ((double) numReturnValuesMatched)/expectedPre.length;
            double precentTagsMatched  =  ((double) namesMatched.size())/(expectedNames.length - 2); // two alternatives
            if (percentValuesMatched == 1 && precentTagsMatched == 1)
                    return pass();
            double valueWeightage = 0.7;
            double tagWeightage = 0.3;
            double overallWeighage = valueWeightage*percentValuesMatched + tagWeightage*precentTagsMatched;
            return partialPass (overallWeighage, "Values and/or tags did not match,see transcript:" );
//            int correct = count(results, true);
//            if (correct == 0) {
//                return fail("Incorrect or no approach");
//            } else if (correct == 3) {
//                return pass();
//            } else {
//                int raw = results[0] ? 1 : 0;
//                raw += results[1] ? 2 : 0;
//                raw += results[2] ? 2 : 0;
//                double score = ((double)raw) / 5;
//                String message = buildMessage(results);
//                return partialPass(score, message);
//            }
        } finally {
            try {
                if (outRedir.isPresent()) {
                    String out = RedirectionEnvironment.restore(outRedir.get()).orElse("");
//                    System.gc();
                    ARunningProject.appendToTranscriptFile(project, getCheckable().getName(), out);
                }
//                String anOutput = restoreOutputAndGetRedirectedOutput();
//                if (anOutput != null && !anOutput.isEmpty()) {
//                    System.out.println(anOutput);
//                    RunningProject.appendToTranscriptFile(project, getCheckable().getName(), anOutput);
//                }
            } catch(OutOfMemoryError e) {
                System.gc();
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    protected Boolean[] expectedPre = {
    		
    		false, true, true, true,
    		true, false, false, false,
    		true, false, false};
    protected String[] expectedNames = {
    		"approach",
    		"passed",
    		"failed",
    		"say",
    		"pass",
    		"fail"
    		
    };
    protected int numMatches (Boolean[] expected, Object[] actual) {
    	System.out.println("Expected pres:" + Common.toString(expected));
    	System.out.println("Actial pres:  " + Common.toString(actual));
    	int retVal = 0;
    	for (int i = 0; i < expected.length; i++) {
    		Object anElement = actual[i];
    		if (anElement== null || !(anElement instanceof Boolean)) {
    			continue;
    		}
    			if (expected[i].equals(anElement))
    				retVal++;
    		
    	}
    	return retVal;
    }
    protected Set<String> matches (String[] anExpectedNames, List<Object> anActualNames) {
    	System.out.println("Expected names: " + Common.toString(anExpectedNames));
    	System.out.println("Actual name set: " +   anActualNames);

    	Set<String> retVal = new HashSet();
    	for (String anExpectedName:anExpectedNames) {
    		for (Object anActualName:anActualNames) {
    			if (anActualName == null || !(anActualName instanceof String))
    				continue;
    			if (anExpectedName.equalsIgnoreCase((String) anActualName))
    					retVal.add(anExpectedName);
    		}
    		
    	}
    	return retVal;    	
    }
    Object[] preData;
    protected Object[] checkMovement(Constructor<?> bridgeSceneConstructor,
    		Method approach, 
    		Method say,
    		Method passed,
    		Method failed,
    		Method getArthur, 
    		Method getLancelot,
    		Method preApproach,
    		Method prePassed,
    		Method preFailed,
    		Method preSay) {
//        boolean[] ret = new boolean[]{false, false, false};
        // should also call pre methods
        MethodEnvironment[] methods = new MethodEnvironment[]{
            MethodEnvironment.get(getArthur),                               // 0
            MethodEnvironment.get(getLancelot),                             // 1
           
            MethodEnvironment.get(approach, MethodExecutionTestCase.M0_RET),// 2
            MethodEnvironment.get(preApproach),// approach == false , 3
            MethodEnvironment.get(prePassed),// passed == true, 4
            MethodEnvironment.get(preFailed),// failed == true, 5
            MethodEnvironment.get(preSay),// say == true, 6

            MethodEnvironment.get(say, "Name?"),// 7
            MethodEnvironment.get(say, "Arthur?"),// 8
            MethodEnvironment.get(passed), // 9
            MethodEnvironment.get(preApproach),// approach == true, 10
            MethodEnvironment.get(preFailed),// failed == false, 11
            MethodEnvironment.get(prePassed),// passed == false, 12
            MethodEnvironment.get(preSay),// passed == false, 13

         // let us not check approach again
            MethodEnvironment.get(approach, MethodExecutionTestCase.M1_RET), // 14
            
            MethodEnvironment.get(say, "Name"), // 15
            MethodEnvironment.get(say, "Lancelot"), // 16
            MethodEnvironment.get(failed),  // 17
            MethodEnvironment.get(preApproach),// approach == true, 18
            MethodEnvironment.get(preFailed),// failed == false, 19
            MethodEnvironment.get(prePassed),// passed == false, 20

        };
        Object bridgeSceneInstance = null;
        try {
         bridgeSceneInstance = BasicProjectExecution.timedInvokeWithExceptions(bridgeSceneConstructor, new Object[]{});
        } catch (Exception e) {
        	return new Object[] {e};
        	
        }
        try {
        ObjectAdapter anObjectAdapter = ObjectEditor.toObjectAdapter(bridgeSceneInstance);
        preTags.clear(); 
        preValues.clear();
        System.out.println ("Adding traceable listner");
        TraceableBus.addTraceableListener(this);
        parentThread = Thread.currentThread();
        forkedThreads.clear();
        Object[] exData = MethodExecutionTestCase.invoke(bridgeSceneInstance, methods);
       preData =  new Object[]{
        		exData[3],exData[4], exData[5], exData[6], // after approach
        		exData[10], exData[11], exData[12], exData[13], // after passed
        		exData[18], exData[19], exData[20] // after failed
        };
        System.out.println("Pre tags:" + preTags);
        System.out.println("Pre values:" + preValues);

       
        
        return exData;
        } finally {
        	System.out.println("removing traceable listner");
            TraceableBus.removeTraceableListener(this);
            killThreads();
//            if (forkedThreads.size() > 0)
//            	ThreadSupport.sleep(1000); // give them time to register
//            for (Thread aThread:forkedThreads) {
//            	System.out.println("Interrupting:" + aThread);
//            	aThread.interrupt();
//            }

        }
    }
    protected synchronized void killThreads() {
    	if (forkedThreads.size() > 0)
        	ThreadSupport.sleep(1000); // give them time to register
        for (Thread aThread:forkedThreads) {
        	System.out.println("Interrupting:" + aThread);
        	aThread.interrupt();
        }
    }
//    protected Integer getTransition (String aTag, Boolean fromValue, int fromIndex) {
//    	boolean foundOriginal = false;
//    	
//    	for (int i = fromIndex; i < preTags.size(); i++ ) {
//    		if (!foundOriginal && aTag.equals(preTags.get(i)) && 
//    				fromValue.equals(preValues.get(i))){
//    			foundOriginal = true;
//    		}
//    	}
//    	
//    }
    
//    private static int checkEqualValue(Object[] resutls, int a, int b) {
//        return checkCompare(resutls, a, b, 0);
//    }
//    
//    private static int checkLTValue(Object[] resutls, int a, int b) {
//        return checkCompare(resutls, a, b, -1);
//    }
//    
//    private static int checkGTValue(Object[] resutls, int a, int b) {
//        return checkCompare(resutls, a, b, 1);
//    }
//    
//    private static int checkCompare(Object[] results, int a, int b, int sign) {
//        if (results.length < a || results.length < b) {
//            return 1; // checking out of bounds
//        }
//        Object oA = results[a];
//        Object oB = results[b];
//        if (oA instanceof Exception || oB instanceof Exception) {
//            return 2; // error in execution
//        }
//        if (!(oA instanceof Comparable) || !(oB instanceof Comparable)) {
//            return 3; // not comparable
//        }
//        Comparable cA = (Comparable)oA;
//        Comparable cB = (Comparable)oB;
//        if (Math.signum(cA.compareTo(cB)) != sign) {
//            return 4; // check comparison
//        }
//        
//        return 0;
//    }
    
//    private static String buildMessage(boolean[] notes) {
//        StringBuilder ret = new StringBuilder();
//        if (notes[0] == false) {
//            ret.append("Does not set occupied properly\n");
//        }
//        if (notes[1] == false) {
//            ret.append("Does not move knight when not occupied\n");
//        }
//        if (notes[2] == false) {
//            ret.append("Moves knigth when occupied");
//        }
//        return ret.toString();
//    }
//    
//    private static int count(boolean[] arr, boolean value) {
//        int count = 0;
//        for(boolean bool : arr) {
//            if (bool == value) {
//                count ++;
//            }
//        }
//        return count;
//    }
    protected List<Object> preTags = new ArrayList();
    protected List<Object> preValues = new ArrayList();
    protected void getPre (PropertyChangeEvent anEvent) {
    	Object tag = anEvent.getOldValue();
    	int i = 0;
    	if (!"this".equalsIgnoreCase(anEvent.getPropertyName()) 
//    			|| !(newValue instanceof String) 
    			) 
    			return ;
    	preTags.add(tag);
    	preValues.add(anEvent.getNewValue());
    }
    Thread parentThread;
    List<Thread> forkedThreads = new ArrayList();
    protected boolean isParentThread(Thread aThread) {
		return (aThread == parentThread) || aThread.getName().contains("ool");
	}

    public synchronized void newEvent(Exception aTraceable) {
		if (aTraceable instanceof ObjectAdapterReceivedPropertyChangeEvent) { // multiple PropertyChangeeventInfo will be sent
			
			ObjectAdapterPropertyChangeEventInfo aPropertyChange = (ObjectAdapterPropertyChangeEventInfo) aTraceable;
			getPre(aPropertyChange.getPropertyChangeEvent());
//			System.out.println("Property change:" + aPropertyChange.getPropertyChangeEvent() );
			if (!isParentThread(Thread.currentThread())) {
				System.out.println("forked thread, interrupting it " + Thread.currentThread());

				Thread.currentThread().interrupt();
//				if (!forkedThreads.contains(Thread.currentThread())) {
//
//				System.out.println("forked thread " + Thread.currentThread());
//				forkedThreads.add(Thread.currentThread());
//				}
			}
			
			}
			
			
				
	}
}
