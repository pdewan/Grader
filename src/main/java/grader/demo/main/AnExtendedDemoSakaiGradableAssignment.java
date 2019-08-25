package grader.demo.main;

import grader.assignment.AGradingFeature;
import grader.assignment.GradingFeature;
import grader.basics.file.FileProxy;
import grader.checkers.AFailingChecker;
import grader.checkers.AProjectTracer;
import grader.demo.feedback.ATracingAllTextSourceDisplayer;
import grader.demo.feedback.ATracingAutoFeedbackFileWriter;
import grader.demo.feedback.ATracingManualFeedbackFileDisplayer;
import grader.demo.feedback.ATracingScoreFeedbackFileWriter;
import grader.demo.scores.ATracingFeatureScoreRecorder;
import grader.feedback.AutoFeedback;
import grader.feedback.ManualFeedback;
import grader.feedback.ScoreFeedback;
import grader.feedback.SourceDisplayer;
import grader.sakai.project.SakaiProjectDatabase;
import grader.spreadsheet.FeatureGradeRecorder;
import grader.trace.settings.InvalidOnyenRangeException;

import java.util.List;
import java.util.Set;

public class AnExtendedDemoSakaiGradableAssignment extends ADemoSakaiGradableAssignment {

	public AnExtendedDemoSakaiGradableAssignment(String aBulkAssignmentsFolderName, String anAssignmentsDataFolderName) {
		super(aBulkAssignmentsFolderName, anAssignmentsDataFolderName);
		
		
	}
	public GradingFeature createTracingFeature() {

		GradingFeature aGradingFeature = new AGradingFeature("Tracer", 10, new AProjectTracer(), true);
		return aGradingFeature;
//		featureChecker.init(aGradingFeature);
//		aGradingFeature.init(featureChecker );
	}
	public GradingFeature createUngradableAutoFeature() {

		GradingFeature aGradingFeature = new AGradingFeature("Autofailing", 10, new AFailingChecker(), false);
		return aGradingFeature;
//		featureChecker.init(aGradingFeature);
//		aGradingFeature.init(featureChecker );
	}
	public List<GradingFeature> createGradingFeatures() {
		List<GradingFeature> aGradingFeatures = super.createGradingFeatures();
		aGradingFeatures.add(createTracingFeature());
		aGradingFeatures.add(createUngradableAutoFeature());
		return aGradingFeatures;
	}
	
	@Override
	public String outputSuffix() {
		return  ".txt";
	}
	@Override
	public String sourceSuffix() {
		return  ".txt";
	}
	@Override
	protected FeatureGradeRecorder createFeatureGradeRecorder() {
		return new ATracingFeatureScoreRecorder(this);
	}
//	@Override
//	protected FinalGradeRecorder createFinalGradeRecorder() {
//		return featureGradeRecorder;
//	}
//	protected FinalGradeRecorder createTotalScoreRecorder() {
////		return featureGradeRecorder;
//		return featureGradeRecorder;
//	}

	protected AutoFeedback createAutoFeedback() {
		return new ATracingAutoFeedbackFileWriter();
	}
	
	protected ManualFeedback createManualFeedback() {
		return new ATracingManualFeedbackFileDisplayer();
	}
	
	protected ScoreFeedback createScoreFeedback() {
		return new ATracingScoreFeedbackFileWriter();
	}

	protected SourceDisplayer createSourceDisplayer() {
		return new ATracingAllTextSourceDisplayer();
	}
	public static void main (String[] args) {
//		ObjectEditor.setMethodAttribute(AGradingFeature.class, "autoGrade", AttributeNames.SHOW_BUTTON, true);
//		ObjectEditor.setAttribute(AGradingFeature.class, AttributeNames.SHOW_UNBOUND_BUTTONS, new Boolean(true) );
//		SakaiProjectDatabase projectDatabase = new AnExtendedDemoSakaiGradableAssignment("C:/Users/dewan/Downloads/bulk_download", "C:/Users/dewan/Downloads/GraderData");
		SakaiProjectDatabase projectDatabase = new AnExtendedDemoSakaiGradableAssignment(bulk, graderData);

		Set<String> onyens = projectDatabase.getOnyens();
		System.out.println("Grading onyens:" + onyens);
		FileProxy fileProxy = projectDatabase.getAssignmentDataFolder().getFeatureGradeFile();
		System.out.println("Made projects:" + projectDatabase.getProjects());
		System.out.println("will add feature and final grades to:" + fileProxy.getAbsoluteName());
		Set<String> inputFiles = projectDatabase.getAssignmentDataFolder().getInputFiles();
		System.out.println("Input files are:" + inputFiles);
		
//		projectDatabase.runProjectInteractively("mkcolema");
//		projectDatabase.runProjectsInteractively();
        try {
			projectDatabase.nonBlockingRunProjectsInteractively();
		} catch (InvalidOnyenRangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
