package grader.sakai;

import grader.basics.file.RootFolderProxy;

public interface AssignmentFolderExtractor {

	public abstract RootFolderProxy extractAssignmentFolder(
			RootFolderProxy aRootBulkDownloadFolder, String anAssignmentName);

}