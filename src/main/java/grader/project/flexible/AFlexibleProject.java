package grader.project.flexible;

import framework.grading.testing.Checkable;
import framework.grading.testing.TestCase;
import framework.logging.loggers.FeedbackTextSummaryLogger;
import framework.project.StandardProject;
import grader.basics.execution.CommandGenerator;
import grader.basics.execution.RunningProject;
import grader.basics.file.FileProxy;
import grader.basics.file.RootFolderCreator;
import grader.basics.file.RootFolderProxy;
import grader.basics.file.filesystem.AFileSystemRootFolderProxy;
import grader.basics.file.zipfile.AZippedRootFolderProxy;
import grader.basics.project.source.BasicTextManager;
import grader.basics.settings.BasicGradingEnvironment;
import grader.basics.trace.ProjectFolderNotFound;
import grader.config.StaticConfigurationUtils;
import grader.execution.AProxyProjectClassLoader;
import grader.execution.FlexibleMainClassFinder;
import grader.execution.ProjectRunnerSelector;
import grader.execution.ProxyBasedClassesManager;
import grader.execution.ProxyClassLoader;
import grader.language.LanguageDependencyManager;
import grader.project.folder.ARootCodeFolder;
import grader.project.folder.RootCodeFolder;
import grader.project.source.AClassesTextManager;
import grader.project.source.ClassesTextManager;
import grader.project.view.AClassViewManager;
import grader.project.view.ClassViewManager;
import grader.sakai.StudentCodingAssignment;
import grader.sakai.project.SakaiProject;
import grader.trace.execution.MainClassFound;
import grader.trace.execution.MainClassNotFound;
import grader.trace.execution.MainMethodNotFound;
import grader.trace.overall_transcript.OverallTranscriptCleared;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.misc.Common;
import wrappers.framework.project.ProjectWrapper;

import com.thoughtworks.qdox.JavaDocBuilder;

public class AFlexibleProject implements FlexibleProject {

//    public static final String ZIP_SUFFIX_1 = ".zip";
//    public static final String ZIP_SUFFIX_2 = ".jar";
    public static final String ZIP_SUFFIX_1 = RootFolderCreator.ZIP_SUFFIX_1;
    public static final String ZIP_SUFFIX_2 = RootFolderCreator.ZIP_SUFFIX_2;
//    public static final String ZIP_SUFFIX_2 = ".zip"; // forget about jars, but keep another suffix

    public static final String DEFAULT_PROJECT_FOLDER = ".";
    public static final String DEFAULT_GRADING_FOLDER = "C:/Users/dewan/Downloads/GraderData";
    public static final String DEFAULT_TRANSCRIPT_FILE_PREFIX = "transcript";
    public static final String DEFAULT_TRANSCRIPT_FILE_SUFFIX = ".txt";
    public static final String DEFAULT_TRASNCRIPT_FILE_NAME = DEFAULT_TRANSCRIPT_FILE_PREFIX + DEFAULT_TRANSCRIPT_FILE_SUFFIX;
//    public static final String PROJECT_DIRECTORY = "D:/dewan_backup/Java/AmandaKaramFinalUpdated/Final";
//    public static final String PROJECT_ZIPPED_DIRECTORY = "D:/dewan_backup/Java/AmandaKaramFinalUpdated.zip";
    public static final String DEFAULT_CHECK_STYLE_FILE_PREFIX = "checkstyle";
    public static final String DEFAULT_CHECK_STYLE_FILE_SUFFIX = ".txt";
    public static final String DEFAULT_CHECK_STYLE__NAME = DEFAULT_CHECK_STYLE_FILE_PREFIX + DEFAULT_CHECK_STYLE_FILE_SUFFIX;
    String projectFolderName = DEFAULT_PROJECT_FOLDER;
    String gradingProjectFolderName = DEFAULT_GRADING_FOLDER;
    ProxyBasedClassesManager classesManager;
    ClassViewManager classViewManager;
    ClassesTextManager classesTextManager;
    boolean zippedFolder;
    RootCodeFolder rootCodeFolder;
    RootFolderProxy rootFolder;
    ProxyClassLoader proxyClassLoader;
    ClassLoader oldClassLoader;
    String outputFolder = ".";
    String sourceFileName, outputFileName, checkStyleFileName;
    String sourceSuffix = BasicTextManager.DEFAULT_SOURCES_FILE_SUFFIX;
    String outputSuffix = DEFAULT_TRANSCRIPT_FILE_SUFFIX;
    String checkStyleSuffix = DEFAULT_CHECK_STYLE_FILE_SUFFIX;
    boolean hasBeenRun, canBeRun = true; // strange that initial value is true
    boolean hasBeenCompiled, canBeCompiled; 
    boolean hasBeenLoaded, canBeLoaded; 


