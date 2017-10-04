package grader.sakai;

import grader.file.FileProxy;
import grader.file.RootFolderCreatorFactory;
import grader.file.RootFolderProxy;
import grader.file.zipfile.AZippedRootFolderProxy;
import grader.project.flexible.FlexibleProject;
import grader.trace.sakai_bulk_folder.AssignmentRootFolderLoaded;
import grader.trace.sakai_bulk_folder.FinalGradeFileLoaded;
import grader.trace.sakai_bulk_folder.FinalGradeFileNotFound;
import grader.trace.sakai_bulk_folder.StudentFolderLoaded;
import grader.trace.sakai_bulk_folder.StudentFolderNamesSorted;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import util.trace.Tracer;


public class ASakaiBulkAssignmentFolder implements BulkAssignmentFolder {
    public static String DEFAULT_BULK_DOWNLOAD_FOLDER = "C:/Users/dewan/Downloads/bulk_download";
    public static String DEFAULT_GRADER_DATA_FOLDER= "-GraderData";

    public static String DEFAULT_ASSIGNMENT_NAME = "Assignment 11";
    public static String GRADES_SPREADSHEET_NAME = "grades.csv";
    boolean isAssignmentRoot;

    String bulkDownloadDirectory;
    String assignmentName;
    String mixedCaseAssignmentName;
    RootFolderProxy rootBulkDownloadFolder;
    boolean isZippedRootFolder;
    RootFolderProxy assignmentFolder;
    Set<String> studentFolderNames;
    Set<FlexibleProject> studentFolders;
//    FileProxy submissionFolder;
    FileProxy gradeSpreadsheet;
    Comparator<String> fileNameComparator;

    public ASakaiBulkAssignmentFolder(String aBulkDownloadFolder, String anAssignmentName,  Comparator<String> aFileNameComparator) {
        bulkDownloadDirectory = aBulkDownloadFolder;
        assignmentName = anAssignmentName;
        isAssignmentRoot = true;
        fileNameComparator = aFileNameComparator;
        initializeBullkDownloadChidren();
    }

//    public ASakaiBulkAssignmentFolder(String aBulkDownloadFolder) {
//    	this(aBulkDownloadFolder, true);
////        bulkDownloadDirectory = aBulkDownloadFolder;
////        initializeAssignmentData();
////        assignmentName = assignmentFolder.getLocalName();
////        mixedCaseAssignmentName = assignmentFolder.getMixedCaseLocalName();
//    }
     public ASakaiBulkAssignmentFolder(String aBulkDownloadFolder, boolean assignmentRoot, Comparator<String> aFileNameComparator) {
    	isAssignmentRoot = assignmentRoot;
        bulkDownloadDirectory = aBulkDownloadFolder;
        fileNameComparator = aFileNameComparator;
        initializeBullkDownloadChidren();
        assignmentName = assignmentFolder.getLocalName();
        mixedCaseAssignmentName = assignmentFolder.getMixedCaseLocalName();
    }

    void initializeBullkDownloadChidren() {
//        rootBulkDownloadFolder = ARootFolderCreator.createRootFolder(bulkDownloadDirectory);
        Tracer.info (this, " Initializing root folder" + bulkDownloadDirectory);
    	rootBulkDownloadFolder = RootFolderCreatorFactory.getSingleton().createRootFolder(bulkDownloadDirectory);

        isZippedRootFolder = rootBulkDownloadFolder instanceof AZippedRootFolderProxy;
        bulkDownloadDirectory = rootBulkDownloadFolder.getAbsoluteName(); // normalize name
        setAssignmentFolder();
        setGradeSpreadsheet();
        setStudentFolderNames();
    }
    @Override
	public void clear() {
    	System.out.println ("Clearing bulk assignment folder");
		rootBulkDownloadFolder.clear();
		assignmentFolder.clear();
		
	}
    public String getAssignmentName() {
        return assignmentName;
    }

