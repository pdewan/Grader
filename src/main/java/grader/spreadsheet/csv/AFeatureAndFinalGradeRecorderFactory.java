package grader.spreadsheet.csv;

import grader.sakai.project.SakaiProjectDatabase;
import grader.spreadsheet.FinalGradeRecorder;
import grader.spreadsheet.FinalGradeRecorderFactory;
import wrappers.grader.sakai.project.ProjectDatabaseFactory;

public class AFeatureAndFinalGradeRecorderFactory implements FinalGradeRecorderFactory {
	FinalGradeRecorder recorder;
	@Override
	public FinalGradeRecorder createFeatureGradeRecorder(
			SakaiProjectDatabase aSakaiProjectDatabase) {
		return new AFeatureAndFinalGradeRecorder(aSakaiProjectDatabase);
	}
	@Override
	public FinalGradeRecorder getOrCreateFeatureGradeRecorder(
			SakaiProjectDatabase aSakaiProjectDatabase) {
		if (recorder == null)
			recorder = createFeatureGradeRecorder(aSakaiProjectDatabase);
		return  recorder;
	}
	@Override
	public FinalGradeRecorder getOrCreateFeatureGradeRecorder() {
		if (recorder == null)
			recorder = createFeatureGradeRecorder(ProjectDatabaseFactory.getOrCreateProjectDatabase());
		return  recorder;
	}
	

}
