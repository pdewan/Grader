package grader.sakai;

import grader.basics.file.FileProxy;
import grader.basics.file.RootFolderProxy;
import grader.basics.file.zipfile.AZippedRootFolderProxy;
import grader.basics.trace.ProjectFolderNotFound;
import grader.project.flexible.AFlexibleProject;
import grader.project.flexible.FlexibleProject;
import grader.trace.project.ProjectFolderAssumed;
import grader.trace.project.RubrickFileLoaded;
import util.trace.Tracer;

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
            if (child.isDirectory() && childName.indexOf("macosx") == -1) { // all names seem to be lowercase so let us not convert again
                if (folderChild != null) {
                    return aFolder;
                } else {
                    folderChild = child;
                }
            }
        }
        if (folderChild == null) {
            ProjectFolderNotFound.newCase(submissionFolder.getLocalName(), this);
            folderChild = submissionFolder;
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

    void findRubrickAndProject() {
    	rubrick = findRubrick(submissionFolder);
//        FileProxy zipFile = getZipChild(submissionFolder);
        zipFile = getZipChild(submissionFolder);
        
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