    JavaDocBuilder javaDocBuilder;
    CommandGenerator mainClassFinder;
    Checkable currentGradingFeature; // ugly but do not want to change project runner code that has access to project and not grading feature
    String[][] args;
    boolean runChecked;
    StringBuffer currentOutput = new StringBuffer();
    StringBuffer currentInput = new StringBuffer();
    
    TestCase currentTestCase;
    RunningProject currentRunningProject;




	//    Map<String, String> processToOutput = new HashMap();
//    Map<String, String> processToInput = new HashMap();
    String[] currentArgs;
//    FileWriter outputFile ;

    protected Class mainClass;
    protected Method mainMethod;
    protected String mainClassName;
    protected String[] inputFiles;
    protected String[] outputFiles;
    boolean noProjectFolder;
//    List<String> nonCompiledClasses = new ArrayList();
    List<String> classNamesThatCouldNotBeCompiled = new ArrayList();

    List<String> classNamesCompiled = new ArrayList();

    
     boolean filesCompiled = false;
     boolean filesUnzipped = false;

   

//	static boolean forceCompile = false; //compile whether that is needed or not
//	static boolean checkStyle = false; 
	Map<String, String> entryPoints;
	
	ProjectWrapper wrapper;



	

	public AFlexibleProject(String aProjectFolder, String anOutputFolder, boolean aZippedFolder) {
        init(aProjectFolder, anOutputFolder, aZippedFolder);
    }

    public AFlexibleProject(StudentCodingAssignment aStudentCodingAssignment) {
        //init(aStudentCodingAssignment.getProjectFolder(), aStudentCodingAssignment.getFeedbackFolder().getAbsoluteName());
        init(aStudentCodingAssignment.getProjectFolder(), aStudentCodingAssignment.getFeedbackFolder().getMixedCaseAbsoluteName());
    }

    public AFlexibleProject(StudentCodingAssignment aStudentCodingAssignment, String aSourceSuffix, String anOutputSuffix) {
        sourceSuffix = aSourceSuffix;
        outputSuffix = anOutputSuffix;
        RootFolderProxy aProjectFolder = aStudentCodingAssignment.getProjectFolder();
        FileProxy aFeedbackFolder = aStudentCodingAssignment.getFeedbackFolder();
        if (aFeedbackFolder == null) {
//        	System.err.println("No feedback folder,not creating project");
        	throw new RuntimeException("No feedback folder,not creating project");
        }
        String aFeedbackFolderName = aFeedbackFolder.getMixedCaseAbsoluteName();
        
        //init(aStudentCodingAssignment.getProjectFolder(), aStudentCodingAssignment.getFeedbackFolder().getAbsoluteName());
        init(aStudentCodingAssignment.getProjectFolder(), aStudentCodingAssignment.getFeedbackFolder().getMixedCaseAbsoluteName());
        init(aProjectFolder, aFeedbackFolderName);

    }
    public void clear() {
    	if (classesManager != null)
		classesManager.clear();
    	if (proxyClassLoader != null)
		proxyClassLoader.clear();
    }

    public String toString() {
        return "(" + projectFolderName + "," + outputFolder + ")";
    }

//    public AProject(RootFolderProxy aRootFolder, String anOutputFolder) {
//        init(aRootFolder, anOutputFolder);
//    }

    @Override
    public String getOutputFolder() {
        return outputFolder;
    }

    @Override
    public void setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    protected CommandGenerator createMainClassFinder() {
//        return new AMainClassFinder();
//    	return JavaMainClassFinderSelector.getMainClassFinder();
        return LanguageDependencyManager.getMainClassFinder();

    }

    public void init(String aProjectFolder, String anOutputFolder, boolean aZippedFolder) {
        if (aZippedFolder) {
            rootFolder = new AZippedRootFolderProxy(aProjectFolder);
        } else {
            rootFolder = new AFileSystemRootFolderProxy(aProjectFolder);
        }
        init(rootFolder, anOutputFolder);
    }

