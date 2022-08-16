package gradingTools.gradebook;

import static gradingTools.gradebook.GradebookUtils.gradebookToMap;
import static gradingTools.gradebook.GradebookUtils.toGradebookHeader;
import static gradingTools.gradebook.GradebookUtils.toGradebookRow;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import util.misc.Common;

public class GradebookGradescopeConverter {
	public static void gradebookToGradescope(String aSakaiFileName, String aGradescopeFileName,
			String[] aSubstitutions) {
		// File aSakaiFile = new File(aSakaiFileName);
		try {

			StringBuffer aSakaiString = Common.toText(aSakaiFileName);
			String aGradescopeString = gradebookToGradescope(aSakaiString, aSubstitutions);
			File aFile = new File(aGradescopeFileName);

//				aFile.createNewFile();

			Common.writeText(aGradescopeFileName, aGradescopeString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String gradebookToGradescope(StringBuffer aSakaiString, String[] aSubstitutions) {
		Map<String, String> anOnyenToEmail = new HashMap();
		for (String aSubstitution : aSubstitutions) {
			String[] anOnyenAndEmail = aSubstitution.split(":");
			anOnyenToEmail.put(anOnyenAndEmail[0], anOnyenAndEmail[1]);
		}
		String aInputLinesWithoutQuotes = aSakaiString.toString().replaceAll("\"", "");
		String[] anInputLines = aInputLinesWithoutQuotes.toString().split("\n");
		StringBuilder aGradescopeString = new StringBuilder(anInputLines.length);
		aGradescopeString.append("Full Name,");
		aGradescopeString.append("Email,");
		aGradescopeString.append("SID,");
		aGradescopeString.append("PID\n");
		Map<String, GradebookEntry> anOnyenToGradebookEntry = gradebookToMap(aSakaiString);
		for (String anOnyen : anOnyenToGradebookEntry.keySet()) {
			GradebookEntry aGradebookEntry = anOnyenToGradebookEntry.get(anOnyen);

			String aFullName = aGradebookEntry.getFirstName() + " " + aGradebookEntry.getLastName();
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
		return aGradescopeString.toString();
	}

	public static void gradescopeToGradebook(String aGradescopeFileName, String aSakaiFileName, String[] aSubstitutions,
			String aGradeColumnName) {
		// File aSakaiFile = new File(aSakaiFileName);
		try {

			StringBuffer aGradeScopeString = Common.toText(aGradescopeFileName);
			String aGradebookString = gradescopeToGradebook(aGradeScopeString, aSubstitutions, aGradeColumnName);
			Common.writeText(aSakaiFileName, aGradebookString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void gradescopeToGradebook(String aFolder, String[] anAssignmentNames, String aSakaiInputFileName,
			String[] aSubstitutions) {
		for (String anAssignmentName : anAssignmentNames) {
			String aGradescopeFileName = anAssignmentName + "_scores.csv";
			String aSakaiFileName = "GradeBook_" + aGradescopeFileName;
			String aGradeColumnName = anAssignmentName;
			gradescopeToGradebook(aFolder + aGradescopeFileName, aFolder + aSakaiFileName,
					aFolder + aSakaiInputFileName, aSubstitutions, aGradeColumnName);
		}
	}

	public static void writeText(String aFileName, String aText) throws IOException {

		BufferedWriter writer;

		writer = new BufferedWriter(new FileWriter(aFileName));

		writer.write(aText);

		writer.close();

	}

	public static void gradescopeToGradebook(String aGradescopeFileName, String aSakaiFileName, String aSakaiInputFile,
			String[] aSubstitutions, String aGradeColumnName) {
		// File aSakaiFile = new File(aSakaiFileName);
		try {

			StringBuffer aGradeScopeString = Common.toText(aGradescopeFileName);
			StringBuffer aGradebookInputString = Common.toText(aSakaiInputFile);
			String aGradebookString = gradescopeToGradebook(aGradeScopeString, aGradebookInputString, aSubstitutions,
					aGradeColumnName);
//			Common.writeText(aSakaiFileName, aGradebookString);
			writeText(aSakaiFileName, aGradebookString);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void gradescopeToGradebook(String aGradescopeFileName, String aSakaiFileName, String aSakaiInputFile,
			String[] aSubstitutions, String aGradeColumnName, boolean isAssignment) {
		// File aSakaiFile = new File(aSakaiFileName);
		try {

			StringBuffer aGradeScopeString = Common.toText(aGradescopeFileName);
			StringBuffer aGradebookInputString = Common.toText(aSakaiInputFile);
			String aGradebookString = gradescopeToGradebook(aGradeScopeString, aGradebookInputString, aSubstitutions,
					aGradeColumnName, isAssignment);
			Common.writeText(aSakaiFileName, aGradebookString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String gradescopeToGradebook(StringBuffer aGradescopeString, String[] aSubstitutions,
			String aGradeColumnName) {
		Map<String, String> anOnyenToEmail = new HashMap();
		for (String aSubstitution : aSubstitutions) {
			String[] anOnyenAndEmail = aSubstitution.split(":");
			anOnyenToEmail.put(anOnyenAndEmail[1], anOnyenAndEmail[0]);
		}
		String aInputLinesWithoutQuotes = aGradescopeString.toString().replaceAll("\"", "");
		String[] anInputLines = aInputLinesWithoutQuotes.toString().split("\n");
		StringBuilder aGradebookString = new StringBuilder(anInputLines.length);

		aGradebookString.append(toGradebookHeader(aGradeColumnName));

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

			String[] aNames = aFullName.split(" ");
			String aFirstName = aNames[0];
			String aLastName = aNames[1];
			String aGradebookFullName = "\"" + aLastName + "," + aFirstName + "\"";

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

	public static String emailToOnyen(String anEmail, Map<String, String> anEmailToOnyen) {
		String anOnyen = anEmailToOnyen.get(anEmail); // anOnyen is
		if (anOnyen == null) {

			String[] anEmailComponents = anEmail.split("@");
			anOnyen = anEmailComponents[0];
		}
		return anOnyen;
	}

	public static GradebookEntry getGradebookEntryFromOnyen(String anOnyen,
			Map<String, GradebookEntry> anOnyenToGradebook, String[] aRow, Map<String, String> anEmailToOnyen) {
		try {
		
		if (Character.isDigit(anOnyen.charAt(0))) {
			String anEmail = aRow[3];
			anOnyen = emailToOnyen(anEmail, anEmailToOnyen);
//			 anOnyen = anEmailToOnyen.get(anEmail); // anOnyen is
//			 if (anOnyen == null) {
//
//			String[] anEmailComponents = anEmail.split("@");
//			anOnyen = anEmailComponents[0];
//			 }
		} else if (anOnyen.contains("@")) {
			anOnyen = emailToOnyen(anOnyen, anEmailToOnyen);
		}
		return anOnyenToGradebook.get(anOnyen);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static String gradescopeToGradebook(StringBuffer aGradescopeString, StringBuffer aGradebookTemplate,
			String[] aSubstitutions, String aGradeColumnName) {
		Map<String, String> anEmailToOnyen = new HashMap();
		for (String aSubstitution : aSubstitutions) {
			String[] anOnyenAndEmail = aSubstitution.split(":");
			anEmailToOnyen.put(anOnyenAndEmail[1], anOnyenAndEmail[0]);
		}
		String aInputLinesWithoutQuotes = aGradescopeString.toString().replaceAll("\"", "");
		String[] anInputLines = aInputLinesWithoutQuotes.toString().split("\n");
		if (anInputLines.length <= 1) {
			System.err.println("No input lines in " + anInputLines);
		}
		StringBuilder aGradebookString = new StringBuilder(anInputLines.length);
//		aGradebookString.append("Student ID,");
//		aGradebookString.append("Student Name,");
//		aGradebookString.append("PID,");
//		aGradebookString.append("Grade\n");
		aGradebookString.append(toGradebookHeader(aGradeColumnName));
		Map<String, GradebookEntry> anOnyenToGradebook = gradebookToMap(aGradebookTemplate);
		Map<String, GradebookEntry> aPIDToGradebook = GradebookUtils.gradebookToPIDMap(aGradebookTemplate);

		for (int aRowNum = 1; aRowNum < anInputLines.length; aRowNum++) {
			String[] aRow = anInputLines[aRowNum].split(",");
			if (aRow.length < 4) {
				System.out.println("Ignoring row " + aRow);
				continue;
			}

//			String aFullName = aRow[0];
//			String anOnyen = aRow[1];
			String aPID = aRow[1];
			String anOnyen = aRow[2];
			if (aPID.isEmpty()) {
				continue;
			}
			try {
				GradebookEntry aGradebookEntry = null;
				if (aPID != null &&
						aPID.length() > 0) {
					if (Character.isDigit(aPID.charAt(0))) {
					aGradebookEntry = aPIDToGradebook.get(aPID); 
					} else {
						aGradebookEntry = anOnyenToGradebook.get(aPID); 
					}
				}
				if (aGradebookEntry == null) {
					aGradebookEntry = getGradebookEntryFromOnyen(anOnyen, anOnyenToGradebook, aRow, anEmailToOnyen);
				}

//			try {
//			if (Character.isDigit(anOnyen.charAt(0))) {
//				String anEmail = aRow[3];
//				anOnyen = emailToOnyen(anEmail, anEmailToOnyen);
////				 anOnyen = anEmailToOnyen.get(anEmail); // anOnyen is
////				 if (anOnyen == null) {
////
////				String[] anEmailComponents = anEmail.split("@");
////				anOnyen = anEmailComponents[0];
////				 }
//			} else if (anOnyen.contains("@")) {
//				anOnyen = emailToOnyen(anOnyen, anEmailToOnyen);
//			}
//			} catch (Exception e) {
//				e.printStackTrace();
//				continue;
//			}
//
//			GradebookEntry aGradebookEntry = anOnyenToGradebook.get(anOnyen);
				if (aGradebookEntry == null) {
					System.out.println("did not find in Gradebook:" + anOnyen + " " + aPID);
					continue;
				}
//			String anEmail = aRow[2];
//			String aGrade = aRow[3];
				
//				String aGrade = aRow[5];
				String aGrade = aRow[4];


				if (aGrade.isEmpty()) {
					aGrade = "0";
				}
				String aGradebookRow = toGradebookRow(aGradebookEntry, aGrade);
				aGradebookString.append(aGradebookRow);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		return aGradebookString.toString();
	}

	public static String gradescopeToGradebook(StringBuffer aGradescopeString, StringBuffer aGradebookTemplate,
			String[] aSubstitutions, String aGradeColumnName, boolean isAssignment) {
		Map<String, String> anOnyenToEmail = new HashMap();
		for (String aSubstitution : aSubstitutions) {
			String[] anOnyenAndEmail = aSubstitution.split(":");
			anOnyenToEmail.put(anOnyenAndEmail[1], anOnyenAndEmail[0]);
		}
		String aInputLinesWithoutQuotes = aGradescopeString.toString().replaceAll("\"", "");
		String[] anInputLines = aInputLinesWithoutQuotes.toString().split("\n");
		StringBuilder aGradebookString = new StringBuilder(anInputLines.length);
//		aGradebookString.append("Student ID,");
//		aGradebookString.append("Student Name,");
//		aGradebookString.append("PID,");
//		aGradebookString.append("Grade\n");
		if (isAssignment) {
			aGradebookString.append(GradebookUtils.toGradebookAssignmentHeader(aGradeColumnName));

		} else {
			aGradebookString.append(toGradebookHeader(aGradeColumnName));
		}
		Map<String, GradebookEntry> anOnyenToGradebook = gradebookToMap(aGradebookTemplate);
//		Map<String, GradebookEntry> aPIDToGradebook = gradebookToPIDMap(aGradebookTemplate);

		for (int aRowNum = 1; aRowNum < anInputLines.length; aRowNum++) {
			String[] aRow = anInputLines[aRowNum].split(",");
			if (aRow.length < 4) {
				System.out.println("Ignoring row " + aRow);
				continue;
			}
//			String aFullName = aRow[0];
			String anOnyen = aRow[1];
			if (Character.isDigit(anOnyen.charAt(0))) {
				anOnyen = aRow[2];
			}
			GradebookEntry aGradebookEntry = anOnyenToGradebook.get(anOnyen);
			if (aGradebookEntry == null) {
				System.out.println("did not find in Gradebook:" + anOnyen);
				continue;
			}
//			String anEmail = aRow[2];
			String aGrade = aRow[3];
			if (aGrade.isEmpty()) {
				aGrade = "0";
			}
			// do not override existing grade in sakai and can always zero it
			if (aGrade.equals("0"))
				break;

			String aGradebookRow = null;
			if (isAssignment) {
				aGradebookRow = GradebookUtils.toGradebookAssignmentRow(aGradebookEntry, aGrade);
			} else {

				aGradebookRow = toGradebookRow(aGradebookEntry, aGrade);
			}
			aGradebookString.append(aGradebookRow);
		}
		return aGradebookString.toString();
	}
}
