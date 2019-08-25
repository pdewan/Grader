package grader.spreadsheet.csv;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import framework.grading.testing.CheckResult;
import framework.navigation.StudentFolder;
import grader.assignment.AnAssignmenDataFolder;
import grader.assignment.GradingFeature;
import grader.assignment.GradingFeatureList;
import grader.basics.file.FileProxy;
import grader.basics.file.filesystem.AFileSystemFileProxy;
import grader.basics.junit.TestCaseResult;
import grader.sakai.project.ProjectStepperFactory;
import grader.sakai.project.SakaiProject;
import grader.sakai.project.SakaiProjectDatabase;
import grader.spreadsheet.FeatureGradeRecorder;
import grader.trace.studentHistory.StudentHistoryFileCreated;
import grader.trace.studentHistory.StudentHistoryManagerCreated;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnAllStudentsHistoryManager implements AllStudentsHistoryManager, FeatureGradeRecorder{
	SakaiProjectDatabase projectDataBase;
	String historyFolderName;
	public static final String STUDENT_HISTORY_FOLDER = "StudentHistory";

	Map<String, IndividualStudentHistoryManager> onyenToSpreadsheet = new HashMap();
	public AnAllStudentsHistoryManager(SakaiProjectDatabase aProjectDatabase) {
		projectDataBase = aProjectDatabase;
		historyFolderName = projectDataBase.getAssignmentDataFolder().getMixedCaseAbsoluteName() +
				"/" + STUDENT_HISTORY_FOLDER;
		ProjectStepperFactory.getProjectStepper().addPropertyChangeListener(this);
		ProjectStepperFactory.
			getProjectStepper().
				getGradedProjectOverview().
					getTextOverview().
						addPropertyChangeListener(this);		

	}
	
	protected IndividualStudentHistoryManager getOrCreateSpreadsheet(String anOnyen){
		File aFolder = new File (historyFolderName);
		if (!aFolder.exists()) {
			aFolder.mkdirs();
		}
		IndividualStudentHistoryManager result = onyenToSpreadsheet.get(anOnyen);
		if (result == null) {
			String aFileName = historyFolderName
					+ "/" + anOnyen 
					+ ".csv";
			File aFile = new File(aFileName);
			boolean newHistory = false;
			if (!aFile.exists()) {
				String aFeatureSpreadSheetName =  projectDataBase.
						getAssignmentDataFolder().getFeatureGradeFile().getMixedCaseAbsoluteName();
				File aFetaureSpreadsheet = new File(aFeatureSpreadSheetName);
                try {
					Files.copy(aFetaureSpreadsheet.toPath(), aFile.toPath(), REPLACE_EXISTING);
					newHistory = true;
					StudentHistoryFileCreated.newCase(aFeatureSpreadSheetName, this);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}        

			}
            FileProxy aFileProxy = new AFileSystemFileProxy(aFile);
            result = new AnIndividualStudentHistoryManager(aFileProxy, projectDataBase.getGradingFeatures());
            onyenToSpreadsheet.put(anOnyen, result); 
            StudentHistoryManagerCreated.newCase(result, aFileProxy.getMixedCaseAbsoluteName(), this);
            if (newHistory) {
            	result.resetHistory();
            }
		}
		return result;
	}
	
	public void setGrade(String aStudentName, String anOnyen, String aFeature, double aScore) {
		// this should always be get
		IndividualStudentHistoryManager aManager = getOrCreateSpreadsheet(anOnyen);
		aManager.setGrade(aStudentName, anOnyen, aFeature, aScore);
	}


	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (!"onyen".equalsIgnoreCase(evt.getPropertyName())) {
			return;
			
		}
//
		String anOnyen = (String) evt.getNewValue();
		newSession(anOnyen, null);
//		IndividualStidentHistoryManager aManager = getOrCreateSpreadsheet(anOnyen);
//		System.out.println("Property change event:" + evt);
//		aManager.addNewRow();

		
	}

	@Override
	public void setGrade(String aStudentName, String anOnyen, double aScore) {
		IndividualStudentHistoryManager aManager = getOrCreateSpreadsheet(anOnyen);
		aManager.setGrade(aStudentName, anOnyen, aScore);
		
	}

	@Override
	public double getGrade(String aStudentName, String anOnyen) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getFileName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileProxy getGradeSpreadsheet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGrade(String aStudentName, String anOnyen, String aFeature,
			double aScore, List<CheckResult> results) {
		IndividualStudentHistoryManager aManager = getOrCreateSpreadsheet(anOnyen);
		aManager.setGrade(aStudentName, anOnyen, aFeature, aScore, results);
		
	}

	@Override
	public double getGrade(String aStudentName, String anOnyen, String aFeature) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setNotes(String aStudentName, String anOnyen, String aFeature,
			String aNotes) {
		IndividualStudentHistoryManager aManager = getOrCreateSpreadsheet(anOnyen);
		aManager.setNotes(aStudentName, anOnyen, aFeature, aNotes);
		
	}

	@Override
	public String getNotes(String aStudentName, String anOnyen, String aFeature) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void newSession(String anOnyen, StudentFolder aStudentFolder) {
		IndividualStudentHistoryManager aManager = getOrCreateSpreadsheet(anOnyen);
		aManager.addNewRow();
		
	}

	@Override
	public void saveMultiplier(double gradePercentage) {
//		IndividualStidentHistoryManager aManager = getOrCreateSpreadsheet(anOnyen);
//		aManager.saveMultiplier(gradePercentage);
		
	}

	@Override
	public void saveOverallNotes(String comments) {
//		IndividualStidentHistoryManager aManager = getOrCreateSpreadsheet(anOnyen);

		
	}

	@Override
	public void setFeatureComments(String comments) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void comment(GradingFeature aGradingFeature) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFeatureResults(List<TestCaseResult> results) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGradingFeatures(GradingFeatureList newVal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GradingFeatureList getGradingFeatures() {
		// TODO Auto-generated method stub
		return null;
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
	public void setEarlyLatePoints(String aStudentName, String anOnyen,
			double aScore) {
		IndividualStudentHistoryManager aManager = getOrCreateSpreadsheet(anOnyen);
		aManager.setEarlyLatePoints(aStudentName, anOnyen, aScore);

		
	}

	@Override
	public double getEarlyLatePoints(String aStudentName, String anOnyen) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getStoredSummary() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setResultFormat(String aStudentName, String anOnyen,
			String aFeature, String aResult) {
		IndividualStudentHistoryManager aManager = getOrCreateSpreadsheet(anOnyen);
		aManager.setResultFormat(aStudentName, anOnyen, aFeature, aResult);

		
	}

	@Override
	public String getResult(String aStudentName, String anOnyen, String aFeature) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean logSaved() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void saveSourceCodeComments(String comments) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getSourcePoints(String aStudentName, String anOnyen) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setSourcePoints(String aStudentName, String anOnyen,
			double aScore) {
		IndividualStudentHistoryManager aManager = getOrCreateSpreadsheet(anOnyen);
		aManager.setSourcePoints(aStudentName, anOnyen, aScore);

		
	}

	@Override
	public void clearGrades(String anOnyen, String aStudentName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getFullName(String anOnyen) {
		// TODO Auto-generated method stub
		 throw new RuntimeException("Not implemented");
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener aListener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getManuallyGraded(String aStudentName, String anOnyen) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setFullyGraded(String aStudentName, String anOnyen, double aScore) {
		// TODO Auto-generated method stub
		
	}


}
