package gradingTools.comp401f15.assignment3.testcases;

import java.util.Map;

import util.misc.Common;
import framework.grading.testing.BasicTestCase;
import grader.basics.execution.BasicProjectExecution;
import grader.basics.execution.NotRunnableException;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.util.ProjectExecution;


public abstract class AbstractTokenBeanTestCase extends BasicTestCase {
	abstract protected  String classIdentifier();
	abstract protected String[] beanDescriptions(); 
//	abstract double missingClassPenalty();
//	abstract protected double missingExpectedConstructorPenalty();
//	abstract protected double missingNullConstructorPenalty();
//	abstract protected double missingPropertyPenalty();
//	abstract protected double getsNotSetsPenalty();
//	abstract protected double incorrectDependentsPenalty();
//	
	abstract protected String inputPropertyName();
	abstract protected String outputPropertyName();
	abstract protected String input();
	abstract protected Object value();
	protected double missingClassPenalty() {return 1.0;};
	protected double missingExpectedConstructorPenalty() {return 0.2;};
	protected double missingNullConstructorPenalty() {return 0.0;}
	protected double missingPropertyPenalty() {return 0.2;};
	protected double getsNotSetsPenalty() {return 0.2;}
	protected double incorrectDependentsPenalty(){return 0.3;}

    public AbstractTokenBeanTestCase(String aName) {
        super(aName);
    }
    protected boolean isNullOrTrue (Boolean aBoolean) {
    	return aBoolean == null || aBoolean;
    }
    protected boolean isNotNullAndTrue (Boolean aBoolean) {
    	return aBoolean != null && aBoolean;
    }
    public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
        try {
        	Map<String, Object> retVal = ProjectExecution.testBeanWithStringConstructor(getCheckable().getName(), getName(), project, beanDescriptions(), input(), inputPropertyName(), input(), outputPropertyName(), value());
        	Boolean missingExpectedConstructor = (Boolean) retVal.get(BasicProjectExecution.MISSING_CONSTRUCTOR);
        	Boolean missingClass = (Boolean) retVal.get(BasicProjectExecution.MISSING_CLASS);
        	double penalty = 0.0;
        	String aMessage = "";
        	// clear existing value
    		getCheckable().getRequirements().putUserObject(classIdentifier(), null);

        	Boolean missingNullConstructor = null;
        	if (missingClass != null) {
        		return fail ("Class matching:" + Common.toString(beanDescriptions()) + " not found");
        	} else {
        		getCheckable().getRequirements().putUserObject(classIdentifier(), retVal.get(BasicProjectExecution.CLASS_MATCHED));
        	}
        	if (missingExpectedConstructor != null) {
            	 retVal = ProjectExecution.testBeanWithNoConstructor(getCheckable().getName(), getName(), project, beanDescriptions(), inputPropertyName(), input(), outputPropertyName(), value());
            	 penalty += missingExpectedConstructorPenalty();
            	 aMessage += "Expected constructor missing ";

        	}
        	
        	        	
        	boolean getterReturnsSetter = isNullOrTrue((Boolean) retVal.get(BasicProjectExecution.EXPECTED_EQUAL_ACTUAL));
        	boolean correctDependent = isNullOrTrue((Boolean) retVal.get(BasicProjectExecution.EXPECTED_EQUAL_ACTUAL));
        	boolean missingProperty = isNotNullAndTrue((Boolean) retVal.get(BasicProjectExecution.MISSING_PROPERTY));
        	if (missingProperty) {
        		penalty += missingPropertyPenalty();
        		aMessage += "Property missing " ;
        	}
        	if (!correctDependent) {
        		penalty += missingPropertyPenalty();
        		aMessage += "Incorrect dependent property value ";
        	}
        	if (!getterReturnsSetter) {
        		penalty += getsNotSetsPenalty();
        		aMessage += "Get is not set  ";

        	}
        	double score = Math.max(0, 1 - penalty);
        	if (score == 1.0)
        		return pass();
        	return partialPass(score, aMessage);
        	
        	
        } catch (NotRunnableException e) {
            throw new NotGradableException();
        }
    }
   
}

