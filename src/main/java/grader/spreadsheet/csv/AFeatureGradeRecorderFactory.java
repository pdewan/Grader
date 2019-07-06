package grader.spreadsheet.csv;

import grader.sakai.project.SakaiProjectDatabase;
import grader.spreadsheet.FeatureGradeRecorder;
import grader.spreadsheet.FeatureGradeRecorderFactory;
import grader.spreadsheet.FinalGradeRecorder;
import wrappers.grader.sakai.project.ProjectDatabaseFactory;
// chnage its name to have SakaiCSV in it
public class AFeatureGradeRecorderFactory implements FeatureGradeRecorderFactory{
	FeatureGradeRecorder recorder;
//	@Override
//	public FinalGradeRecorder createFinalGradeRecorder(
//			SakaiProjectDatabase aSakaiProjectDatabase) {
//		return new ASakaiCSVFinalGradeManager(aSakaiProjectDatabase);
//	}
// why are we vreatying feature grade manager here?
	@Override
	public FeatureGradeRecorder createFeatureGradeRecorder(
			SakaiProjectDatabase aSakaiProjectDatabase) {
		return new ASakaiCSVFeatureGradeManager(aSakaiProjectDatabase);
	}

	@Override
	public FinalGradeRecorder getOrCreateFeatureGradeRecorder(
			SakaiProjectDatabase aSakaiProjectDatabase) {
		if (recorder == null)
			recorder = createFeatureGradeRecorder(aSakaiProjectDatabase);
		return recorder;
	}

	@Override
	// This should not be called
	public FinalGradeRecorder getOrCreateFeatureGradeRecorder() {
		if (recorder == null)
			recorder = createFeatureGradeRecorder(ProjectDatabaseFactory.getOrCreateProjectDatabase());
		return recorder;
	}
}
