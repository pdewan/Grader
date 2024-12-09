package gradingTools.gradebook;

import static gradingTools.gradebook.CanvasUtils.gradebookToMap;
import static gradingTools.gradebook.CanvasUtils.toGradebookRow;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

import framework.grading.FrameworkProjectRequirements;
import util.misc.Common;




public class DiaryManagementCanvas {
	public static final int EMAIL_COLUMN = 0;
	public static final int FULL_NAME_COLUMN = 1;
	public static final int DIARY_GRADE_COLUMN = 2;
	public static final int QA_GRADE_COLUMN = 3;
	public static final int GRADER_NAME_COLUMN = 4;
	public static final int GRADER_EMAIL_COLUMN = 5;
	public static final int GRADER_COMMENTS_COLUMN = 7;
	public static final int DIARY_TEXT_COLUMN = 8;
	public static final int DATE_COLUMN = 6;
	public static final int MIN_COLUMNS = DIARY_TEXT_COLUMN;
	
	public static final String CLASS_QA_COLUMN = "ClassQA";
	public static final String MY_QA_COLUMN = "MyQA";
	
	protected int emailColumn() {
		return EMAIL_COLUMN;
	}
	protected int fullNameColumn() {
		return FULL_NAME_COLUMN;
	}
	protected int diaryGradeColumn() {
		return DIARY_GRADE_COLUMN;
	}
	protected int qAGradeColumn() {
		return QA_GRADE_COLUMN;
	}
	protected int graderNameColumn() {
		return GRADER_NAME_COLUMN;
	}
	protected int graderEmailColumn() {
		return GRADER_EMAIL_COLUMN;
	}
	protected int graderCommentsColumn() {
		return GRADER_COMMENTS_COLUMN;
	}
	protected int diaryTextColumn() {
		return DIARY_TEXT_COLUMN;
	}
	protected int dateColumn() {
		return DATE_COLUMN;
	}
	protected int minColumns() {
		return MIN_COLUMNS;
	}
	protected boolean isSummary() {
		return false;
	}

//	public static void diaryToGradebook(String aDate, String aDiaryFileName,
//			boolean isDiaryPoints,
//			String aSakaiInputFile, String[] aSubstitutions) {
//		try {
//			String aDiaryOrQA = isDiaryPoints?"_diary_":"_QA_";
//			String[] aDiaryFileComponents = aDiaryFileName.split("\\.");
//			String aSakaiFileName = aDiaryFileComponents[0] + "_gradebook_" + aDiaryOrQA+ aDate.replace("/", "_") + ".csv";
//			 diaryToGradebook(aDate, aDiaryFileName, isDiaryPoints, aSakaiInputFile, aSubstitutions, aSakaiFileName);		 
//		} catch (Exception e) {
//			
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}	
//	public  void diarySummaryToGradebook(String[] aDates, String aDiaryFileName,
////			boolean isDiaryPoints,
//			Boolean[] isDiaryPoints,
//			String aSakaiInputFile, String[] aSubstitutions, Integer[] aMaxLimits) {
//		try {
//			String[] aDiaryFileComponents = aDiaryFileName.split("\\.");
//			StringBuffer aDiaryString = Common.toText(aDiaryFileName);
//			String aInputLinesWithoutQuotes = aDiaryString.toString()
//					.replaceAll("\"", "");
//			StringBuffer aGradebookInputString = Common.toText(aSakaiInputFile);
//			for (int i = 0; i < aDates.length && i < aMaxLimits.length && i < isDiaryPoints.length; i++) {
//			  String aDate = aDates[i];
////			  boolean isDiaryPoints = aDate.isEmpty()?false:true;
////			  boolean isDiaryPoints = isDiaryPointsArr[i];
////			  String aGradeColumnName = isDiaryPoints[i]?"Diary":"QA";
//			  String aGradeColumnName = isDiaryPoints[i]?CLASS_QA_COLUMN:MY_QA_COLUMN;
//
////			  String aDiaryOrQA = isDiaryPoints?"_diary_":"_QA_";
//			  String aFileSuffix = aGradeColumnName;
//
//
////			  String aSakaiFileName = aDiaryFileComponents[0] + "_gradebook_" + aDiaryOrQA+ aDate.replace("/", "_") + ".csv";
//			  String aSakaiFileName = aDiaryFileComponents[0] + "_gradebook_" + aFileSuffix+ aDate.replace("/", "_") + ".csv";
//
////			  String aGradebookString = diaryToGradebook(aDate, aInputLinesWithoutQuotes, isDiaryPoints, aGradebookInputString, aSubstitutions);		
//			  String aGradebookString = diaryToGradebook(aDate, aInputLinesWithoutQuotes, isDiaryPoints[i], aGradebookInputString, aSubstitutions, aGradeColumnName, aMaxLimits[i]);		
//
//			  Common.writeText(aSakaiFileName, aGradebookString);
//			}
//
//		} catch (Exception e) {
//			
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	public  void diarySummaryToCanvas(String[] aDates, String aDiaryFileName,
//			boolean isDiaryPoints,
			Boolean[] isDiaryPoints,
			String aCanvasInputFile, String[] aSubstitutions, Integer[] aMaxLimits) {
		try {
			String[] aCanvasFileComponents= aCanvasInputFile.split("\\.");
			StringBuffer aDiaryString = Common.toText(aDiaryFileName);
			String aInputLinesWithoutQuotes = aDiaryString.toString()
					.replaceAll("\"", "");
			StringBuffer aGradebookInputString = Common.toText(aCanvasInputFile);
			for (int i = 0; i < aDates.length && i < aMaxLimits.length && i < isDiaryPoints.length; i++) {
			  String aDate = aDates[i];
//			  boolean isDiaryPoints = aDate.isEmpty()?false:true;
//			  boolean isDiaryPoints = isDiaryPointsArr[i];
//			  String aGradeColumnName = isDiaryPoints[i]?"Diary":"QA";
			  String aGradeColumnName = isDiaryPoints[i]?CLASS_QA_COLUMN:MY_QA_COLUMN;

//			  String aDiaryOrQA = isDiaryPoints?"_diary_":"_QA_";
			  String aFileSuffix = aGradeColumnName;


//			  String aSakaiFileName = aDiaryFileComponents[0] + "_gradebook_" + aDiaryOrQA+ aDate.replace("/", "_") + ".csv";
			  String aSakaiFileName = aCanvasFileComponents[0] + "_out.csv";

//			  String aGradebookString = diaryToGradebook(aDate, aInputLinesWithoutQuotes, isDiaryPoints, aGradebookInputString, aSubstitutions);		
			  String aGradebookString = diaryToGradebook(aDate, aInputLinesWithoutQuotes, isDiaryPoints[i], aGradebookInputString, aSubstitutions, aGradeColumnName, aMaxLimits[i]);		

			  Common.writeText(aSakaiFileName, aGradebookString);
			}

		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//	public  void diaryToGradebook(String[] aDates, String aDiaryFileName,
////			boolean isDiaryPoints,
//			Boolean[] isDiaryPoints,
//			String aSakaiInputFile, String[] aSubstitutions, Integer[] aMaxLimits) {
//		try {
//			String[] aDiaryFileComponents = aDiaryFileName.split("\\.");
//			StringBuffer aDiaryString = Common.toText(aDiaryFileName);
//			String aInputLinesWithoutQuotes = aDiaryString.toString()
//					.replaceAll("\"", "");
//			StringBuffer aGradebookInputString = Common.toText(aSakaiInputFile);
//			for (int i = 0; i < aDates.length && i < aMaxLimits.length && i < isDiaryPoints.length; i++) {
//			  String aDate = aDates[i];
////			  boolean isDiaryPoints = aDate.isEmpty()?false:true;
////			  boolean isDiaryPoints = isDiaryPointsArr[i];
////			  String aGradeColumnName = isDiaryPoints[i]?"Diary":"QA";
//			  String aGradeColumnName = isDiaryPoints[i]?CLASS_QA_COLUMN:MY_QA_COLUMN;
//
////			  String aDiaryOrQA = isDiaryPoints?"_diary_":"_QA_";
//			  String aFileSuffix = aGradeColumnName;
//
//
////			  String aSakaiFileName = aDiaryFileComponents[0] + "_gradebook_" + aDiaryOrQA+ aDate.replace("/", "_") + ".csv";
//			  String aSakaiFileName = aDiaryFileComponents[0] + "_gradebook_" + aFileSuffix+ aDate.replace("/", "_") + ".csv";
//
////			  String aGradebookString = diaryToGradebook(aDate, aInputLinesWithoutQuotes, isDiaryPoints, aGradebookInputString, aSubstitutions);		
//			  String aGradebookString = detailedDiaryToGradebook(aDate, aInputLinesWithoutQuotes, isDiaryPoints[i], aGradebookInputString, aSubstitutions, aGradeColumnName, aMaxLimits[i]);		
//
//			  Common.writeText(aSakaiFileName, aGradebookString);
//			}
//
//		} catch (Exception e) {
//			
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	public  void diaryToGradebook(String aDate, String aDiaryFileName,
//			boolean isDiaryPoints,
//			String aSakaiInputFile, String[] aSubstitutions, String aSakaiFileName, String aGradeColumn, int aMaxLimit) {
//		// File aSakaiFile = new File(aSakaiFileName);
//		try {
//
//			StringBuffer aDiaryString = Common.toText(aDiaryFileName);
//			String aInputLinesWithoutQuotes = aDiaryString.toString()
//					.replaceAll("\"", "");
//			StringBuffer aGradebookInputString = Common.toText(aSakaiInputFile);
//			String aGradebookString = diaryToGradebook(aDate, aInputLinesWithoutQuotes, isDiaryPoints, aGradebookInputString,
//					aSubstitutions, aGradeColumn, aMaxLimit);
//		 
//			Common.writeText(aSakaiFileName, aGradebookString);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}	

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
	static DateTime normalizedDateToDateTime(String aDateString) {
		if (aDateString == null || aDateString.isEmpty()) {
			return null;
		}
		String[] aDateComponents = aDateString.split("-");
		int aYear = Integer.parseInt(aDateComponents[0].trim());
		int aMonth = Integer.parseInt(aDateComponents[1].trim());
		int aDay = Integer.parseInt(aDateComponents[2].trim());
		DateTime aDateTime = null;
		try {
		 aDateTime = new DateTime(aYear, aMonth, aDay, 0, 0);	
		} catch (Exception e) {
			System.err.println ("Invalid year: " + aYear + " month " + aMonth + " or day " + aDay);
			System.err.println ("Date exception : "  + e.getMessage());

			aDateTime = new DateTime(now.getYear(), 0, 0, 0, 0);
		}
//		String aNormalizedDateString = 
//				aDateComponents[0] + "/" + 
//				aDateComponents[1] + "/" +
//						now.getYear();
//		return new DateTime(aNormalizedDateString);		
		return aDateTime;
	}
	public  boolean withinGradingDays(String anExpectedDate, String anActualDate) {
		
		return anExpectedDate == null ||  withinGradingDays(toDateTime(anExpectedDate), toDateTime(anActualDate) );
//		DateTime anExpectedDateTime = toDateTime(anExpectedDate);
//		DateTime anEarliestDateTime = anExpectedDateTime.minus(GRADING_DAY_PERIOD_MS);
//		DateTime aLatestDateTime = anExpectedDateTime.plus(GRADING_DAY_PERIOD_MS );
//		DateTime anActualDateTime = toDateTime(anActualDate);
//		
//		return anActualDateTime.isAfter(anEarliestDateTime) && anActualDateTime.isBefore(aLatestDateTime);
		
		
	}
	public  boolean withinGradingDays(DateTime anExpectedDateTime, DateTime anActualDateTime) {
		if (anExpectedDateTime == null) {
			return true;
		}
		DateTime anEarliestDateTime = anExpectedDateTime.minus(GRADING_DAY_PERIOD_MS);
		DateTime aLatestDateTime = anExpectedDateTime.plus(GRADING_DAY_PERIOD_MS );
		
		return anActualDateTime.isAfter(anEarliestDateTime) && anActualDateTime.isBefore(aLatestDateTime);
		
		
	}
	
	public  String diaryToGradebook(String anExpectedDate, String aDiaryString, boolean isDiaryPoints,
			StringBuffer aGradebookTemplate,
			String[] aSubstitutions,
			String aGradeColumnName, 
			Integer aMaxLimit) {
//		String[] aGradeBookLines = aGradebookTemplate.toString().split("\n");

		Map<String, DiaryEntry> anOnyenToDiaryEntry = diaryToMap(anExpectedDate, aDiaryString, aGradebookTemplate, aSubstitutions);
		String[] aGradebookLines = aGradebookTemplate.toString().split("\n");
		StringBuffer anOutputBuffer = new StringBuffer(aGradebookTemplate.capacity());
		StringBuffer aLineBuffer = new StringBuffer();
		for (int anIndex = 0; anIndex < CanvasUtils.START_ROW; anIndex++) {
			anOutputBuffer.append(aGradebookLines[anIndex]);
			anOutputBuffer.append("\r\n");
		}
		for (int anIndex = CanvasUtils.START_ROW; anIndex < aGradebookLines.length; anIndex++) {
			String aLine = aGradebookLines[anIndex];
			aLineBuffer.setLength(0);
			aLineBuffer.append(aLine);
			GradebookEntry aGradeBookEntry = CanvasUtils.toGradebookEntry(aLine);
			String anOnyen = aGradeBookEntry.getStudentID();
			DiaryEntry aDiaryEntry = anOnyenToDiaryEntry.get(anOnyen);
			int anIntGrade = 0;
			if (aDiaryEntry == null) {
				System.out.println("Null diary entry for:" + anOnyen);
				
			} else {
		    anIntGrade = isDiaryPoints?aDiaryEntry.getDiaryPoints():aDiaryEntry.getQuestionPoints();
			}
			if (aMaxLimit != null && aMaxLimit >= 0) {
				anIntGrade = Math.min(anIntGrade, aMaxLimit);
			}
			String aGrade = Integer.toString(
					anIntGrade);
			toGradebookRow(aLineBuffer, aGrade);
			anOutputBuffer.append(aLineBuffer);
			anOutputBuffer.append("\r\n");
		}
		return anOutputBuffer.toString();
		
		
		
	}
	
	
//	public  String detailedDiaryToGradebook(String anExpectedDate, String aDiaryString, boolean isDiaryPoints,
//			StringBuffer aGradebookTemplate,
//			String[] aSubstitutions,
//			String aGradeColumnName, 
//			Integer aMaxLimit) {
//		Map<String, DiaryEntry> anOnyenToDiaryEntry = detailedDiaryToMap(anExpectedDate, aDiaryString, aGradebookTemplate, aSubstitutions);
//		StringBuilder aGradebookString = new StringBuilder();
//		aGradebookString.append(toGradebookHeader(aGradeColumnName));
//		for (String anOnyen:anOnyenToDiaryEntry.keySet()) {
//			DiaryEntry aDiaryEntry = anOnyenToDiaryEntry.get(anOnyen);
//			int anIntGrade = isDiaryPoints?aDiaryEntry.getDiaryPoints():aDiaryEntry.getQuestionPoints();
//			if (aMaxLimit != null && aMaxLimit >= 0) {
//				anIntGrade = Math.min(anIntGrade, aMaxLimit);
//			}
////			String aGrade = Integer.toString(
////					isDiaryPoints?aDiaryEntry.getDiaryPoints():aDiaryEntry.getQuestionPoints());
//			String aGrade = Integer.toString(
//					anIntGrade);
//			String aGradebookRow = toGradebookRow(aDiaryEntry.getGradebookEntry(), aGrade);
//			aGradebookString.append(aGradebookRow);
//		}
////		String aGradebookRow = toGradebookRow(aGradebookEntry, aDiaryGrade);
////		aGradebookString.append(aGradebookRow);
//		
//		return aGradebookString.toString();
//		
//	}
	static StringBuffer toText (String[] aStrings) {
		StringBuffer retVal = new StringBuffer();
		for (String aString:aStrings) {
			retVal.append(aString);
		}
		return retVal;
	}
	protected static boolean matches (String aDiaryFirstName, String aDiaryLastName, String aGradebookFirstName, String aGradebookLastName) {
		boolean aMatch = 
				aGradebookLastName.equals(aDiaryLastName) &&
				aGradebookFirstName.startsWith(aDiaryFirstName);
		if (!aMatch && aDiaryFirstName.equals(aDiaryLastName)) {
			
			return aGradebookLastName.equals(aDiaryFirstName) ||
					aGradebookFirstName.equals(aDiaryFirstName);
		}
		return aMatch;
				
	}
	public  Map<String, DiaryEntry> diaryToMap(String anExpectedDate, String aInputLinesWithoutQuotes,
			StringBuffer aGradebookTemplate,
			String[] aSubstitutions) {
		DateTime anExpectedDateTime = toDateTime(anExpectedDate);
		
		
		Map<String, String> anEmailToOnyen = new HashMap();
		for (String aSubstitution : aSubstitutions) {
			String[] anOnyenAndEmail = aSubstitution.split(":");
			anEmailToOnyen.put(anOnyenAndEmail[1], anOnyenAndEmail[0]);
		}
//		String aInputLinesWithoutQuotes = aDiaryString.toString()
//				.replaceAll("\"", "");
		String[] anInputLines = aInputLinesWithoutQuotes.toString().split("\n");
//		StringBuilder aGradebookString = new StringBuilder(anInputLines.length);

//		aGradebookString.append(toGradebookHeader());
		Map<String, GradebookEntry> anOnyenToGradebook = gradebookToMap(aGradebookTemplate);
		Map<String, DiaryEntry> anOnyenToDiaryEntry = new HashMap<>();
		DiaryEntry aLastDiaryEntry = null;
		for (int aRowNum = CanvasUtils.START_ROW; aRowNum < anInputLines.length; aRowNum++) {
			
			String[] aRow = anInputLines[aRowNum].split(",");
			String anEmail = aRow[ emailColumn()];
			if (!anEmail.contains("@")) {
				System.out.println("Ignoring row without email:" + Arrays.toString(aRow));
				continue;
			}

			if (aRow.length < minColumns()) {
				if (aLastDiaryEntry == null) {
					System.out.println("Ignoring row with < 8 elements and no previous student row " + Arrays.toString(aRow));
				} else {
					aLastDiaryEntry.getDiaryText().append(toText(aRow));
				}
//				System.out.println("Ignoring row with < 8 elements " + Arrays.toString(aRow));
				continue;
			}
			try {
//			String anEmail = aRow[ emailColumn()];
			String anOnyen = anEmailToOnyen.get(anEmail);
			if (anOnyen == null) {
				String[] aNameAndDomain = anEmail.split("@");
				if (aNameAndDomain.length != 2) {
					System.out.println("Ignoring row with no email in first column " + Arrays.toString(aRow));
					continue;
				}
				anOnyen = aNameAndDomain[0];				
			}	
			DateTime anActualDateTime = null;
			if (anExpectedDateTime != null || dateColumn() != -1) {
			String anActualDate = aRow[dateColumn()];
			 anActualDateTime = normalizedDateToDateTime(anActualDate);
			if (!withinGradingDays(anExpectedDateTime, anActualDateTime)) {
				continue;
			}	
			}
						
			GradebookEntry aGradebookEntry = anOnyenToGradebook.get(anOnyen);
			if (aGradebookEntry == null) {
//				System.out.println("Guessing onyen for:" + anOnyen);
				String[] aNameComponents = aRow[fullNameColumn()].trim().split(" ");
				String aFirstName = aNameComponents[0].trim();
				String aLastName = aNameComponents[aNameComponents.length - 1].trim();
				for (String aGradebookOnyen: anOnyenToGradebook.keySet() ) {
					
					GradebookEntry aTestEntry = anOnyenToGradebook.get(aGradebookOnyen);
//					String aGradebookFullName = aGradebookEntry.getFirstName() + " " + aGradebookEntry.getLastName();
					if (
							matches(aFirstName, aLastName, aTestEntry.getFirstName(), aTestEntry.getLastName()) 
//							aTestEntry.getLastName().equals(aLastName) &&
//							aTestEntry.getFirstName().startsWith(aFirstName)

//							aTestEntry.getFirstName().equals(aFirstName)
							)
							{
						System.out.println("Predicted onyen for:" + anOnyen + " is " + aGradebookOnyen + "\n");

						anOnyen = aGradebookOnyen;

						aGradebookEntry = aTestEntry;
						break;
					}
				}
				if (aGradebookEntry == null) {
					System.out.println("did not find in Gradebook:" + anOnyen + ":" + aFirstName + "," + aLastName);
					continue;
				}
			}
			
//			DiaryEntry 
			aLastDiaryEntry = anOnyenToDiaryEntry.get(anOnyen);
			if (aLastDiaryEntry == null) {
				if (isSummary()) {
					aLastDiaryEntry = new ADiaryEntry(anActualDateTime,
							anEmail, 
							aRow[fullNameColumn()].trim(), 
							0, 
							0 
							);
				} else {
				aLastDiaryEntry = new ADiaryEntry(anActualDateTime,
						anEmail, 
						aRow[fullNameColumn()].trim(), 
						0, 
						0, 
						aRow[graderNameColumn()].trim(), 
						aRow[graderCommentsColumn()].trim(),
						aRow[diaryTextColumn()].trim()
						);
			}
				aLastDiaryEntry.setGradebookEntry(aGradebookEntry);
				anOnyenToDiaryEntry.put(anOnyen, aLastDiaryEntry);
			}
			
//			String anEmail = aRow[2];
			int aDiaryGrade = Integer.parseInt(aRow[diaryGradeColumn()].trim());
			int aQAGrade = Integer.parseInt(aRow[qAGradeColumn()].trim());
			aLastDiaryEntry.incrementDiaryPoints(aDiaryGrade);
			aLastDiaryEntry.incrementQuestionPoints(aQAGrade);
			
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
	public  Map<String, DiaryEntry> diaryToCavasMap(String anExpectedDate, String aInputLinesWithoutQuotes,
			StringBuffer aGradebookTemplate,
			String[] aSubstitutions) {
		DateTime anExpectedDateTime = toDateTime(anExpectedDate);
		
		
		Map<String, String> anEmailToOnyen = new HashMap();
		for (String aSubstitution : aSubstitutions) {
			String[] anOnyenAndEmail = aSubstitution.split(":");
			anEmailToOnyen.put(anOnyenAndEmail[1], anOnyenAndEmail[0]);
		}
//		String aInputLinesWithoutQuotes = aDiaryString.toString()
//				.replaceAll("\"", "");
		String[] anInputLines = aInputLinesWithoutQuotes.toString().split("\n");
//		StringBuilder aGradebookString = new StringBuilder(anInputLines.length);

//		aGradebookString.append(toGradebookHeader());
		Map<String, GradebookEntry> anOnyenToGradebook = CanvasUtils.gradebookToMap(aGradebookTemplate);
		Map<String, DiaryEntry> anOnyenToDiaryEntry = new HashMap<>();
		DiaryEntry aLastDiaryEntry = null;
		for (int aRowNum = 0; aRowNum < anInputLines.length; aRowNum++) {
			
			String[] aRow = anInputLines[aRowNum].split(",");
			String anEmail = aRow[ emailColumn()];
			if (!anEmail.contains("@")) {
				System.out.println("Ignoring row without email:" + Arrays.toString(aRow));
				continue;
			}

			if (aRow.length < minColumns()) {
				if (aLastDiaryEntry == null) {
					System.out.println("Ignoring row with < 8 elements and no previous student row " + Arrays.toString(aRow));
				} else {
					aLastDiaryEntry.getDiaryText().append(toText(aRow));
				}
//				System.out.println("Ignoring row with < 8 elements " + Arrays.toString(aRow));
				continue;
			}
			try {
//			String anEmail = aRow[ emailColumn()];
			String anOnyen = anEmailToOnyen.get(anEmail);
			if (anOnyen == null) {
				String[] aNameAndDomain = anEmail.split("@");
				if (aNameAndDomain.length != 2) {
					System.out.println("Ignoring row with no email in first column " + Arrays.toString(aRow));
					continue;
				}
				anOnyen = aNameAndDomain[0];				
			}	
			DateTime anActualDateTime = null;
			if (anExpectedDateTime != null || dateColumn() != -1) {
			String anActualDate = aRow[dateColumn()];
			 anActualDateTime = normalizedDateToDateTime(anActualDate);
			if (!withinGradingDays(anExpectedDateTime, anActualDateTime)) {
				continue;
			}	
			}
						
			GradebookEntry aGradebookEntry = anOnyenToGradebook.get(anOnyen);
			if (aGradebookEntry == null) {
				String[] aNameComponents = aRow[fullNameColumn()].trim().split(" ");
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
			
//			DiaryEntry 
			aLastDiaryEntry = anOnyenToDiaryEntry.get(anOnyen);
			if (aLastDiaryEntry == null) {
				if (isSummary()) {
					aLastDiaryEntry = new ADiaryEntry(anActualDateTime,
							anEmail, 
							aRow[fullNameColumn()].trim(), 
							0, 
							0 
							);
				} else {
				aLastDiaryEntry = new ADiaryEntry(anActualDateTime,
						anEmail, 
						aRow[fullNameColumn()].trim(), 
						0, 
						0, 
						aRow[graderNameColumn()].trim(), 
						aRow[graderCommentsColumn()].trim(),
						aRow[diaryTextColumn()].trim()
						);
			}
				aLastDiaryEntry.setGradebookEntry(aGradebookEntry);
				anOnyenToDiaryEntry.put(anOnyen, aLastDiaryEntry);
			}
			
//			String anEmail = aRow[2];
			int aDiaryGrade = Integer.parseInt(aRow[diaryGradeColumn()].trim());
			int aQAGrade = Integer.parseInt(aRow[qAGradeColumn()].trim());
			aLastDiaryEntry.incrementDiaryPoints(aDiaryGrade);
			aLastDiaryEntry.incrementQuestionPoints(aQAGrade);
			
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
	
	public  Map<String, DiaryEntry> detailedDiaryToMap(String anExpectedDate, String aInputLinesWithoutQuotes,
			StringBuffer aGradebookTemplate,
			String[] aSubstitutions) {
		DateTime anExpectedDateTime = toDateTime(anExpectedDate);
		
		
		Map<String, String> anEmailToOnyen = new HashMap();
		for (String aSubstitution : aSubstitutions) {
			String[] anOnyenAndEmail = aSubstitution.split(":");
			anEmailToOnyen.put(anOnyenAndEmail[1], anOnyenAndEmail[0]);
		}
//		String aInputLinesWithoutQuotes = aDiaryString.toString()
//				.replaceAll("\"", "");
		String[] anInputLines = aInputLinesWithoutQuotes.toString().split("\n");
//		StringBuilder aGradebookString = new StringBuilder(anInputLines.length);

//		aGradebookString.append(toGradebookHeader());
		Map<String, GradebookEntry> anOnyenToGradebook = gradebookToMap(aGradebookTemplate);
		Map<String, DiaryEntry> anOnyenToDiaryEntry = new HashMap<>();
//		DiaryEntry aLastDiaryEntry = null;
		for (int aRowNum = 0; aRowNum < anInputLines.length; aRowNum++) {
			DiaryEntry aLastDiaryEntry = null;

			String[] aRow = anInputLines[aRowNum].split(",");
			String anEmail = aRow[ emailColumn()];
			if (!anEmail.contains("@")) {
				System.out.println("Ignoring row without email:" + Arrays.toString(aRow));
				continue;
			}

			if (aRow.length < minColumns()) {
				if (aLastDiaryEntry == null) {
					System.out.println("Ignoring row with < 8 elements and no previous student row " + Arrays.toString(aRow));
				} else {
					aLastDiaryEntry.getDiaryText().append(toText(aRow));
				}
//				System.out.println("Ignoring row with < 8 elements " + Arrays.toString(aRow));
				continue;
			}
			try {
//			String anEmail = aRow[ emailColumn()];
			String anOnyen = anEmailToOnyen.get(anEmail);
			if (anOnyen == null) {
				String[] aNameAndDomain = anEmail.split("@");
				if (aNameAndDomain.length != 2) {
					System.out.println("Ignoring row with no email in first column " + Arrays.toString(aRow));
					continue;
				}
				anOnyen = aNameAndDomain[0];				
			}	
			DateTime anActualDateTime = null;
			if (anExpectedDateTime != null || dateColumn() != -1) {
			String anActualDate = aRow[dateColumn()];
			 anActualDateTime = normalizedDateToDateTime(anActualDate);
			if (!withinGradingDays(anExpectedDateTime, anActualDateTime)) {
				continue;
			}	
			}
						
			GradebookEntry aGradebookEntry = anOnyenToGradebook.get(anOnyen);
			if (aGradebookEntry == null) {
				String[] aNameComponents = aRow[fullNameColumn()].trim().split(" ");
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
			
//			DiaryEntry 
			DiaryEntry aFirstDiaryEntry = anOnyenToDiaryEntry.get(anOnyen);
//			if (aLastDiaryEntry == null) {
				if (isSummary()) {
					aLastDiaryEntry = new ADiaryEntry(anActualDateTime,
							anEmail, 
							aRow[fullNameColumn()].trim(), 
							0, 
							0 
							);
				} else {
				aLastDiaryEntry = new ADiaryEntry(anActualDateTime,
						anEmail, 
						aRow[fullNameColumn()].trim(), 
						0, 
						0, 
						aRow[graderNameColumn()].trim(), 
						aRow[graderCommentsColumn()].trim(),
						aRow[diaryTextColumn()].trim()
						);
			}
				aLastDiaryEntry.setGradebookEntry(aGradebookEntry);
				if (aFirstDiaryEntry == null) {
					aFirstDiaryEntry = aLastDiaryEntry;
					anOnyenToDiaryEntry.put(anOnyen, aLastDiaryEntry);
				} else {
					aFirstDiaryEntry.lastDiaryEntry().setNextDiaryEntry(aLastDiaryEntry);
				}
//				anOnyenToDiaryEntry.put(anOnyen, aLastDiaryEntry);
//			}
			
//			String anEmail = aRow[2];
			int aDiaryGrade = Integer.parseInt(aRow[diaryGradeColumn()].trim());
			int aQAGrade = Integer.parseInt(aRow[qAGradeColumn()].trim());
			aLastDiaryEntry.incrementDiaryPoints(aDiaryGrade);
			aLastDiaryEntry.incrementQuestionPoints(aQAGrade);
			
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
