package grader.sakai;

import grader.basics.file.FileProxy;
import grader.basics.file.RootFolderProxy;
import grader.basics.file.filesystem.AFileSystemFileProxy;
import grader.basics.file.filesystem.AFileSystemRootFolderProxy;
import grader.basics.file.zipfile.AZippedRootFolderProxy;
import grader.basics.trace.ProjectFolderNotFound;
import grader.project.flexible.AFlexibleProject;
import grader.project.flexible.FlexibleProject;
import grader.trace.project.ProjectFolderAssumed;
import grader.trace.project.RubrickFileLoaded;
import util.trace.Tracer;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

public class ASakaiStudentCodingAssignment extends ASakaiStudentAssignment implements StudentCodingAssignment {

    public static final String RUBRICK_SUBSTRING = "rubric";

    FileProxy rubrick;
    RootFolderProxy projectFolder;
    FlexibleProject project;
    FileProxy zipFile;

    

	public FlexibleProject getProject() {
        return project;
    }

    public void setProject(FlexibleProject newVal) {
        this.project = newVal;
    }
    // this needs a constructor that takes a file name instead of a FileProxy
    public ASakaiStudentCodingAssignment(String aFolderName, FileProxy aFileProxy) {
        super(aFolderName, aFileProxy);
        if (isSubmitted()) {
            findRubrickAndProject();
        }
    }
    
    RootFolderProxy searchForProjectFolder (RootFolderProxy aFolder) {
    	Set<String> childrenNames = aFolder.getChildrenNames();
    	for (String aChildName:childrenNames) {
    		if (aChildName.equals("src")) {
    			return aFolder;
    		}
    	}
    	List<FileProxy> aChildren = aFolder.getFileEntries();
    	for (FileProxy aChild:aChildren) {
    		if (isZipName(aChild.getLocalName())) {
    			RootFolderProxy aChildRootFolder = new AZippedRootFolderProxy(aChild.getMixedCaseAbsoluteName());
    			return searchForProjectFolder(aChildRootFolder);
    		}
    		
    		if (aChild.isDirectory()) {
    			return searchForProjectFolder(aChild);
    		}
    	}
    	return null;
    	
    }

    FileProxy getZipChild(FileProxy aFolder) {
        Set<String> childrenNames = aFolder.getChildrenNames();
        for (String childName : childrenNames) {
        	String shortFileName = Paths.get(childName).getFileName().toString();
        	if (shortFileName.startsWith(".")) {
        		continue;
        	}
//            if (childName.endsWith(".zip")) {
            if (childName.endsWith(AFlexibleProject.ZIP_SUFFIX_1) || 
            		childName.endsWith(AFlexibleProject.ZIP_SUFFIX_2)) {

                return submissionFolder.getFileEntry(childName);
            }
        }
        return null;

    }
    
    protected boolean isZipName (String childName) {
    	return childName.endsWith(AFlexibleProject.ZIP_SUFFIX_1) || 
        		childName.endsWith(AFlexibleProject.ZIP_SUFFIX_2);
    }

    FileProxy getUniqueNonMACOSFolderChild(FileProxy aFolder) {
        Set<String> childrenNames = aFolder.getChildrenNames();
        FileProxy folderChild = null;
        
        for (String childName : childrenNames) {
            FileProxy child = submissionFolder.getFileEntry(childName);
            if (child.isDirectory() &&
            		childName.indexOf("macosx") == -1 &&
            		childName.indexOf("ogs") == -1 &&
            		childName.indexOf("onfig") == -1) { // all names seem to be lowercase so let us not convert again
                if (folderChild != null) {
                    return aFolder;
                } else {
                    folderChild = child;
                }
            }
        }
        if (folderChild == null) {
            ProjectFolderNotFound.newCase(submissionFolder.getLocalName(), this);
//            folderChild = submissionFolder;
            folderChild = aFolder;
            
            System.out.println("Assuming project folder is:" + aFolder.getAbsoluteName());
            ProjectFolderAssumed.newCase(submissionFolder.getLocalName(), this);

        }
        return folderChild;

    }

