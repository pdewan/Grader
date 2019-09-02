package grader.sakai.project;


import framework.grading.ProjectRequirements;
import framework.grading.testing.Checkable;
import framework.grading.testing.Feature;
import framework.grading.testing.Restriction;
import framework.logging.loggers.FeedbackTextSummaryLogger;
import grader.assignment.AGradingFeature;
import grader.assignment.AGradingFeatureList;
import grader.assignment.AnAssignmenDataFolder;
import grader.assignment.AssignmentDataFolder;
import grader.assignment.GradingFeature;
import grader.assignment.GradingFeatureList;
import grader.auto_notes.ANotesGenerator;
import grader.auto_notes.NotesGenerator;
import grader.basics.execution.MainClassFinder;
import grader.basics.file.RootFolderProxy;
import grader.basics.project.source.BasicTextManager;
import grader.basics.requirements.interpreter.specification.ACSVRequirementsSpecification;
import grader.basics.requirements.interpreter.specification.CSVRequirementsSpecification;
import grader.basics.settings.BasicGradingEnvironment;
import grader.basics.trace.ProjectFolderNotFound;
import grader.colorers.Colorer;
import grader.colorers.GradingFeatureColorerSelector;
import grader.colorers.MultiplierColorerSelector;
import grader.colorers.NotesColorerSelector;
import grader.colorers.OverallScoreColorerSelector;
import grader.documents.AWordDocumentDisplayer;
import grader.documents.DocumentDisplayer;
import grader.documents.DocumentDisplayerRegistry;
import grader.execution.AReflectionBasedProjectRunner;
import grader.feedback.APrintingAutoFeedbackManager;
import grader.feedback.APrintingManualFeedbackManager;
import grader.feedback.AScoreFeedbackFileWriter;
import grader.feedback.AnAllTextSourceDisplayer;
import grader.feedback.AutoFeedback;
import grader.feedback.ManualFeedback;
import grader.feedback.ScoreFeedback;
import grader.feedback.SourceDisplayer;
import grader.language.LanguageDependencyManager;
import grader.navigation.AProjectNavigator;
import grader.navigation.NavigationListManager;
import grader.navigation.NavigationListManagerFactory;
import grader.navigation.ProjectNavigator;
import grader.navigation.automatic.AnAutomaticProjectNavigator;
import grader.navigation.automatic.AutomaticProjectNavigator;
import grader.navigation.filter.ADispatchingFilter;
import grader.navigation.filter.BasicNavigationFilter;
import grader.navigation.hybrid.AHybridProjectNavigator;
import grader.navigation.hybrid.HybridProjectNavigator;
import grader.navigation.manual.AManualProjectNavigator;
import grader.navigation.manual.ManualProjectNavigator;
import grader.navigation.sorter.FileNameSorterSelector;
import grader.photos.APhotoReader;
import grader.photos.PhotoReader;
import grader.project.flexible.AFlexibleProject;
import grader.sakai.ASakaiBulkAssignmentFolder;
import grader.sakai.ASakaiStudentCodingAssignmentsDatabase;
import grader.sakai.BulkAssignmentFolder;
import grader.sakai.GenericStudentAssignmentDatabase;
import grader.sakai.StudentCodingAssignment;
import grader.settings.GraderSettingsModel;
import grader.settings.GraderSettingsModelSelector;
import grader.settings.folders.OnyenRangeModel;
import grader.spreadsheet.FeatureGradeRecorder;
import grader.spreadsheet.FeatureGradeRecorderSelector;
import grader.spreadsheet.FinalGradeRecorder;
import grader.steppers.AComplexProjectStepper;
import grader.steppers.AGradedProjectNavigator;
import grader.steppers.OverviewProjectStepper;
import grader.trace.assignment_data.AssignmentDataFolderCreated;
import grader.trace.assignment_data.AssignmentDataFolderLoaded;
import grader.trace.settings.InvalidOnyenRangeException;
import grader.trace.settings.MissingOnyenException;
import grader.trace.steppers.ProjectIORedirected;
import grader.trace.steppers.ProjectStepperDisplayed;
import grader.trace.steppers.ProjectWindowsDisposed;
import grader.trace.steppers.ProjectWindowsRecorded;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import util.misc.AClearanceManager;
import util.misc.ClearanceManager;
import util.misc.Common;
import util.models.Hashcodetable;
import util.trace.Tracer;
import wrappers.grader.checkers.FeatureCheckerWrapper;
import bus.uigen.OEFrame;
import bus.uigen.uiFrame;
import bus.uigen.uiFrameList;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualFrame;

public class ASakaiProjectDatabase implements SakaiProjectDatabase {
	static SakaiProjectDatabase currentSakaiProjectDatabase;

	// public static final String DEFAULT_ASSIGNMENT_DATA_FOLDER =
	// "C:/Users/dewan/Downloads/GraderData";
	public static final String DEFAULT_SCORE_FILE_NAME = "scores.txt";
	boolean assignmentRoot;
	Map<String, SakaiProject> onyenToProject = new HashMap();
	String bulkAssignmentsFolderName;
	BulkAssignmentFolder bulkFolder;
	// String assignmentsDataFolderName = DEFAULT_ASSIGNMENT_DATA_FOLDER;
	String assignmentsDataFolderName;
	ClearanceManager clearanceManager;
	ProjectNavigator projectNavigator;
	ManualProjectNavigator manualProjectNavigator;
	AutomaticProjectNavigator automaticProjectNavigator;
	HybridProjectNavigator hybridProjectNavigator;

//
	AssignmentDataFolder assignmentDataFolder;
	// String outputFileName;
	FinalGradeRecorder gradeRecorder;
	FinalGradeRecorder totalScoreRecorder;
	protected FeatureGradeRecorder featureGradeRecorder;
	protected CSVRequirementsSpecification csvRequirementsSpecification;
	GradingFeatureList gradingFeatures = new AGradingFeatureList();

	String sourceFileNameSuffix = BasicTextManager.DEFAULT_SOURCES_FILE_SUFFIX;
	String sourceFileNamePrefix = BasicTextManager.DEFAULT_SOURCES_FILE_SUFFIX;

	String outputSuffix = AFlexibleProject.DEFAULT_TRANSCRIPT_FILE_SUFFIX;
//
	ScoreFeedback scoreFeedback;
	AutoFeedback autoFeedback;
	ManualFeedback manualFeedback;
	SourceDisplayer sourceDisplayer;
	MainClassFinder mainClassFinder;

	String startStudentID, endStudentID;
	ProjectStepper projectStepper;
	PhotoReader photoReader;
	Hashcodetable<GradingFeature, Checkable> featureToCheckable = new Hashcodetable<>();
	protected ProjectRequirements projectRequirements;
	Colorer<GradingFeature> gradingFeatureColorer;
	Colorer<Double> scoreColorer, multiplierColorer;
	Colorer<String> overallNotesColorer;
	GraderSettingsModel graderSettings;
	BasicNavigationFilter navigationFilter;
	NotesGenerator notesGenerator;
	Comparator<String> fileNameSorter;
	String graderDirectory;
	protected OnyenRangeModel onyens;

	public ASakaiProjectDatabase(String aBulkAssignmentsFolderName,
			String anAssignmentsDataFolderName, String aStartStudentID,
			String anEndStudentID, boolean anAssignmentRoot) {

		startStudentID = aStartStudentID;
		endStudentID = anEndStudentID;
		init(aBulkAssignmentsFolderName, anAssignmentsDataFolderName,
				anAssignmentRoot);

	}
	
	

