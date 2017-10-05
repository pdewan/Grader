package grader.sakai.project;

import framework.grading.ProjectRequirements;
import framework.grading.testing.Checkable;
import grader.assignment.AssignmentDataFolder;
import grader.assignment.GradingFeature;
import grader.assignment.GradingFeatureList;
import grader.auto_notes.NotesGenerator;
import grader.colorers.Colorer;
import grader.feedback.AutoFeedback;
import grader.feedback.ManualFeedback;
import grader.feedback.ScoreFeedback;
import grader.feedback.SourceDisplayer;
import grader.navigation.ProjectNavigator;
import grader.navigation.automatic.AutomaticProjectNavigator;
import grader.navigation.filter.BasicNavigationFilter;
import grader.navigation.hybrid.HybridProjectNavigator;
import grader.navigation.manual.ManualProjectNavigator;
import grader.photos.PhotoReader;
import grader.requirements.interpreter.specification.CSVRequirementsSpecification;
import grader.sakai.BulkAssignmentFolder;
import grader.sakai.GenericStudentAssignmentDatabase;
import grader.sakai.StudentCodingAssignment;
import grader.settings.GraderSettingsModel;
import grader.spreadsheet.FeatureGradeRecorder;
import grader.spreadsheet.FinalGradeRecorder;
import grader.trace.settings.InvalidOnyenRangeException;
import grader.trace.settings.MissingOnyenException;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.swing.Icon;

import util.misc.ClearanceManager;

public interface SakaiProjectDatabase {
	public BulkAssignmentFolder getBulkAssignmentFolder();
	
	public AssignmentDataFolder getAssignmentDataFolder() ;
	
	public SakaiProject getProject(String aName) ;
	public Set<String> getOnyens() ;
	
	public Collection<SakaiProject> getProjects();
	public SakaiProject runProject(String anOnyen);
	public void runProjectInteractively(String anOnyen);
	
	public void runProjectInteractively(String anOnyen, ProjectStepper projectStepper) ;
	public void displayOutput();
	public void runProjectsInteractively();

	FinalGradeRecorder getGradeRecorder();
	
	public GradingFeatureList getGradingFeatures();

	FeatureGradeRecorder getFeatureGradeRecorder();
	public void setGradeRecorder(FinalGradeRecorder gradeRecorder) ;

	public void setFeatureGradeRecorder(FeatureGradeRecorder featureGradeRecorder) ;
	SakaiProject runProject(String anOnyen, SakaiProject aProject);

	void resetRunningProject(SakaiProject aProject);
	public FinalGradeRecorder getTotalScoreRecorder() ;
	public void  setTotalScoreRecorder(FinalGradeRecorder newVal) ;

	public AutoFeedback getAutoFeedback() ;
	
	public ManualFeedback getManualFeedback();
	
	public ScoreFeedback getScoreFeedback() ;

	public SourceDisplayer getSourceDisplayer() ;
	void initIO();
	
		
	void recordWindows();
	
	void resetIO();
	void clearWindows();
	public List<String> getOnyenNavigationList(SakaiProjectDatabase aSakaiProjectDatabase);
	public List<String> getOnyenNavigationList();
	boolean nonBlockingRunProjectsInteractively() throws InvalidOnyenRangeException;
//	public String getNavigationFilter() ;
//
//	public void setNavigationFilter(String navigationFilter) ;

	void startProjectStepper();

	public Object displayProjectStepper(ProjectStepper aProjectStepper) ;

	GenericStudentAssignmentDatabase<StudentCodingAssignment> getStudentAssignmentDatabase();

	 public ProjectStepper getProjectStepper() ;

	public void setProjectStepper(ProjectStepper projectStepper) ;
	ProjectStepper getOrCreateProjectStepper();

	void setProjectRequirements(ProjectRequirements requirements);

	ProjectRequirements getProjectRequirements();

	Checkable getRequirement(GradingFeature aGradingFeature);

	PhotoReader getPhotoReader();

	void setPhotoReader(PhotoReader pictureReader);

	String getAssignmentsDataFolderName();

	void setAssignmentsDataFolderName(String assignmentsDataFolderName);

	Colorer<GradingFeature> getGradingFeatureColorer();

	void setGradingFeatureColorer(
			Colorer<GradingFeature> gradingFeatureColorComputer);

	Icon getStudentPhoto(String anOnyen, SakaiProject aProject);
	public Colorer<Double> getScoreColorer() ;
	public void setScoreColorer(Colorer<Double> scoreColorer) ;

	public Colorer<Double> getMultiplierColorer() ;

	public void setMultiplierColorer(Colorer<Double> multiplierColorer) ;

	public Colorer<String> getOverallNotesColorer();

	public void setOverallNotesColorer(Colorer<String> overallNotesColorer) ;

	GraderSettingsModel getGraderSettings();

	void setGraderSettings(GraderSettingsModel graderSettings);

	BasicNavigationFilter getNavigationFilter();

	void setNavigationFilter(BasicNavigationFilter navigationFilter);

	NotesGenerator getNotesGenerator();

	void setNotesGenerator(NotesGenerator notesGenerator);

	boolean nonBlockingRunProjectsInteractively(String aGoToOnyen)
			throws MissingOnyenException, InvalidOnyenRangeException;

	String getSourceFileNameSuffix();

	void setSourceFileNameSuffix(String sourceSuffix);

	ClearanceManager getClearanceManager();

	void setClearanceManager(ClearanceManager clearanceManager);

	AutomaticProjectNavigator getAutomaticProjectNavigator();

	public void setAutomaticProjectNavigator(
			AutomaticProjectNavigator automaticProjectNavigator);

	public ManualProjectNavigator getManualProjectNavigator() ;

	public void setManualProjectNavigator(
			ManualProjectNavigator manualProjectNavigator) ;

	public ProjectNavigator getProjectNavigator() ;

	public void setProjectNavigator(ProjectNavigator projectNavigator) ;

	boolean startProjectStepper(String aGoToOnyen) throws MissingOnyenException, InvalidOnyenRangeException;

	HybridProjectNavigator getHybridProjectNavigator();

	void setHybridProjectNavigator(HybridProjectNavigator hybridProjectNavigator);

	Comparator<String> getFileNameSorter();

	void setFileNameSorter(Comparator<String> fileNameSorter);

	CSVRequirementsSpecification getCSVRequirementsSpecification();

	void setCSVRequirementsSpecification(CSVRequirementsSpecification newValue);

	void restoreGraderDirectory();

	void clear();
	




}
