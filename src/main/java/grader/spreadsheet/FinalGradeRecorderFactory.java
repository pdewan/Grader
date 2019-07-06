package grader.spreadsheet;

import grader.sakai.project.SakaiProjectDatabase;

public interface FinalGradeRecorderFactory {
	FinalGradeRecorder createFeatureGradeRecorder(SakaiProjectDatabase aSakaiProjectDatabase);
	FinalGradeRecorder getOrCreateFeatureGradeRecorder(SakaiProjectDatabase aSakaiProjectDatabase);
	FinalGradeRecorder getOrCreateFeatureGradeRecorder();
}
