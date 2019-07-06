package grader.spreadsheet;

import grader.sakai.project.SakaiProjectDatabase;
import grader.spreadsheet.csv.AFinalGradeRecorderFactory;

public class TotalScoreRecorderSelector {
//	static FinalGradeRecorderFactory factory = new AFeatureAndFinalGradeRecorderFactory();
	static FinalGradeRecorderFactory factory = new AFinalGradeRecorderFactory();


	public static FinalGradeRecorderFactory getFactory() {
		return factory;
	}

	public static void setFactory(FinalGradeRecorderFactory factory) {
		TotalScoreRecorderSelector.factory = factory;
	}
	
	public static FinalGradeRecorder createFinalGradeRecorder(SakaiProjectDatabase aSakaiProjectDatabase) {
		return factory.createFeatureGradeRecorder(aSakaiProjectDatabase);
	}

	
}
