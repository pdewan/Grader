package gradingTools.sakai.gradebook;

import static gradingTools.sakai.gradebook.GradebookUtils.gradebookToMap;
import static gradingTools.sakai.gradebook.GradebookUtils.toGradebookHeader;
import static gradingTools.sakai.gradebook.GradebookUtils.toGradebookRow;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

import framework.grading.FrameworkProjectRequirements;
import util.misc.Common;




public class DiaryManagement {
	public static final int EMAIL_COLUMN = 0;
	public static final int FULL_NAME_COLUMN = 1;
	public static final int DIARY_GRADE_COLUMN = 2;
	public static final int QA_GRADE_COLUMN = 3;
	public static final int GRADER_NAME_COLUMN = 4;
	public static final int GRADER_EMAIL_COLUMN = 5;
	public static final int GRADER_COMMENTS_COLUMN = 7;
	public static final int DATE_COLUMN = 6;

	public static void diaryToGradebook(String aDate, String aDiaryFileName,
			boolean isDiaryPoints,
			String aSakaiInputFile, String[] aSubstitutions) {
		try {
			String aDiaryOrQA = isDiaryPoints?"_diary_":"_QA_";
			String[] aDiaryFileComponents = aDiaryFileName.split("\\.");
			String aSakaiFileName = aDiaryFileComponents[0] + "_gradebook_" + aDiaryOrQA+ aDate.replace("/", "_") + ".csv";
			 diaryToGradebook(aDate, aDiaryFileName, isDiaryPoints, aSakaiInputFile, aSubstitutions, aSakaiFileName);		 
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	public static void diaryToGradebook(String aDate, String aDiaryFileName,
			boolean isDiaryPoints,
			String aSakaiInputFile, String[] aSubstitutions, String aSakaiFileName) {
		// File aSakaiFile = new File(aSakaiFileName);
		try {

			StringBuffer aDiaryString = Common.toText(aDiaryFileName);
			StringBuffer aGradebookInputString = Common.toText(aSakaiInputFile);
			String aGradebookString = diaryToGradebook(aDate, aDiaryString, isDiaryPoints, aGradebookInputString,
					aSubstitutions);
		 
			Common.writeText(aSakaiFileName, aGradebookString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	static DateTime now = DateTime.now();
	public static long GRADING_DAY_PERIOD_MS = 7*FrameworkProjectRequirements.MILLI_SECONDS_IN_DAY;
	static DateTime toDateTime(String aDateString) {
		if (aDateString == null || aDateString.isEmpty()) {
			return null;
		}
		String[] aDateComponents = aDateString.split("/");
		int aMonth = Integer.parseInt(aDateComponents[0].trim());
		int aDay = Integer.parseInt(aDateComponents[1].trim());
		DateTime aDateTime = new DateTime(now.getYear(), aMonth, aDay, 0, 0);		
//		String aNormalizedDateString = 
//				aDateComponents[0] + "/" + 
//				aDateComponents[1] + "/" +
//						now.getYear();
//		return new DateTime(aNormalizedDateString);		
		return aDateTime;
	}
	public static boolean withinGradingDays(String anExpectedDate, String anActualDate) {
		
		return anExpectedDate == null ||  withinGradingDays(toDateTime(anExpectedDate), toDateTime(anActualDate) );
//		DateTime anExpectedDateTime = toDateTime(anExpectedDate);
//		DateTime anEarliestDateTime = anExpectedDateTime.minus(GRADING_DAY_PERIOD_MS);
//		DateTime aLatestDateTime = anExpectedDateTime.plus(GRADING_DAY_PERIOD_MS );
//		DateTime anActualDateTime = toDateTime(anActualDate);
//		
//		return anActualDateTime.isAfter(anEarliestDateTime) && anActualDateTime.isBefore(aLatestDateTime);
		
		
	}
	public static boolean withinGradingDays(DateTime anExpectedDateTime, DateTime anActualDateTime) {
		if (anExpectedDateTime == null) {
			return true;
		}
		DateTime anEarliestDateTime = anExpectedDateTime.minus(GRADING_DAY_PERIOD_MS);
		DateTime aLatestDateTime = anExpectedDateTime.plus(GRADING_DAY_PERIOD_MS );
		
		return anActualDateTime.isAfter(anEarliestDateTime) && anActualDateTime.isBefore(aLatestDateTime);
		
		
	}
	public static String diaryToGradebook(String anExpectedDate, StringBuffer aDiaryString, boolean isDiaryPoints,
			StringBuffer aGradebookTemplate,
			String[] aSubstitutions) {
		Map<String, DiaryEntry> anOnyenToDiaryEntry = diaryToMap(anExpectedDate, aDiaryString, aGradebookTemplate, aSubstitutions);
		StringBuilder aGradebookString = new StringBuilder();
		aGradebookString.append(toGradebookHeader());
		for (String anOnyen:anOnyenToDiaryEntry.keySet()) {
			DiaryEntry aDiaryEntry = anOnyenToDiaryEntry.get(anOnyen);
			String aGrade = Integer.toString(
					isDiaryPoints?aDiaryEntry.getDiaryPoints():aDiaryEntry.getQuestionPoints());
			String aGradebookRow = toGradebookRow(aDiaryEntry.getGradebookEntry(), aGrade);
			aGradebookString.append(aGradebookRow);
		}
//		String aGradebookRow = toGradebookRow(aGradebookEntry, aDiaryGrade);
//		aGradebookString.append(aGradebookRow);
		
		return aGradebookString.toString();
		
	}
	public static Map<String, DiaryEntry> diaryToMap(String anExpectedDate, StringBuffer aDiaryString,
			StringBuffer aGradebookTemplate,
			String[] aSubstitutions) {
		DateTime anExpectedDateTime = toDateTime(anExpectedDate);
		
		
		Map<String, String> anEmailToOnyen = new HashMap();
		for (String aSubstitution : aSubstitutions) {
			String[] anOnyenAndEmail = aSubstitution.split(":");
			anEmailToOnyen.put(anOnyenAndEmail[1], anOnyenAndEmail[0]);
		}
		String aInputLinesWithoutQuotes = aDiaryString.toString()
				.replaceAll("\"", "");
		String[] anInputLines = aInputLinesWithoutQuotes.toString().split("\n");
//		StringBuilder aGradebookString = new StringBuilder(anInputLines.length);

//		aGradebookString.append(toGradebookHeader());
		Map<String, GradebookEntry> anOnyenToGradebook = gradebookToMap(aGradebookTemplate);
		Map<String, DiaryEntry> anOnyenToDiaryEntry = new HashMap<>();

		for (int aRowNum = 1; aRowNum < anInputLines.length; aRowNum++) {
			
			String[] aRow = anInputLines[aRowNum].split(",");
			if (aRow.length < 8) {
				System.out.println("Ignoring row with < 8 elements " + Arrays.toString(aRow));
				continue;
			}
			try {
			String anEmail = aRow[EMAIL_COLUMN];
			String anOnyen = anEmailToOnyen.get(anEmail);
			if (anOnyen == null) {
				String[] aNameAndDomain = anEmail.split("@");
				if (aNameAndDomain.length != 2) {
					System.out.println("Ignoring row with no email in first column " + Arrays.toString(aRow));
				}
				anOnyen = aNameAndDomain[0];				
			}	
			String anActualDate = aRow[DATE_COLUMN];
			DateTime anActualDateTime = toDateTime(anActualDate);
			if (!withinGradingDays(anExpectedDateTime, anActualDateTime)) {
				continue;
			}			
						
			GradebookEntry aGradebookEntry = anOnyenToGradebook.get(anOnyen);
			if (aGradebookEntry == null) {
				String[] aNameComponents = aRow[FULL_NAME_COLUMN].trim().split(" ");
				String aFirstName = aNameComponents[0].trim();
				String aLastName = aNameComponents[aNameComponents.length - 1].trim();
				for (String aGradebookOnyen: anOnyenToGradebook.keySet() ) {
					GradebookEntry aTestEntry = anOnyenToGradebook.get(aGradebookOnyen);
//					String aGradebookFullName = aGradebookEntry.getFirstName() + " " + aGradebookEntry.getLastName();
					if (aTestEntry.getFirstName().equals(aFirstName) && aTestEntry.getLastName().equals(aLastName)) {
						anOnyen = aGradebookOnyen;
						aGradebookEntry = aTestEntry;
						break;
					}
				}
				if (aGradebookEntry == null) {
					System.out.println("did not find in Gradebook:" + anOnyen);
					continue;
				}
			}
			
			DiaryEntry aDiaryEntry = anOnyenToDiaryEntry.get(anOnyen);
			if (aDiaryEntry == null) {
				aDiaryEntry = new ADiaryEntry(anActualDateTime,
						anEmail, 
						aRow[FULL_NAME_COLUMN].trim(), 
						0, 
						0, 
						aRow[GRADER_NAME_COLUMN].trim(), 
						aRow[GRADER_COMMENTS_COLUMN].trim());
				aDiaryEntry.setGradebookEntry(aGradebookEntry);
				anOnyenToDiaryEntry.put(anOnyen, aDiaryEntry);
			}
			
//			String anEmail = aRow[2];
			int aDiaryGrade = Integer.parseInt(aRow[DIARY_GRADE_COLUMN].trim());
			int aQAGrade = Integer.parseInt(aRow[QA_GRADE_COLUMN].trim());
			aDiaryEntry.incrementDiaryPoints(aDiaryGrade);
			aDiaryEntry.incrementQuestionPoints(aQAGrade);
			
//			String aGradebookRow = toGradebookRow(aGradebookEntry, aDiaryGrade);
//			aGradebookString.append(aGradebookRow);
			
			} catch (Exception e) {
				System.err.println ("Ignoring row: " + Arrays.toString(aRow));
				e.printStackTrace();
			}

		}
		return anOnyenToDiaryEntry;
//		return aGradebookString.toString();

	}

}
