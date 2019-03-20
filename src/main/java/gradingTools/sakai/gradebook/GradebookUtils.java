package gradingTools.sakai.gradebook;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import util.misc.Common;

public class GradebookUtils {	
	public static final short ONYEN_COLUMN = 0;
	public static final short LAST_NAME_COLUMN = 2;
	public static final short FIRST_NAME_COLUMN = 3;
	public static final short PID_COLUMN = 1;
	public static Map<String, GradebookEntry> gradebookToMap(StringBuffer aSakaiString) {
		String aInputLinesWithoutQuotes = aSakaiString.toString().replaceAll(
				"\"", "");
		Map<String, GradebookEntry> retVal = new HashMap<String, GradebookEntry>();
		String[] anInputLines = aInputLinesWithoutQuotes.toString().split("\n");
		for (int aRowNum = 1; aRowNum < anInputLines.length; aRowNum++) {
			GradebookEntry aGradebookEntry = toGradebookEntry(anInputLines[aRowNum]);
			if (aGradebookEntry == null) {
				continue;
			}
			retVal.put(aGradebookEntry.getStudentID(), aGradebookEntry);			
		}
		return retVal;		
	}
	public static Map<String, GradebookEntry> gradebookToMap(File aFile) {
		try {
			StringBuffer aGradebookString = Common.toStringBuffer(aFile);
			return gradebookToMap(aGradebookString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}		
	}	
	public static String toGradebookRow(GradebookEntry aGradebookEntry, String aGrade) {
		StringBuffer aGradebookString = new StringBuffer();
		String aGradebookFullName = "\"" + aGradebookEntry.getLastName() + "," + aGradebookEntry.getFirstName()
				+ "\"";
		aGradebookString.append(aGradebookEntry.getStudentID());
		aGradebookString.append(",");
		aGradebookString.append(aGradebookFullName);
		aGradebookString.append(",");
		aGradebookString.append(aGradebookEntry.getPid());
		aGradebookString.append(",");
		aGradebookString.append(aGrade);
		aGradebookString.append("\n");
		return aGradebookString.toString();
	}
	public static String toGradebookAssignmentRow(GradebookEntry aGradebookEntry, String aGrade) {
		StringBuffer aGradebookString = new StringBuffer();
//		String aGradebookFullName = "\"" + aGradebookEntry.getLastName() + "," + aGradebookEntry.getFirstName()
//				+ "\"";
		aGradebookString.append(aGradebookEntry.getStudentID());
		aGradebookString.append(",");
		aGradebookString.append(aGradebookEntry.getStudentID());
		aGradebookString.append(",");
		aGradebookString.append(aGradebookEntry.getLastName());
		aGradebookString.append(",");
		aGradebookString.append(aGradebookEntry.getFirstName());
		aGradebookString.append(",");
		aGradebookString.append(aGrade);
		aGradebookString.append("\n");
		return aGradebookString.toString();
	}
	
	
	public static GradebookEntry toGradebookEntry(String aRowString) {
		String[] aRow = aRowString.split(",");
		if (aRow.length < 4) {
			System.out.println("Ignoring row " + aRow);
			return null;
		}
		String anOnyen = aRow[ONYEN_COLUMN].trim();
		String aLastName = aRow[LAST_NAME_COLUMN].trim();
		String aFirstName = aRow[FIRST_NAME_COLUMN].trim();
		String aPID = aRow[PID_COLUMN].trim();
		return new AGradebookEntry(anOnyen, aFirstName, aLastName, aPID);
		
	}
	public static String toGradebookHeader(String aGradeColumnTitle) {
		StringBuffer aGradeboookHeader = new StringBuffer();

		aGradeboookHeader.append("Student ID,");
		aGradeboookHeader.append("Student Name,");
		aGradeboookHeader.append("PID,");
		if (aGradeColumnTitle == null || aGradeColumnTitle.isEmpty()){
//		     aGradeboookHeader.append("Grade\n");
		     aGradeColumnTitle = "Grade";
		}
		aGradeboookHeader.append(aGradeColumnTitle + "\n");

		return aGradeboookHeader.toString();
	}
	public static String toGradebookAssignmentHeader(String aGradeColumnTitle) {
		StringBuffer aGradeboookHeader = new StringBuffer();
		aGradeboookHeader.append (aGradeColumnTitle + ", Points,,,\n,,,,\n");
		aGradeboookHeader.append("Display ID,");
		aGradeboookHeader.append("ID,");
		aGradeboookHeader.append("Last Name,");
		aGradeboookHeader.append("Fist Name,");

		if (aGradeColumnTitle == null || aGradeColumnTitle.isEmpty()){
//		     aGradeboookHeader.append("Grade\n");
		     aGradeColumnTitle = "Grade";
		}
		aGradeboookHeader.append(aGradeColumnTitle + "\n");

		return aGradeboookHeader.toString();
	}
}
