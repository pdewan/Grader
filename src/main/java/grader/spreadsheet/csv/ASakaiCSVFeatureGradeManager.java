package grader.spreadsheet.csv;

import framework.grading.testing.CheckResult;
import framework.navigation.StudentFolder;
import grader.assignment.GradingFeature;
import grader.assignment.GradingFeatureList;
import grader.basics.junit.TestCaseResult;
import grader.file.FileProxy;
import grader.file.filesystem.AFileSystemFileProxy;
import grader.sakai.project.SakaiProjectDatabase;
import grader.spreadsheet.FeatureGradeRecorder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
//import bus.uigen.Message;

public class ASakaiCSVFeatureGradeManager extends ASakaiCSVFinalGradeManager implements SakaiCSVFeatureGradeManager {
//	SakaiProjectDatabase projectDatabase;
//	public static final int ONYEN_COLUMN = 0;
//	public static final int GRADE_COLUMN = 4;
//	InputStream input; // this may have to be reinitialized each time
//	OutputStream output; // may have to reinitialized and closed each time
//	FileProxy gradeSpreadsheet;
//	List<String[]>  table;
	 List<GradingFeature> gradingFeatures;
	 Map<String, Integer> featureToColumnNumber = new HashMap();
	 Map<String, Integer> resultToColumnNumber = new HashMap();
	 List<String> featureNames = new ArrayList();
	 public static final int SOURCE_POINTS_COLUMN = GRADE_COLUMN + 1;
	 public static final int EARLY_LATE_COLUMN = SOURCE_POINTS_COLUMN + 1;
	 public static final String SOURCE_POINTS_TITLE = "Source Points";
	 public static final int TOTAL_COLUMN = EARLY_LATE_COLUMN+ 1;
	 public static final String LATE_TITLE = "Early/Late";
	 public static final String TOTAL_TITLE = "Weighted Grade";
	 public static final int PRE_FEATURE_COLUMN = TOTAL_COLUMN ;

	public ASakaiCSVFeatureGradeManager(FileProxy aGradeSpreadsheet, List<GradingFeature> aGradingFeatures) {
		super(aGradeSpreadsheet);
		gradingFeatures = aGradingFeatures;
		
//		gradeSpreadsheet = aGradeSpreadsheet;		
	}
	public ASakaiCSVFeatureGradeManager(File aGradeSpreadsheet) {
		super(aGradeSpreadsheet)	;	
	}
	public ASakaiCSVFeatureGradeManager(String aFileName) {
		super(aFileName)	;
	}
	
	public ASakaiCSVFeatureGradeManager(SakaiProjectDatabase aSakaiProjectDatabase) {
		super(aSakaiProjectDatabase.getAssignmentDataFolder().getFeatureGradeFile());
		gradingFeatures = aSakaiProjectDatabase.getGradingFeatures();
		
//		gradeSpreadsheet = aGradeSpreadsheet;		
	}
	@Override
	public void readFeatureNames() {
		featureNames.clear();
		String[] aTitleRow = table.get(TITLE_ROW);
		int aNumFeatureResultColumns = aTitleRow.length - PRE_FEATURE_COLUMN - 1;
		int aFirstFeatureColumn = PRE_FEATURE_COLUMN + 1;
		int aNumFeatures = aNumFeatureResultColumns/2;
		for (int i= aFirstFeatureColumn; i < aFirstFeatureColumn + aNumFeatures; i++) {
			String aFeatureName = aTitleRow[i];
			featureNames.add(aFeatureName);
			featureToColumnNumber.put(aFeatureName, i);
			resultToColumnNumber.put(aFeatureName, i + aNumFeatures );
		}
		
	}
	
