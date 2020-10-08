package gradingTools.comp110s15.assignment2.testcases;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import gradingTools.sharedTestCase.CodeInspectorTestCase;

//import com.github.antlrjavaparser.api.expr.VariableDeclarationExpr;

public class HasVar extends CodeInspectorTestCase {

	boolean hasVar = false;
	
	public HasVar() {
		super("Has hoursRequired variable test case");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void resetVariablesForEachProject() {
		hasVar = false;
	}
	
	@Override
	public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException,
			NotGradableException {
		hasVar = false;
		return super.test(project, autoGrade);
	}
	
//	@Override
//	protected void inspectVariableDeclarationExpr(VariableDeclarationExpr expr) {
//		// Code to check if it is there
//		String line=expr.getType().toString().toLowerCase();
//		if(line.contains("int"))hasVar=true;
//		super.inspectVariableDeclarationExpr(expr);
//	}

	@Override
	public TestCaseResult codeInspectionResult() {
		if (hasVar) {
			return pass();
		} else {
			return fail("Did not use hoursrequired variable, or hoursrequired variable not present");
		}
	}

}
