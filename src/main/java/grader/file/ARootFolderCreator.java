package grader.file;

import grader.file.filesystem.AFileSystemRootFolderProxy;
import grader.file.zipfile.AZippedRootFolderProxy;
import grader.project.flexible.AFlexibleProject;

public class ARootFolderCreator implements RootFolderCreator {
    public  RootFolderProxy createRootFolder(String aFolder) {
        boolean isZipperFolder = aFolder.endsWith(AFlexibleProject.ZIP_SUFFIX_1) || aFolder.endsWith(AFlexibleProject.ZIP_SUFFIX_2);
        
        if (isZipperFolder) {
        	System.out.println ("Creating zipped folder:" + aFolder + " with siffx2 " + AFlexibleProject.ZIP_SUFFIX_2);
            return new AZippedRootFolderProxy(aFolder);
        } else {
            return new AFileSystemRootFolderProxy(aFolder);
        }
    }

}
