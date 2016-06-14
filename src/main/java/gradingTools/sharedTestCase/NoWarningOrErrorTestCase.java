package gradingTools.sharedTestCase;

import java.util.ArrayList;
import java.util.List;

import wrappers.framework.project.ProjectWrapper;
import framework.execution.BasicRunningProject;
import framework.execution.NotRunnableException;
import framework.execution.RunningProject;
import framework.grading.testing.BasicTestCase;
import framework.grading.testing.OutputAndErrorCheckingTestCase;
import framework.grading.testing.NotAutomatableException;
import framework.grading.testing.NotGradableException;
import framework.grading.testing.TestCaseResult;
import framework.project.Project;
import grader.sakai.project.SakaiProject;
import grader.util.GraderCommon;
import gradingTools.assignment1.FlexibleProgramRunner;


public class NoWarningOrErrorTestCase extends OutputAndErrorCheckingTestCase {
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
    	return aLine.contains("E**") || aLine.contains("W**");
    }
    public  List<String> warningAndErrorLines(String anOutput) {
    	List<String> retVal = new ArrayList<>();
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
    
   
    BasicRunningProject lastRunningProject;

    @Override
    public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
        try {
        	if (!autoGrade || project == null)
        		return fail("No graded in auto run");
        	SakaiProject aSakaiProject = ((ProjectWrapper) project).getProject();
        	BasicRunningProject aRunner = aSakaiProject.getCurrentRunningProject();
        	if (aRunner == null ) {
        		System.err.println("No project run interactively");
        		return fail("No project run interactively");
//        	} else if (aRunner == lastRunningProject) {
//        		System.err.println("Have already graded this interactive run");
        	} else {
        		lastRunningProject = aRunner;
        		String anOutput = lastRunningProject.getOutput();
        		List<String> anErrorOrWarnings = warningAndErrorLines(anOutput);
        		double score = Math.max(0.0, 1 - (anErrorOrWarnings.size() * pointsPerErrorOrWarning()));
        		if (score == 1.0) {
        			return pass();
        		} else {
        			return partialPass(score, anErrorOrWarnings.toString());
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

