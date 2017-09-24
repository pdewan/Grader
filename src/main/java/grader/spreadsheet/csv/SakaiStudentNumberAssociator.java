package grader.spreadsheet.csv;

import grader.file.FileProxy;
import grader.spreadsheet.FinalGradeRecorder;

public interface SakaiStudentNumberAssociator {
	
	String getFileName();
	
	void setNumber(String anOnyen, double aNumber);

	double getNumber(String anOnyen);

	void setNumber(String anOnyen, String aStudentName, double aNumber);

}
