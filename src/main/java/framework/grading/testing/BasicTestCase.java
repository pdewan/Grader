package framework.grading.testing;

import grader.basics.junit.BasicJUnitUtils;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.CurrentProjectHolder;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.junit.Test;


/**
 * All test cases should extend this class.
 * Subclasses will implement the {@link TestCase#test(framework.project.Project, boolean)} method.
 * This method should call and return one of the following helper functions:
 * <ul>
<<<<<<< HEAD
 *     <li>{@link framework.grading.testing.BasicTestCase#pass(boolean)}</li>
 *     <li>{@link framework.grading.testing.BasicTestCase#pass(String, boolean)}</li>
 *     <li>{@link framework.grading.testing.BasicTestCase#partialPass(double, boolean)}</li>
 *     <li>{@link framework.grading.testing.BasicTestCase#partialPass(double, String, boolean)}</li>
 *     <li>{@link framework.grading.testing.BasicTestCase#fail(String, boolean)}</li>
=======
 *     <li>{@link framework.grading.testing.BasicTestCase#pass()}</li>
 *     <li>{@link framework.grading.testing.BasicTestCase#pass(String)}</li>
 *     <li>{@link framework.grading.testing.BasicTestCase#partialPass(double)}</li>
 *     <li>{@link framework.grading.testing.BasicTestCase#partialPass(double, String)}</li>
 *     <li>{@link framework.grading.testing.BasicTestCase#fail(String)}</li>
>>>>>>> working
 * </ul>
 *
 * An example:
 * <pre>
 * {@code
 * return partialPass(0.5, "Only got half of the points");
 * }
 * </pre>
 */
public abstract class BasicTestCase implements TestCase {
	@JsonIgnore
    protected Checkable checkable;
    
	protected String name;
	
	protected double pointWeight = -1;

   
	public BasicTestCase(String name) {
        this.name = name;
    }
	public BasicTestCase(String name, double aPointWeight) {
        this.name = name;
        pointWeight = aPointWeight;
    }
    public BasicTestCase() {
    }
   
    @Override
    public Checkable getCheckable() {
		return checkable;
	}

    @Override
    public void setCheckable(Checkable checkable) {
        this.checkable = checkable;
    }

    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void setName (String aName) {
    	name = aName;
    }

    protected TestCaseResult partialPass(double percentage, boolean autograded) {
        return new TestCaseResult(percentage, name, autograded);
    }

    protected TestCaseResult partialPass(double percentage, String notes, boolean autograded) {
        return new TestCaseResult(percentage, notes, name, autograded);
    }
    protected TestCaseResult partialPass(double percentage, String notes) {
        return partialPass(percentage, notes, true);
    }

    protected TestCaseResult pass() {
        return new TestCaseResult(true, name, true);
    }

    protected TestCaseResult pass(boolean autograded) {
        return new TestCaseResult(true, name, autograded);
    }

    protected TestCaseResult pass(String notes) {
        return new TestCaseResult(true, notes, name, true);
    }
    protected TestCaseResult pass(String notes, boolean autograded) {
        return new TestCaseResult(true, notes, name, autograded);
    }

    protected TestCaseResult fail(String notes) {
        return new TestCaseResult(false, notes, name, true);
    }

    protected TestCaseResult fail(String notes, boolean autograded) {
        return new TestCaseResult(false, notes, name, autograded);

    }
    @Override
    @JsonIgnore
	public Object[] getPermissions() {
		return checkable.getPermissions();
	}
    public double getPointWeight() {
		return pointWeight;
	}
	public void setPointWeight(double pointWeight) {
		this.pointWeight = pointWeight;
	}
	
	@Test
	public void defaultTest() {
		TestCaseResult result = null;
        try {
        	result = test(CurrentProjectHolder.getOrCreateCurrentProject(), true);  
        	
    		BasicJUnitUtils.assertTrue(result.getNotes(), result.getPercentage(), result.isPass());
        } catch (Throwable e) {
        	e.printStackTrace();
        	if (result != null) {
        		BasicJUnitUtils.assertTrue(e, result.getPercentage());
        	} else {
        		BasicJUnitUtils.assertTrue(e, 0);
        	}
        }
	}
}