	public void createTable() {
		super.createTable();
		if (gradingFeatures == null) return;
		String[] headers = table.get(TITLE_ROW);
		if (headers.length < PRE_FEATURE_COLUMN + gradingFeatures.size() + 1) {
			
		extendTable();
		makeTitles();
		writeTable();
		}
		makeMap();
		
	}
	void makeTitles() {
		String[] titleRow = table.get(TITLE_ROW);
		titleRow[EARLY_LATE_COLUMN] = LATE_TITLE;
		titleRow[TOTAL_COLUMN] = TOTAL_TITLE;
		titleRow[SOURCE_POINTS_COLUMN] = SOURCE_POINTS_TITLE;
		for (int i = 0; i < gradingFeatures.size(); i++) {
			int featureColumn = PRE_FEATURE_COLUMN + 1 + i;
//			featureToColumnNumber.put(gradingFeatures.get(i).getFeature(), featureColumn);
			titleRow[featureColumn] = gradingFeatures.get(i).getFeatureName();
			int resultsColumn = PRE_FEATURE_COLUMN + 1 + gradingFeatures.size() + i;
			titleRow[resultsColumn] = gradingFeatures.get(i).getFeatureName();

			
		}
	}
	
	void makeMap() {
//		System.out.println ("Making map for features:" + gradingFeatures);
		for (int i = 0; i < gradingFeatures.size(); i++) {
			int featureColumn = PRE_FEATURE_COLUMN + 1 + i;
			featureToColumnNumber.put(gradingFeatures.get(i).getFeatureName(), featureColumn);
			int resultColumn = PRE_FEATURE_COLUMN + 1 + gradingFeatures.size() + i;
			resultToColumnNumber.put(gradingFeatures.get(i).getFeatureName(), resultColumn);
			
		}
	}
	
	void extendTable() {
		for (int i = 0; i < table.size(); i++) {
			table.set (i, extendedRow(table.get(i)));
		}
	}
	void extendTable(int newSize) {
		for (int i = 0; i < table.size(); i++) {
			table.set (i, extendedRow(table.get(i), newSize));
		}
	}
	String[] extendedRow(String[] anExistinRow, int newSize) {
		String[] retVal = new String[newSize];

		for (int index = 0; index < anExistinRow.length; index++) {
			retVal[index] = anExistinRow[index];
		}
		for (int index = anExistinRow.length; index < retVal.length; index++) {
//			retVal[index] = "0";
			retVal[index] = DEFAULT_CHAR;
		}
		return retVal;
	}

	String[] extendedRow(String[] anExistinRow) {
		// adding late penalty column also and results column
//		String[] retVal = new String[anExistinRow.length + 1 + 2*gradingFeatures.size()];
		String[] retVal = new String[anExistinRow.length + PRE_FEATURE_COLUMN - GRADE_COLUMN + 2*gradingFeatures.size()];

		for (int index = 0; index < anExistinRow.length; index++) {
			retVal[index] = anExistinRow[index];
		}
		for (int index = anExistinRow.length; index < retVal.length; index++) {
//			retVal[index] = "0";
			retVal[index] = DEFAULT_CHAR;
		}
		return retVal;
		
	}
	
	public double getGrade (String[] aRow, String aFeatureName) {
		return getGrade(aRow, featureToColumnNumber.get(aFeatureName));

	}
	@Override
	public String getResult(int aRowNum, String aFeature) {
		return getResult(table.get(toActualRow(aRowNum)), aFeature );
	}
	
	public String getResult (String[] aRow, String aFeatureName) {
		return getResult(aRow, resultToColumnNumber.get(aFeatureName));

	}
	
	public void recordGrade (String[] aRow, String aFeature, double aScore) {
		Integer aColumnNumber = featureToColumnNumber.get(aFeature);
		if (aColumnNumber == null) {
			System.err.println("No column number for:" + aFeature);
			System.err.println(featureToColumnNumber);
			return;
		}
//		recordGrade(aRow, featureToColumnNumber.get(aFeature), aScore);
		recordGrade(aRow, aColumnNumber, aScore);
	}
	
