package grader.assignment;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import grader.basics.settings.BasicGradingEnvironment;
import grader.config.AConfigurationManager;
import grader.config.StaticConfigurationUtils;
import grader.file.FileProxy;
import grader.file.FileProxyUtils;
import grader.file.filesystem.AFileSystemFileProxy;
import grader.file.filesystem.AFileSystemRootFolderProxy;
import grader.sakai.project.ASakaiProjectDatabase;
import grader.settings.AGraderSettingsManager;
import grader.settings.GraderSettingsManager;
import grader.settings.GraderSettingsManagerSelector;
import grader.trace.assignment_data.FeatureGradeFileCleared;
import grader.trace.assignment_data.FeatureGradeFileCreatedFromFinalGradeFile;
import grader.trace.assignment_data.FeatureGradeFileLoaded;
import grader.trace.assignment_data.FeatureGradeFileRestored;
import grader.trace.assignment_data.InputFileFound;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.misc.Common;
import util.trace.Tracer;

public class AnAssignmenDataFolder extends AFileSystemRootFolderProxy implements AssignmentDataFolder {
	public static final String DEFAULT_CONFIGURATION_FILE = "checks.xml";

	public static final String DEFAULT_REQUIREMENTS_SPREADHEET_NAME = "Requirements.csv";
    public static final String ID_FILE_NAME = "onyens.txt";
    public static final String DEFAULT_LOG_FILE_NAME = "log.txt";
    public static final String DEFAULT_SKIPPED_FILE_NAME = "skipped_onyens.txt";
    public static final String DEFAULT_GRADED_ID_FILE_NAME = "graded_onyens.txt";

    public static final String DEFAULT_INPUT_FOLDER_NAME = "input";
    public static final String DEFAULT_FEATURE_GRADE_FILE_NAME = "FeatureGrades.csv";
    public static final String DEFAULT_BACKUP_FEATURE_GRADE_FILE_NAME = "BackupFeatureGrades.csv";
    String idFileName = ID_FILE_NAME;
    String gradedIdFileName;
    String skippedIdFileName;
    String logFileName;
    String inputFolderName = DEFAULT_INPUT_FOLDER_NAME;
    String featureGradeFileName = DEFAULT_FEATURE_GRADE_FILE_NAME;
    String backupFeatureGradeFileName = DEFAULT_BACKUP_FEATURE_GRADE_FILE_NAME;
    String requirementsSpreadsheetFileName = DEFAULT_REQUIREMENTS_SPREADHEET_NAME;
    
    File originalFeatureGradeFile, backupFeatureGradeFile;
    String checkStyleConfigurationFileName;

  



	String idText;
    Set<String> inputFiles;
    List<String> studentIDs;
    FileProxy finalGradeFile, featureGradeFile;
    FileProxy requirementsSpreadsheetFile;