    @Override
    public boolean isNoProjectFolder() {
        return noProjectFolder;
    }

    @Override
    public void setNoProjectFolder(boolean noProjectFolder) {
        this.noProjectFolder = noProjectFolder;
    }
    @Override
    public void setNewClassLoader() {
    	if (BasicGradingEnvironment.get().isLoadClasses()) {
            if (rootCodeFolder.hasValidBinaryFolder()) {
                proxyClassLoader = new AProxyProjectClassLoader(rootCodeFolder);
            } else {
                proxyClassLoader = new AProxyProjectClassLoader(rootCodeFolder); // create class loader in this case also
            }
        }
    }

    public void init(RootFolderProxy aRootFolder, String anOutputFolder) {
        outputFolder = anOutputFolder;

        rootFolder = aRootFolder;
        outputFileName = createFullOutputFileName();
        checkStyleFileName = createFullCheckStyleFileName();

        if (aRootFolder == null) {
            setNoProjectFolder(true);
            return;
        } else {

            //projectFolderName = aRootFolder.getAbsoluteName();
            projectFolderName = aRootFolder.getMixedCaseAbsoluteName();
//        if (projectFolderName.contains("bluong"))
//        	System.out.println("bluoing");
//        outputFolder = anOutputFolder;
            try {
                rootCodeFolder = new ARootCodeFolder(rootFolder);
            } catch (ProjectFolderNotFound e) {
            	FeedbackTextSummaryLogger.logNoSrcFolder(this);
                setNoProjectFolder(true);
                return;
            }
//            if (AProject.isLoadClasses()) {
//                if (rootCodeFolder.hasValidBinaryFolder()) {
//                    proxyClassLoader = new AProxyProjectClassLoader(rootCodeFolder);
//                } else {
//                    proxyClassLoader = new AProxyProjectClassLoader(rootCodeFolder); // create class loader in this case also
//                }
//            }
            setNewClassLoader();
            sourceFileName = createFullSourceFileName();
//        outputFileName = createFullOutputFileName();
            classesManager = new AProxyBasedClassesManager();
            mainClassFinder = createMainClassFinder();
        }
    }

    public String createLocalSourceFileName() {
        return BasicTextManager.DEFAULT_SOURCES_FILE_PREFIX + sourceSuffix;
    }
    
    

    public String createLocalOutputFileName() {
        return DEFAULT_TRANSCRIPT_FILE_PREFIX + outputSuffix;
    }

    public String createFullSourceFileName() {
        return outputFolder + "/" + createLocalSourceFileName();
    }

    public String createFullOutputFileName() {
        return outputFolder + "/" + createLocalOutputFileName();
    }
    public String createLocalCheckStyleFileName() {
        return DEFAULT_CHECK_STYLE_FILE_PREFIX + checkStyleSuffix;
    }
    
    protected String checkStyleText = null;
    @Override
    public void setCheckstyleText(String newVal) {
    	checkStyleText = newVal;
    }
  
    @Override
    public String getCheckstyleText() {
    	if (checkStyleText == null) { // assume it will never change
			checkStyleText = "";
    		if (!isNoProjectFolder()) {
    			checkStyleText = "";    		
    			String aFileName = getCheckStyleFileName();
    			File aFile = new File(aFileName);
    			if (aFile.exists()) {    	
    			    try {
						checkStyleText = Common.readFile(aFile).toString();
					} catch (IOException e) {
						e.printStackTrace();
					}
    			}
    		}
    	}
    	return checkStyleText;
    }

    public String createFullCheckStyleFileName() {
        return outputFolder + "/" + createLocalCheckStyleFileName();
    }


    boolean madeClassDescriptions;
    List<Class> classesImplicitlyLoaded;

    public List<Class> getImplicitlyLoadedClasses() {
        return classesImplicitlyLoaded;
    }

