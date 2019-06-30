package grader.spreadsheet.csv;

import grader.spreadsheet.FinalGradeRecorder;
import util.models.PropertyListenerRegisterer;

public interface SakaiCSVFinalGradeRecorder extends FinalGradeRecorder{
	public int size() ;
	public String getOnyen(int aRowIndex) ;
	public String getFirstName(int aRowIndex) ;
	public String getLastName(int aRowIndex) ;
	public double getGrade (int aRowIndex) ;
	public String[] getStudentRow(int aRowIndex) ;
	public void createTable();
	String[] getRow(String anOnyen);
	public String getFullName(int aRowIndex) ;

//	String getFullName(String anOnyen);

}
