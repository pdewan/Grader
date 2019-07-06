package grader.spreadsheet.csv;

import framework.utils.GraderSettings;
import grader.sakai.ASakaiBulkAssignmentFolder;
import grader.sakai.project.SakaiProjectDatabase;
import grader.spreadsheet.FinalGradeRecorder;
import grader.spreadsheet.FinalGradeRecorderFactory;

public class AFinalGradeRecorderFactory implements FinalGradeRecorderFactory{
	FinalGradeRecorder recorder;
//	@Override
//	public FeatureGradeRecorder createFeatureGradeRecorder(
//			SakaiProjectDatabase aSakaiProjectDatabase) {
//		return new ASakaiCSVFeatureGradeManager(aSakaiProjectDatabase);
//	}
	
	@Override
	public FinalGradeRecorder createFeatureGradeRecorder(
			SakaiProjectDatabase aSakaiProjectDatabase) {
		return new ASakaiCSVFinalGradeManager(aSakaiProjectDatabase);
	}
	@Override
	public FinalGradeRecorder getOrCreateFeatureGradeRecorder(
			SakaiProjectDatabase aSakaiProjectDatabase) {
		if (recorder == null)
			recorder = createFeatureGradeRecorder(aSakaiProjectDatabase);
		return  recorder;
	}
	@Override
	public  FinalGradeRecorder getOrCreateFeatureGradeRecorder() {
		if (recorder == null) {
			String aPath = GraderSettings.get().get("path");
			recorder = new ASakaiCSVFinalGradeManager(aPath + "/" + ASakaiBulkAssignmentFolder.GRADES_SPREADSHEET_NAME);
		}
		return recorder;

	}


}