    public AnAssignmenDataFolder(String aRootFolderName, FileProxy aFinalGradeFile) {
        super(aRootFolderName);
        String userName = BasicGradingEnvironment.get().getUserName();
        if (userName != null && !userName.isEmpty())
        	featureGradeFileName =  userName + "_" + featureGradeFileName;
        finalGradeFile = aFinalGradeFile;
        if (rootFolder != null)
            initGraderData();
    }
    @Override
    public void clearLogFile() {
    	try {
			Common.writeText(getLogFileName(), "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static List<File> getMatchingFiles(File aFolder, String aRegex) {
    	List<File> aRetVal = new ArrayList();
    	File[] aFiles = aFolder.listFiles();
    	for (File aFile:aFiles) {
//    		System.out.println ("Examining file:" + aFile.getName());
    		if (aFile.getName().matches(aRegex)) {
//        		System.out.println ("Returning file:" + aFile.getName());

    			aRetVal.add(aFile);
    		}
    	}
    	return aRetVal;
    }
    public static File getMatchingFile(File aFolder, String aRegex) {
    	List<File> aRetVal = getMatchingFiles(aFolder, aRegex);
    	if (aRetVal.size() == 1)
    		return aRetVal.get(0);
    	return null;
    }

    void initGraderData() {

        FileProxy inputFolder = this.getFileEntryFromLocalName(inputFolderName);
        if (inputFolder != null)
            inputFiles = inputFolder.getChildrenNames();
        else
        	inputFiles = new HashSet<>();
        for (String inputFile:inputFiles) {
        	InputFileFound.newCase(inputFile, this);
        }
        FileProxy idFileProxy = getFileEntryFromLocalName(idFileName);
        featureGradeFile = getFileEntryFromLocalName(featureGradeFileName);
        gradedIdFileName = rootFolder.getAbsolutePath() + "/" + DEFAULT_GRADED_ID_FILE_NAME;
        skippedIdFileName = rootFolder.getAbsolutePath() + "/" + DEFAULT_SKIPPED_FILE_NAME;
        logFileName = rootFolder.getAbsolutePath() + "/" + DEFAULT_LOG_FILE_NAME;
//        checkStyleConfigurationFileName = rootFolder.getAbsolutePath() + "/"  + DEFAULT_CONFIGURATION_FILE;
        checkStyleConfigurationFileName = rootFolder.getAbsolutePath() + "/"  + StaticConfigurationUtils.getCheckStyleFile();
//        File aFoundFile =  getMatchingFile(rootFolder, ".*check.*xml");

        File aFile = new File(checkStyleConfigurationFileName);
        if (!aFile.exists()) {
//        	System.out.println("Searching for checkstyle file");
        	File aFoundFile = getMatchingFile(rootFolder, ".*check.*xml");
        	if (aFoundFile != null) {
        		aFile = aFoundFile;
        		checkStyleConfigurationFileName = aFoundFile.getAbsolutePath();
        		System.out.println ("Found check style file:" + checkStyleConfigurationFileName);
        		
        	}
        }
        if (!aFile.exists()) {
        	GraderSettingsManager graderSettingsManager = GraderSettingsManagerSelector
    				.getGraderSettingsManager();
    		String aModule = graderSettingsManager.getModule();
    		String aProblem = graderSettingsManager.getNormalizedProblem(aModule);
    		// "Comp401f17"
    		int aCourseIndex = "Comp".length();
    		int aSemesterIndex = aCourseIndex + "401".length();
    		
    		
    		String aCourseNumber = aModule.substring(aCourseIndex, aSemesterIndex);
    		String aSemester = aModule.substring(aSemesterIndex);
    		String aProblemNumber = aProblem.replace("Assignment", "a");
    		String aFileName = String.join("_", "unc_checks", aCourseNumber, aSemester, 
    				aProblemNumber) + ".xml";
    		aFile = new File("config" + "/checkstyle/" + aModule  + "/" + aFileName);
    		
    		
    		
    		
        }
        if (!aFile.exists()) {
        	System.err.println("Could not find checkstyle file:" + checkStyleConfigurationFileName);
        	checkStyleConfigurationFileName = AConfigurationManager.CONFIG_DIR + "/"  + DEFAULT_CONFIGURATION_FILE;
        	Tracer.warning("Using default checkstyle file:" + checkStyleConfigurationFileName );
        } else {
        	System.out.println("Using check style file:" + aFile.getAbsolutePath());
        	setCheckStyleConfigurationFileName(aFile.getAbsolutePath());
        }
        
        clearLogFile();
        requirementsSpreadsheetFile = getFileEntryFromLocalName(requirementsSpreadsheetFileName);
        initFeatureGradeFiles();
//        if (finalGradeFile != null && (featureGradeFile == null || !featureGradeFile.exists())) {
//            String fullFeatureGradeFileName = rootFolder.getAbsolutePath() + "/" + featureGradeFileName;
//            File featureFile = new File(fullFeatureGradeFileName);
//            File aFinalGradeFile = new File(finalGradeFile.getAbsoluteName());
//
//            try {
//                Files.copy(aFinalGradeFile.toPath(), featureFile.toPath(), REPLACE_EXISTING);
//                featureGradeFile = new AFileSystemFileProxy(this, new File(fullFeatureGradeFileName), this.getAbsoluteName());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
        studentIDs = FileProxyUtils.toList(idFileProxy);

    }
     void initFeatureGradeFiles() {
         String fullFeatureGradeFileName = rootFolder.getAbsolutePath() + "/" + featureGradeFileName;
         backupFeatureGradeFile = new File(rootFolder.getAbsolutePath() + "/" + backupFeatureGradeFileName);
         originalFeatureGradeFile = new File(fullFeatureGradeFileName);

    	if (finalGradeFile != null && (featureGradeFile == null || !featureGradeFile.exists())) {
//            String fullFeatureGradeFileName = rootFolder.getAbsolutePath() + "/" + featureGradeFileName;
//             originalFeatureGradeFile = new File(fullFeatureGradeFileName);
             
            //File aFinalGradeFile = new File(finalGradeFile.getAbsoluteName());
            File aFinalGradeFile = new File(finalGradeFile.getMixedCaseAbsoluteName());
            
            try {
                //System.out.println("(*) " + aFinalGradeFile.toPath() + ", " + originalFeatureGradeFile.toPath());
                //System.out.println("(*) " + Common.toText(aFinalGradeFile));
                Files.copy(aFinalGradeFile.toPath(), originalFeatureGradeFile.toPath(), REPLACE_EXISTING);
                FeatureGradeFileCreatedFromFinalGradeFile.newCase(originalFeatureGradeFile.getName(), aFinalGradeFile.getName(), this);            
                //featureGradeFile = new AFileSystemFileProxy(this, new File(fullFeatureGradeFileName), this.getAbsoluteName());
                featureGradeFile = new AFileSystemFileProxy(this, new File(fullFeatureGradeFileName), this.getMixedCaseAbsoluteName());
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (featureGradeFile.exists()) {
        	//FeatureGradeFileLoaded.newCase(featureGradeFile.getAbsoluteName(), this);
            FeatureGradeFileLoaded.newCase(featureGradeFile.getMixedCaseAbsoluteName(), this);
        } 
//        else {
//        	FinalGradeFileNotFound.newCase(aFileName, aFinder);
//        }
    	
//        backupFeatureGradeFile = new File(rootFolder.getAbsolutePath() + "/" + backupFeatureGradeFileName);
//        originalFeatureGradeFile = new File(fullFeatureGradeFileName);

    	
    }

    public Set<String> getInputFiles() {
        return inputFiles;
    }

    public List<String> getStudentIDs() {
        return studentIDs;
    }

    @Override
    public FileProxy getFeatureGradeFile() {
        return featureGradeFile;
    }

    public String getIdFileName() {
        return idFileName;
    }

//    public void setIdFileName(String idFileName) {
//        this.idFileName = idFileName;
//    }

    public String getGradedIdFileName() {
        return gradedIdFileName;
    }

    public void setGradedIdFileName(String gradedIdFileName) {
        this.gradedIdFileName = gradedIdFileName;
    }

    public String getSkippedIdFileName() {
        return skippedIdFileName;
    }

    public void setSkippedIdFileName(String skippedIdFileName) {
        this.skippedIdFileName = skippedIdFileName;
    }

    public String getLogFileName() {
        return logFileName;
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

    public String getInputFolderName() {
        return inputFolderName;
    }

    public void setInputFolderName(String inputFolderName) {
        this.inputFolderName = inputFolderName;
    }

    public String getFeatureGradeFileName() {
        return featureGradeFileName;
    }

    public void setFeatureGradeFileName(String featureGradeFileName) {
        this.featureGradeFileName = featureGradeFileName;
    }

    public String getIdText() {
        return idText;
    }

    public void setIdText(String idText) {
        this.idText = idText;
    }

    public FileProxy getFinalGradeFile() {
        return finalGradeFile;
    }

    public void setFinalGradeFile(FileProxy finalGradeFile) {
        this.finalGradeFile = finalGradeFile;
    }

    public void setInputFiles(Set<String> inputFiles) {
        this.inputFiles = inputFiles;
    }

    public void setStudentIDs(List<String> studentIDs) {
        this.studentIDs = studentIDs;
    }

    public void setFeatureGradeFile(FileProxy featureGradeFile) {
        this.featureGradeFile = featureGradeFile;
    }


    @Override
	public String getBackupFeatureGradeFileName() {
		return backupFeatureGradeFileName;
	}


    @Override
	public void setBackupFeatureGradeFileName(String backupFeatureGradeFileName) {
		this.backupFeatureGradeFileName = backupFeatureGradeFileName;
	}
    // throw exception instead?
    public boolean removeBackupFeatureGradeFile() {
//    	String fullBackupName = getAbsoluteName() + "/" + backupFeatureGradeFileName;
//    	File backup = new File(fullBackupName);
    	if (!backupFeatureGradeFile.exists()) return true;    	
    	return backupFeatureGradeFile.delete();
    }
    @Override
    public boolean removeFeatureGradeFile() {
    	boolean retVal = removeBackupFeatureGradeFile();
    	if (!retVal) return false;
//    	String fullFeatureName = getAbsoluteName() + "/" + featureGradeFileName;
//    	File original = new File (fullFeatureName);
//    	String fullBackupName = getAbsoluteName() + "/" + backupFeatureGradeFileName;
//    	File backup = new File(fullBackupName);    	
    	originalFeatureGradeFile.renameTo(backupFeatureGradeFile);
    	FeatureGradeFileCleared.newCase(originalFeatureGradeFile.getName(), this);
//    	initFeatureGradeFiles();
    	return true;   	
    	
    }
    @Override
    public boolean restoreFeatureGradeFile() {
    	if (!backupExists())
    		return false;
    	boolean retVal;
    	retVal = originalFeatureGradeFile.delete();
    	if (!retVal)
    		return false;
    	 retVal =  backupFeatureGradeFile.renameTo(originalFeatureGradeFile);
    	if (!retVal) 
    		return false;
    	initFeatureGradeFiles();
    	FeatureGradeFileRestored.newCase(originalFeatureGradeFile.getName(), this);
    	return true;
    	
    	
    }
    @Override
    public boolean backupExists() {
//    	String fullBackupName = getAbsoluteName() + "/" + backupFeatureGradeFileName;
//    	File backup = new File(fullBackupName);
    	return backupFeatureGradeFile.exists();
    	
    }
//    public boolean restoreFeatureGradeFile() {
//    	if (!backupExists)
//    		return;
//    	String fullBackupName = getAbsoluteName() + "/" + backupFeatureGradeFileName;
//    	File backup = new File(fullBackupName); 
//    	if (!retVal) return false;
//    	String fullFeatureName = getAbsoluteName() + "/" + featureGradeFileName;
//    	File original = new File (fullFeatureName);
//    	   	
//    	original.renameTo(backup);
//    	return true;   	
//    	
//    }



	public String getRequirementsSpreadsheetFileName() {
		return requirementsSpreadsheetFileName;
	}



	public void setRequirementsSpreadsheetFileName(
			String requirementsSpreadsheetFileName) {
		this.requirementsSpreadsheetFileName = requirementsSpreadsheetFileName;
	}



	public FileProxy getRequirementsSpreadsheetFile() {
		return requirementsSpreadsheetFile;
	}



	public void setRequirementsSpreadsheetFile(FileProxy requirementsSpreadsheetFile) {
		this.requirementsSpreadsheetFile = requirementsSpreadsheetFile;
	}
	@Override
	  public String getCheckStyleConfigurationFileName() {
			return checkStyleConfigurationFileName;
		}
	@Override
		public void setCheckStyleConfigurationFileName(
				String checkStyleConfigurationFileName) {
			this.checkStyleConfigurationFileName = checkStyleConfigurationFileName;
		}


}