	public ASakaiProjectDatabase(String aBulkAssignmentsFolderName,
			String anAssignmentsDataFolderName, boolean anAssignmentRoot) {
		init(aBulkAssignmentsFolderName, anAssignmentsDataFolderName,
				anAssignmentRoot);
		

	}

	public void init(String aBulkAssignmentsFolderName,
			String anAssignmentsDataFolderName, boolean anAssigmentRoot) {
		setCurrentSakaiProjectDatabase(this);
		graderDirectory = AReflectionBasedProjectRunner.getCurrentDirectory();
		assignmentRoot = anAssigmentRoot;
		sourceFileNameSuffix = sourceSuffix();
		outputSuffix = outputSuffix();
		bulkAssignmentsFolderName = aBulkAssignmentsFolderName;
		assignmentsDataFolderName = anAssignmentsDataFolderName;
		fileNameSorter = createFileNameSorter();
		maybeMakeProjects();
		
		featureGradeRecorder = createFeatureGradeRecorder();
		gradeRecorder = createFinalGradeRecorder();

		// totalScoreRecorder = createTotalScoreRecorder();
		totalScoreRecorder = createTotalScoreRecorder();
		autoFeedback = createAutoFeedback();
		manualFeedback = createManualFeedback();
		scoreFeedback = createScoreFeedback();
		sourceDisplayer = createSourceDisplayer();
		mainClassFinder = createMainClassFinder();
		projectStepperDisplayer = createProjectStepperDisplayer();
		navigationListCreator = createNavigationListCreator();

		photoReader = createPhotoReader();
		gradingFeatureColorer = createGradingFeatureColorer();
		scoreColorer = createScoreColorer();
		multiplierColorer = createMultiplierColorer();
		overallNotesColorer = createOverallNotesColorer();
		notesGenerator = createNotesGenerator();
		clearanceManager = createClearanceManager();
		projectNavigator = createProjectNavigator();
		manualProjectNavigator = createManualProjectNavigator();
		automaticProjectNavigator = createAutomaticProjectNavigator();
		hybridProjectNavigator = createHybridProjectNavigator();

		// maybeMakeProjects();
		
		initInputFiles();
		onyens = GraderSettingsModelSelector.getGraderSettingsModel().getOnyens();
		onyens.addPropertyChangeListener(this);

	}

	private AutomaticProjectNavigator createAutomaticProjectNavigator() {
		return new AnAutomaticProjectNavigator(this);
	}

	private HybridProjectNavigator createHybridProjectNavigator() {
		return new AHybridProjectNavigator(this);
	}

	private ManualProjectNavigator createManualProjectNavigator() {
		return new AManualProjectNavigator(this);
	}

	private ProjectNavigator createProjectNavigator() {
		return new AProjectNavigator(this);
	}

	Comparator<String> createFileNameSorter() {
//		return new AnAlphabeticFileNameSorter();
		return FileNameSorterSelector.getSorter();
	}

	public static SakaiProjectDatabase getCurrentSakaiProjectDatabase() {
		return currentSakaiProjectDatabase;
	}

	public static void setCurrentSakaiProjectDatabase(
			SakaiProjectDatabase currentSakaiProjectDatabase) {
		ASakaiProjectDatabase.currentSakaiProjectDatabase = currentSakaiProjectDatabase;
	}

	public AutoFeedback getAutoFeedback() {
		return autoFeedback;
	}

	public ManualFeedback getManualFeedback() {
		return manualFeedback;
	}

	public ScoreFeedback getScoreFeedback() {
		return scoreFeedback;
	}


	public void setScoreFeedback(ScoreFeedback newVal) {
		scoreFeedback = newVal;
	}

	public SourceDisplayer getSourceDisplayer() {
		return sourceDisplayer;
	}

	protected AutoFeedback createAutoFeedback() {
		// return new AnAutoFeedbackManager();
		return new APrintingAutoFeedbackManager();
	}

	protected ManualFeedback createManualFeedback() {
		// return new AManualFeedbackManager();
		return new APrintingManualFeedbackManager();
	}

	protected ScoreFeedback createScoreFeedback() {
		return new AScoreFeedbackFileWriter();
	}

	protected SourceDisplayer createSourceDisplayer() {
		return new AnAllTextSourceDisplayer();
	}


	protected PhotoReader createPhotoReader() {
		return new APhotoReader(this);
	}

	protected Colorer<GradingFeature> createGradingFeatureColorer() {
//		return new AGradingFeatureColorer(this);
		return GradingFeatureColorerSelector.createColorer(this);
	}

	protected Colorer<Double> createScoreColorer() {
//		return new AScoreColorer(this, 100);
		return OverallScoreColorerSelector.createColorer(this);
		
	}

	protected Colorer<Double> createMultiplierColorer() {
//		return new AScoreColorer(this, 1.0);
		return MultiplierColorerSelector.createColorer(this);

	}

	protected Colorer<String> createOverallNotesColorer() {
//		return new ANotesColorer(this);
		return NotesColorerSelector.createColorer(this);
	}

	protected FinalGradeRecorder createFinalGradeRecorder() {
		// return FinalGradeRecorderSelector.createFinalGradeRecorder(this);
		return featureGradeRecorder;

	}

	protected FeatureGradeRecorder createFeatureGradeRecorder() {
		return FeatureGradeRecorderSelector.createFeatureGradeRecorder(this);
	}
	// This will return conglomerate recorder
	protected FinalGradeRecorder createTotalScoreRecorder() {
		return featureGradeRecorder;

		// return TotalScoreRecorderSelector.createFinalGradeRecorder(this);
	}

	public FinalGradeRecorder getTotalScoreRecorder() {
		return totalScoreRecorder;
	}

	public void setTotalScoreRecorder(FinalGradeRecorder newVal) {
		totalScoreRecorder = newVal;
	}

	public String sourceSuffix() {
		return BasicTextManager.DEFAULT_SOURCES_FILE_SUFFIX;
	}

	public String outputSuffix() {

		return AFlexibleProject.DEFAULT_TRANSCRIPT_FILE_SUFFIX;
	}

	public GradingFeatureList getGradingFeatures() {
		return gradingFeatures;
	}


	protected MainClassFinder createMainClassFinder() {
//		return new AMainClassFinder();
//		return JavaMainClassFinderSelector.getMainClassFinder();
        return LanguageDependencyManager.getMainClassFinder();

	}

	public void addGradingFeatures(List<GradingFeature> aGradingFeatures) {
		for (GradingFeature aGradingFeature : aGradingFeatures) {
			aGradingFeature.setInputFiles(inputFiles);
			gradingFeatures.add(aGradingFeature);
			aGradingFeature.setProjectDatabase(this);

			if (aGradingFeature.isAutoGradable()
					&& aGradingFeature.getFeatureChecker().isOverridable()) {
				GradingFeature manualFeature = new AGradingFeature("Override"
						+ aGradingFeature.getFeatureName(),
						aGradingFeature.getMax(),
						aGradingFeature.isExtraCredit());
				manualFeature.setProjectDatabase(this);
				gradingFeatures.add(manualFeature);
				aGradingFeature.setLinkedFeature(manualFeature);
				manualFeature.setLinkedFeature(aGradingFeature);


			}
		}
		// gradingFeatures.addAll(aGradingFeatures);

	}