    public void maybeMakeClassDescriptions() {
    	// earlier it expected class descroptions to be fetched after running
        // but we need the class descriptions to find the main method sometimes
        // so removing this check
//        if (!runChecked && !hasBeenRun)
//            return;
//    	if (!isLoadClasses())
//    		return;
        if (madeClassDescriptions) {
            return;
        }

        makeClassDescriptions();
        madeClassDescriptions = true;

//        try { // Added by Josh: Exceptions can occur when making class descriptions
//            classesManager.makeClassDescriptions(this);
//            classViewManager = new AClassViewManager(classesManager);
//            classesTextManager = new AClassesTextManager(classViewManager);
//            classesTextManager.initializeAllSourcesText();
//            System.out.println("Write sources to:" + sourceFileName);
//            classesTextManager.writeAllSourcesText(sourceFileName);
//            madeClassDescriptions = true;
//        } catch (Exception e) {
//            System.out.println("Error making class descriptions");
//        }
    }

    public void makeClassDescriptions() {
    	
        if (isNoProjectFolder()) {
            return;
        }
//        if (!LanguageDependencyManager.isJava()) {
//    		return;
//    	}

        try { // Added by Josh: Exceptions can occur when making class descriptions
        	if (LanguageDependencyManager.isJava()) {
        	classesManager.makeClassDescriptions(this);
            classViewManager = new AClassViewManager(classesManager);
        	}
//            classesTextManager = new AClassesTextManager(new File(rootCodeFolder.getSourceProjectFolderName()), classViewManager);
            classesTextManager = new AClassesTextManager(wrapper.getSourceFolder(), classViewManager);

//            classesTextManager = new AClassesTextManager(new File(sourceFileName), classViewManager);
            classesTextManager.initializeAllSourcesText();
//            System.out.println("Write sources to:" + sourceFileName);
            classesTextManager.writeAllSourcesText(sourceFileName);
            madeClassDescriptions = true;
        } catch (Exception e) {
            System.out.println("Error making class descriptions");
            e.printStackTrace();
        }
    }

    public String getOutputFileName() {
        return outputFileName;
    }
    

    public String getCheckStyleFileName() {
        return checkStyleFileName;
    }

    public boolean hasBeenRun() {
        return hasBeenRun;
    }

    public void setHasBeenRun(boolean newVal) {
        hasBeenRun = newVal;
        runChecked = true;
        if (hasBeenRun && proxyClassLoader != null) {
            classesImplicitlyLoaded = new ArrayList(proxyClassLoader.getClassesLoaded());
        }
    }

    public boolean hasBeenCompiled() {
		return hasBeenCompiled;
	}

	public void setHasBeenCompiled(boolean hasBeenCompiled) {
		this.hasBeenCompiled = hasBeenCompiled;
	}

	public boolean canBeCompiled() {
		return canBeCompiled;
	}

	public void setCanBeCompiled(boolean canBeCompiled) {
		this.canBeCompiled = canBeCompiled;
	}
	
	 public boolean hasBeenLoaded() {
			return hasBeenLoaded;
		}

		public void setHasBeenLoaded(boolean hasBeenLoaded) {
			this.hasBeenLoaded = hasBeenLoaded;
		}

		public boolean canBeLoaded() {
			return canBeLoaded;
		}

		public void setCanBeLoaded(boolean canBeLoaded) {
			this.canBeLoaded = canBeLoaded;
		}