    FileProxy getUnzippedFolder(FileProxy aFolder, FileProxy zipFile) {
        String name = zipFile.getParentRelativeName();
        String normalizedName = name.substring(0, name.indexOf(".")).toLowerCase();
        Set<String> childrenNames = aFolder.getChildrenNames();
        FileProxy folderChild = null;
        
        for (String childName : childrenNames) {
            FileProxy child = submissionFolder.getFileEntry(childName);
            if (child == zipFile) {
                continue;
            }
            if (child.getParentRelativeName().toLowerCase().equals(normalizedName)) {
                return child;
            }
        }
        
        return null;

    }

    FileProxy findRubrick(FileProxy aFolder) {
        Set<String> childrenNames = aFolder.getChildrenNames();
//    	FileProxy retVal;
        for (String childName : childrenNames) {
            FileProxy childProxy = submissionFolder.getFileEntry(childName);
            if (childName.toLowerCase().indexOf(RUBRICK_SUBSTRING) > -1) {
                RubrickFileLoaded.newCase(childName, this);
                return childProxy;
            }
        }
        return null;
    }
    
    boolean isGradescopeFolder(String aSubmissionFolderName) {
    	return (!aSubmissionFolderName.endsWith(".zip") &&
    			aSubmissionFolderName.contains("autograder"));
    	
    }
    public static File findChildFile(File aFolder, String aChildName) {
//    	File dir = new File(directory);
//    	FilenameFilter foo;
    	if (!aFolder.isDirectory()) {
    		return null;
    	}

    	File[] matches = aFolder.listFiles(new FilenameFilter()
    	{
    	  public boolean accept(File dir, String name)
    	  {
    	     return name.equals("aChildName");
    	  }
    	});
    	if (matches.length == 1) {
    		return matches[0];
    	}
    	
    		return null;
    	
    }
    /*
     * Project folder:/autograder/source/Assignment5/grade, me(student)/Submission attachment(s)/submission/autograder
Buildfolder:/autograder/source/Assignment5/grade, me(student)/Submission attachment(s)/submission/autograder/submission/F22A5Fixed/bin classpath: .:..::.:/autograder/source/Comp524GraderAll.jar:.:./source/AssignmentSetup.jar:.:/autograder/source/Comp524GraderAll.jar:.::./source/AssignmentSetup.jar
Project folder:/autograder/source/Assignment5/grade, me(student)/Submission attachment(s)/submission.zip
Project folder:/autograder/source/Assignment5/grade, me(student)/Submission attachment(s)/submission/autograder
Buffer traced messages =true
     */
//    private static final String GRADESOPE_SUFFIX = 
    File findChild (String aFileName, String aChild) {
    	String aChildFileName = aFileName + "/" + aChild;
    	File retVal = new File (aChildFileName);
    	if (retVal.exists()) {
    		return retVal;
    	}
    	return null;
    }
    RootFolderProxy maybeToGradescopeProjectFolder(RootFolderProxy aProjectFolder) {
    	String aProjectFolderName = aProjectFolder.getAbsoluteName();
    	if (!aProjectFolderName.contains("grade, me")) {
    		return aProjectFolder;
    	}
    	System.out.println("Gradescope project folder" + aProjectFolderName);
//    	FileProxy aSubmissionChild = aProjectFolder.getFileEntry("submission");
//    	if (aSubmissionChild != null) {
//    		List<FileProxy> aChildren = aSubmissionChild.getChildren();
//    		if (aChildren.size() == 1) {
//    			return aChildren.get(0);
//    		}
////    		return aSubmissionChild;
//    	};
//    	return aProjectFolder;
//    	String aSubmissionChildFolderName = aProjectFolderName+"/submission";
//    	File aProjectFolderFile = new File(aProjectFolderName);
    	File aChild = findChild(aProjectFolderName, "submission");
    	if (aChild == null) 
    		aChild = findChild(aProjectFolderName, "autograder/submission"); 
    	if (aChild != null) {
    		File[] aFiles = aChild.listFiles();
    		for (File aFile:aFiles) {
    			if (aFile.isDirectory()) {
    				return new AFileSystemRootFolderProxy(aFile);
    			}
    		}
    	
//    		return aSubmissionChildFolder;
    	}    	
    	
//    	File[] aProjectFiles = aProjectFolderFile.listFiles();
//    	File aSubmissionChildFolder = new File (aSubmissionChildFolderName);
//    	if (aSubmissionChildFolder.exists()) {
//    		File[] aFiles = aSubmissionChildFolder.listFiles();
//    		if (aFiles.length == 0) {
//    			return new AFileSystemFileProxy(aFiles[0]);
//    		}
////    		return aSubmissionChildFolder;
//    	}    	
    	return aProjectFolder;      	
    }
    