	public BulkAssignmentFolder getBulkAssignmentFolder() {
		return bulkFolder;
	}
	@Override
	public AssignmentDataFolder getAssignmentDataFolder() {
		return assignmentDataFolder;
	}

	public Set<String> getOnyens() {
		return onyenToProject.keySet();
	}

	public Collection<SakaiProject> getProjects() {
		return onyenToProject.values();
	}

	@Override
	public FinalGradeRecorder getGradeRecorder() {
		return gradeRecorder;
	}

	@Override
	public FeatureGradeRecorder getFeatureGradeRecorder() {
		return featureGradeRecorder;
	}


	// I changed this to protected so extending classes can call it. -- Josh
	protected SakaiProject makeProject(StudentCodingAssignment anAssignment) {
		RootFolderProxy projectFolder = anAssignment.getProjectFolder();
		

		if (projectFolder == null) {
			FeedbackTextSummaryLogger.logNoSubmission(anAssignment);
			Tracer.error(ProjectFolderNotFound.newCase(anAssignment.getOnyen(), anAssignment.getStudentName(), this).getMessage()); // we will not throw this exception
			if (AGradedProjectNavigator.doNotVisitNullProjects)
			return null;
		}
		// List<OEFrame> oldList = new ArrayList( uiFrameList.getList());


		if (!anAssignment.isSubmitted() && AGradedProjectNavigator.doNotVisitNullProjects) {
			System.out.println("Assignment not submitted:"
					+ anAssignment.getOnyen() + " "
					+ anAssignment.getStudentName());

			return null;
		}


		
		SakaiProject aProject;
		try {
			aProject = new ASakaiProject(anAssignment, sourceFileNameSuffix,
					outputSuffix);
			return aProject;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Project lastProject;
	public SakaiProject runProject(String anOnyen) {
		SakaiProject aProject = getOrCreateProject(anOnyen);
		// if (aProject != null) {
		//
		// String[] strings = {};
		//
		// outputFileName = //aProject.getOutputFileName();
		// aProject.getOutputFolder() + "/" + AProject.DEFAULT_OUTPUT_FILE_NAME;
		// System.out.println("Trying to run:" + anOnyen + " " +
		// aProject.getStudentAssignment().getStudentName());
		// String assignmentName =
		// this.getBulkAssignmentFolder().getAssignmentName().replaceAll("\\s","");
		// Thread thread = aProject.run("main." + assignmentName, strings, null,
		// outputFileName);
		//
		// resetRunningProject(aProject);
		// // return aProject;
		// }
		runProject(anOnyen, aProject);
		return aProject;
	}

	public void setRunParameters(SakaiProject aProject) {

	}

	String assignmentName, mixedCaseAssignmentName;

	protected String getAssignmentName() {
		if (assignmentName == null)
			assignmentName = this.getBulkAssignmentFolder().getAssignmentName()
					.replaceAll("\\s", "");
		return assignmentName;
	}


	protected String getMixedCaseAssignmentName() {
		if (mixedCaseAssignmentName == null)
			mixedCaseAssignmentName = this.getBulkAssignmentFolder()
					.getMixedCaseAssignmentName().replaceAll("\\s", "");
		return mixedCaseAssignmentName;
	}

	public String getOutputFileName(SakaiProject aProject, String anInputFile) {
		String inQualifier = "";
		if (anInputFile != null) {
			inQualifier = Common.toFilePrefix(Common
					.absoluteNameToLocalName(anInputFile)) + "_";
		}
		String outputFileName = // aProject.getOutputFileName();
		aProject.getOutputFolder() + "/" + inQualifier
				+ getDefaultOutputFileName();
		return outputFileName;

	}

	public String getDefaultOutputFileName() {

		return AFlexibleProject.DEFAULT_TRANSCRIPT_FILE_PREFIX + outputSuffix;
	}

	public String[] getOutputFileNames(SakaiProject aProject,
			String[] anInputFiles) {
		String[] anOutputFiles = new String[anInputFiles.length];
		for (int i = 0; i < anInputFiles.length; i++) {
			anOutputFiles[i] = getOutputFileName(aProject, anInputFiles[i]);
		}
		return anOutputFiles;
		/*
		 * String inQualifier = ""; if (anInputFile != null) inQualifier =
		 * anInputFile + "_"; outputFileName = //aProject.getOutputFileName();
		 * aProject.getOutputFolder() + "/" + inQualifier +
		 * AProject.DEFAULT_OUTPUT_FILE_NAME; return outputFileName;
		 */

	}

	public String getClassName() {
		return "main." + getMixedCaseAssignmentName();
	}

	protected String[][] getArgs(String[] anInputFiles) {
		String[][] args = new String[anInputFiles.length][];
		for (int i = 0; i < anInputFiles.length; i++) {
			args[i] = getArgs(anInputFiles[i]);
		}
		return args;
	}

	protected String[] getArgs(String inputFile) {
		return new String[] {};
	}

	String[] outputFiles;
	String outputFileName;
	String[] inputFiles;
	String[][] args;

	public String[] getInputFiles() {
		return inputFiles;
	}


	protected void initInputFiles() {
		Set<String> inputFilesSet = assignmentDataFolder.getInputFiles();
		inputFiles = new String[inputFilesSet.size()];
		int nextFileIndex = 0;
		for (String inputFile : inputFilesSet) {
			inputFiles[nextFileIndex] = inputFile;
			nextFileIndex++;

		}
	}

	@Override
	public SakaiProject runProject(String anOnyen, SakaiProject aProject) {
		// SakaiProject aProject = getProject(anOnyen);
		if (aProject != null) {

			// String[] strings = {};

			// outputFileName = //aProject.getOutputFileName();
			// aProject.getOutputFolder() + "/" +
			// AProject.DEFAULT_OUTPUT_FILE_NAME;

			// System.out.println("Trying to run:" + anOnyen + " "
			// + aProject.getStudentAssignment().getStudentName());
			System.out.println("Trying to run:" + anOnyen);
			// String assignmentName =
			// this.getBulkAssignmentFolder().getAssignmentName().replaceAll("\\s","");
			String assignmentName = getMixedCaseAssignmentName();

			String mainClassName = getClassName();
			Set<String> inputFilesSet = assignmentDataFolder.getInputFiles();
			// inputFiles = new String[inputFilesSet.size()];
			// int nextFileIndex = 0;
			// for (String inputFile : inputFilesSet) {
			// inputFiles[nextFileIndex] = inputFile;
			// nextFileIndex++;
			//
			// }
			outputFiles = getOutputFileNames(aProject, inputFiles);
			outputFileName = aProject.getOutputFileName();

			String[][] strings = getArgs(inputFiles);

			aProject.setRunParameters(mainClassName, strings, inputFiles,
					outputFiles, mainClassFinder);
			Thread thread = aProject.runProject();

			//
			// if (inputFiles.size() > 0) {
			// for (String inputFile:inputFiles) {
			// if (!aProject.canBeRun()) break;
			// aProject.setRunParameters (mainClassName, strings, inputFile,
			// outputFileName);
			//
			// // Thread thread = aProject.run("main." + assignmentName,
			// strings, null, outputFileName);
			// // Thread thread = aProject.run(mainClassName, strings, null,
			// outputFileName);
			// Thread thread = aProject.runProject();
			// System.out.println("returned from run");
			//
			// }
			// } else {
			//
			// aProject.setRunParameters (mainClassName, strings, null,
			// outputFileName);
			//
			//
			// // Thread thread = aProject.run("main." + assignmentName,
			// strings, null, outputFileName);
			// // Thread thread = aProject.run(mainClassName, strings, null,
			// outputFileName);
			// Thread thread = aProject.runProject();
			// }


			// resetRunningProject(aProject);
			// return aProject;
		}
		return aProject;
	}


	@Override
	public void resetRunningProject(SakaiProject aProject) {
		for (GradingFeature gradingFeature : gradingFeatures) {
			gradingFeature.setProject(aProject);
			gradingFeature.setOutputFiles(outputFiles);
		}
	}


	uiFrame frame;

	public ProjectStepper createAndDisplayProjectStepper() {
		// if (projectStepper != null) {
		// ProjectStepper aProjectStepper = createProjectStepper();
		// aProjectStepper.setProjectDatabase(this);
		// projectStepper = aProjectStepper;
		// }
		// OEFrame oeFrame = ObjectEditor.edit(clearanceManager);
		// oeFrame.setLocation(800, 500);
		// oeFrame.setSize(400, 400);
		ProjectStepper projectStepper = getOrCreateProjectStepper();
		displayProjectStepper(projectStepper);
		// projectStepper.setOEFrame(frame);
		return projectStepper;
	}

	public ProjectStepper getProjectStepper() {

		return projectStepper;
	}

	public void setProjectStepper(ProjectStepper projectStepper) {
		this.projectStepper = projectStepper;
	}

	ProjectStepperDisplayer projectStepperDisplayer;

	public ProjectStepperDisplayer getProjectStepperDisplayer() {
		return projectStepperDisplayer;
	}

	public void setProjectStepperDisplayer(
			ProjectStepperDisplayer projectStepperDisplayer) {
		this.projectStepperDisplayer = projectStepperDisplayer;
	}

	protected ProjectStepperDisplayer createProjectStepperDisplayer() {

//		return new AnOEProjectStepperDisplayer();
		return ProjectStepperDisplayerSelector.getProjectStepperDisplayer();
	}

	public Object displayProjectStepper(ProjectStepper aProjectStepper) {
		// return projectStepperDisplayer.display(aProjectStepper);
		Object retVal = projectStepperDisplayer.display(aProjectStepper);
		// if (retVal instanceof uiFrame) {
		projectStepper.setFrame(retVal);
		// }
		ProjectStepperDisplayed.newCase(this, (OverviewProjectStepper) aProjectStepper, this);
//		recordWindows(); // make sure this frame is not disposed on next, set Frame is recording windows, why record again? Also setFrame will do auto run
		// ProjectWindowsRecorded.newCase(this, (OverviewProjectStepper)
		// projectStepper, projectStepper.getProject(), this);

		// frame = (uiFrame) displayProjectStepper(projectStepper);
		// projectStepper.setOEFrame(retVal);
		return retVal;

		// OEFrame oeFrame = ObjectEditor.edit(aProjectStepper);
		// oeFrame.setLocation(700, 500);
		// oeFrame.setSize(500, 700);
		// return oeFrame;
	}
	@Override
	public ProjectStepper getOrCreateProjectStepper() {
		if (projectStepper == null) {
//			projectStepper = new AComplexProjectStepper();
			projectStepper = ProjectStepperFactory.createProjectStepper();


			// projectStepper = new AMainProjectStepper();

			// projectStepper = new AnOverviewProjectStepper();
			// projectStepper = new ABasicProjectStepper();

			// projectStepper = new AProjectStepper();
			projectStepper.setProjectDatabase(this);
		}

		return projectStepper;
//
	}

	public void runProjectInteractively(String anOnyen) {
		runProjectInteractively(anOnyen, createAndDisplayProjectStepper());

	}


	NavigationListManager navigationListCreator;


	public NavigationListManager getNavigationListCreator() {
		return navigationListCreator;
	}


	public void setNavigationListCreator(
			NavigationListManager navigationListCreator) {

		this.navigationListCreator = navigationListCreator;
	}

	NavigationListManager createNavigationListCreator() {

//		return new AnUnsortedNavigationListCreator();
		return NavigationListCreatorSelector.getNavigationListCreator();
	}

	public List<String> getOnyenNavigationList(
			SakaiProjectDatabase aSakaiProjectDatabase) {
		// Set<String> onyens = new HashSet(aSakaiProjectDatabase.getOnyens());
		// return new ArrayList(aSakaiProjectDatabase.getOnyens());
		return getNavigationListCreator().getOnyenNavigationList(
				aSakaiProjectDatabase);

	}

	public List<String> getOnyenNavigationList() {
		// Set<String> onyens = new HashSet(aSakaiProjectDatabase.getOnyens());
		return getOnyenNavigationList(this);


	}

	public void runProjectsInteractively() {
		ProjectStepper aProjectStepper = createAndDisplayProjectStepper();
		maybeMakeProjects();

		// aProjectStepper.setProjectDatabase(this);
		// Set<String> onyens = new HashSet(onyenToProject.keySet());
		// aProjectStepper.setHasMoreSteps(true);
		List<String> onyens = getOnyenNavigationList(this);
		if (onyens.size() == 0) {
			if (!GraphicsEnvironment.isHeadless())
			JOptionPane.showMessageDialog(null,
					"No onyens matching specification found");
			Tracer.error("No onyens matching specification found");
		}


		for (String anOnyen : onyens) {
			runProjectInteractively(anOnyen, aProjectStepper);
		}
		aProjectStepper.setHasMoreSteps(false);

	}


	public static void setVisible(Object aFrame, boolean newVal) {
		if (aFrame instanceof Component) {
			((Component) aFrame).setVisible(newVal);
		} else if (aFrame instanceof VirtualComponent) {
			((VirtualComponent) aFrame).setVisible(newVal);
		} else if (aFrame instanceof OEFrame) {
			((OEFrame) aFrame).getFrame().setVisible(newVal);
		}
	}

	public static void dispose(Object aFrame) {
		if (aFrame instanceof Frame) {
			((Frame) aFrame).dispose();
		} else if (aFrame instanceof VirtualFrame) {
			((VirtualFrame) aFrame).dispose();
		} else if (aFrame instanceof JFrame) {
			((JFrame) aFrame).dispose();

		} else if (aFrame instanceof OEFrame) {
			((OEFrame) aFrame).getFrame().dispose();
			;
		}
	}

	public boolean nonBlockingRunProjectsInteractively()
			throws InvalidOnyenRangeException {
		try {
			return nonBlockingRunProjectsInteractively("");
		} catch (MissingOnyenException e) {
			// unreachable code
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean nonBlockingRunProjectsInteractively(String aGoToOnyen)
			throws MissingOnyenException, InvalidOnyenRangeException {
		maybeMakeProjects();
		ProjectStepper aProjectStepper = createAndDisplayProjectStepper();
		Object frame = aProjectStepper.getFrame();
		// setVisible(frame, false);

		// ProjectStepper aProjectStepper = getOrCreateProjectStepper();

		aProjectStepper.configureNavigationList();
		boolean retVal = false;
		try {
			retVal = aProjectStepper.runProjectsInteractively(aGoToOnyen);
		} catch (MissingOnyenException e) {
			dispose(frame);
			throw e;
			// return false;// or could throw the exception to caller, header
			// allows this to happen
		}
		if (!retVal) {
			dispose(frame);
		}
		return retVal;
		// aProjectStepper.setProjectDatabase(this);
		// Set<String> onyens = new HashSet(onyenToProject.keySet());
		// aProjectStepper.setHasMoreSteps(true);
		// List<String> onyens = getOnyenNavigationList(this);
		//
		// for (String anOnyen : onyens) {
		// runProjectInteractively(anOnyen, aProjectStepper);
		// }
		// aProjectStepper.setHasMoreSteps(false);

	}

	@Override
	public boolean startProjectStepper(String aGoToOnyen)
			throws MissingOnyenException, InvalidOnyenRangeException {
		maybeMakeProjects();
		ProjectStepper aProjectStepper = getOrCreateProjectStepper();
		// Object frame = aProjectStepper.getFrame();
		// setVisible(frame, false);

		// ProjectStepper aProjectStepper = getOrCreateProjectStepper();

		aProjectStepper.configureNavigationList();
		boolean retVal = false;
		try {
			retVal = aProjectStepper.runProjectsInteractively(aGoToOnyen);
		} catch (MissingOnyenException e) {
			// dispose(frame);
			throw e;
			// return false;// or could throw the exception to caller, header
			// allows this to happen
		}
		// if (!retVal ) {
		// dispose(frame);
		// }
		return retVal;
		// aProjectStepper.setProjectDatabase(this);
		// Set<String> onyens = new HashSet(onyenToProject.keySet());
		// aProjectStepper.setHasMoreSteps(true);
		// List<String> onyens = getOnyenNavigationList(this);
		//
		// for (String anOnyen : onyens) {
		// runProjectInteractively(anOnyen, aProjectStepper);
		// }
		// aProjectStepper.setHasMoreSteps(false);

	}

	@Override
	public void startProjectStepper() {
		maybeMakeProjects();
		ProjectStepper aProjectStepper = createAndDisplayProjectStepper();
		aProjectStepper.configureNavigationList();
	}

	// String navigationFilter = "";
	//
	// public String getNavigationFilter() {
	// return navigationFilter;
	// }
	//
	// public void setNavigationFilter(String navigationFilter) {
	// this.navigationFilter = navigationFilter;
	// }


	PrintStream origOut;
	InputStream origIn;

	// SakaiProject nextProject;
	// String onyen;
	// ProjectStepper projectStepper;

	List<OEFrame> oldList;
	Window[] oldWindows;
	@Override
	public void restoreGraderDirectory() {
		AReflectionBasedProjectRunner.setCurrentDirectory(graderDirectory);
	}

	public void recordWindows() {
		oldList = new ArrayList(uiFrameList.getList());
		oldWindows = Window.getWindows();
		ProjectWindowsRecorded.newCase(this,
				(OverviewProjectStepper) projectStepper,
				projectStepper.getProject(), this);

	}
	protected static ThreadPoolExecutor executor;
	public static void maybeCreateThreadPoolExecutor() {
		if (executor == null) {
			executor = 
					  (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
		}
		
	}
	public static ThreadPoolExecutor executor() {
		maybeCreateThreadPoolExecutor();
		return executor;
	}
	public static int DISPOSE_TIMEOUT = 4000;
	@Override
	public  void clearWindows() {
		if (oldWindows != null && oldList != null) {// somebody went before me,
													// get rid of their windows
		// System.out.println("dispoing old windows");
			List<uiFrame> newList = new ArrayList(uiFrameList.getList());

			for (uiFrame frame : newList) {

				if (oldList.contains(frame))
					continue;
				frame.dispose(); // will this work
			}

			Window[] newWindows = Window.getWindows();

			for (Window frame : newWindows) {

				if (Common.containsReference(oldWindows, frame)) {
					continue;
				}
				Future aFuture =
			     executor().submit(() -> {
					frame.setVisible(false);
					frame.dispose(); // this hangs
				    return null;
				});
				try {
					Object aRetVal = aFuture.get(DISPOSE_TIMEOUT, TimeUnit.MILLISECONDS);
				} catch (TimeoutException | InterruptedException | ExecutionException e) {
					System.err.println("Cannot dispose: " + frame);
					System.err.println("Exiting, Please restart grader, Eclipse and/or OS");

//					System.err.println("Exiting, Please remove from grading, onyen:" + getProjectStepper().getOnyen());

//					e.printStackTrace();
//					System.exit(-1);
					
				}
//				frame.setVisible(false);
//				frame.dispose(); // this hangs
			}
		}

		ProjectWindowsDisposed.newCase(this,
				(OverviewProjectStepper) projectStepper,
				projectStepper.getProject(), this);

	}


	public void initIO() {
		origOut = System.out;
		origIn = System.in;
	}



	public void runProjectInteractively(String anOnyen,
			ProjectStepper aProjectStepper) {
		SakaiProject aProject = getOrCreateProject(anOnyen);


		// origOut = System.out;
		// origIn = System.in;
		initIO();

		// if (aProjectStepper.isAutoRun()) {
		// runProject(anOnyen, aProject);
		// }

		recordWindows();
		// ProjectWindowsRecorded.newCase(this, (OverviewProjectStepper)
		// projectStepper, projectStepper.getProject(), this);

		if (aProjectStepper.isAutoRun()) {
			runProject(anOnyen, aProject);
		}

		aProjectStepper.setProject(anOnyen);


		aProjectStepper.waitForClearance();

		resetIO();
		clearWindows();


	}


	public void resetIO() {
		if (System.in != origIn) {

			System.setIn(origIn);
		}

		if (System.out != origOut) {
			System.out.close();
			System.setOut(origOut);
		}

		ProjectIORedirected.newCase(this,
				(OverviewProjectStepper) projectStepper,
				projectStepper.getProject(), this);


	}

	protected DocumentDisplayer wordSourceCodeDisplayer = new AWordDocumentDisplayer();;

	public void displayOutput() {
		resetIO();

		if (outputFiles == null || outputFiles.length == 0) {

			DocumentDisplayerRegistry.display(outputFileName);
			return;
		}
		for (String anOutputFileName : outputFiles) {

			System.out.println("Displaying output from:" + anOutputFileName);
			DocumentDisplayerRegistry.display(anOutputFileName);
			// String windowsName= Common.toWindowsFileName(outputFileName);

			// Common.toCanonicalFileName(aFileName);

			// wordSourceCodeDisplayer.displayFile(windowsName);
			// wordSourceCodeDisplayer.displayAllSources();
		}

	}

	boolean projectsMade;

	public SakaiProject getOrCreateProject(String aName) {
		SakaiProject project = onyenToProject.get(aName);
		if (project == null) {
			StudentCodingAssignment aStudentAssignment = getStudentAssignment(aName);

			if (aStudentAssignment == null || aStudentAssignment.getSubmissionFolder() == null) {
//				Tracer.error("No project for student:" + aName);

				return null;
			}
			project = makeProject(aStudentAssignment);
			if (project != null /*&& !project.isNoProjectFolder()*/) {
				onyenToProject.put(aStudentAssignment.getOnyen(), project);
			}
		}
		return project;
	}

	public StudentCodingAssignment getStudentAssignment(String anOnyen) {
		GenericStudentAssignmentDatabase<StudentCodingAssignment> aStudentAssignmentDatabase = getStudentAssignmentDatabase();
		return aStudentAssignmentDatabase.getStudentAssignmentFromOnyen(anOnyen);
//		Collection<StudentCodingAssignment> studentAssignments = aStudentAssignmentDatabase
//				.getStudentAssignments();
//		for (StudentCodingAssignment anAssignment : studentAssignments) {
//			if (anAssignment.getOnyen().equals(anOnyen))
//				return anAssignment;
//
//		}
//		return null;

	}


	/**
	 * replacing it with Josh's better version below that called
	 * getBulkAssignmentFolder()
	 */
	// @Override
	// public GenericStudentAssignmentDatabase<StudentCodingAssignment>
	// getStudentAssignmentDatabase() {
	// if (studentAssignmentDatabase == null) {
	// studentAssignmentDatabase = new ASakaiStudentCodingAssignmentsDatabase(
	// bulkFolder);
	//
	// }
	// return studentAssignmentDatabase;
	//
	// }

	@Override
	public GenericStudentAssignmentDatabase<StudentCodingAssignment> getStudentAssignmentDatabase() {
		if (studentAssignmentDatabase == null)
			studentAssignmentDatabase = new ASakaiStudentCodingAssignmentsDatabase(
					getBulkAssignmentFolder());
		return studentAssignmentDatabase;
	}

	GenericStudentAssignmentDatabase<StudentCodingAssignment> studentAssignmentDatabase;

	// duplicated fiunctinality in ProjectDatabaseWrapper
	 File maybeCreateFolder(String aName) {
		File theDir = new File(aName);

		// if the directory does not exist, create it
		if (!theDir.exists()) {
			// boolean result = theDir.mkdir();
			theDir.mkdirs();
			AssignmentDataFolderCreated.newCase(theDir.getName(), this);

		} else {
			AssignmentDataFolderLoaded.newCase(theDir.getName(), this);
		}
		return theDir;
	}

	// duplicated functinality in ProjectDatabaseWrapper

	public static File maybeCreateFile(String aFileName) {
		File theFile = new File(aFileName);

		// if the file does not exist, create it
		if (!theFile.exists()) {
			try {
				boolean result = theFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return theFile;
	}

	// duplicated functinality in ProjectDatabaseWrapper

	void maybeWriteStudentIDs(File file) {
		if (startStudentID == null && endStudentID == null)
			return;
		StringBuffer stringBuffer = new StringBuffer();
		Set<String> allStudentFolderNames = bulkFolder.getStudentFolderNames();
		boolean first = true;
		for (String studentFolderName : allStudentFolderNames) {
			String studentID = ASakaiBulkAssignmentFolder
					.extractOnyen(studentFolderName);
			if (startStudentID.compareToIgnoreCase(studentID) <= 0
					&& endStudentID.compareToIgnoreCase(studentID) >= 0) {
				if (!first) {
					stringBuffer.append("\n");
				}
				stringBuffer.append(studentID);
				first = !first;
			}

		}
		try {
			Common.writeText(file, stringBuffer.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// void initAssignmentDataAndFolder() {
	// bulkFolder = new ASakaiBulkAssignmentFolder(bulkAssignmentsFolderName,
	// assignmentRoot);
	// }

	@Override
	public void clear() {
		System.out.println("Clearing project database");
		bulkFolder.clear();
		assignmentDataFolder.clear();
		GenericStudentAssignmentDatabase<StudentCodingAssignment> aStudentAssignmentDatabase = getStudentAssignmentDatabase();

		Collection<StudentCodingAssignment> studentAssignments = aStudentAssignmentDatabase
				.getStudentAssignments();
		for (StudentCodingAssignment anAssignment : studentAssignments) {
			RootFolderProxy projectFolder = anAssignment.getProjectFolder();
			

//			if (assignmentDataFolder != null
//					&& !assignmentDataFolder.getStudentIDs().contains(
//							anAssignment.getOnyen()))
//				continue;
//			if (anAssignment.getStudentFolder() == null
//					|| anAssignment.getSubmissionFolder() == null)
//				continue; // assume a message has already been given

			SakaiProject project = onyenToProject.get(anAssignment.getOnyen());
			if (project != null) {
				project.clear();
			}
			if (projectFolder != null) {
				projectFolder.clear();
			}
			
			
		}
		
	}
	/*
	 * this does more than initialize projects
	 */
	public void maybeMakeProjects() {
		if (projectsMade)
			return;
		projectsMade = true;

		bulkFolder = new ASakaiBulkAssignmentFolder(bulkAssignmentsFolderName,
				assignmentRoot, fileNameSorter);
		String assignmentName = bulkFolder.getAssignmentName();
		if (assignmentsDataFolderName == null)
			assignmentsDataFolderName = BasicGradingEnvironment.get()
					.getDefaultAssignmentsDataFolderName();
		// if (assignmentsDataFolderName.startsWith("null"))
		// assignmentsDataFolderName = null;
		if (assignmentsDataFolderName != null) { // we may be creating the
													// database without folder
													// name
			String specificAssignmentDataFolderName = assignmentsDataFolderName
					+ "/" + assignmentName;
			maybeCreateFolder(specificAssignmentDataFolderName);
			File idFile = maybeCreateFile(specificAssignmentDataFolderName
					+ "/" + AnAssignmenDataFolder.ID_FILE_NAME);

			maybeWriteStudentIDs(idFile);

			assignmentDataFolder = new AnAssignmenDataFolder(
					specificAssignmentDataFolderName,
					bulkFolder.getSpreadsheet());

			if (!assignmentDataFolder.exists()) {
				System.out.println("Expecting assignment data folder:"
						+ specificAssignmentDataFolderName);
			} else if (assignmentDataFolder.getRequirementsSpreadsheetFile() != null) {
				
				csvRequirementsSpecification = new ACSVRequirementsSpecification(assignmentDataFolder.getRequirementsSpreadsheetFile());
			}
			

		}
		addStudentProjects();

//		// GenericStudentAssignmentDatabase<StudentCodingAssignment>
//		// studentAssignmentDatabase = new
//		// ASakaiStudentCodingAssignmentsDatabase(bulkFolder);
//		GenericStudentAssignmentDatabase<StudentCodingAssignment> aStudentAssignmentDatabase = getStudentAssignmentDatabase();
//
//		// studentAssignmentDatabase = new
//		// ASakaiStudentCodingAssignmentsDatabase(bulkFolder);
//
//		System.out.println("Student ids from assignment database:" + aStudentAssignmentDatabase.getStudentIds());
//		Collection<StudentCodingAssignment> studentAssignments = aStudentAssignmentDatabase
//				.getStudentAssignments();
//		
//		for (StudentCodingAssignment anAssignment : studentAssignments) {
//			if (onyenToProject.get(anAssignment.getOnyen()) != null) {
//				continue;
//			}
//			RootFolderProxy projectFolder = anAssignment.getProjectFolder();
//
//			if (assignmentDataFolder != null
//					&& !assignmentDataFolder.getStudentIDs().contains(
//							anAssignment.getOnyen()))
//				continue;
//			if (anAssignment.getStudentFolder() == null
//					|| anAssignment.getSubmissionFolder() == null)
//				continue; // assume a message has already been given
//
//			SakaiProject project = makeProject(anAssignment);
//			if (project != null /* && !project.isNoProjectFolder() */) {
//				onyenToProject.put(anAssignment.getOnyen(), project);
//			}
//		}
	}
	Set<String> currentOnyentSet;

	@Override
	public void addStudentProjects() {
		List<String> aRawOnyens = NavigationListManagerFactory.getNavigationListManager().getRawOnyenNavigationList();
   	 Set<String> aRawOnyenSet = new HashSet(aRawOnyens);
   	 if (currentOnyentSet != null  && currentOnyentSet.containsAll(aRawOnyenSet)) {
   		 return;
   	 }
   	 
   		 
   			 
		// GenericStudentAssignmentDatabase<StudentCodingAssignment>
				// studentAssignmentDatabase = new
				// ASakaiStudentCodingAssignmentsDatabase(bulkFolder);
				GenericStudentAssignmentDatabase<StudentCodingAssignment> aStudentAssignmentDatabase = getStudentAssignmentDatabase();

				// studentAssignmentDatabase = new
				// ASakaiStudentCodingAssignmentsDatabase(bulkFolder);
//		if (currentOnyentSet != null) {
//			writeOnyens(); // the database checks this ile
//			aStudentAssignmentDatabase.createStudentAssignments();
//		}
		System.out.println("Student ids from assignment database:" + aStudentAssignmentDatabase.getStudentIds());
		Collection<StudentCodingAssignment> studentAssignments = aStudentAssignmentDatabase
				.getStudentAssignments();
		
		for (StudentCodingAssignment anAssignment : studentAssignments) {
			if (onyenToProject.get(anAssignment.getOnyen()) != null) {
				continue;
			}
			RootFolderProxy projectFolder = anAssignment.getProjectFolder();

			if (assignmentDataFolder != null
					&& !assignmentDataFolder.getStudentIDs().contains(
							anAssignment.getOnyen()))
				continue;
			if (anAssignment.getStudentFolder() == null
					|| anAssignment.getSubmissionFolder() == null)
				continue; // assume a message has already been given

			SakaiProject project = makeProject(anAssignment);
			if (project != null /* && !project.isNoProjectFolder() */) {
				onyenToProject.put(anAssignment.getOnyen(), project);
			}
		}
		currentOnyentSet = aRawOnyenSet;
	}
	@Override
	public void removeProject(String anOnyen) {
		onyenToProject.remove(anOnyen);
	}
//	public void oldMaybeMakeProjects() {
//		if (projectsMade)
//			return;
//		projectsMade = true;
//		if (bulkFolder == null)
//
//		bulkFolder = new ASakaiBulkAssignmentFolder(bulkAssignmentsFolderName,
//				assignmentRoot, fileNameSorter);
//		String assignmentName = bulkFolder.getAssignmentName();
//		if (assignmentsDataFolderName == null)
//			assignmentsDataFolderName = BasicGradingEnvironment.get()
//					.getDefaultAssignmentsDataFolderName();
//		// if (assignmentsDataFolderName.startsWith("null"))
//		// assignmentsDataFolderName = null;
//		if (assignmentsDataFolderName != null) { // we may be creating the
//													// database without folder
//													// name
//			String specificAssignmentDataFolderName = assignmentsDataFolderName
//					+ "/" + assignmentName;
//			maybeCreateFolder(specificAssignmentDataFolderName);
//			File idFile = maybeCreateFile(specificAssignmentDataFolderName
//					+ "/" + AnAssignmenDataFolder.ID_FILE_NAME);
//
//			maybeWriteStudentIDs(idFile);
//
//			assignmentDataFolder = new AnAssignmenDataFolder(
//					specificAssignmentDataFolderName,
//					bulkFolder.getSpreadsheet());
//
//			if (!assignmentDataFolder.exists()) {
//				System.out.println("Expecting assignment data folder:"
//						+ specificAssignmentDataFolderName);
//			} else if (assignmentDataFolder.getRequirementsSpreadsheetFile() != null) {
//				
//				csvRequirementsSpecification = new ACSVRequirementsSpecification(assignmentDataFolder.getRequirementsSpreadsheetFile());
//			}
//			
//
//		}
//
//		// GenericStudentAssignmentDatabase<StudentCodingAssignment>
//		// studentAssignmentDatabase = new
//		// ASakaiStudentCodingAssignmentsDatabase(bulkFolder);
//		GenericStudentAssignmentDatabase<StudentCodingAssignment> aStudentAssignmentDatabase = getStudentAssignmentDatabase();
//
//		// studentAssignmentDatabase = new
//		// ASakaiStudentCodingAssignmentsDatabase(bulkFolder);
//
//		System.out.println("Student ids from assignment database:" + aStudentAssignmentDatabase.getStudentIds());
//		Collection<StudentCodingAssignment> studentAssignments = aStudentAssignmentDatabase
//				.getStudentAssignments();
//		
//		for (StudentCodingAssignment anAssignment : studentAssignments) {
//			if (onyenToProject.get(anAssignment.getOnyen()) != null) {
//				continue;
//			}
//			RootFolderProxy projectFolder = anAssignment.getProjectFolder();
//
//			if (assignmentDataFolder != null
//					&& !assignmentDataFolder.getStudentIDs().contains(
//							anAssignment.getOnyen()))
//				continue;
//			if (anAssignment.getStudentFolder() == null
//					|| anAssignment.getSubmissionFolder() == null)
//				continue; // assume a message has already been given
//
//			SakaiProject project = makeProject(anAssignment);
//			if (project != null /* && !project.isNoProjectFolder() */) {
//				onyenToProject.put(anAssignment.getOnyen(), project);
//			}
//		}
//	}

	public void setGradeRecorder(FinalGradeRecorder gradeRecorder) {
		this.gradeRecorder = gradeRecorder;
	}

	public void setFeatureGradeRecorder(
			FeatureGradeRecorder featureGradeRecorder) {
		this.featureGradeRecorder = featureGradeRecorder;
	}

	public static void main(String[] args) {
		SakaiProjectDatabase projectDatabase = new ASakaiProjectDatabase(
				ASakaiBulkAssignmentFolder.DEFAULT_BULK_DOWNLOAD_FOLDER,

				"C:/Users/dewan/Downloads/GraderData", false);
		// projectDatabase.runProjectInteractively("mkcolema");
		// projectDatabase.runProjectsInteractively();
		try {
			projectDatabase.nonBlockingRunProjectsInteractively();
		} catch (InvalidOnyenRangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Added by Josh
	public void setManualFeedback(ManualFeedback manualFeedback) {
		this.manualFeedback = manualFeedback;
	}

	// Added by Josh
	public void setAutoFeedback(AutoFeedback autoFeedback) {
		this.autoFeedback = autoFeedback;
	}

	// Added by Josh
	public void setBulkFolder(BulkAssignmentFolder bulkFolder) {
		this.bulkFolder = bulkFolder;
	}

	// Added by Josh
	public void setAssignmentDataFolder(
			AssignmentDataFolder assignmentDataFolder) {
		this.assignmentDataFolder = assignmentDataFolder;
	}

	public void saveProject(String onyen, SakaiProject project) {
		onyenToProject.put(onyen, project);
	}

	@Override
	public Checkable getRequirement(GradingFeature aGradingFeature) {
		return featureToCheckable.get(aGradingFeature);
	}

	/**
	 * This generates grading features based on the project requirements
	 * 
	 * @param requirements
	 *            The FrameworkProjectRequirements to add to the project
	 *            database
	 */
	@Override
	public void setProjectRequirements(ProjectRequirements requirements) {
		projectRequirements = requirements;
		List<GradingFeature> gradingFeatures = new ArrayList<GradingFeature>();
		if (requirements != null) {
			// Add the features
			for (Feature feature : requirements.getFeatures()) {
				GradingFeature gradingFeature = new AGradingFeature(
						feature.isManual(),
						feature.getName(), feature.getPoints(),
						new FeatureCheckerWrapper(feature),
						feature.isExtraCredit());
				gradingFeatures.add(gradingFeature);
				gradingFeature.setFeature(feature);
				feature.setGradingFeature(gradingFeature);
				featureToCheckable.put(gradingFeature, feature);
			}

			// Add the restrictions
			for (Restriction restriction : requirements.getRestrictions()) {
				GradingFeature gradingFeature = new AGradingFeature(
						restriction.getName(), restriction.getPoints(),
						new FeatureCheckerWrapper(restriction));
				gradingFeatures.add(gradingFeature);
				gradingFeature.setFeature(restriction);
				restriction.setGradingFeature(gradingFeature);
				featureToCheckable.put(gradingFeature, restriction);
			}
		}

		addGradingFeatures(gradingFeatures);
	}

	@Override
	public ProjectRequirements getProjectRequirements() {
		return projectRequirements;
	}

	@Override
	public PhotoReader getPhotoReader() {
		return photoReader;
	}

	@Override
	public void setPhotoReader(PhotoReader pictureReader) {
		this.photoReader = pictureReader;
	}

	@Override
	public String getAssignmentsDataFolderName() {
		return assignmentsDataFolderName;
	}

	@Override
	public void setAssignmentsDataFolderName(String assignmentsDataFolderName) {
		this.assignmentsDataFolderName = assignmentsDataFolderName;
	}

	@Override
	public Colorer<GradingFeature> getGradingFeatureColorer() {
		return gradingFeatureColorer;
	}

	@Override
	public void setGradingFeatureColorer(
			Colorer<GradingFeature> gradingFeatureColorComputer) {
		this.gradingFeatureColorer = gradingFeatureColorComputer;
	}

	@Override
	public Icon getStudentPhoto(String anOnyen, SakaiProject aProject) { // so
																			// we
																			// do
																			// not
																			// lookup
																			// the
																			// project
		Icon retVal = aProject.getStudentPhoto();
		if (retVal == null) {
			retVal = getPhotoReader().getIcon(anOnyen);
			aProject.setStudentPhoto(retVal);
		}
		return retVal;
	}

	public Colorer<Double> getScoreColorer() {
		return scoreColorer;
	}

	public void setScoreColorer(Colorer<Double> scoreColorer) {
		this.scoreColorer = scoreColorer;
	}

	public Colorer<Double> getMultiplierColorer() {
		return multiplierColorer;
	}

	public void setMultiplierColorer(Colorer<Double> multiplierColorer) {
		this.multiplierColorer = multiplierColorer;
	}

	public Colorer<String> getOverallNotesColorer() {
		return overallNotesColorer;
	}

	public void setOverallNotesColorer(Colorer<String> overallNotesColorer) {
		this.overallNotesColorer = overallNotesColorer;
	}

	@Override
	public GraderSettingsModel getGraderSettings() {
		return graderSettings;
	}

	@Override
	public void setGraderSettings(GraderSettingsModel graderSettings) {
		this.graderSettings = graderSettings;
		if (graderSettings != null) {
			BasicNavigationFilter dispatcher = new ADispatchingFilter(
					graderSettings.getNavigationSetter()
							.getNavigationFilterSetter());
			setNavigationFilter(dispatcher);
			// maybeReinit();

		}
	}

	// void maybeReinit() {
	// String aBulkAssignmentsFolderName =
	// graderSettings.getFileBrowsing().getDownloadFolder().getText(); // update
	// in case user changed the name
	// if (bulkAssignmentsFolderName.equals(aBulkAssignmentsFolderName)) return;
	// init(aBulkAssignmentsFolderName, assignmentsDataFolderName,
	// assignmentRoot);
	// }
	@Override
	public BasicNavigationFilter getNavigationFilter() {
		return navigationFilter;
	}

	@Override
	public void setNavigationFilter(BasicNavigationFilter navigationFilter) {
		this.navigationFilter = navigationFilter;

	}

	protected NotesGenerator createNotesGenerator() {
		return new ANotesGenerator(this);
	}

	protected ClearanceManager createClearanceManager() {
		return new AClearanceManager();
	}

	@Override
	public NotesGenerator getNotesGenerator() {
		return notesGenerator;
	}

	@Override
	public void setNotesGenerator(NotesGenerator notesGenerator) {
		this.notesGenerator = notesGenerator;
	}

	@Override
	public String getSourceFileNameSuffix() {
		return sourceFileNameSuffix;
	}

	@Override
	public void setSourceFileNameSuffix(String sourceSuffix) {
		this.sourceFileNameSuffix = sourceSuffix;
	}

	@Override
	public ClearanceManager getClearanceManager() {
		return clearanceManager;
	}

	@Override
	public void setClearanceManager(ClearanceManager clearanceManager) {
		this.clearanceManager = clearanceManager;
	}

	@Override
	public AutomaticProjectNavigator getAutomaticProjectNavigator() {
		return automaticProjectNavigator;
	}

	@Override
	public void setAutomaticProjectNavigator(
			AutomaticProjectNavigator automaticProjectNavigator) {
		this.automaticProjectNavigator = automaticProjectNavigator;
	}

	@Override
	public HybridProjectNavigator getHybridProjectNavigator() {
		return hybridProjectNavigator;
	}

	@Override
	public void setHybridProjectNavigator(
			HybridProjectNavigator hybridProjectNavigator) {
		this.hybridProjectNavigator = hybridProjectNavigator;
	}

	public ManualProjectNavigator getManualProjectNavigator() {
		return manualProjectNavigator;
	}

	public void setManualProjectNavigator(
			ManualProjectNavigator manualProjectNavigator) {
		this.manualProjectNavigator = manualProjectNavigator;
	}

	public ProjectNavigator getProjectNavigator() {
		return projectNavigator;
	}

	public void setProjectNavigator(ProjectNavigator projectNavigator) {
		this.projectNavigator = projectNavigator;
	}

	public String getSourceFileNamePrefix() {
		return sourceFileNamePrefix;
	}

	public void setSourceFileNamePrefix(String sourceFileNamePrefix) {
		this.sourceFileNamePrefix = sourceFileNamePrefix;
	}

	@Override
	public Comparator<String> getFileNameSorter() {
		return fileNameSorter;
	}

	@Override
	public void setFileNameSorter(Comparator<String> fileNameSorter) {
		this.fileNameSorter = fileNameSorter;
	}
	@Override
	public CSVRequirementsSpecification getCSVRequirementsSpecification() {
		return csvRequirementsSpecification;
	}
	@Override
	public void setCSVRequirementsSpecification(CSVRequirementsSpecification newValue) {
		 csvRequirementsSpecification = newValue;
	}



	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == onyens) {
//			addStudentProjects();
			if (currentOnyentSet != null) {
//				writeOnyens(); // the database checks this ile
				GenericStudentAssignmentDatabase<StudentCodingAssignment> aStudentAssignmentDatabase = getStudentAssignmentDatabase();
				aStudentAssignmentDatabase.createStudentAssignments();
				writeOnyens(); // will call the navigator which will create the project
				
			}
		}		
	}
	/*
	 * To be overridden
	 */
	 protected void writeOnyens() {
		 
	 }
}
