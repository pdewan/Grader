package gradingTools;

import grader.spreadsheet.csv.ASakaiCSVFinalGradeManager;
import grader.spreadsheet.csv.SakaiCSVFinalGradeRecorder;

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
	public static String generateGradescopeText(String aFileName, String[] aSubstitutions) {
		Map<String, String> anOnyenToEmail = new HashMap();
		for (String aSubstitution:aSubstitutions) {
			String[] anOnyenAndEmail = aSubstitution.split(":");
			anOnyenToEmail.put(anOnyenAndEmail[0], anOnyenAndEmail[1]);
		}
		StringBuilder aStringBuilder = new StringBuilder();
		SakaiCSVFinalGradeRecorder aFinalGradeRecorder = new ASakaiCSVFinalGradeManager(aFileName);
		aFinalGradeRecorder.createTable();
		aStringBuilder.append("Full Name,");
		aStringBuilder.append("Email,");
		aStringBuilder.append("Onyen\n");
		for (int i=0; i < aFinalGradeRecorder.size(); i++) {
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

	public static void createAndWriteGeneratedFile(String aFileName, String[] aSubstitutions) {
		String aText = generateGradescopeText(aFileName, aSubstitutions);
		writeGeneratedFile(aFileName, aText);
	}
	public static void main (String[] args) {
		createAndWriteGeneratedFile("tmp/grades.csv", 
				new String[]{"ecredit:ecredit@email.unc.edu"});
		
	 
	}

}
