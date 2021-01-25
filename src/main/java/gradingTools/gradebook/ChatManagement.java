package gradingTools.gradebook;

import static gradingTools.gradebook.GradebookUtils.gradebookToMap;
import static gradingTools.gradebook.GradebookUtils.toGradebookHeader;
import static gradingTools.gradebook.GradebookUtils.toGradebookRow;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import framework.grading.FrameworkProjectRequirements;
import util.misc.Common;




public class ChatManagement {

	
	public static final String MY_CHAT_COLUMN = "MyQA";
	
	
	
	
////	public  void chatToGradebook(String[] aDates, String aChatDirectoryName,
//////			,
////			String aSakaiInputFile, String[] aSubstitutions) {
////		try {
////			String[] aDiaryFileComponents = aChatDirectoryName.split("\\.");
////			StringBuffer aDiaryString = Common.toText(aDiaryFileName);
////			String aInputLinesWithoutQuotes = aDiaryString.toString()
////					.replaceAll("\"", "");
////			StringBuffer aGradebookInputString = Common.toText(aSakaiInputFile);
////			for (int i = 0; i < aDates.length && i < aMaxLimits.length && i < isDiaryPoints.length; i++) {
////			  String aDate = aDates[i];
//////			  boolean isDiaryPoints = aDate.isEmpty()?false:true;
//////			  boolean isDiaryPoints = isDiaryPointsArr[i];
//////			  String aGradeColumnName = isDiaryPoints[i]?"Diary":"QA";
////			  String aGradeColumnName = isDiaryPoints[i]?CLASS_QA_COLUMN:MY_QA_COLUMN;
////
//////			  String aDiaryOrQA = isDiaryPoints?"_diary_":"_QA_";
////			  String aFileSuffix = aGradeColumnName;
////
////
//////			  String aSakaiFileName = aDiaryFileComponents[0] + "_gradebook_" + aDiaryOrQA+ aDate.replace("/", "_") + ".csv";
////			  String aSakaiFileName = aDiaryFileComponents[0] + "_gradebook_" + aFileSuffix+ aDate.replace("/", "_") + ".csv";
////
//////			  String aGradebookString = diaryToGradebook(aDate, aInputLinesWithoutQuotes, isDiaryPoints, aGradebookInputString, aSubstitutions);		
////			  String aGradebookString = detailedDiaryToGradebook(aDate, aInputLinesWithoutQuotes, isDiaryPoints[i], aGradebookInputString, aSubstitutions, aGradeColumnName, aMaxLimits[i]);		
////
////			  Common.writeText(aSakaiFileName, aGradebookString);
////			}
////
////		} catch (Exception e) {
////			
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////	}
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
//
//	static DateTime now = DateTime.now();
//	public static long GRADING_DAY_PERIOD_MS = 7*FrameworkProjectRequirements.MILLI_SECONDS_IN_DAY;
//	static DateTime toDateTime(String aDateString) {
//		if (aDateString == null || aDateString.isEmpty()) {
//			return null;
//		}
//		String[] aDateComponents = aDateString.split("/");
//		int aMonth = Integer.parseInt(aDateComponents[0].trim());
//		int aDay = Integer.parseInt(aDateComponents[1].trim());
//		DateTime aDateTime = new DateTime(now.getYear(), aMonth, aDay, 0, 0);		
////		String aNormalizedDateString = 
////				aDateComponents[0] + "/" + 
////				aDateComponents[1] + "/" +
////						now.getYear();
////		return new DateTime(aNormalizedDateString);		
//		return aDateTime;
//	}
//	static DateTime normalizedDateToDateTime(String aDateString) {
//		if (aDateString == null || aDateString.isEmpty()) {
//			return null;
//		}
//		String[] aDateComponents = aDateString.split("-");
//		int aYear = Integer.parseInt(aDateComponents[0].trim());
//		int aMonth = Integer.parseInt(aDateComponents[1].trim());
//		int aDay = Integer.parseInt(aDateComponents[2].trim());
//		DateTime aDateTime = null;
//		try {
//		 aDateTime = new DateTime(aYear, aMonth, aDay, 0, 0);	
//		} catch (Exception e) {
//			System.err.println ("Invalid year: " + aYear + " month " + aMonth + " or day " + aDay);
//			System.err.println ("Date exception : "  + e.getMessage());
//
//			aDateTime = new DateTime(now.getYear(), 0, 0, 0, 0);
//		}
////		String aNormalizedDateString = 
////				aDateComponents[0] + "/" + 
////				aDateComponents[1] + "/" +
////						now.getYear();
////		return new DateTime(aNormalizedDateString);		
//		return aDateTime;
//	}
//	public  boolean withinGradingDays(String anExpectedDate, String anActualDate) {
//		
//		return anExpectedDate == null ||  withinGradingDays(toDateTime(anExpectedDate), toDateTime(anActualDate) );
////		DateTime anExpectedDateTime = toDateTime(anExpectedDate);
////		DateTime anEarliestDateTime = anExpectedDateTime.minus(GRADING_DAY_PERIOD_MS);
////		DateTime aLatestDateTime = anExpectedDateTime.plus(GRADING_DAY_PERIOD_MS );
////		DateTime anActualDateTime = toDateTime(anActualDate);
////		
////		return anActualDateTime.isAfter(anEarliestDateTime) && anActualDateTime.isBefore(aLatestDateTime);
//		
//		
//	}
//	public  boolean withinGradingDays(DateTime anExpectedDateTime, DateTime anActualDateTime) {
//		if (anExpectedDateTime == null) {
//			return true;
//		}
//		DateTime anEarliestDateTime = anExpectedDateTime.minus(GRADING_DAY_PERIOD_MS);
//		DateTime aLatestDateTime = anExpectedDateTime.plus(GRADING_DAY_PERIOD_MS );
//		
//		return anActualDateTime.isAfter(anEarliestDateTime) && anActualDateTime.isBefore(aLatestDateTime);
//		
//		
//	}
//	public  String diaryToGradebook(String anExpectedDate, String aDiaryString, boolean isDiaryPoints,
//			StringBuffer aGradebookTemplate,
//			String[] aSubstitutions,
//			String aGradeColumnName, 
//			Integer aMaxLimit) {
//		Map<String, DiaryEntry> anOnyenToDiaryEntry = diaryToMap(anExpectedDate, aDiaryString, aGradebookTemplate, aSubstitutions);
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
//	static StringBuffer toText (String[] aStrings) {
//		StringBuffer retVal = new StringBuffer();
//		for (String aString:aStrings) {
//			retVal.append(aString);
//		}
//		return retVal;
//	}
//	public static List<String> findOnyensByLastName(String aLastName, 
//			Map<String, GradebookEntry> anOnyenToGradebook) {
//		List<String> aMatches = new ArrayList<String>();
//		String aLastNameLowerCase = aLastName.toLowerCase();
//		for (String anOnyen:anOnyenToGradebook.keySet()) {
//			GradebookEntry aGradebookEntry = anOnyenToGradebook.get(anOnyen);
//			if (aGradebookEntry.getLastName().toLowerCase().equals(aLastNameLowerCase)) {
//				aMatches.add(anOnyen);
//			}
//		}
//		return aMatches;
//	}
//	public static List<String> filterOnyenByFirstName(
//			String aFirstName, 
//			List<String> aLastNameMatches, 
//			Map<String, GradebookEntry> anOnyenToGradebook) {
//		String aFirstNameLowerCase= aFirstName.toLowerCase();
//		List<String> aFirstNameMatches = new ArrayList();
//		for (String anOnyen:aLastNameMatches) {
//			GradebookEntry aGradebookEntry = anOnyenToGradebook.get(anOnyen);
//			if (aGradebookEntry.getLastName().toLowerCase().equals(aLastNameLowerCase)) {
//				aMatches.add(anOnyen);
//			}
//		}
//		return aMatches;
//	}
//	public static String findOnyenByFullName (
//			String aFullName, 
//			Map<String, String> aFullNameToOnyen, 
//			Map<String, GradebookEntry> anOnyenToGradebook) {
//			String retVal = aFullNameToOnyen.get(aFullName);
//			if (retVal != null) {
//				return retVal;
//			}
//			String[] aNames = aFullName.split(" ");
//			String aLastName = aNames[aNames.length - 1];
//			String aFirstName = aNames[0];				
//			List<String> aMatches = new ArrayList<String>();
//	}
//	public  Map<String, DiaryEntry> chatToMap(String aChatDirectoryName,
//			Map<String, GradebookEntry> anOnyenToGradebook,
//			String[] aSubstitutions) {
//		File aChatDirectory = new File (aChatDirectoryName);
//		if (!aChatDirectory.exists()) {
//			System.err.println("File " + aChatDirectory + " does not exist");
//			return null;
//		}
//		if (!aChatDirectory.isDirectory()) {
//			System.err.println("File " + aChatDirectory + " is not a directory");
//			return null;
//		}
//		File[] aFiles = aChatDirectory.listFiles();
//		Map<String, String> aFullNameToOnyen = new HashMap();
//		for (String aSubstitution : aSubstitutions) {
//			String[] anOnyenAndFullName = aSubstitution.split(":");
//			aFullNameToOnyen.put(anOnyenAndFullName[1], anOnyenAndFullName[0]);
//		}
//		Map<String, ChatEntry> anOnyenToChatEntry = new HashMap<>();
//		ChatEntry aLasEntry = null;
//
//		for (File aFile:aFiles) {
//			String aFileName = aFile.getName();
//			Integer aYear = Integer.parseInt (aFileName.substring(0, 4));
//			Integer aMonth = Integer.parseInt(aFileName.substring(4, 6));
//			Integer aDay =  Integer.parseInt (aFileName.substring(6, 8));
//			String aFileContents = Common.toText(aFile);
//			String[] anInputLines = aFileContents.split("\n");
////		StringBuilder aGradebookString = new StringBuilder(anInputLines.length);
//
////		aGradebookString.append(toGradebookHeader());
////			Map<String, GradebookEntry> anOnyenToGradebook = gradebookToMap(aGradebookTemplate);
//		for (int aRowNum = 0; aRowNum < anInputLines.length; aRowNum++) {
//			
//			String[] aPrefixAndChat = anInputLines[aRowNum].split(":	");
//			String aChat = aPrefixAndChat[1];
//			String aPrefix = aPrefixAndChat[0];
//			String[] aTimeAndName = aPrefix.split("	");
//			String aTime = aTimeAndName[0].trim();
//			String[] aTimes = aTime.split(":");
//			Integer anHour = Integer.parseInt(aTimes[0]);
//			Integer aMinutes = Integer.parseInt(aTimes[1]);
//			Integer aSeconds = Integer.parseInt(aTimes[2]);
//			
//			String aName = aTimeAndName[1].trim();
//			String[] aNames = aName.split(" ");
//			
//			if (aRow.length < minColumns()) {
//				if (aLasEntry == null) {
//					System.out.println("Ignoring row with < 8 elements and no previous student row " + Arrays.toString(aRow));
//				} else {
//					aLasEntry.getDiaryText().append(toText(aRow));
//				}
////				System.out.println("Ignoring row with < 8 elements " + Arrays.toString(aRow));
//				continue;
//			}
//			try {
////			String anEmail = aRow[ emailColumn()];
//			String anOnyen = anEmailToOnyen.get(anEmail);
//			if (anOnyen == null) {
//				String[] aNameAndDomain = anEmail.split("@");
//				if (aNameAndDomain.length != 2) {
//					System.out.println("Ignoring row with no email in first column " + Arrays.toString(aRow));
//					continue;
//				}
//				anOnyen = aNameAndDomain[0];				
//			}	
//			DateTime anActualDateTime = null;
//			if (anExpectedDateTime != null || dateColumn() != -1) {
//			String anActualDate = aRow[dateColumn()];
//			 anActualDateTime = normalizedDateToDateTime(anActualDate);
//			if (!withinGradingDays(anExpectedDateTime, anActualDateTime)) {
//				continue;
//			}	
//			}
//						
//			GradebookEntry aGradebookEntry = anOnyenToGradebook.get(anOnyen);
//			if (aGradebookEntry == null) {
//				String[] aNameComponents = aRow[fullNameColumn()].trim().split(" ");
//				String aFirstName = aNameComponents[0].trim();
//				String aLastName = aNameComponents[aNameComponents.length - 1].trim();
//				for (String aGradebookOnyen: anOnyenToGradebook.keySet() ) {
//					GradebookEntry aTestEntry = anOnyenToGradebook.get(aGradebookOnyen);
////					String aGradebookFullName = aGradebookEntry.getFirstName() + " " + aGradebookEntry.getLastName();
//					if (aTestEntry.getFirstName().equals(aFirstName) && aTestEntry.getLastName().equals(aLastName)) {
//						anOnyen = aGradebookOnyen;
//						aGradebookEntry = aTestEntry;
//						break;
//					}
//				}
//				if (aGradebookEntry == null) {
//					System.out.println("did not find in Gradebook:" + anOnyen);
//					continue;
//				}
//			}
//			
////			DiaryEntry 
//			aLasEntry = anOnyenToDiaryEntry.get(anOnyen);
//			if (aLasEntry == null) {
//				if (isSummary()) {
//					aLasEntry = new ADiaryEntry(anActualDateTime,
//							anEmail, 
//							aRow[fullNameColumn()].trim(), 
//							0, 
//							0 
//							);
//				} else {
//				aLasEntry = new ADiaryEntry(anActualDateTime,
//						anEmail, 
//						aRow[fullNameColumn()].trim(), 
//						0, 
//						0, 
//						aRow[graderNameColumn()].trim(), 
//						aRow[graderCommentsColumn()].trim(),
//						aRow[diaryTextColumn()].trim()
//						);
//			}
//				aLasEntry.setGradebookEntry(aGradebookEntry);
//				anOnyenToDiaryEntry.put(anOnyen, aLasEntry);
//			}
//			
////			String anEmail = aRow[2];
//			int aDiaryGrade = Integer.parseInt(aRow[diaryGradeColumn()].trim());
//			int aQAGrade = Integer.parseInt(aRow[qAGradeColumn()].trim());
//			aLasEntry.incrementDiaryPoints(aDiaryGrade);
//			aLasEntry.incrementQuestionPoints(aQAGrade);
//			
////			String aGradebookRow = toGradebookRow(aGradebookEntry, aDiaryGrade);
////			aGradebookString.append(aGradebookRow);
//			
//			} catch (Exception e) {
//				System.err.println ("Ignoring row: " + Arrays.toString(aRow));
//				e.printStackTrace();
//			}
//
//		}
//		return anOnyenToDiaryEntry;
////		return aGradebookString.toString();
//
//	}
//	public  Map<String, DiaryEntry> detailedDiaryToMap(String anExpectedDate, String aInputLinesWithoutQuotes,
//			StringBuffer aGradebookTemplate,
//			String[] aSubstitutions) {
//		DateTime anExpectedDateTime = toDateTime(anExpectedDate);
//		
//		
//		Map<String, String> anEmailToOnyen = new HashMap();
//		for (String aSubstitution : aSubstitutions) {
//			String[] anOnyenAndEmail = aSubstitution.split(":");
//			anEmailToOnyen.put(anOnyenAndEmail[1], anOnyenAndEmail[0]);
//		}
////		String aInputLinesWithoutQuotes = aDiaryString.toString()
////				.replaceAll("\"", "");
//		String[] anInputLines = aInputLinesWithoutQuotes.toString().split("\n");
////		StringBuilder aGradebookString = new StringBuilder(anInputLines.length);
//
////		aGradebookString.append(toGradebookHeader());
//		Map<String, GradebookEntry> anOnyenToGradebook = gradebookToMap(aGradebookTemplate);
//		Map<String, DiaryEntry> anOnyenToDiaryEntry = new HashMap<>();
////		DiaryEntry aLastDiaryEntry = null;
//		for (int aRowNum = 0; aRowNum < anInputLines.length; aRowNum++) {
//			DiaryEntry aLastDiaryEntry = null;
//
//			String[] aRow = anInputLines[aRowNum].split(",");
//			String anEmail = aRow[ emailColumn()];
//			if (!anEmail.contains("@")) {
//				System.out.println("Ignoring row without email:" + Arrays.toString(aRow));
//				continue;
//			}
//
//			if (aRow.length < minColumns()) {
//				if (aLastDiaryEntry == null) {
//					System.out.println("Ignoring row with < 8 elements and no previous student row " + Arrays.toString(aRow));
//				} else {
//					aLastDiaryEntry.getDiaryText().append(toText(aRow));
//				}
////				System.out.println("Ignoring row with < 8 elements " + Arrays.toString(aRow));
//				continue;
//			}
//			try {
////			String anEmail = aRow[ emailColumn()];
//			String anOnyen = anEmailToOnyen.get(anEmail);
//			if (anOnyen == null) {
//				String[] aNameAndDomain = anEmail.split("@");
//				if (aNameAndDomain.length != 2) {
//					System.out.println("Ignoring row with no email in first column " + Arrays.toString(aRow));
//					continue;
//				}
//				anOnyen = aNameAndDomain[0];				
//			}	
//			DateTime anActualDateTime = null;
//			if (anExpectedDateTime != null || dateColumn() != -1) {
//			String anActualDate = aRow[dateColumn()];
//			 anActualDateTime = normalizedDateToDateTime(anActualDate);
//			if (!withinGradingDays(anExpectedDateTime, anActualDateTime)) {
//				continue;
//			}	
//			}
//						
//			GradebookEntry aGradebookEntry = anOnyenToGradebook.get(anOnyen);
//			if (aGradebookEntry == null) {
//				String[] aNameComponents = aRow[fullNameColumn()].trim().split(" ");
//				String aFirstName = aNameComponents[0].trim();
//				String aLastName = aNameComponents[aNameComponents.length - 1].trim();
//				for (String aGradebookOnyen: anOnyenToGradebook.keySet() ) {
//					GradebookEntry aTestEntry = anOnyenToGradebook.get(aGradebookOnyen);
////					String aGradebookFullName = aGradebookEntry.getFirstName() + " " + aGradebookEntry.getLastName();
//					if (aTestEntry.getFirstName().equals(aFirstName) && aTestEntry.getLastName().equals(aLastName)) {
//						anOnyen = aGradebookOnyen;
//						aGradebookEntry = aTestEntry;
//						break;
//					}
//				}
//				if (aGradebookEntry == null) {
//					System.out.println("did not find in Gradebook:" + anOnyen);
//					continue;
//				}
//			}
//			
////			DiaryEntry 
//			DiaryEntry aFirstDiaryEntry = anOnyenToDiaryEntry.get(anOnyen);
////			if (aLastDiaryEntry == null) {
//				if (isSummary()) {
//					aLastDiaryEntry = new ADiaryEntry(anActualDateTime,
//							anEmail, 
//							aRow[fullNameColumn()].trim(), 
//							0, 
//							0 
//							);
//				} else {
//				aLastDiaryEntry = new ADiaryEntry(anActualDateTime,
//						anEmail, 
//						aRow[fullNameColumn()].trim(), 
//						0, 
//						0, 
//						aRow[graderNameColumn()].trim(), 
//						aRow[graderCommentsColumn()].trim(),
//						aRow[diaryTextColumn()].trim()
//						);
//			}
//				aLastDiaryEntry.setGradebookEntry(aGradebookEntry);
//				if (aFirstDiaryEntry == null) {
//					aFirstDiaryEntry = aLastDiaryEntry;
//					anOnyenToDiaryEntry.put(anOnyen, aLastDiaryEntry);
//				} else {
//					aFirstDiaryEntry.lastDiaryEntry().setNextDiaryEntry(aLastDiaryEntry);
//				}
////				anOnyenToDiaryEntry.put(anOnyen, aLastDiaryEntry);
////			}
//			
////			String anEmail = aRow[2];
//			int aDiaryGrade = Integer.parseInt(aRow[diaryGradeColumn()].trim());
//			int aQAGrade = Integer.parseInt(aRow[qAGradeColumn()].trim());
//			aLastDiaryEntry.incrementDiaryPoints(aDiaryGrade);
//			aLastDiaryEntry.incrementQuestionPoints(aQAGrade);
//			
////			String aGradebookRow = toGradebookRow(aGradebookEntry, aDiaryGrade);
////			aGradebookString.append(aGradebookRow);
//			
//			} catch (Exception e) {
//				System.err.println ("Ignoring row: " + Arrays.toString(aRow));
//				e.printStackTrace();
//			}
//
//		}
//		return anOnyenToDiaryEntry;
////		return aGradebookString.toString();
//
//	}

}
