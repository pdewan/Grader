package gradingTools;

import grader.spreadsheet.csv.ASakaiCSVFinalGradeManager;
import grader.spreadsheet.csv.SakaiCSVFinalGradeRecorder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import scala.collection.parallel.immutable.HashMapCombiner.CreateGroupedTrie;
import util.misc.Common;

public class GradeScopeFileGenerator {
	public static final String DEFAULT_GRADES_FILE = "grades.csv";
	static String[] emptyStringArray = {};

	public static String toGradeScopeFileName(String aGradesFileName) {
		int aDotIndex = aGradesFileName.indexOf(".");
		String aBaseFileName = aGradesFileName.substring(0, aDotIndex);
		return aBaseFileName + "GradeScope" + ".csv";
	}

	public static String generateGradescopeText(String aFileName,
			String[] aSubstitutions) {
		Map<String, String> anOnyenToEmail = new HashMap();
		for (String aSubstitution : aSubstitutions) {
			String[] anOnyenAndEmail = aSubstitution.split(":");
			anOnyenToEmail.put(anOnyenAndEmail[0], anOnyenAndEmail[1]);
		}
		StringBuilder aStringBuilder = new StringBuilder();
		SakaiCSVFinalGradeRecorder aFinalGradeRecorder = new ASakaiCSVFinalGradeManager(
				aFileName);
		aFinalGradeRecorder.createTable();
		aStringBuilder.append("Full Name,");
		aStringBuilder.append("Email,");
		aStringBuilder.append("Onyen\n");
		for (int i = 0; i < aFinalGradeRecorder.size(); i++) {
			aStringBuilder.append(aFinalGradeRecorder.getFullName(i));
			String anOnyen = aFinalGradeRecorder.getOnyen(i);
			String anEmail = anOnyenToEmail.get(anOnyen);
			if (anEmail == null)
				anEmail = anOnyen + "@live.unc.edu";
			aStringBuilder.append(",");
			aStringBuilder.append(anEmail);
			aStringBuilder.append(",");
			aStringBuilder.append(anOnyen);
			aStringBuilder.append("\n");
		}
		return aStringBuilder.toString();

	}

	public static void gradeBookToGradescope(String aSakaiFileName,
			String aGradescopeFileName, String[] aSubstitutions) {
		// File aSakaiFile = new File(aSakaiFileName);
		try {

			StringBuffer aSakaiString = Common.toText(aSakaiFileName);
			String aGradescopeString = gradeBookToGradescope(aSakaiString,
					aSubstitutions);
			Common.writeText(aGradescopeFileName, aGradescopeString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void gradescopeToGradeBook(String aGradescopeFileName,
			String aSakaiFileName, String[] aSubstitutions) {
		// File aSakaiFile = new File(aSakaiFileName);
		try {

			StringBuffer aGradeScopeString = Common.toText(aGradescopeFileName);
			String aGradeBookString = gradescopeToGradebook(aGradeScopeString,
					aSubstitutions);
			Common.writeText(aSakaiFileName, aGradeBookString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String gradeBookToGradescope(StringBuffer aSakaiString,
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
		aGradescopeString.append("Onyen,");
		aGradescopeString.append("PID\n");
		for (int aRowNum = 1; aRowNum < anInputLines.length; aRowNum++) {
			String[] aRow = anInputLines[aRowNum].split(",");
			if (aRow.length < 4) {
				System.out.println("Ignoring row " + aRow);
				continue;
			}
			String anOnyen = aRow[0];
			String aLastName = aRow[1];
			String aFirstName = aRow[2];
			String aPID = aRow[3];
			String aFullName = aFirstName + " " + aLastName;
			aGradescopeString.append(aFullName);
			String anEmail = anOnyenToEmail.get(anOnyen);
			if (anEmail == null)
				anEmail = anOnyen + "@live.unc.edu";
			aGradescopeString.append(",");
			aGradescopeString.append(anEmail);
			aGradescopeString.append(",");
			aGradescopeString.append(anOnyen);
			aGradescopeString.append(",");
			aGradescopeString.append(aPID);
			aGradescopeString.append("\n");

		}

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

	public static void writeGeneratedFile(String aFileName, String aText) {
		String aGradescopeFileName = toGradeScopeFileName(aFileName);
		try {
			Common.writeText(aGradescopeFileName, aText);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void createAndWriteGeneratedFile(String aFileName) {
		createAndWriteGeneratedFile(aFileName, emptyStringArray);
	}

	public static void createAndWriteGeneratedFile(String aFileName,
			String[] aSubstitutions) {
		String aText = generateGradescopeText(aFileName, aSubstitutions);
		writeGeneratedFile(aFileName, aText);
	}

	public static void main(String[] args) {
		createAndWriteGeneratedFile("tmp/grades.csv",
				new String[] { "ecredit:ecredit@email.unc.edu" });

	}

}