    public String getMixedCaseAssignmentName() {
        return mixedCaseAssignmentName;
    }

    RootFolderProxy extractAssignmentFolder() {
    	if (isAssignmentRoot) return rootBulkDownloadFolder;
    	 return AssignmentFolderExtractorFactory.getSingleton().
         		extractAssignmentFolder(rootBulkDownloadFolder, assignmentName);
//        Set<String> childrenNames = rootBulkDownloadFolder.getChildrenNames();
//        for (String childName : childrenNames) {
//            FileProxy fileProxy = rootBulkDownloadFolder.getFileEntry(childName);
//            if (fileProxy.isDirectory()) return fileProxy;
//        }
//        return rootBulkDownloadFolder.getFileEntry(rootBulkDownloadFolder.getAbsoluteName() + "/" + assignmentName);
    }

    FileProxy extractGradeSpreadsheet() {
    	String gradeSpreadsheetFullName = assignmentFolder.getAbsoluteName().replace("\\", "/") + "/" + GRADES_SPREADSHEET_NAME;
    	FileProxy retVal = rootBulkDownloadFolder.getFileEntry(gradeSpreadsheetFullName);
    	if (retVal == null)
    		System.out.println(FinalGradeFileNotFound.newCase(gradeSpreadsheetFullName, this).getMessage());
    	else 
    		FinalGradeFileLoaded.newCase(gradeSpreadsheetFullName, this);
    	return retVal;
//    	return rootBulkDownloadFolder.getFileEntry(gradeSpreadsheetFullName);
    	
    	
    }

    public RootFolderProxy getAssignmentFolder() {
        return assignmentFolder;
    }

    void setAssignmentFolder() {
        assignmentFolder = extractAssignmentFolder();
//        assignmentFolder = AssignmentFolderExtractorFactory.getSingleton().
//        		extractAssignmentFolder(rootBulkDownloadFolder, assignmentName);

        AssignmentRootFolderLoaded.newCase(assignmentFolder.getAbsoluteName(), this);
    }
    
    public static boolean hasOnyenSyntax(String fileName) {
    	return fileName.length() > 0 &&  Character.isLetter(fileName.charAt(0)) && fileName.matches("\\w.*, .*\\(.*\\).*");
    }
    
    public static String extractOnyen(String fileName) {
    	return fileName.replaceAll(".*\\((.*)\\).*", "$1");
    }

    Set<String> extractStudentFolderNames() {
        Set<String> children = new HashSet(
                assignmentFolder.getChildrenNames());
        List<String> fileChildren = new ArrayList();
        for (String childName : children) {
            FileProxy fileProxy = rootBulkDownloadFolder.getFileEntry(childName);
            
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
        Set<String> sortedStudentFiles = new TreeSet<String>(fileNameComparator);
        StudentFolderNamesSorted.newCase(sortedStudentFiles, this);
        sortedStudentFiles.addAll(children);
        return sortedStudentFiles;
    }

    void setStudentFolderNames() {
//        studentFolderNames = extractStudentFolderNames();
        studentFolderNames = StudentFoldersExtractorFactory.getSingleton().
        		extractStudentFolderNames(rootBulkDownloadFolder, assignmentFolder, fileNameComparator);
        for (String folderName:studentFolderNames)
        	StudentFolderLoaded.newCase(folderName, this);
    }

    void setGradeSpreadsheet() {
//        gradeSpreadsheet = extractGradeSpreadsheet();
        gradeSpreadsheet = GradeSpreadsheetExtractorFactory.getSingleton().
        		extractGradeSpreadsheet(rootBulkDownloadFolder, assignmentFolder);
    }

    @Override
    public FileProxy getSpreadsheet() {
        return gradeSpreadsheet;
    }

    @Override
    public Set<String> getStudentFolderNames() {
        return studentFolderNames;
    }

    @Override
    public FileProxy getStudentFolder(String aName) {
        return rootBulkDownloadFolder.getFileEntry(aName);
    }

	

}
