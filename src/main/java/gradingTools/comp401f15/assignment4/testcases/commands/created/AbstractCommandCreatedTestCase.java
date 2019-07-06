package gradingTools.comp401f15.assignment4.testcases.commands.created;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import framework.grading.testing.BasicTestCase;
import grader.basics.config.BasicProjectExecution;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.BasicProjectIntrospection;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.util.ProjectExecution;
import gradingTools.comp401f15.assignment4.testcases.ScannerBeanReturnsTokenInterfaceArrayTestCase;
import gradingTools.comp401f15.assignment6.testcases.ClearableHistoryFunctionTestCase;

public abstract class AbstractCommandCreatedTestCase extends BasicTestCase {

    public AbstractCommandCreatedTestCase(String aName) {
        super(aName);
    }

    String[] beanDescriptions = {null, "ScannerBean", ".*Bean.*", ".*Bean.*"};
    Class[] constructorArgTypes2 = {String.class};
    Class[] constructorArgTypes1 = {};
    String[] constructorArgs2 = {""};
    String[] constructorArgs1 = {};
    
    protected String commandIdentifier() { return "";}
    protected String[] commandDescriptions() { return new String[]{null, "", "", ""};}
    protected String commandName() { return "";}

    protected String inputEndingSpaces() {return commandName() + " ";}
    protected String input(){return commandName();}
    protected String tokenPropertyName = "Tokens";
    
    String[] outputPropertyNames()  {
    	Method getTokenswMethod = (Method) getCheckable().getRequirements().getUserObject(ScannerBeanReturnsTokenInterfaceArrayTestCase.TOKEN_METHOD);
    	if (getTokenswMethod != null) {
    		tokenPropertyName = getTokenswMethod.getName().substring(3);
    	} 
        return new String[]{tokenPropertyName};
    }

    public TestCaseResult test(Project aProject, Class[] aConstructorArgTypes, Object[] aConstructorArgs, String aScannedString) throws NotAutomatableException, NotGradableException {
        Map<String, Object> anInputs = new HashMap();
        anInputs.put("ScannedString", aScannedString);
        Map<String, Object> anActualOutputs = ProjectExecution.testBean(getCheckable().getName(), getName(), aProject, beanDescriptions, aConstructorArgTypes, aConstructorArgs, anInputs, outputPropertyNames());

        if (anActualOutputs.get(BasicProjectExecution.MISSING_CLASS) != null) { // only output, no object
            return fail("Could not find scanner bean");
        }
        if (!anActualOutputs.containsKey(BasicProjectExecution.MISSING_READ)) {
            Object tokenRet = anActualOutputs.get(tokenPropertyName);
            Object token = null;
            
            ClearableHistoryFunctionTestCase.locateClearableHistory(aProject, this);
            Method clearMethod = (Method)getCheckable().getRequirements().getUserObject(ClearableHistoryFunctionTestCase.CLEARABLE_HISTORY);
            if (clearMethod != null) {
                Class<?> clearableHistoryClass = clearMethod.getReturnType();
                Method getTokens = Arrays.stream(clearableHistoryClass.getMethods()).filter((m)->m.getName().matches(".*get.*")).findFirst().orElse(null);
                if (getTokens != null) {
//                    token = BasicProjectExecution.timedInvoke(tokenRet, getTokens, (Object)0);
                    try {
						token = BasicProjectExecution.timedInvoke(tokenRet, getTokens, new Object[] {0});
					} catch (Throwable e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return fail("Invocation exception:" + e.getMessage());
					}

                } else {
                    return fail("Can't find method to get from clearable history");
                }
            } else if (tokenRet instanceof Object[]) {
                Object[] tokens = (Object[])tokenRet;
                token = tokens[0];
            } else {
                return fail("Can't find token getter");
            }
            if (token != null) {
                Class aClass = BasicProjectIntrospection.findClass(aProject, 
                            commandDescriptions()[0],
                            commandDescriptions()[1],
                            commandDescriptions()[2],
                            commandDescriptions()[3]);
                if (aClass == null) {
                    return fail("Cannot find a class for the '" + commandName() + "' command token");
                }
                if (aClass.isInstance(token)) {
                    return pass("Correctly creates '" + commandName() + "' command tokens");
                } else {
                    return fail("Fails to create '" + commandName() + "' command tokens");
                }
            } else {
                return fail("No token found");
            }
        }
        return fail("Can't find token getter");
    }

    @Override
    public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
       try {
    	if (project.getClassesManager().isEmpty()) {
            throw new NotGradableException();
        }

        TestCaseResult result = test(project, constructorArgTypes1, constructorArgs1, inputEndingSpaces());
        if (result.getNotes().contains("Could not find scanner bean")) {
            return result;
        }
        if (result.getPercentage() < 0.7) {
            result = test(project, constructorArgTypes2, constructorArgs2, inputEndingSpaces());
            if (result.getPercentage() < 0.7) {
                result = test(project, constructorArgTypes1, constructorArgs1, input());
                if (result.getPercentage() < 0.7) {
                    result = test(project, constructorArgTypes2, constructorArgs2, input());
                }
            }
        }
        return result;
       } catch (Throwable e) {
    	   e.printStackTrace();
       		throw new  NotGradableException();
       }
    }
    
}
