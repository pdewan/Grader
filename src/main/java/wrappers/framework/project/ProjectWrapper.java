package wrappers.framework.project;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import framework.navigation.SakaiStudentFolder;
import framework.navigation.StudentFolder;
import framework.project.StandardProject;
import framework.utils.GraderSettings;
import grader.basics.project.source.BasicTextManager;
import grader.basics.trace.file.load.FileUnzipped;
import grader.basics.util.DirectoryUtils;
import grader.project.flexible.AFlexibleProject;
import grader.project.flexible.FlexibleProject;
import grader.sakai.project.ASakaiProjectDatabase;
import grader.sakai.project.SakaiProject;
import grader.sakai.project.SakaiProjectDatabase;
import util.trace.Tracer;

/**
 * This transforms a "grader" project into a "framework" project.
 * This class and {@link wrappers.framework.grading.testing.TestCaseWrapper} are needed for the
 * {@link wrappers.grader.sakai.project.ProjectStepperDisplayerWrapper} to work properly.
 */
public class ProjectWrapper extends StandardProject {

    // changed to SakaiProject
    public ProjectWrapper(SakaiProject project, String name) throws FileNotFoundException {
        super(project, getDirectoryAndMaybeUnzip(project), name, null);
    	project.setWrapper(this);

//        this.project = project;
    }
    public String toString() {
    	return project.toString();
    }

    public SakaiProject getProject() {
        return (SakaiProject) project;
    }

    /**
     * Given a grader project, this figures out what folder the project is in, extracting the zip file if necessary.
     *
     * @param project The grader project
     * @return The folder of the project
     * @throws FileNotFoundException
     */
    public static File getDirectoryAndMaybeUnzip(SakaiProject project) throws FileNotFoundException {
//    	if (project.isNoProjectFolder()) {
//            return null;
//        }
    	
//    	File retVal = getDirectoryAndMaybeUnzip(project.getRootCodeFolder().getMixedCaseAbsoluteName());
    	File retVal = getDirectoryAndMaybeUnzip(project.getProjectZipFileOrFolderMixedCaseAbsoluteName());

    	project.setFilesUnzipped(true);
    	return retVal;
        
//        // Can be a path or a directory
//        //System.out.println(")()()()()( " + project.getRootCodeFolder().getAbsoluteName());
//        //File path = new File(project.getProjectFolderName());
//        File path = new File(project.getRootCodeFolder().getMixedCaseAbsoluteName());
//        //System.out.println("()()()()() " + path.getAbsolutePath());
//        if (path.isFile()) {
////            if (path.getName().endsWith(".zip")) {
//            if (path.getName().endsWith(AProject.ZIP_SUFFIX_1) || path.getName().endsWith(AProject.ZIP_SUFFIX_2)) {
//
//                // A zip file, so unzip
////                File dir = new File(path.getParentFile(), path.getName().replace(".zip", ""));
//            	String aFileName = path.getName().replace(AProject.ZIP_SUFFIX_1, "").replace(AProject.ZIP_SUFFIX_2, "");
////                File dir = new File(path.getParentFile(), path.getName().replace(AProject.ZIP_SUFFIX_1, ""));
//
//            	File dir = new File(path.getParentFile(), aFileName);
//
//                if (dir.exists()) {
//                    return dir;
//                }
//                dir.mkdir();
//
//                try {
//                    ZipFile zip = new ZipFile(path);
//                    zip.extractAll(dir.getAbsolutePath());
//                    FileUnzipped.newCase(path.getName(), ProjectWrapper.class);
//                    return dir;
//                } catch (ZipException e) {
//                    throw new FileNotFoundException();
//                }
//            } else {
//                throw new FileNotFoundException();
//            }
//        } else {
//            return path;
//            }
    }
    
    
    /**
     * Given a  zip file, unzips it and returns the file object
     *
     * @param project The grader project
     * @return The folder of the project
     * @throws FileNotFoundException
     */
    public static File getDirectoryAndMaybeUnzip(String aZipFileName) throws FileNotFoundException {
    	if (aZipFileName == null) {
            return null;
        }
        
        // Can be a path or a directory
        //System.out.println(")()()()()( " + project.getRootCodeFolder().getAbsoluteName());
        //File path = new File(project.getProjectFolderName());
        File path = new File(aZipFileName);
        Tracer.info(ProjectWrapper.class, "got path:" + path);

//        System.out.println("got path:" + path);

        //System.out.println("()()()()() " + path.getAbsolutePath());
        if (path.isFile()) {
//            System.out.println("is File:" + path);

//            if (path.getName().endsWith(".zip")) {
            if (path.getName().endsWith(AFlexibleProject.ZIP_SUFFIX_1) || path.getName().endsWith(AFlexibleProject.ZIP_SUFFIX_2)) {
//                System.out.println("is Zip suffix:" + path);

                // A zip file, so unzip
//                File dir = new File(path.getParentFile(), path.getName().replace(".zip", ""));
            	String aFileName = path.getName().replace(AFlexibleProject.ZIP_SUFFIX_1, "").replace(AFlexibleProject.ZIP_SUFFIX_2, "");
//                File dir = new File(path.getParentFile(), path.getName().replace(AProject.ZIP_SUFFIX_1, ""));

            	File dir = new File(path.getParentFile(), aFileName);

                if (dir.exists()) {
//                    System.out.println("Directory exisrts:" + dir);

                    return dir;
                }
                dir.mkdir();
//                System.out.println("Made directory:" + dir);

                try {
                	Tracer.info(ProjectWrapper.class, "unzipping:" + path);

//                	System.out.println ("unzipping:" + path);
                    ZipFile zip = new ZipFile(path);
                    extractFolder(path.getAbsolutePath());
//                    ZipReader.readRecursive(path, dir);
//                    zip.extractAll(dir.getAbsolutePath());
                    FileUnzipped.newCase(path.getName(), ProjectWrapper.class);
                    return dir;
                } catch (IOException e) {
                    FileNotFoundException f = new FileNotFoundException();
                    f.initCause(e);
                    throw f;
                } 
            } else {
                throw new FileNotFoundException();
            }
        } else {
            return path;
        }
    }

