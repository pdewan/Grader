package gradingTools.comp110f14lab.lab5.testcases;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import gradingTools.sharedTestCase.CodeInspectorTestCase;

//import com.github.antlrjavaparser.api.expr.VariableDeclarationExpr;

public class HasArray extends CodeInspectorTestCase {

	boolean hasArray = false;
	
	public HasArray() {
		super("Has Array test case");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void resetVariablesForEachProject() {
		hasArray = false;
	}
	
	@Override
	public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException,
			NotGradableException {
		hasArray = false;
		return super.test(project, autoGrade);
	}
	
//	@Override
//	protected void inspectVariableDeclarationExpr(VariableDeclarationExpr expr) {
//		// Code to check if it is an array
//		if(expr.getType().toString().toLowerCase().contains("int[]")||expr.getType().toString().toLowerCase().contains("myarray[]")||expr.getType().toString().toLowerCase().contains("double[]"))hasArray=true;
//		super.inspectVariableDeclarationExpr(expr);
//	}

	@Override
	public TestCaseResult codeInspectionResult() {
		if (hasArray) {
			return pass();
		} else {
			return fail("No array declared");
		}
	}

}
