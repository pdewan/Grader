package gradingTools.comp110f14lab.lab4.testcases;

import grader.basics.junit.TestCaseResult;
import gradingTools.sharedTestCase.CodeInspectorTestCase;

//import com.github.antlrjavaparser.api.stmt.WhileStmt;

public class HasWhileLoop extends CodeInspectorTestCase {
	
	boolean hasWhileLoop = false;

	public HasWhileLoop() {
		super("Has while loop");
	}

	@Override
	public void resetVariablesForEachProject() {
		hasWhileLoop = false;
	}
	
//	@Override
//	protected void inspectWhileStatement(WhileStmt statement) {
//		hasWhileLoop = true;
//		super.inspectWhileStatement(statement);
//	}

	@Override
	public TestCaseResult codeInspectionResult() {
		if (hasWhileLoop) {
			return pass();
		} else {
			return fail("No while loop found");
		}
	}
}