    void findRubrickAndProject() {
//    	submissionFolder = toGradescopeProjectFolder(submissionFolder);
    	rubrick = findRubrick(submissionFolder);
//        FileProxy zipFile = getZipChild(submissionFolder);
        zipFile = getZipChild(submissionFolder);
        String aSubmissionFolderName = submissionFolder.getAbsoluteName();
      
//        File aSubmissionFolderFile = new File(aSubmissionFolderName);
        
        
        if (zipFile == null) {
            projectFolder = getUniqueNonMACOSFolderChild(submissionFolder);
        } else {
            FileProxy unzippedFolder = getUnzippedFolder(submissionFolder, zipFile);
            if (unzippedFolder == null) {
            	Tracer.info (this, "Did not find unzipped folder, processing zip file:" + zipFile);
//                projectFolder = new AZippedRootFolderProxy(zipFile.getAbsoluteName());
                
                
            	projectFolder = new AZippedRootFolderProxy(zipFile.getMixedCaseAbsoluteName());
//            	projectFolder = new AZippedRootFolderProxy(aProjectProxy.getMixedCaseAbsoluteName());

            } else {
            	Tracer.info (this, "Found unzipped folder, processing zip folder:" + unzippedFolder);

//            	System.out.println ("Found unzipped folder, processing zip folder:" + unzippedFolder);
                projectFolder = getUniqueNonMACOSFolderChild(unzippedFolder);
                if (projectFolder == null) {
                    projectFolder = unzippedFolder; // not sure if this is ever reasonable
                }
            }
//            RootFolderProxy searchedProjectChild =  searchForProjectFolder(projectFolder);
//            if (searchedProjectChild != null) {
//            	projectFolder = searchedProjectChild;
//            }

        }
        if (projectFolder == null) {
            System.out.println("!!! " + ProjectFolderNotFound.newCase(submissionFolder.getAbsoluteName(), this).getMessage());
            projectFolder = submissionFolder;
//    		Tracer.error("No project folder found in " + submissionFolder.getAbsoluteName());
        }
//       projectFolder = maybeToGradescopeProjectFolder(projectFolder);
//        System.out.println("Project folder:" + projectFolder);

    }
//    void findRubrickAndProjectOld() {
//        Set<String> childrenNames = submissionFolder.getChildrenNames();
//        for (String childName : childrenNames) {
//            FileProxy childProxy = submissionFolder.getFileEntry(childName);
//            if (childName.toLowerCase().indexOf(RUBRICK_SUBSTRING) > -1) {
//                rubrick = childProxy;
//            } else if (childProxy.isDirectory() ) {
//            	Set<String> grandChildrenNames = childProxy.getChildrenNames();
//            	if (childrenNames.size() == 0) continue;
//            	String localChildName = FileUtils.toRelativeName(submissionFolder.getLocalName(), childProxy.getLocalName()).toLowerCase();
//            	for (String grandChildName:grandChildrenNames) { // look for unzipped project folder
//            		 FileProxy grandChildProxy = submissionFolder.getFileEntry(grandChildName);
//            		String localGrandChildName = FileUtils.toRelativeName(childProxy.getLocalName(), grandChildProxy.getLocalName()).toLowerCase();
//            		if (localGrandChildName.equals(localChildName)) {
//            			projectFolder = grandChildProxy;
//            			break;
//            		}
//            	}
//            	if (projectFolder != null) 
//            		continue;
//            	projectFolder = childProxy;
//            } else if (childProxy.getAbsoluteName().endsWith(".zip") && projectFolder == null) { // unzipped wins
//                projectFolder = new AZippedRootFolderProxy(childProxy.getAbsoluteName());
//            }
//        }
//    }

    public FileProxy getRubrick() {
        return rubrick;
    }

    public RootFolderProxy getProjectFolder() {
        return projectFolder;
    }
    @Override
    public FileProxy getZipFile() {
		return zipFile;
	}
    @Override
	public void setZipFile(FileProxy zipFile) {
		this.zipFile = zipFile;
	}
}
