package grader.sakai;

import grader.basics.file.FileProxy;
import grader.basics.file.RootFolderProxy;

public interface GradeSpreadsheetExtractor {
	public FileProxy extractGradeSpreadsheet(RootFolderProxy rootBulkDownloadFolder, RootFolderProxy assignmentFolder);

}