package grader.trace.file.assignment;

import grader.trace.file.SerializableFileInfo;

public class FeatureGradeFileCleared extends SerializableFileInfo {

	public FeatureGradeFileCleared(String aMessage, String aFileName,
			Object aFinder) {
		super(aMessage, aFileName, aFinder);
	}
	
	public static FeatureGradeFileCleared newCase(String aFileName,
			Object aFinder) {
		String aMessage =  "feature grade file cleared: " + aFileName;
		FeatureGradeFileCleared retVal = new FeatureGradeFileCleared(aMessage, aFileName, aFinder);
		retVal.announce();
		return retVal;
	}

}