    /**
     * Given a grader project, this figures out where the student folder is.
     *
     * @param project The grader project
     * @return The student folder
     */
    public static StudentFolder getStudentFolder(FlexibleProject project) {
        File path = new File(project.getProjectFolderName());
        return new SakaiStudentFolder(path.getParentFile().getParentFile());
    }

    public static StudentFolder getStudentFolder(final String onyen) {
        File folder = DirectoryUtils.find(new File(GraderSettings.get().get("path")), new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().contains("(" + onyen + ")");
            }
        }).get();
        return new SakaiStudentFolder(folder);
    }
    @Override
    public  String getSource() {
    	
		return project.
				getClassesTextManager().getEditedAllSourcesText(project.getSourceFileName());
		
//		return Common.toText(aRunningProject.getProject().getSourceFileName()).toString();
	}
    public BasicTextManager getTextManager() {
    	return project.getClassesTextManager();
    }
    public String getAssignmentDataFolderName() {
		return ASakaiProjectDatabase.getCurrentSakaiProjectDatabase().getAssignmentDataFolder().getMixedCaseAbsoluteName();
		
	}
    static public void extractFolder(String zipFile) throws ZipException, IOException 
    {
        Tracer.info(ProjectWrapper.class, "Extracting folder from" + zipFile);

//        System.out.println(zipFile);
        int BUFFER = 2048;
        File file = new File(zipFile);

        ZipFile zip = new ZipFile(file);
        String newPath = zipFile.substring(0, zipFile.length() - 4);

        new File(newPath).mkdir();
        Enumeration zipFileEntries = zip.entries();

        // Process each entry
        while (zipFileEntries.hasMoreElements())
        {
            // grab a zip file entry
            ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
            String currentEntry = entry.getName();
            File destFile = new File(newPath, currentEntry);
            //destFile = new File(newPath, destFile.getName());
            File destinationParent = destFile.getParentFile();

            // create the parent directory structure if needed
            destinationParent.mkdirs();

            if (!entry.isDirectory())
            {
                BufferedInputStream is = new BufferedInputStream(zip
                .getInputStream(entry));
                int currentByte;
                // establish buffer for writing file
                byte data[] = new byte[BUFFER];

                // write the current file to disk
                FileOutputStream fos = new FileOutputStream(destFile);
                BufferedOutputStream dest = new BufferedOutputStream(fos,
                BUFFER);

                // read and write until last byte is encountered
                while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, currentByte);
                }
                dest.flush();
                dest.close();
                is.close();
            }

            if (currentEntry.endsWith(".zip"))
            {
                // found a zip file, try to open
                extractFolder(destFile.getAbsolutePath());
            }
        }
    }
  
    
}
