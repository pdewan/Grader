package framework.grading.testing;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import util.trace.Tracer;
import wrappers.framework.project.ProjectWrapper;
import framework.grading.ProjectRequirements;
import grader.assignment.GradingFeature;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.trace.feature.FeatureChecked;

/**
 * The idea for this class is that features and restrictions both check their test cases. This handles that process.
 */
public abstract class Checkable implements Gradable {
	@JsonIgnore
	ProjectRequirements requirements; //inheritance link
	

	protected boolean manual; // added by pd
    protected boolean extraCredit; // moved by pd
    protected String name; // moved by pd
    protected double points; //moved by pd
    protected List<TestCase> testCases; // moved by pd


    public Checkable(String name, double points, List<TestCase> testCases) {
        this.name = name;
        this.points = points;
        this.extraCredit = false;
        this.testCases = testCases;
    }

    public Checkable(String name, double points, boolean extraCredit, List<TestCase> testCases) {
        this.name = name;
        this.points = points;
        this.extraCredit = extraCredit;
        this.testCases = testCases;
    }
    
    public Checkable(boolean anIsManual, String name, double points, boolean extraCredit, List<TestCase> testCases) {
      this(name, points, extraCredit, testCases);
      manual = anIsManual;
    }
    public Checkable(boolean anIsManual, String name, double points, boolean extraCredit, TestCase ... testCases) {
        this(name, points, extraCredit, testCases);
        manual = anIsManual;
        makeTestCasesReferenceMe(testCases);
    }
    
    public Checkable(String name, double points, TestCase ... testCases) {
        this.name = name;
        this.points = points;
        this.extraCredit = false;
        this.testCases = Arrays.asList(testCases);
        makeTestCasesReferenceMe(testCases);
    }
    protected void makeTestCasesReferenceMe(TestCase ... testCases) {
    	for (TestCase aTestCase : this.testCases) {
            if (aTestCase != null) {
        	aTestCase.setCheckable(this);
            }
        }
    }

    public Checkable(String name, double points, boolean extraCredit, TestCase ... testCases) {
        this.name = name;
        this.points = points;
        this.extraCredit = extraCredit;
        this.testCases = Arrays.asList(testCases);
        makeTestCasesReferenceMe(testCases);
//        for (TestCase aTestCase : this.testCases) {
//            if (aTestCase != null) {
//        	aTestCase.setCheckable(this);
//            }
//        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPoints() {
        return points;
    }

    public boolean isExtraCredit() {
        return extraCredit;
    }
    
    
    public boolean isManual() {
        return manual;
    }
    static Date date = new Date();

    /**
     * This checks the test cases against the project
     *
     * @param points    The max points to award
     * @param testCases The test cases to test
     * @param project   The project the test cases will be checked against
     * @return The results of the check
     */
    protected CheckResult check(double points, List<TestCase> testCases, Project project, boolean autoMode) {
    	if (project == null || (isManual() && !autoMode))
//        	if (isManual() && testCases.isEmpty())

    		 return new CheckResult(0, "", CheckResult.CheckStatus.NotGraded, this);
        if (testCases.isEmpty())
            return new CheckResult(0, "", CheckResult.CheckStatus.Failed, this);
//        double pointWeight = points / testCases.size();
        int numTestCases = testCases.size();
        double pointWeight = (numTestCases == 0)?1.0: 1.0 / (numTestCases);

        CheckResult result = new CheckResult(points, pointWeight, this);
        try {
            for (TestCase testCase : testCases) {
            	if (testCase == null)
            		continue;
            	if (testCase.getName() == null) {
            		testCase.setName(getName());// so we do not have to repeat the names
            	}
            	if (testCase.getName() == null) {
            		testCase.setName("");
            	}
//            	CurrentProjectHolder.setProject(project); // in case some test reset the project
            	if (project instanceof ProjectWrapper) {
            		((ProjectWrapper) project).getProject().setCurrentTestCase(testCase);
            	}
            	try {
                TestCaseResult testResult = testCase.test(project, autoMode);
                testCase.setLastResult(testResult);
            	
                if (isManual()) {
                	testResult.setAutoGraded(false);
                }
                date.setTime(System.currentTimeMillis());
                System.out.println("Saving result:" + testResult + " at " + date );
                result.save(testResult, testCase.getPointWeight());
            	} catch (Exception e) {
            		e.printStackTrace();
            	}
            }
            result.setStatus(CheckResult.CheckStatus.Successful);
            FeatureChecked.newCase(null, null, null, this, this);
            if (project instanceof ProjectWrapper) {
        		((ProjectWrapper) project).getProject().setCurrentTestCase(null);
        	}
            return result;
        } catch (NotAutomatableException e) {
        	e.announce();
        	String msg;
        	if (e.getMessage() == null || e.getMessage().isEmpty())
        		msg = "Not automatable";
        	else
        		msg = "Not automatable: " + e.getMessage();
//            return new CheckResult(0, "", CheckResult.CheckStatus.NotGraded, this);
        	// Not automatbole should really mean a manual check is neded
            return new CheckResult(0, msg, CheckResult.CheckStatus.NotGraded, this);

        } catch (NotGradableException e) {
        	e.announce();
        	String msg;
        	if (e.getMessage() == null || e.getMessage().isEmpty())
        		msg = "Not gradable";
        	else
        		msg = "Not gradable: " + e.getMessage();
//        	String msg = "Grading failed: " + e.getMessage();
//        	String msg = "Grading failed";


//        	String msg = "Could not grade because did not find classes ";
//        	Tracer.error("Could not grade because did not find classes ");
        	Tracer.error(msg);
//            e.printStackTrace();
            return new CheckResult(0, msg, CheckResult.CheckStatus.Failed, this);
        } catch (Exception e) {
        	NotGradableException.newCase(e.getMessage(), this);
        	e.printStackTrace();
            return new CheckResult(0, "Not gradable", CheckResult.CheckStatus.NotGraded, this);

        }
    }
    @Override
    @JsonIgnore
    public ProjectRequirements getRequirements() {
		return requirements;
	}
    @Override
	public void initRequirements(ProjectRequirements requirements) {
		this.requirements = requirements;
	}

    public CheckResult check(Project project) {
        return check(project, true);
//        return check(project, !isManual());

    }

    /**
     * This is the publicly available check method, to be implemented by the extender
     *
     * @param project The project to check
     * @param autoMode If we are auto grading, that is, to display GUIs/user interaction
     * @return The results of the check
     */
    public abstract CheckResult check(Project project, boolean autoMode);
	@JsonIgnore
    GradingFeature gradingFeature;
    

	public GradingFeature getGradingFeature() {
		return gradingFeature;
	}

	public void setGradingFeature(GradingFeature gradingFeature) {
		this.gradingFeature = gradingFeature;
	}
}