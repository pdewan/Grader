package grader.sakai;

import java.util.Comparator;
import java.util.Set;

import grader.basics.file.RootFolderProxy;

public interface StudentFoldersExtractor {

	public abstract Set<String> extractStudentFolderNames(
			RootFolderProxy aRootBulkDownloadFolder,
			RootFolderProxy anAssignmentFolder,
			Comparator<String> aFileNameComparator);

}