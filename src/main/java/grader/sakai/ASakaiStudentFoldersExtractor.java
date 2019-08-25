package grader.sakai;

import grader.basics.file.FileProxy;
import grader.basics.file.RootFolderProxy;
import grader.trace.sakai_bulk_folder.StudentFolderNamesSorted;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ASakaiStudentFoldersExtractor implements StudentFoldersExtractor {
	@Override
	public Set<String> extractStudentFolderNames(RootFolderProxy aRootBulkDownloadFolder, 
			RootFolderProxy anAssignmentFolder,
			Comparator<String> aFileNameComparator) {
        Set<String> children = new HashSet(
                anAssignmentFolder.getChildrenNames());
        List<String> fileChildren = new ArrayList();
        for (String childName : children) {
            FileProxy fileProxy = aRootBulkDownloadFolder.getFileEntry(childName);
            
            if (!fileProxy.isDirectory() || !hasOnyenSyntax(fileProxy.getLocalName())) { // can add grader data or other info to downloaded folder
                fileChildren.add(childName);
            }
        }
        for (String fileChild : fileChildren) {
            children.remove(fileChild);
        }
        
//        Set<String> sortedStudentFiles = new TreeSet<String>(new Comparator<String>() {
//
//			@Override
//			public int compare(String s1, String s2) {
//				String onyen1 = s1.substring(s1.lastIndexOf('(') + 1,
//						s1.lastIndexOf(')'));
//				String onyen2 = s2.substring(s2.lastIndexOf('(') + 1,
//						s2.lastIndexOf(')'));
//				return onyen1.compareTo(onyen2);
//			}
//        }); 
        Set<String> sortedStudentFiles = new TreeSet<String>(aFileNameComparator);
        StudentFolderNamesSorted.newCase(sortedStudentFiles, this);
        sortedStudentFiles.addAll(children);
        return sortedStudentFiles;
    }
	 public static boolean hasOnyenSyntax(String fileName) {
	    	return fileName.length() > 0 &&  Character.isLetter(fileName.charAt(0)) && fileName.matches("\\w.*, .*\\(.*\\).*");
	    }

}
