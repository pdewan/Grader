package gradingTools.comp999f23.assignment0;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.project.BasicProjectIntrospection;
import grader.junit.AJUnitProjectRequirements;
import gradingTools.basics.sharedTestCase.checkstyle.predefined.BasicGeneralStyleSuite;
import gradingTools.sharedTestCase.DocumentEnclosedTestCase;

public class Assignment0Requirements extends AJUnitProjectRequirements {
	public Assignment0Requirements() {
//		Comp533TraceUtility.setTurnOn(true);
//		Comp533TraceUtility.setTracing();
//		GraderTraceUtility.setTurnOn(true);
//		GraderTraceUtility.setTracing();
		addDueDate("06/24/2024 01:00:00", 1.00);
     	addManualFeature("Annotate Code", 100);
     	BasicExecutionSpecificationSelector.getBasicExecutionSpecification().
		setCheckStyleConfiguration("unc_checks_999_f23_A0.xml");
     	addJUnitTestSuite(BasicGeneralStyleSuite.class);	

	}
}
