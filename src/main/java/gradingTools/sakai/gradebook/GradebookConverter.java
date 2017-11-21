package gradingTools.sakai.gradebook;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import util.misc.Common;

public class GradebookConverter {
	public static void gradebookToGradescope(String aSakaiFileName,
			String aGradescopeFileName, String[] aSubstitutions) {
		// File aSakaiFile = new File(aSakaiFileName);
		try {

			StringBuffer aSakaiString = Common.toText(aSakaiFileName);
			String aGradescopeString = gradebookToGradescope(aSakaiString,
					aSubstitutions);
			Common.writeText(aGradescopeFileName, aGradescopeString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void gradescopeToGradebook(String aGradescopeFileName,
			String aSakaiFileName, String[] aSubstitutions) {
		// File aSakaiFile = new File(aSakaiFileName);
		try {

			StringBuffer aGradeScopeString = Common.toText(aGradescopeFileName);
			String aGradebookString = gradescopeToGradebook(aGradeScopeString,
					aSubstitutions);
			Common.writeText(aSakaiFileName, aGradebookString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static void gradescopeToGradebook(String aGradescopeFileName,
			String aSakaiFileName, String aSakaiInputFile, String[] aSubstitutions) {
		// File aSakaiFile = new File(aSakaiFileName);
		try {

			StringBuffer aGradeScopeString = Common.toText(aGradescopeFileName);
			StringBuffer aGradebookInputString = Common.toText(aSakaiInputFile);
			String aGradebookString = gradescopeToGradebook(aGradeScopeString, aGradebookInputString,
					aSubstitutions);
			Common.writeText(aSakaiFileName, aGradebookString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
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

	public static String gradebookToGradescope(StringBuffer aSakaiString,
			String[] aSubstitutions) {
		Map<String, String> anOnyenToEmail = new HashMap();
		for (String aSubstitution : aSubstitutions) {
			String[] anOnyenAndEmail = aSubstitution.split(":");
			anOnyenToEmail.put(anOnyenAndEmail[0], anOnyenAndEmail[1]);
		}
		
		String aInputLinesWithoutQuotes = aSakaiString.toString().replaceAll(
				"\"", "");
		String[] anInputLines = aInputLinesWithoutQuotes.toString().split("\n");
		StringBuilder aGradescopeString = new StringBuilder(anInputLines.length);
		aGradescopeString.append("Full Name,");
		aGradescopeString.append("Email,");
		aGradescopeString.append("SID,");
		aGradescopeString.append("PID\n");
		Map<String, GradebookEntry> anOnyenToGradebookEntry = gradebookToMap(aSakaiString);
		for (String anOnyen:anOnyenToGradebookEntry.keySet()) {
			GradebookEntry aGradebookEntry = anOnyenToGradebookEntry.get(anOnyen);
	
			String aFullName = aGradebookEntry.getFirstName() + " " + 
			aGradebookEntry.getLastName();
			aGradescopeString.append(aFullName);
			String anEmail = anOnyenToEmail.get(anOnyen);
			if (anEmail == null)
				anEmail = anOnyen + "@live.unc.edu";
			aGradescopeString.append(",");
			aGradescopeString.append(anEmail);
			aGradescopeString.append(",");
			aGradescopeString.append(anOnyen);
			aGradescopeString.append(",");
			aGradescopeString.append(aGradebookEntry.getPid());
			aGradescopeString.append("\n");
			
		}
//		for (int aRowNum = 1; aRowNum < anInputLines.length; aRowNum++) {
//			String[] aRow = anInputLines[aRowNum].split(",");
//			if (aRow.length < 4) {
//				System.out.println("Ignoring row " + aRow);
//				continue;
//			}
//			String anOnyen = aRow[0];
//			String aLastName = aRow[1];
//			String aFirstName = aRow[2];
//			String aPID = aRow[3];
//			String aFullName = aFirstName + " " + aLastName;
//			aGradescopeString.append(aFullName);
//			String anEmail = anOnyenToEmail.get(anOnyen);
//			if (anEmail == null)
//				anEmail = anOnyen + "@live.unc.edu";
//			aGradescopeString.append(",");
//			aGradescopeString.append(anEmail);
//			aGradescopeString.append(",");
//			aGradescopeString.append(anOnyen);
//			aGradescopeString.append(",");
//			aGradescopeString.append(aPID);
//			aGradescopeString.append("\n");
//
//		}

		return aGradescopeString.toString();

	}
	
	
	

	public static String gradescopeToGradebook(StringBuffer aGradescopeString,
			String[] aSubstitutions) {
		Map<String, String> anOnyenToEmail = new HashMap();
		for (String aSubstitution : aSubstitutions) {
			String[] anOnyenAndEmail = aSubstitution.split(":");
			anOnyenToEmail.put(anOnyenAndEmail[1], anOnyenAndEmail[0]);
		}
		String aInputLinesWithoutQuotes = aGradescopeString.toString()
				.replaceAll("\"", "");
		String[] anInputLines = aInputLinesWithoutQuotes.toString().split("\n");
		StringBuilder aGradebookString = new StringBuilder(anInputLines.length);
		aGradebookString.append("Student ID,");
		aGradebookString.append("Student Name,");
		aGradebookString.append("PID,");
		aGradebookString.append("Grade\n");

		for (int aRowNum = 1; aRowNum < anInputLines.length; aRowNum++) {
			String[] aRow = anInputLines[aRowNum].split(",");
			if (aRow.length < 4) {
				System.out.println("Ignoring row " + aRow);
				continue;
			}
			String aFullName = aRow[0];
			String anOnyen = aRow[1];
			String anEmail = aRow[2];
			String aGrade = aRow[3];
			String aPID = "";
			// if (aRow.length > 4) {
			// aPID = aRow[3];
			// }
			String[] aNames = aFullName.split(" ");
			String aFirstName = aNames[0];
			String aLastName = aNames[1];
			String aGradebookFullName = "\"" + aLastName + "," + aFirstName
					+ "\"";

			aGradebookString.append(anOnyen);
			aGradebookString.append(",");
			aGradebookString.append(aGradebookFullName);
			aGradebookString.append(",");
			aGradebookString.append(aPID);
			aGradebookString.append(",");
			aGradebookString.append(aGrade);
			aGradebookString.append("\n");

		}

		return aGradebookString.toString();

	}
	public static String gradescopeToGradebook(StringBuffer aGradescopeString,
			StringBuffer aGradebookTemplate,
			String[] aSubstitutions) {
		Map<String, String> anOnyenToEmail = new HashMap();
		for (String aSubstitution : aSubstitutions) {
			String[] anOnyenAndEmail = aSubstitution.split(":");
			anOnyenToEmail.put(anOnyenAndEmail[1], anOnyenAndEmail[0]);
		}
		String aInputLinesWithoutQuotes = aGradescopeString.toString()
				.replaceAll("\"", "");
		String[] anInputLines = aInputLinesWithoutQuotes.toString().split("\n");
		StringBuilder aGradebookString = new StringBuilder(anInputLines.length);
		aGradebookString.append("Student ID,");
		aGradebookString.append("Student Name,");
		aGradebookString.append("PID,");
		aGradebookString.append("Grade\n");
		Map<String, GradebookEntry> anOnyenToGradebook = gradebookToMap(aGradebookTemplate);

		for (int aRowNum = 1; aRowNum < anInputLines.length; aRowNum++) {
			String[] aRow = anInputLines[aRowNum].split(",");
			if (aRow.length < 4) {
				System.out.println("Ignoring row " + aRow);
				continue;
			}
//			String aFullName = aRow[0];
			String anOnyen = aRow[1];
			GradebookEntry aGradebookEntry = anOnyenToGradebook.get(anOnyen);
			if (aGradebookEntry == null) {
				System.out.println("did not find in Gradebook:" + anOnyen);
				continue;
			}
			
//			String anEmail = aRow[2];
			String aGrade = aRow[3];
//			String aPID = "";
			// if (aRow.length > 4) {
			// aPID = aRow[3];
			// }
//			String[] aNames = aFullName.split(" ");
//			String aFirstName = aNames[0];
//			String aLastName = aNames[1];
			String aGradebookFullName = 
					"\"" + aGradebookEntry.getLastName() + "," + 
			aGradebookEntry.getFirstName()
					+ "\"";

			aGradebookString.append(anOnyen);
			aGradebookString.append(",");
			aGradebookString.append(aGradebookFullName);
			aGradebookString.append(",");
			aGradebookString.append(aGradebookEntry.getPid());
			aGradebookString.append(",");
			aGradebookString.append(aGrade);
			aGradebookString.append("\n");

		}

		return aGradebookString.toString();

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
	public static GradebookEntry toGradebookEntry(String aRowString) {
		String[] aRow = aRowString.split(",");
		if (aRow.length < 4) {
			System.out.println("Ignoring row " + aRow);
			return null;
		}
		String anOnyen = aRow[0];
		String aLastName = aRow[1];
		String aFirstName = aRow[2];
		String aPID = aRow[3];
		return new AGradebookEntry(anOnyen, aFirstName, aLastName, aPID);
		
	}
	public static String toGradebookHeader() {
		StringBuffer aGradebookString = new StringBuffer();

		aGradebookString.append("Student ID,");
		aGradebookString.append("Student Name,");
		aGradebookString.append("PID,");
		aGradebookString.append("Grade\n");
		return aGradebookString.toString();
	}
}
