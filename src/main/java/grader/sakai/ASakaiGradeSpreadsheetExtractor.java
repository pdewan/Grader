package grader.sakai;

import grader.basics.file.FileProxy;
import grader.basics.file.RootFolderProxy;
import grader.trace.sakai_bulk_folder.FinalGradeFileLoaded;
import grader.trace.sakai_bulk_folder.FinalGradeFileNotFound;

public class ASakaiGradeSpreadsheetExtractor implements GradeSpreadsheetExtractor {
    public static String GRADES_SPREADSHEET_NAME = "grades.csv";
	public FileProxy extractGradeSpreadsheet(RootFolderProxy rootBulkDownloadFolder, RootFolderProxy assignmentFolder) {
    	String gradeSpreadsheetFullName = assignmentFolder.getAbsoluteName().replace("\\", "/") + "/" + GRADES_SPREADSHEET_NAME;
    	FileProxy retVal = rootBulkDownloadFolder.getFileEntry(gradeSpreadsheetFullName);
    	if (retVal == null)
    		System.out.println(FinalGradeFileNotFound.newCase(gradeSpreadsheetFullName, this).getMessage());
    	else 
    		FinalGradeFileLoaded.newCase(gradeSpreadsheetFullName, this);
    	return retVal;
//    	return rootBulkDownloadFolder.getFileEntry(gradeSpreadsheetFullName);   	
    	
    }

}
