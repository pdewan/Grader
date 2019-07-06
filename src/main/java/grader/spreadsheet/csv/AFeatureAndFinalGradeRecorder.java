package grader.spreadsheet.csv;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import grader.file.FileProxy;
import grader.sakai.project.SakaiProjectDatabase;
import grader.spreadsheet.FinalGradeRecorder;

public class AFeatureAndFinalGradeRecorder implements FinalGradeRecorder {
	FinalGradeRecorder finalGradeRecorder, featureGradeRecorder;
	public AFeatureAndFinalGradeRecorder(SakaiProjectDatabase aProjectDatabase) {
		finalGradeRecorder = aProjectDatabase.getGradeRecorder();
		featureGradeRecorder = aProjectDatabase.getFeatureGradeRecorder();	
	}
	@Override
	public void setGrade(String aStudentName, String anOnyen, double aScore) {
		finalGradeRecorder.setGrade(aStudentName, anOnyen, aScore);
		featureGradeRecorder.setGrade(aStudentName, anOnyen, aScore);
	}
	@Override
	public double getGrade(String aStudentName, String anOnyen) {
		return finalGradeRecorder.getGrade(aStudentName, anOnyen);
	}
	@Override
	public String getFileName() {
		return featureGradeRecorder.getFileName();
	}
	@Override
	public FileProxy getGradeSpreadsheet() {
		return featureGradeRecorder.getGradeSpreadsheet();
	}
	@Override
	public String getFullName(String anOnyen) {
		// TODO Auto-generated method stub
		return finalGradeRecorder.getFullName(anOnyen);
	}
	@Override
	public void addPropertyChangeListener(PropertyChangeListener aListener) {
		// TODO Auto-generated method stub
		featureGradeRecorder.addPropertyChangeListener(aListener);
		finalGradeRecorder.addPropertyChangeListener(aListener);
		
	}
	// children spteadsheets will take care of this
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		
	}
	

}
