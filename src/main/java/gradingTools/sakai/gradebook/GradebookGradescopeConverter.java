package gradingTools.sakai.gradebook;

import static gradingTools.sakai.gradebook.GradebookUtils.gradebookToMap;
import static gradingTools.sakai.gradebook.GradebookUtils.toGradebookHeader;
import static gradingTools.sakai.gradebook.GradebookUtils.toGradebookRow;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import util.misc.Common;
public class GradebookGradescopeConverter {
	public static void gradebookToGradescope(String aSakaiFileName,
			String aGradescopeFileName, String[] aSubstitutions) {
		// File aSakaiFile = new File(aSakaiFileName);
		try {

			StringBuffer aSakaiString = Common.toText(aSakaiFileName);
			String aGradescopeString = gradebookToGradescope(aSakaiString,
					aSubstitutions);
			Common.writeText(aGradescopeFileName, aGradescopeString);
		} catch (IOException e) {
			e.printStackTrace();
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
		return aGradescopeString.toString();
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

		aGradebookString.append(toGradebookHeader());

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
//		aGradebookString.append("Student ID,");
//		aGradebookString.append("Student Name,");
//		aGradebookString.append("PID,");
//		aGradebookString.append("Grade\n");
		aGradebookString.append(toGradebookHeader());
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
			String aGradebookRow = toGradebookRow(aGradebookEntry, aGrade);
			aGradebookString.append(aGradebookRow);
		}
		return aGradebookString.toString();
	}
}
