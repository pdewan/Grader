package grader.spreadsheet;

import java.util.List;

import framework.grading.testing.CheckResult;
import framework.navigation.StudentFolder;
import grader.assignment.GradingFeature;
import grader.assignment.GradingFeatureList;
import grader.basics.junit.TestCaseResult;

public interface FeatureGradeRecorder extends FinalGradeRecorder{
	void setGrade(String aStudentName, String anOnyen, String aFeature, double aScore);
	void setGrade(String aStudentName, String anOnyen, String aFeature, double aScore, List<CheckResult> results);
	double getGrade(String aStudentName, String anOnyen, String aFeature);
	void setNotes(String aStudentName, String anOnyen, String aFeature, String aNotes);
	String getNotes(String aStudentName, String anOnyen, String aFeature);
	// making all feature grade recorders have the same functionality as Josh's ConglomerateRecorder
	public void newSession(final String onyen, StudentFolder aStudentFolder);
	public void saveMultiplier(double gradePercentage);
//	public void saveScore(double score);

	public void saveOverallNotes(String comments) ;
	
	// these two should be combined
	 public void setFeatureComments(String comments) ;
		public void comment(GradingFeature aGradingFeature);


	public void setFeatureResults(List<TestCaseResult> results);
	public void setGradingFeatures(GradingFeatureList newVal);
	public GradingFeatureList  getGradingFeatures();

	void finish();
	String computeSummary();
	
	void setEarlyLatePoints(String aStudentName, String anOnyen, double aScore);
	double getEarlyLatePoints(String aStudentName, String anOnyen);
	String getStoredSummary();
	void setResultFormat(String aStudentName, String anOnyen, String aFeature,
			String aResult);
	String getResult(String aStudentName, String anOnyen, String aFeature);
//	boolean logSaved(String aUserId);
	boolean logSaved();
	void saveSourceCodeComments(String comments);
	double getSourcePoints(String aStudentName, String anOnyen);
	void setSourcePoints(String aStudentName, String anOnyen, double aScore);
	double getManuallyGraded(String aStudentName, String anOnyen);
	void setFullyGraded(String aStudentName, String anOnyen, double aScore);
	void clearGrades(String anOnyen, String aStudentName);
	final double TRUE_VALUE = 1.0;
//	final double FALSE_VALUE = 0.0;		
//	void setGradingFeatures(List<GradingFeature> newVal);
	
}
