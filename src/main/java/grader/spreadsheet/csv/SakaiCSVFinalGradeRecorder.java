package grader.spreadsheet.csv;

import grader.spreadsheet.FinalGradeRecorder;

public interface SakaiCSVFinalGradeRecorder extends FinalGradeRecorder{
	public int size() ;
	public String getOnyen(int aRowIndex) ;
	public String getFirstName(int aRowIndex) ;
	public String getLastName(int aRowIndex) ;
	public String getFullName(int aRowIndex) ;
	public double getGrade (int aRowIndex) ;
	public String[] getStudentRow(int aRowIndex) ;
	public void createTable();
	String[] getRow(String anOnyen);
//	String getFullName(String anOnyen);

}
