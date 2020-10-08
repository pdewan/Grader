package gradingTools.comp110f14lab.lab4.testcases;

import grader.basics.junit.TestCaseResult;
import gradingTools.sharedTestCase.CodeInspectorTestCase;

//import com.github.antlrjavaparser.api.stmt.ForStmt;

public class HasForLoop extends CodeInspectorTestCase {
	
	boolean hasForLoop = false;

	public HasForLoop() {
		super("Has for loop");
	}

	@Override
	public void resetVariablesForEachProject() {
		hasForLoop = true;
	}
//	
//	@Override
//	protected void inspectForStatement(ForStmt statement) {
//		hasForLoop = true;
//		super.inspectForStatement(statement);
//	}

	@Override
	public TestCaseResult codeInspectionResult() {
		if (hasForLoop) {
			return pass();
		} else {
			return fail("No for loop found");
		}
	}
}
