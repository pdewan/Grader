package grader.spreadsheet;

import java.beans.PropertyChangeListener;

import grader.file.FileProxy;
import util.models.PropertyListenerRegisterer;

public interface FinalGradeRecorder extends PropertyListenerRegisterer, PropertyChangeListener{
//	public String getFullName(int aRowIndex) ;

	void setGrade(String aStudentName, String anOnyen, double aScore);
//	public void recordGrade (Row aRow, int aColumn, double aScore);
	double getGrade(String aStudentName, String anOnyen);
//	 double getGrade (Row aRow, int aColumn);
	public String getFileName();
	public FileProxy getGradeSpreadsheet();
	String getFullName(String anOnyen);
	public static final String GRADE_PROPERTY = "Grade";
	 public static final String DEFAULT_CHAR = "";
	 public static final double  DEFAULT_VALUE = -1;




}
