package grader.sakai;

import grader.basics.file.FileProxy;
import grader.basics.file.RootFolderProxy;
import grader.trace.sakai_bulk_folder.FinalGradeFileLoaded;
import grader.trace.sakai_bulk_folder.FinalGradeFileNotFound;

public class ASakaiGradeSpreadsheetExtractor implements GradeSpreadsheetExtractor {
    public static String GRADES_SPREADSHEET_NAME = "grades.csv";
    public static String ANON_GRADES_SPREADSHEET_NAME = "ANONGrades.csv";

    public FileProxy extractGradeSpreadsheet(RootFolderProxy rootBulkDownloadFolder, RootFolderProxy assignmentFolder) {
    	String anAssignmentFolderName = assignmentFolder.getAbsoluteName().replace("\\", "/") + "/";
//		String gradeSpreadsheetFullName = assignmentFolder.getAbsoluteName().replace("\\", "/") + "/" + GRADES_SPREADSHEET_NAME;
    	String gradeSpreadsheetFullName = anAssignmentFolderName + GRADES_SPREADSHEET_NAME;

    	FileProxy retVal = rootBulkDownloadFolder.getFileEntry(gradeSpreadsheetFullName);
    	if (retVal == null) {
   	 gradeSpreadsheetFullName = anAssignmentFolderName + ANON_GRADES_SPREADSHEET_NAME;
   	 retVal = rootBulkDownloadFolder.getFileEntry(gradeSpreadsheetFullName);
	}
    	if (retVal == null)
    		System.out.println(FinalGradeFileNotFound.newCase(gradeSpreadsheetFullName, this).getMessage());
    	else 
    		FinalGradeFileLoaded.newCase(gradeSpreadsheetFullName, this);
    	return retVal;
//    	return rootBulkDownloadFolder.getFileEntry(gradeSpreadsheetFullName);   	
    	
    }
//    FileProxy extractGradeSpreadsheet() {
//    	String anAssignmentFolderName = assignmentFolder.getAbsoluteName().replace("\\", "/") + "/";
////    	String gradeSpreadsheetFullName = assignmentFolder.getAbsoluteName().replace("\\", "/") + "/" + GRADES_SPREADSHEET_NAME;
//    	String gradeSpreadsheetFullName = anAssignmentFolderName + GRADES_SPREADSHEET_NAME;
//
//    	FileProxy retVal = rootBulkDownloadFolder.getFileEntry(gradeSpreadsheetFullName);
//    	if (retVal == null) {
//        	 gradeSpreadsheetFullName = anAssignmentFolderName + ANON_GRADES_SPREADSHEET_NAME;
//        	 retVal = rootBulkDownloadFolder.getFileEntry(gradeSpreadsheetFullName);
//    	}
//    		
//    	
//    	if (retVal == null)
//    		System.out.println(FinalGradeFileNotFound.newCase(gradeSpreadsheetFullName, this).getMessage());
//    	else 
//    		FinalGradeFileLoaded.newCase(gradeSpreadsheetFullName, this);
//    	return retVal;
//    	
//    	
//    }

}
