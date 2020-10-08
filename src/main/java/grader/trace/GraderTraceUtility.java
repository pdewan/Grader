package grader.trace;

import framework.execution.ProcessRunner;
import framework.logging.loggers.CsvLogger;
import framework.logging.recorder.ConglomerateRecorder;
import framework.navigation.SakaiStudentFolder;
import framework.project.ProjectClassesManager;
import grader.assignment.timestamp.SakaiTimestampComputer;
import grader.basics.execution.ARunnerInputStreamProcessor;
import grader.basics.file.AnAbstractRootFolderProxy;
import grader.basics.project.BasicProjectClassesManager;
import grader.compilation.AFilesToCompileFinder;
import grader.config.AnExecutionSpecification;
import grader.config.StaticConfigurationUtils;
import grader.navigation.AlphabeticNavigationListManager;
import grader.sakai.ASakaiBulkAssignmentFolder;
import grader.sakai.ASakaiStudentCodingAssignment;
import grader.sakai.project.ASakaiProjectDatabase;
import grader.settings.AGraderSettingsModel;
import grader.spreadsheet.csv.ASakaiCSVGradeBookManager;
import grader.steppers.AnAutoVisitBehavior;
import grader.steppers.AnOverviewProjectStepper;
import gradingTools.Driver;
import trace.grader.basics.GraderBasicsTraceUtility;
import util.trace.ImplicitKeywordKind;
import util.trace.Tracer;
import wrappers.framework.project.ProjectWrapper;

public class GraderTraceUtility extends GraderBasicsTraceUtility {
	static boolean turnOn = true;
	@Deprecated
	public static boolean isTurnOn() {
		return turnOn;
	}
	
	@Deprecated
	public static void setTurnOn(boolean turnOn) {
		GraderTraceUtility.turnOn = turnOn;
	}
	public static void setTracing() {
		GraderBasicsTraceUtility.setTracing();
		Tracer.setImplicitPrintKeywordKind(ImplicitKeywordKind.OBJECT_PACKAGE_NAME);	
//		if (isTurnOn()) {
			Tracer.setKeywordPrintStatus(AlphabeticNavigationListManager.class, true);
			Tracer.setKeywordPrintStatus(ASakaiCSVGradeBookManager.class, true);
			Tracer.setKeywordPrintStatus(AGraderSettingsModel.class, true);
			Tracer.setKeywordPrintStatus(ASakaiStudentCodingAssignment.class, true);
			Tracer.setKeywordPrintStatus(ASakaiProjectDatabase.class, true);
			Tracer.setKeywordPrintStatus(ASakaiBulkAssignmentFolder.class, true);
			Tracer.setKeywordPrintStatus(AnAbstractRootFolderProxy.class, true);

			Tracer.setKeywordPrintStatus(ProjectWrapper.class, true);
			Tracer.setKeywordPrintStatus(SakaiStudentFolder.class, true);


			Tracer.setKeywordPrintStatus(ProcessRunner.class, true);
//			Tracer.setKeywordPrintStatus(ARunnerInputStreamProcessor.class, true);
			Tracer.setKeywordPrintStatus(AnExecutionSpecification.class, true);
			Tracer.setKeywordPrintStatus(Driver.class, true);
			Tracer.setKeywordPrintStatus(AnOverviewProjectStepper.class, true);
			Tracer.setKeywordPrintStatus(ProjectClassesManager.class, true);
			Tracer.setKeywordPrintStatus(AnAutoVisitBehavior.class, true);
			Tracer.setKeywordPrintStatus(StaticConfigurationUtils.class, true);
			Tracer.setKeywordPrintStatus(AFilesToCompileFinder.class, true);
			Tracer.setKeywordPrintStatus(ConglomerateRecorder.class, true);
			Tracer.setKeywordPrintStatus(AnAutoVisitBehavior.class, true);
			Tracer.setKeywordPrintStatus(SakaiTimestampComputer.class, true);
			Tracer.setKeywordPrintStatus(ASakaiStudentCodingAssignment.class, true);
		     Tracer.setKeywordPrintStatus(CsvLogger.class, true);
				Tracer.setKeywordPrintStatus(ProjectClassesManager.class, true);









//		}

	}
}
