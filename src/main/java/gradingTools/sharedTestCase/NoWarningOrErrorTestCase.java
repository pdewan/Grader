package gradingTools.sharedTestCase;

import java.util.ArrayList;
import java.util.List;

import wrappers.framework.project.ProjectWrapper;
import grader.basics.execution.NotRunnableException;
import grader.basics.execution.RunningProject;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.sakai.project.SakaiProject;
import gradingTools.sharedTestCase.checkstyle.OldOutputAndErrorCheckingTestCase;


public class NoWarningOrErrorTestCase extends OldOutputAndErrorCheckingTestCase {
	protected String ignoreFilter;
	protected String includeFilter;
	protected double penalty;
    public NoWarningOrErrorTestCase() {
        super("No warning test case");
    }
    public NoWarningOrErrorTestCase(String aName, String anIgnoreFilter, String anIncludeFilter, double aPenalty) {
        super(aName);
        ignoreFilter = anIgnoreFilter;
        includeFilter = anIncludeFilter;
        penalty = aPenalty;
    }
    protected String ignoreFilter() {
    	return ignoreFilter;
    }
    protected String includeFilter() {
    	return includeFilter;
    }
    public static boolean isErrorOrWarning (String aLine) {
    	return aLine.contains("E**") || aLine.contains("W**") || aLine.contains("Error:");
    }
    
    public  List<String> oeWarningAndErrorLines(String anOutput) {
    	List<String> retVal = new ArrayList<>();
    	if (anOutput == null || anOutput.isEmpty()) {
    		return retVal;
    	}
    	String[] anOutputLines = anOutput.split("\n");
    	String anIgnoreFilter = ignoreFilter();
    	String anIncludeFilter = includeFilter();
    	for (String anOutputLine:anOutputLines) {
    		if (!isErrorOrWarning(anOutputLine) 
    				|| (anIgnoreFilter != null && anOutputLine.matches(anIgnoreFilter))
    				|| (anIncludeFilter != null && !anOutputLine.matches(anIncludeFilter)))
    			continue;
    		retVal.add(anOutputLine);
    		
    	}
    	return retVal;
    }
    
    protected double pointsPerErrorOrWarning() {
    	return penalty;
    }
    
   
    RunningProject lastRunningProject;

    @Override
    public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
        try {
        	if (!autoGrade || project == null)
        		return fail("Not graded in auto run");
        	SakaiProject aSakaiProject = ((ProjectWrapper) project).getProject();
        	RunningProject aRunner = aSakaiProject.getCurrentRunningProject();
        	if (aRunner == null ) {
        		System.err.println("No project run interactively");
        		return fail("No project run interactively");
//        	} else if (aRunner == lastRunningProject) {
//        		System.err.println("Have already graded this interactive run");
        	} else {
        		lastRunningProject = aRunner;
//        		String anOutput = lastRunningProject.getOutput();
        		String anOutput = lastRunningProject.getOutput();
        		String anError = lastRunningProject.getErrorOutput();
        		if (!anError.isEmpty()) {
//        			System.err.println("Project running results in  error");
            		return fail("Interactive run:" + anError);
        		}
        		List<String> anErrorOrWarnings = oeWarningAndErrorLines(anOutput);
        		double score = Math.max(0.0, 1 - (anErrorOrWarnings.size() * pointsPerErrorOrWarning()));
        		if (score == 1.0) {
        			return pass();
        		} else {
        			return partialPass(score, "Interactive run: " + anErrorOrWarnings.toString());
        		}
//        		if (anOutput.contains("E**"))
//        			return fail("output contains errors");
//        		else if (anOutput.contains("W**"))
//        			return partialPass(0.5, "output contains warnings");
//        		else
//        			return pass();

        	}
//        	String aTranscript = aSakaiProject.getCurrentOutput().toString();
//        	return pass();
        	
        	
        } catch (NotRunnableException e) {
            throw new NotGradableException();
        }
    }
}

