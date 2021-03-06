package wrappers.grader.sakai.project;

import edu.emory.mathcs.backport.java.util.Arrays;
import framework.grading.ProjectRequirements;
import framework.utils.GraderSettings;
import framework.utils.GradingEnvironment;
import grader.assignment.AnAssignmenDataFolder;
import grader.navigation.sorter.AFileObjectSorter;
import grader.navigation.sorter.FileNameSorterSelector;
import grader.sakai.ASakaiStudentCodingAssignmentsDatabase;
import grader.sakai.project.ASakaiProjectDatabase;
import grader.trace.assignment_data.StudentFolderNamesWrittenInOnyenFile;
import grader.trace.sakai_bulk_folder.StudentFolderNamesSorted;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import util.misc.Common;

/**
 * This extends the project database class to support adding FrameworkProjectRequirements
 */
public class ProjectDatabaseWrapper extends ASakaiProjectDatabase {
	// replacing this with the configuration setting
//    private static final String GraderPath = "./GraderData/";
	
    private static final String GraderPath = GradingEnvironment.get().getDefaultAssignmentsDataFolderName() + "/";	

    private ProjectRequirements projectRequirements;
    private boolean projectsMade = false;
    private static boolean graderDataMade = false;
    private ASakaiStudentCodingAssignmentsDatabase studentAssignmentDatabase;

    /**
     * If you don't pass in any arguments, then it attempts to find/create the folders.
     * This requires that you register the download path in the GraderSettings singleton.
     */
    public ProjectDatabaseWrapper() {
        super(GraderSettings.get().get("path"), getDataFolder(),true);

        // We want to go alphabetically, so set the NavigationListCreator
        setNavigationListCreator(new AlphabeticNavigationList());
    }

    public ProjectDatabaseWrapper(String aBulkAssignmentsFolderName, String anAssignmentsDataFolderName) {
        super(aBulkAssignmentsFolderName, anAssignmentsDataFolderName, true);
    }

    /**
     * This attempts to find/make the data folder.
     *
     * @return The path of the data folder
     */
    /**
     * 
     * not sure if I should keep my version in the super class of this one by Josh, have both now, Josh's is perhaps better
     */
    private static String getDataFolder() {

        // Make sure the appropriate folder exists
    	String assignmentName = GradingEnvironment.get().getAssignmentName();
//        File dataFolder = new File(GraderPath + GradingEnvironment.get().getAssignmentName());
        File dataFolder = new File(GraderPath + assignmentName);

        if (GraderPath.startsWith("null")) return null;
        if (!graderDataMade) {
            dataFolder.mkdirs();

            // Make sure the onyens.txt file exists
            String onyens = "";
            Boolean include = false;
            Set<String> onyenSet = new TreeSet<String>();
            File files[] = new File(GraderSettings.get().get("path")).listFiles();
            Arrays.sort(files, new AFileObjectSorter(FileNameSorterSelector.getSorter())) ;

            //maybe this is the only sort we need now, at least we do not need it in getStudentIds, but will keep it for now
            StudentFolderNamesSorted.newCase(Common.arrayToArrayList(files), ProjectDatabaseWrapper.class);
    		
//            for (File file : new File(GraderSettings.get().get("path")).listFiles()) {
            for (File file : files) {

                if (file.isDirectory()) {
                    String name = file.getName();
                    String onyen = name.substring(name.indexOf("(") + 1, name.indexOf(")"));
                    onyenSet.add(onyen);
                }
            }
            for (String onyen: onyenSet) {
            	if (onyen.equals(GraderSettings.get().get("start")))
                    include = true;
                if (include)
                    onyens += (onyens.isEmpty() ? "" : "\n") + onyen;
                if (onyen.equals(GraderSettings.get().get("end")))
                    include = false;
            }
            if (include) { // did not find end
            	onyens = "";
            }
            try {
            	File file = new File(dataFolder, AnAssignmenDataFolder.ID_FILE_NAME);
//                FileUtils.writeStringToFile(new File(dataFolder, "onyens.txt"), onyens);
                FileUtils.writeStringToFile(file, onyens);

                StudentFolderNamesWrittenInOnyenFile.newCase(onyenSet, file.getAbsolutePath(), ProjectDatabaseWrapper.class);
            } catch (IOException e) {
                System.out.println("Error creating onyens.txt file.");
            }

            // Make sure the log.txt file exits
            try {
//                new File(dataFolder, "log.txt").createNewFile();
                new File(dataFolder, AnAssignmenDataFolder.DEFAULT_LOG_FILE_NAME).createNewFile();

            } catch (IOException e) {
                System.out.println("Error creating log.txt file.");
            }

            // Make sure that there is input
            // there is no need, AnAssignmentData folder simply sets the files to null set
//            File inputFolder = new File(dataFolder, "input");
//            inputFolder.mkdir();
//            if (inputFolder.listFiles().length == 0)
//            try {
//                FileUtils.writeStringToFile(new File(inputFolder, "Input1.txt"), "");
//            } catch (IOException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
            graderDataMade = true;
        }

//        return dataFolder.getParentFile().getAbsolutePath();
        String path = dataFolder.getParentFile().getAbsolutePath();
        return Common.toCanonicalFileName(path);
    }


}
