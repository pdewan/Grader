package grader.spreadsheet;

import framework.utils.GraderSettings;
import grader.sakai.project.SakaiProjectDatabase;
import grader.spreadsheet.csv.AFinalGradeRecorderFactory;

public class FinalGradeRecorderSelector {
	static FinalGradeRecorderFactory factory = new AFinalGradeRecorderFactory();

	public static FinalGradeRecorderFactory getFactory() {
		return factory;
	}

	public static void setFactory(FinalGradeRecorderFactory factory) {
		FinalGradeRecorderSelector.factory = factory;
	}
	
	
	
	public static FinalGradeRecorder createFinalGradeRecorder(SakaiProjectDatabase aSakaiProjectDatabase) {
		return factory.createFeatureGradeRecorder(aSakaiProjectDatabase);
	}
	public static FinalGradeRecorder getOrCreateFinalGradeRecorder() {
		return factory.getOrCreateFeatureGradeRecorder();
	}

	
}