	public void recordResult (String[] aRow, String aFeature, String aScore) {
//		Integer aColumnNumber = featureToColumnNumber.get(aFeature);
		Integer aColumnNumber = resultToColumnNumber.get(aFeature);

		if (aColumnNumber == null) {
			System.err.println("No column number for:" + aFeature);
//			System.err.println(featureToColumnNumber);
			System.err.println(resultToColumnNumber);
			// get a stack trace so we can see on the console
//			return;
		}
//		recordResult(aRow, resultToColumnNumber.get(aFeature), aScore);
		recordResult(aRow, aColumnNumber, aScore);


	}

	@Override
	public void setGrade(String aStudentName, String anOnyen, String aFeature, double aScore) {
            try {
                maybeCreateTable();
                String[] row = getStudentRow(table, aStudentName, anOnyen);
                if (row == null) {
                    System.out.println("Cannot find row for:" + aStudentName + " " + anOnyen);
                    return;
                }

                recordGrade(row, aFeature, aScore);
                writeTable();
            } catch (Exception e) {
                    e.printStackTrace();
            }
	}
        
	@Override
	public void setGrade(String aStudentName, String anOnyen, String aFeature, double aScore, List<CheckResult> results) {
            setGrade(aStudentName, anOnyen, aFeature, aScore);
        }
        
	@Override
	public void clearGrades(String anOnyen, String aStudentName) {
    try {
			

			maybeCreateTable();
			clearStudentRow(table, aStudentName, anOnyen);
			
		
		    writeTable();



	} catch (Exception e) {
		e.printStackTrace();
		
	}
	}
	
	@Override
	public void setResultFormat(String aStudentName, String anOnyen, String aFeature,
			String aResult) {
		try {

			maybeCreateTable();
			
		    String[] row = getStudentRow(table, aStudentName, anOnyen);
		    if (row == null) {
				System.out.println("Cannot find row for:" + aStudentName + " " + anOnyen);
				return;
		    }
		    
		    recordResult(row, aFeature, aResult);
		    writeTable();



	} catch (Exception e) {
		e.printStackTrace();
		
	}
		
	}
	@Override
	public double getGrade(int aRowNum, String aFeature) {
		return getGrade(table.get(toActualRow(aRowNum)), aFeature );
	}

	@Override
	public double getGrade(String aStudentName, String anOnyen, String aFeature) {
		try {

				maybeCreateTable();
			
		   
	    String[] row = getStudentRow(table, aStudentName, anOnyen);
	    if (row == null) {
			System.out.println("Cannot find row for:" + aStudentName + " " + anOnyen);
			return -1;
	    }
	   double retVal =  getGrade(row, aFeature);	
	    return retVal;
	    
		} catch (Exception e) {
			e.printStackTrace();
			return DEFAULT_VALUE;
			
		}		
	}
	
	@Override
	public String getResult(String aStudentName, String anOnyen, String aFeature) {
		try {

				maybeCreateTable();
			
		   
	    String[] row = getStudentRow(table, aStudentName, anOnyen);
	    if (row == null) {
			System.out.println("Cannot find row for:" + aStudentName + " " + anOnyen);
			return "";
	    }
	   String retVal =  getResult(row, aFeature);	
	    return retVal;
	    
		} catch (Exception e) {
			e.printStackTrace();
			return "";
			
		}		
	}
	// called after feature scores, or maybe before and after
	public void setGrade(String aStudentName, String anOnyen, double aScore) {
		super.setGrade(aStudentName, anOnyen, aScore);
		String[] row = getStudentRow(table, aStudentName, anOnyen);
		 refreshTotalGrade(row, aStudentName, anOnyen);
//	    recordGrade(row, TOTAL_COLUMN, getGrade(aStudentName, anOnyen)*getEarlyLatePoints(aStudentName, anOnyen));

	}

	@Override
	public void setEarlyLatePoints(String aStudentName, String anOnyen,
			double aScore) {
		maybeCreateTable();
		
	    String[] row = getStudentRow(table, aStudentName, anOnyen);
	    if (row == null) {
			System.out.println("Cannot find row for:" + aStudentName + " " + anOnyen);
			return;
	    }
	    
	    recordGrade(row, EARLY_LATE_COLUMN, aScore);
//	    recordGrade(row, TOTAL_COLUMN, getGrade(aStudentName, anOnyen)*getEarlyLatePoints(aStudentName, anOnyen));
	    refreshTotalGrade(row, aStudentName, anOnyen);
	    writeTable();

		
	}
	
