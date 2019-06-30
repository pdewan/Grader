package grader.spreadsheet.display;

import util.annotations.Visible;

public interface SpreadsheetItemDisplay {

	String getGrade();

	void setGrade(String grade);

	String getId();

	String getName();

}