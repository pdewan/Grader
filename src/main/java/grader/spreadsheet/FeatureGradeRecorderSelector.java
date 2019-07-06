package grader.spreadsheet;

import grader.sakai.project.SakaiProjectDatabase;
import grader.spreadsheet.csv.AFeatureGradeRecorderFactory;

public class FeatureGradeRecorderSelector {
	static FeatureGradeRecorderFactory factory = new AFeatureGradeRecorderFactory();

	public static FeatureGradeRecorderFactory getFactory() {
		return factory;
	}

	public static void setFactory( FeatureGradeRecorderFactory factory) {
		FeatureGradeRecorderSelector.factory = factory;
	}
	
	public static FeatureGradeRecorder createFeatureGradeRecorder(SakaiProjectDatabase aSakaiProjectDatabase) {
		return factory.createFeatureGradeRecorder(aSakaiProjectDatabase);
	}

	
}