    public static double getTotalGrade(double featureScore, double multiplier, double sourcePoints) {
		
		double anActualPoints = (featureScore + sourcePoints) * multiplier;
		long anActualPoint10timesRounded =  Math.round(anActualPoints*10);
		double anActualPointsRounded = anActualPoint10timesRounded/10.0;
		return anActualPointsRounded;
		
//		return  Math.max(0, (Math.round(featureScore + sourcePoints) * multiplier * 10)/10);
//		return  Math.max(0, (Math.round(featureScore + sourcePoints) * multiplier * 10)/10);


	}
	
	void refreshTotalGrade(String[] row, String aStudentName, String anOnyen) {
		
		double featureScore = getGrade(aStudentName, anOnyen);
		double multiplier =  getEarlyLatePoints(aStudentName, anOnyen);
		double sourcePoints = getSourcePoints(aStudentName, anOnyen);
		
		if (multiplier == ASakaiCSVFeatureGradeManager.DEFAULT_VALUE)
			multiplier = 1;
		if (sourcePoints == ASakaiCSVFeatureGradeManager.DEFAULT_VALUE)
			sourcePoints = 0;
		double total = getTotalGrade(featureScore, multiplier, sourcePoints);

		
//	    recordGrade(row, TOTAL_COLUMN, (featureScore + sourcePoints) * multiplier);
	    recordGrade(row, TOTAL_COLUMN, total);


	}

	@Override
	public double getEarlyLatePoints(String aStudentName, String anOnyen) {
		 String[] row = getStudentRow(table, aStudentName, anOnyen);
		    if (row == null) {
				System.out.println("Cannot find row for:" + aStudentName + " " + anOnyen);
				return -1;
		    }
		   return getGrade(row, EARLY_LATE_COLUMN);

		
	}
	@Override
	public void setSourcePoints(String aStudentName, String anOnyen,
			double aScore) {
		maybeCreateTable();
		
	    String[] row = getStudentRow(table, aStudentName, anOnyen);
	    if (row == null) {
			System.out.println("Cannot find row for:" + aStudentName + " " + anOnyen);
			return;
	    }
	    
	    recordGrade(row, SOURCE_POINTS_COLUMN, aScore);
	    refreshTotalGrade(row, aStudentName, anOnyen);
	   
	    writeTable();

		
	}

	@Override
	public double getSourcePoints(String aStudentName, String anOnyen) {
		 String[] row = getStudentRow(table, aStudentName, anOnyen);
		    if (row == null) {
				System.out.println("Cannot find row for:" + aStudentName + " " + anOnyen);
				return -1;
		    }
		   return getGrade(row, SOURCE_POINTS_COLUMN);

		
	}

	@Override
	public void setNotes(String aStudentName, String anOnyen, String aFeature,
			String aNotes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getNotes(String aStudentName, String anOnyen, String aFeature) {
		// TODO Auto-generated method stub
		return "";
	}
	String onyen;
	@Override
	public void newSession(String anOnyen, StudentFolder aStudentFolder) {
		onyen = anOnyen;
		
	}

	@Override
	public void saveMultiplier(double gradePercentage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveOverallNotes(String comments) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFeatureComments(String comments) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFeatureResults(List<TestCaseResult> results) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String computeSummary() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void comment(GradingFeature aGradingFeature) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGradingFeatures(GradingFeatureList newVal) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public List<String> getFeatureNames() {
		return featureNames;
	}

	@Override
	public GradingFeatureList getGradingFeatures() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStoredSummary() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	// should really check that all grading features are non null for current onyen
	public boolean logSaved() {
		return true;
	}

	@Override
	public void saveSourceCodeComments(String comments) {
		// TODO Auto-generated method stub
		
	}

	
	

	

}
