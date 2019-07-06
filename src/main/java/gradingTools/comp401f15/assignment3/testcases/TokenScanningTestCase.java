package gradingTools.comp401f15.assignment3.testcases;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.misc.Common;
import grader.basics.config.BasicProjectExecution;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.util.ProjectExecution;
import gradingTools.sharedTestCase.checkstyle.OldOutputAndErrorCheckingTestCase;

public abstract class  TokenScanningTestCase extends OldOutputAndErrorCheckingTestCase{

	public TokenScanningTestCase(String aName) {
        super(aName);
    }
	
//	static String[] emptyArgs = {};
//	
//	public Object createScannerBean (Class aClass) {
//		Constructor aConstructor = null;
//		try {
//			aConstructor = aClass.getConstructor();
////			return aConstructor.newInstance();
//			Object aResult = ExecutionUtil.timedInvoke(aConstructor, emptyArgs, 300);
//			return aResult;
//		} catch (Exception e) {
//			try {
//				aConstructor = aClass.getConstructor(String.class);
//				Object aResult = ExecutionUtil.timedInvoke(aConstructor, new String[] {""}, 300);
//
//				return aResult;
//			} catch (Exception e1) {
//				return null;
//			}			
//		}
//		
//		
//		
//	}
	String[] beanDescriptions =  {null, "ScannerBean", ".*Bean.*", ".*Bean.*"};
    Class[] constructorArgTypes2 = {String.class};
    Class[] constructorArgTypes1 = {};
    String[] constructorArgs2 = {""};
    String[] constructorArgs1 = {};
    protected abstract String inputEndingSpaces();
    protected abstract String input();
    String[] outputPropertyNames = {"ScannedString"};
    
   protected abstract String[] expectedOutputs();

    
   
	
    public TestCaseResult test(Project aProject, Class[] aConstructorArgTypes, Object[] aConstructorArgs, String aScannedString) throws NotAutomatableException, NotGradableException {
    	String[] anExpectedOutputs = expectedOutputs();
    	if (anExpectedOutputs == null) { // maybe there should be an expecption thrown
    		return fail ("Did not find class with appropriate tag");
    	}
    	Map<String, Object> anInputs = new HashMap();
        anInputs.put("ScannedString", aScannedString);
        Map<String, Object> anActualOutputs = ProjectExecution.testBean(getCheckable().getName(), getName(), aProject, beanDescriptions, aConstructorArgTypes, aConstructorArgs, anInputs, outputPropertyNames);
//        String aTranscript = (String) anActualOutputs.get("System.out");
//        if (aTranscript != null && !aTranscript.isEmpty()) {
//        	RunningProject.appendToTranscriptFile(aProject, getCheckable().getName(), aTranscript);
//        }
        if (anActualOutputs.get(BasicProjectExecution.MISSING_CLASS) != null) { // only output, no object
        	return fail ("Could not find scanner bean");
        }
        Boolean getsReturnedSets = (Boolean) anActualOutputs.get(BasicProjectExecution.GETS_EQUAL_SETS);
        String anOutput = (String) anActualOutputs.get(BasicProjectExecution.PRINTS);
        String[] anOutputLines =anOutput.split("\n");
        List<String> anOutputLinesList = Common.arrayToArrayList(anOutputLines);
        int i = 0;
        boolean correctTokensPrinted = OldOutputAndErrorCheckingTestCase.isValidOutputInDifferentLines(anOutputLinesList, anExpectedOutputs);

//        boolean correctTokensPrinted = OutputAndErrorCheckingTestCase.isValidOutputInDifferentLines(anOutputLinesList, expectedOutputs());
        if (getsReturnedSets && correctTokensPrinted) {
        	return pass();
        }
        if (!getsReturnedSets && !correctTokensPrinted)
        	return fail("Gets do not returns sets and incorrect tokens printed in:" + getName());
        if (!getsReturnedSets)	{
        	return partialPass (0.9, "Gets do not returns sets, a minor penalty as you may have been penalized for this in an earlier assignment: " + getName());
        }
        return partialPass (0.1, "Incorrect tokens printed:" + getName());
    }

    @Override
    public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
        if (project.getClassesManager().isEmpty())
            throw new NotGradableException();
        
      TestCaseResult result = test(project, constructorArgTypes1, constructorArgs1, inputEndingSpaces());
      if (result.getNotes().contains("Could not find scanner bean"))
      	return result;
      if (result.getPercentage() < 0.7) {
    	  result = test(project, constructorArgTypes2, constructorArgs2, inputEndingSpaces());
    	  if (result.getPercentage() < 0.7) {
    		  result = test(project, constructorArgTypes1, constructorArgs1, input());
    		  if (result.getPercentage() < 0.7)
    			  result = test(project, constructorArgTypes2, constructorArgs2, input());
    	  }
      }
      return result;
    }
}