	@Override()
    public boolean setRunParameters(String aMainClassName, String anArgs[][], String[] anInputFiles, String[] anOutputFiles, CommandGenerator aMainClassFinder) {
        args = anArgs;
        try {
            mainClassName = aMainClassName;
            mainClass = proxyClassLoader.loadClass(mainClassName);
            inputFiles = anInputFiles;
            outputFiles = anOutputFiles;
            if (mainClass == null) {
                mainClass = ((FlexibleMainClassFinder) mainClassFinder).mainClass(rootCodeFolder, proxyClassLoader, mainClassName, this);
            }
            if (mainClass == null) {
//                System.out.println("Missing main class:" + mainClassName + " for student:" + getProjectFolderName());
                setCanBeRun(false);
                MainClassNotFound.newCase(mainClassName, getProjectFolderName(), this);
                return false;
            }

            mainMethod = mainClass.getMethod("main", String[].class);
            if (mainMethod == null) {
//                System.out.println("Missing main method:" + "main");
                MainMethodNotFound.newCase(mainClassName, getProjectFolderName(), this);

                setCanBeRun(false);
                return false;
            }
            MainClassFound.newCase(mainClassName, getProjectFolderName(), this);

            return true;
        } catch (Exception e) {
            System.out.println("cannot  run:" + getProjectFolderName());
            setCanBeRun(false);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Thread runProject() {
        try {
            if (!canBeRun()) {
                return null;
            }
            Runnable runnable = ProjectRunnerSelector.createProjectRunner(mainClassName, args, this, inputFiles, outputFiles, mainClass, mainMethod);
            Thread retVal = new Thread(runnable);
            retVal.start();
            System.out.println("started:" + retVal);
            return retVal;
        } catch (Exception e) {
            System.out.println("Could not run:" + getProjectFolderName());
            setCanBeRun(false);
            e.printStackTrace();
            return null;
        }
    }

    public Thread run(String aMainClassName, String[][] anArgs, String[] anInputFiles, String[] anOutputFiles) {
        setRunParameters(aMainClassName, anArgs, anInputFiles, anOutputFiles, mainClassFinder);
        return runProject();
    }

    public AFlexibleProject(String aProjectFolder, String anOutputFolder) {
        init(aProjectFolder, anOutputFolder, aProjectFolder.endsWith(ZIP_SUFFIX_1) || aProjectFolder.endsWith(ZIP_SUFFIX_2) );

    }

    public AFlexibleProject(String aProjectFolder) {
        init(aProjectFolder, outputFolder, aProjectFolder.endsWith(ZIP_SUFFIX_1) || aProjectFolder.endsWith(ZIP_SUFFIX_2));

    }

    public ProxyClassLoader getClassLoader() {
        return proxyClassLoader;
    }

    public ProxyBasedClassesManager getClassesManager() {
        maybeMakeClassDescriptions();
        return classesManager;
    }

    public void setClassesManager(ProxyBasedClassesManager aClassesManager) {
        this.classesManager = aClassesManager;
    }

    public ClassViewManager getClassViewManager() {
        maybeMakeClassDescriptions();
        return classViewManager;
    }

    public void setClassViewManager(ClassViewManager aClassViewManager) {
        this.classViewManager = aClassViewManager;
    }

    public ClassesTextManager getClassesTextManager() {
        maybeMakeClassDescriptions();
        return classesTextManager;
    }

    public void setClassesTextManager(ClassesTextManager aClassesTextManager) {
        this.classesTextManager = aClassesTextManager;
    }

    public void setProjectFolder(String aProjectFolder) {
        this.projectFolderName = aProjectFolder;
    }

    public String getProjectFolderName() {
        return projectFolderName;
    }

    public RootCodeFolder getRootCodeFolder() {
        return rootCodeFolder;
    }

    @Override
    public String getSourceProjectFolderName() {
        return rootCodeFolder.getSourceProjectFolderName();
    }

    @Override
    public String getSourceFileName() {
        return sourceFileName;
    }

    @Override
    public String getBinaryProjectFolderName() {
        return rootCodeFolder.getBinaryProjectFolderName();
    }

    @Override
    public boolean runChecked() {
        return runChecked;
    }

    @Override
    public void setCanBeRun(boolean newVal) {
        runChecked = true;
        canBeRun = newVal;

    }

    @Override
    public boolean canBeRun() {
        return canBeRun;
    }

    @Override
    public JavaDocBuilder getJavaDocBuilder() {
        if (javaDocBuilder == null) {
            javaDocBuilder = new JavaDocBuilder();
        }
        return javaDocBuilder;
    }

    @Override
    public StringBuffer getCurrentOutput() {
        return currentOutput;
    }

    @Override
    public void clearOutput() {
        currentOutput.setLength(0);
        try {
            FileWriter fileWriter = new FileWriter(new File(outputFileName));
            OverallTranscriptCleared.newCase(null, null, (SakaiProject) this, outputFileName, this);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setCurrentOutput(StringBuffer currentOutput) {
        this.currentOutput = currentOutput;
    }

    @Override
    public Checkable getCurrentGradingFeature() {
        return currentGradingFeature;
    }

    @Override
    public void setCurrentGradingFeature(Checkable currentGradingFeature) {
        this.currentGradingFeature = currentGradingFeature;
    }

    @Override
    public String getCurrentInput() {
        return currentInput.toString();
    }

    @Override
    public void setCurrentInput(String aCurrentInput) {
        currentInput.setLength(0);
        appendCurrentInput(aCurrentInput);
    }

    @Override
    public void appendCurrentInput(String aCurrentInput) {
        currentInput.append(aCurrentInput);
    }

//    @Override
//  	public void appendCurrentInput(String aProcess, String aCurrentInput) {
////  		currentInput.append(aCurrentInput);
//    }
    @Override
    public String[] getCurrentArgs() {
        return currentArgs;
    }

    @Override
    public void setCurrentArgs(String[] currentArgs) {
        this.currentArgs = currentArgs;
    }

    @Override
    public String getSourceSuffix() {
        return sourceSuffix;
    }

    @Override
    public boolean hasUnCompiledClasses() {
        // TODO Auto-generated method stub
        return classNamesThatCouldNotBeCompiled.size() > 0;
    }

    @Override
    public List<String> getNonCompiledClasses() {
        return classNamesThatCouldNotBeCompiled;
    }

    @Override
    public void addNonCompiledClass(String newVal) {
        classNamesThatCouldNotBeCompiled.add(newVal);

    }

    @Override
    public boolean hasCompiledClasses() {
        // TODO Auto-generated method stub
        return classNamesCompiled.size() > 0;
    }

    @Override
    public List<String> getCompiledClasses() {
        return classNamesCompiled;
    }

    @Override
    public void addCompiledClass(String newVal) {
        classNamesCompiled.add(newVal);

    }
//    static boolean loadClasses = false;
//
//    static boolean compileMissingObjectCode = false;
//    static boolean unzipFiles = false;
// 
//
//	static boolean preCompileMissingObjectCode = false;
//    public static boolean isLoadClasses() {
//        return loadClasses;
//    }
//
//    public static void setLoadClasses(boolean makeClassDescriptions) {
//        AProject.loadClasses = makeClassDescriptions;
//    }
//
//    public static boolean isCompileMissingObjectCode() {
//        return compileMissingObjectCode;
//    }
//
//    public static void setCompileMissingObjectCode(boolean newVal) {
//        AProject.compileMissingObjectCode = newVal;
//    }
//
//    public static boolean isForceCompile() {
//        return forceCompile;
//    }
//
//    public static void setForceCompile(boolean forceCompile) {
//        AProject.forceCompile = forceCompile;
//    }
//
//    public static boolean isPreCompileMissingObjectCode() {
//        return preCompileMissingObjectCode;
//    }
//
//    public static void setPrecompileMissingObjectCode(
//            boolean preCompileMissingObjectCode) {
//        AProject.preCompileMissingObjectCode = preCompileMissingObjectCode;
//    }
    @Override
    public  boolean isFilesCompiled() {
        return filesCompiled;
    }
    @Override

    public  void setFilesCompiled(boolean filesCompiled) {
        this.filesCompiled = filesCompiled;
    }
    @Override
    public  boolean isFilesUnzipped() {
		return filesUnzipped;
	}
    @Override
	public  void setFilesUnzipped(boolean filesUnzipped) {
		this.filesUnzipped = filesUnzipped;
	}
//	   public static boolean isUnzipFiles() {
//			return unzipFiles;
//		}
//
//		public static void setUnzipFiles(boolean unzipFiles) {
//			AProject.unzipFiles = unzipFiles;
//		}
		public TestCase getCurrentTestCase() {
			return currentTestCase;
		}

		public void setCurrentTestCase(TestCase currentTestCase) {
			this.currentTestCase = currentTestCase;
		}

//	    public static boolean isCheckStyle() {
//			return checkStyle;
//		}

//	    public static void setCheckStyle(boolean checkStyle) {
//			AProject.checkStyle = checkStyle;
//		}
	    public Map<String, String> getEntryPoints() {
	    	if (entryPoints == null) {
				entryPoints = LanguageDependencyManager.getMainClassFinder()
						.getEntryPoints(getWrapper(), StaticConfigurationUtils.getPotentialMainEntryPointNames());

			}
			return entryPoints;
//			return entryPoints;
		}

		public void setEntryPoints(Map<String, String> entryPoints) {
			this.entryPoints = entryPoints;
		}
		@Override
		public RunningProject getCurrentRunningProject() {
			return currentRunningProject;
		}
		@Override

		public void setCurrentRunningProject(RunningProject currentRunningProject) {
			this.currentRunningProject = currentRunningProject;
		}
		  @Override
		     public ProjectWrapper getWrapper() {
		 		return wrapper;
		 	}
		     @Override
		     public void setWrapper(ProjectWrapper newValue) {
		 		this.wrapper = newValue;
		 	}
}
