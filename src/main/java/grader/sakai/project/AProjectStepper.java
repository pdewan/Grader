package grader.sakai.project;

//<<<<<<< HEAD
//import bus.uigen.ObjectEditor;
//import grader.assignment.GradingFeature;
//import grader.assignment.GradingFeatureList;
//import grader.documents.DocumentDisplayerRegistry;
//import grader.file.FileProxyUtils;
//import grader.spreadsheet.FeatureGradeRecorder;
//import grader.spreadsheet.FinalGradeRecorder;
//import util.annotations.*;
//import util.misc.AClearanceManager;
//import util.misc.Common;
//import util.trace.Tracer;
//
//=======
import framework.execution.ARunningProject;
import framework.grading.testing.CheckResult;
import framework.grading.testing.Checkable;
import framework.logging.recorder.ConglomerateRecorder;
import framework.navigation.StudentFolder;
import grader.assignment.AGradingFeature;
import grader.assignment.GradingFeature;
import grader.assignment.GradingFeatureList;
import grader.auto_notes.NotesGenerator;
import grader.basics.settings.BasicGradingEnvironment;
import grader.basics.util.Option;
import grader.documents.DocumentDisplayerRegistry;
import grader.feedback.ScoreFeedback;
import grader.navigation.NavigationKind;
import grader.navigation.filter.BasicNavigationFilter;
import grader.photos.APhotoReader;
import grader.settings.GraderSettingsModel;
import grader.settings.navigation.NavigationSetter;
import grader.spreadsheet.FeatureGradeRecorder;
import grader.spreadsheet.FinalGradeRecorder;
import grader.spreadsheet.csv.ASakaiCSVFeatureGradeManager;
import grader.spreadsheet.csv.ASakaiCSVFinalGradeManager;
import grader.spreadsheet.csv.AllStudentsHistoryManagerFactory;
import grader.trace.settings.MissingOnyenException;

import java.awt.Color;
//>>>>>>> working
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
//<<<<<<< HEAD
//import java.io.IOException;
//import java.util.List;
//@StructurePattern(StructurePatternNames.BEAN_PATTERN)
//public class AProjectStepper extends AClearanceManager implements ProjectStepper{
//	PropertyChangeSupport propertyChangeSupport =new PropertyChangeSupport(this);
//	public final static long  UI_LOAD_TIME = 10*1000;
//	boolean firstTime;
////	List<OEFrame>  oldList;
////	Window[] oldWindows;
//=======
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JTextArea;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;

import util.annotations.ComponentHeight;
import util.annotations.ComponentWidth;
import util.annotations.PreferredWidgetClass;
import util.annotations.Row;
import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.annotations.Visible;
import util.misc.AClearanceManager;
import util.misc.Common;
import util.models.ALabelBeanModel;
import util.models.LabelBeanModel;
import util.trace.Tracer;
import wrappers.framework.project.ProjectWrapper;
import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.introspect.Attribute;
import bus.uigen.oadapters.ClassAdapter;
import bus.uigen.oadapters.ObjectAdapter;

@StructurePattern(StructurePatternNames.BEAN_PATTERN)
public class AProjectStepper extends AClearanceManager implements
		ProjectStepper {
	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
			this);
	public final static long UI_LOAD_TIME = 10 * 1000;
	boolean firstTime;
	String COMMENTS_FILE_PREFIX = "Comments";
	// List<OEFrame> oldList;
	// Window[] oldWindows;
//>>>>>>> working
	String name = "";
	SakaiProjectDatabase projectDatabase;
	double score;
	List<String> documents;
	int nextDocumentIndex = 0;
//<<<<<<< HEAD
//	FeatureGradeRecorder featureGradeRecorder;
//	
//	String onyen = "";
//	
//	SakaiProject project;
////	FinalGradeRecorder gradeRecorder;
//=======
	// ideally this stuff should really be done through property change as
	// Josh's wrapper does
	FeatureGradeRecorder featureGradeRecorder;
	double multiplier = 1;
	String onyen = "";
	String commentsFileName = "";

	SakaiProject project;
	grader.basics.project.Project wrappedProject;
	// FinalGradeRecorder gradeRecorder;
//>>>>>>> working
	boolean hasMoreSteps = true;
	FinalGradeRecorder totalScoreRecorder;
	boolean manualOnyen;
	String logFile, gradedFile, skippedFile;
//<<<<<<< HEAD
//	
////	FinalGradeRecorder gradeRecorder() {
////		return projectDatabase.getGradeRecorder();
////	}
////	FinalGradeRecorder totalScoreRecorder() {
////		return projectDatabase.getTotalScoreRecorder();
////	}
//	public void setProjectDatabase(SakaiProjectDatabase aProjectDatabase) {
//		projectDatabase = aProjectDatabase;
////		gradeRecorder = aProjectDatabase.getGradeRecorder();
//=======
	String manualNotes = "", autoNotes = "", feedback = "", overallNotes = "";
	List<CheckResult> featureResults;
	List<CheckResult> restrictionResults;
	GradingFeature selectedGradingFeature;
	StudentFolder studentFolder;
	Object frame;
	uiFrame oeFrame;
	List<ObjectAdapter> gradingObjectAdapters = new ArrayList();
	ClassAdapter stepperViewAdapter;
	ObjectAdapter multiplierAdapter, scoreAdapter, gradingFeaturesAdapter, overallNotesAdapter; 
	List<Color> currentColors = new ArrayList(), nextColors = new ArrayList();
	Color currentScoreColor, currentMultiplierColor, currentOverallNotesColor;
	Color nextScoreColor, nextMultiplierColor, nextOverallNotesColor;

	boolean changed;
	Icon studentPhoto;
	LabelBeanModel photoLabelBeanModel;
	String output = "";

	// FinalGradeRecorder gradeRecorder() {
	// return projectDatabase.getGradeRecorder();
	// }
	// FinalGradeRecorder totalScoreRecorder() {
	// return projectDatabase.getTotalScoreRecorder();
	// }
	public AProjectStepper() {
		photoLabelBeanModel = new ALabelBeanModel("");
//		addPropertyChangeListener(this); // listen to yourself to see if you have changed
	}
	public void setProjectDatabase(SakaiProjectDatabase aProjectDatabase) {
		projectDatabase = aProjectDatabase;
		// gradeRecorder = aProjectDatabase.getGradeRecorder();
//>>>>>>> working
		featureGradeRecorder = aProjectDatabase.getFeatureGradeRecorder();
		totalScoreRecorder = aProjectDatabase.getTotalScoreRecorder();
		registerWithGradingFeatures();
		logFile = aProjectDatabase.getAssignmentDataFolder().getLogFileName();
//<<<<<<< HEAD
//		gradedFile = aProjectDatabase.getAssigmentDataFolder().getGradedIdFileName();
//		skippedFile = aProjectDatabase.getAssigmentDataFolder().getSkippedIdFileName();
////		recordWindows(); // the first project does not wait so we need to record here
//		
//	}
//	boolean runExecuted;
//	
//	boolean runAttempted() {
//		return runExecuted|| isAutoRun();
//	}
//	
//	@ComponentWidth(150)
//	@Row(0)
//	public String getOnyen() {
//		return onyen;
//	}
//	
//	public void setOnyen(String anOnyen) {
////		project = projectDatabase.getProject(anOnyen);
//		String oldOnyen = onyen;
//=======
		gradedFile = aProjectDatabase.getAssignmentDataFolder()
				.getGradedIdFileName();
		skippedFile = aProjectDatabase.getAssignmentDataFolder()
				.getSkippedIdFileName();
		GraderSettingsModel graderSettings = aProjectDatabase.getGraderSettings();
//		graderSettings.getNavigationSetter().getNavigationFilterSetter().addPropertyChangeListener(this);
		getNavigationSetter().getNavigationFilterSetter().addPropertyChangeListener(this);
		
		// recordWindows(); // the first project does not wait so we need to
		// record here

	}

	boolean runExecuted;
@Override
	public boolean runAttempted() {
		return runExecuted || isAutoRun() || isAutoAutoGrade();
	}

	@ComponentWidth(150)
	@Row(0)
	@Override
	public String getOnyen() {
		return onyen;
	}

	String getCommentsFileName(SakaiProject aProject) {
		return AGradingFeature.getFeedbackFolderName(aProject)
				+ COMMENTS_FILE_PREFIX + AGradingFeature.FEEDBACK_FILE_SUFFIX;

	}

	String readComments(SakaiProject aProject) {
		try {
			return FileUtils.readFileToString(new File(
					getCommentsFileName(aProject)));
		} catch (IOException e) {
			return "";
		}
	}

	void writeComments(SakaiProject aProject, String newVal) {
		if (newVal.equals("")) // why create file?
			return;
		try {
			FileUtils.writeStringToFile(
					new File(getCommentsFileName(aProject)), newVal);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void setOnyen(String anOnyen) throws MissingOnyenException {
		internalSetOnyen(anOnyen);
		manualOnyen = true;
	}
	@Override
	public void internalSetOnyen(String anOnyen) throws MissingOnyenException {
		// project = projectDatabase.getProject(anOnyen);
		String oldOnyen = onyen;
		int onyenIndex = onyens.indexOf(anOnyen);
		if (onyenIndex < 0) {
			Tracer.error("Student:" + anOnyen + " does not exist in specified onyen range");
			throw new MissingOnyenException(anOnyen, this);
//			return;
		}
		currentOnyenIndex = onyenIndex;
		maybeSaveState();
		redirectProject();
//>>>>>>> working
		boolean retVal = setProject(anOnyen);
		if (!retVal) {
			onyen = oldOnyen;
			propertyChangeSupport.firePropertyChange("onyen", null, onyen);
			return;
		}
//<<<<<<< HEAD
//		projectDatabase.resetRunningProject(project);
//
//		if (autoRun)
//		projectDatabase.runProject(anOnyen, project);
//		manualOnyen = true;
//	
////		projectDatabase.runProjectInteractively(anOnyen, this);
////		onyen = anOnyen;
////		setProject( projectDatabase.getProject(anOnyen));
////		projectDatabase.
//		
//		
//	}
//	public boolean setProject(String anOnyen) {
//		
//		onyen = anOnyen;
//		return setProject( projectDatabase.getProject(anOnyen));
//		
//				
//	}
//	boolean autoRun = false;
//	public boolean isAutoRun() {
//		return autoRun;
//		
//	}
//    public void setAutoRun(boolean newVal) {
//    	autoRun = newVal;
//		
//	}
//    public void autoRun() {
//    	autoRun = !autoRun;
//    }
//	@Row(1)
//	@ComponentWidth(150)
//
//	public String getName() {
//		return name;
//	}
//	
//	public void setName(String newVal) {
//		name = newVal;
////		System.out.println("name changed to" + newVal);
////		propertyChangeSupport.firePropertyChange("Name", null, newVal);
//		notifyPreconditionChanged();
////		System.out.println("precondition notified");
//	}
//	
//=======
		// again this will void a getter call when properties are redisplayed
		propertyChangeSupport.firePropertyChange(oldOnyen, null, onyen);

		// set project does most of this except the output files part
//		projectDatabase.resetRunningProject(project);
//
//		if (autoRun)
//			projectDatabase.runProject(anOnyen, project);
//		if (autoAutoGrade)
//			autoGrade();
//		manualOnyen = true;

		// projectDatabase.runProjectInteractively(anOnyen, this);
		// onyen = anOnyen;
		// setProject( projectDatabase.getProject(anOnyen));
		// projectDatabase.

	}

	public boolean setProject(String anOnyen) {

		onyen = anOnyen;
		return setProject(projectDatabase.getOrCreateProject(anOnyen));

	}

	boolean autoRun = false;

	public boolean isAutoRun() {
		return autoRun;

	}

	public void setAutoRun(boolean newVal) {
		autoRun = newVal;

	}

	public void autoRun() {
		autoRun = !autoRun;
	}

	boolean autoAutoGrade = false; // should we automatically do all the auto
									// grade

	public boolean isAutoAutoGrade() {
		return autoAutoGrade;

	}

	public void setAutoAutoGrade(boolean newVal) {
		boolean oldVal = autoAutoGrade;
		autoAutoGrade = newVal;
		propertyChangeSupport.firePropertyChange("autoAutoGrade", autoAutoGrade, getOnyen());


	}

	public void autoAutoGrade() {
		autoAutoGrade = !autoAutoGrade;
	}

	@Row(1)
	@ComponentWidth(150)
	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setName(String newVal) {
		name = newVal;
		// System.out.println("name changed to" + newVal);
		// as we are postponing notfications, sending the name change is useful
		 propertyChangeSupport.firePropertyChange("Name", null, newVal);
		notifyPreconditionChanged();
		// System.out.println("precondition notified");
	}

//>>>>>>> working
	void notifyPreconditionChanged() {
		propertyChangeSupport.firePropertyChange("this", null, this);

	}
//<<<<<<< HEAD
//	@Row(2)
//	@ComponentWidth(150)
//
//	public double getScore() {
//		return score;
//	}
//	void registerWithGradingFeatures() {
//		if (projectDatabase == null)
//			return;
//		List<GradingFeature> gradingFeatures = projectDatabase.getGradingFeatures();
//		for (GradingFeature aGradingFeature:gradingFeatures) {
//			aGradingFeature.addPropertyChangeListener(this);
//		}
//	}
//	void resetGradingFeatures() {
//		if (projectDatabase == null)
//			return;
//		
//		List<GradingFeature> gradingFeatures = projectDatabase.getGradingFeatures();
//		for (GradingFeature aGradingFeature:gradingFeatures) {
////			double lastScore = featureGradeRecorder.getGrade(project.getStudentAssignment().getStudentName(), project.getStudentAssignment().getOnyen(), aGradingFeature.getFeature());
//			double lastScore = getGrade(aGradingFeature.getFeature());
//
////			aGradingFeature.initScore(lastScore);
//			aGradingFeature.setProject(project);
////			aGradingFeature.initScore(lastScore); // this resets
//
//		}
//		for (GradingFeature aGradingFeature:gradingFeatures) {
////			double lastScore = featureGradeRecorder.getGrade(project.getStudentAssignment().getStudentName(), project.getStudentAssignment().getOnyen(), aGradingFeature.getFeature());
//			double lastScore = getGrade(aGradingFeature.getFeature());
//
////			aGradingFeature.initScore(lastScore);
////			aGradingFeature.setProject(project);
//			aGradingFeature.initScore(lastScore);
//
//		}
//	}
//	
//    void setScore() {
//    	List<GradingFeature> gradingFeatures = projectDatabase.getGradingFeatures();
//    	double aScore = 0;
//		for (GradingFeature aGradingFeature:gradingFeatures) {
//			aScore += aGradingFeature.getScore();
//		}
//		setScore(aScore);
//	}
//    @Visible(false)
//    public SakaiProject getProject() {
//    	return project;
//    }
//	public static void writeScores(ProjectStepper aProjectStepper) {
//		aProjectStepper.getProjectDatabase().getScoreFeedback().writeScores(aProjectStepper);
////		if (aProjectStepper.getProject() == null) return;
////		FileProxy feedbackFolder = aProjectStepper.getProject().getStudentAssignment().getFeedbackFolder();
////		String totalScoresFile = feedbackFolder.getAbsoluteName() + "/" + ASakaiProjectDatabase.DEFAULT_SCORE_FILE_NAME;
////		try {
////		Common.writeFile(totalScoresFile, scoresText(aProjectStepper).toString());
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
//
//	}
////	public static void writeScores(ProjectStepper aProjectStepper) {
////		if (project == null) return;
////		FileProxy feedbackFolder = project.getStudentAssignment().getFeedbackFolder();
////		String totalScoresFile = feedbackFolder.getAbsoluteName() + "/" + ASakaiProjectDatabase.DEFAULT_SCORE_FILE_NAME;
////		try {
////		Common.writeFile(totalScoresFile, scoresText().toString());
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
////
////	}
//	
//	public static StringBuffer scoresText(ProjectStepper aProjectStepper) {
//		StringBuffer stringBuffer = new StringBuffer();
//		stringBuffer.append("Total Score:" + aProjectStepper.getScore());
//    	List<GradingFeature> gradingFeatures = aProjectStepper.getProjectDatabase().getGradingFeatures();
//
//		 for (GradingFeature aGradingFeature:gradingFeatures) {
//			stringBuffer.append("\n");
//			stringBuffer.append(aGradingFeature.toString());
//		}
//		 return stringBuffer;
//		
//	}
//
//    // Josh: We want to know when a project is set, so I'm adding the project property change event here.
//	@Visible(false)
//	public boolean setProject(SakaiProject newVal) {
//		if (newVal == null) {
//            // Josh: Added event
//            propertyChangeSupport.firePropertyChange("Project", null, null);
//            return false;
//        }
//		writeScores(this);
//		runExecuted = false;
//		project = newVal;
//		if (project == null) {
//			System.out.println("No project submitted for dewan");
//			return false;
//		}
////		setName(project.getStudentAssignment().getStudentDescription());
//=======

	@Row(2)
	@ComponentWidth(150)
	public double getScore() {
		return score;
	}

	void registerWithGradingFeatures() {
		if (projectDatabase == null)
			return;
		List<GradingFeature> gradingFeatures = projectDatabase
				.getGradingFeatures();
		for (GradingFeature aGradingFeature : gradingFeatures) {
			aGradingFeature.addPropertyChangeListener(this);
		}
	}

	void resetGradingFeatures() {
		if (projectDatabase == null)
			return;

		GradingFeatureList gradingFeatures = projectDatabase
				.getGradingFeatures();
		for (GradingFeature aGradingFeature : gradingFeatures) {
			// double lastScore =
			// featureGradeRecorder.getGrade(project.getStudentAssignment().getStudentName(),
			// project.getStudentAssignment().getOnyen(),
			// aGradingFeature.getFeature());
			// double lastScore = getGrade(aGradingFeature.getFeature());

			// aGradingFeature.initScore(lastScore);
			aGradingFeature.setProject(project);
			// aGradingFeature.initScore(lastScore); // this resets

		}
		for (GradingFeature aGradingFeature : gradingFeatures) {
			// double lastScore =
			// featureGradeRecorder.getGrade(project.getStudentAssignment().getStudentName(),
			// project.getStudentAssignment().getOnyen(),
			// aGradingFeature.getFeature());
			double lastScore = getGrade(aGradingFeature.getFeatureName());
			String result = getSavedResult(aGradingFeature);
			if (result != "")
				aGradingFeature.setAutoNotes(result);

			// aGradingFeature.initScore(lastScore);
			// aGradingFeature.setProject(project);
			// initScore was not firing updates
			// aGradingFeature.initScore(lastScore);
			if (lastScore != ASakaiCSVFinalGradeManager.DEFAULT_VALUE)

				aGradingFeature.pureSetScore(lastScore);

		}
	}
	@Override
	public void setComputedScore() {
		List<GradingFeature> gradingFeatures = projectDatabase
				.getGradingFeatures();
		double aScore = 0;
		for (GradingFeature aGradingFeature : gradingFeatures) {
			aScore += aGradingFeature.getScore();
		}
		if (score < 0) {
			Tracer.error("Negative computed Score!");
		}
		setScore(aScore);
	}

	@Visible(false)
	public SakaiProject getProject() {
		return project;
	}

	public static void writeScores(ProjectStepper aProjectStepper) {
		ScoreFeedback scoreFeedback = aProjectStepper.getProjectDatabase().getScoreFeedback();
		if (scoreFeedback != null)
		    scoreFeedback
				.writeScores(aProjectStepper);
		// if (aProjectStepper.getProject() == null) return;
		// FileProxy feedbackFolder =
		// aProjectStepper.getProject().getStudentAssignment().getFeedbackFolder();
		// String totalScoresFile = feedbackFolder.getAbsoluteName() + "/" +
		// ASakaiProjectDatabase.DEFAULT_SCORE_FILE_NAME;
		// try {
		// Common.writeFile(totalScoresFile,
		// scoresText(aProjectStepper).toString());
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

	}

	// public static void writeScores(ProjectStepper aProjectStepper) {
	// if (project == null) return;
	// FileProxy feedbackFolder =
	// project.getStudentAssignment().getFeedbackFolder();
	// String totalScoresFile = feedbackFolder.getAbsoluteName() + "/" +
	// ASakaiProjectDatabase.DEFAULT_SCORE_FILE_NAME;
	// try {
	// Common.writeFile(totalScoresFile, scoresText().toString());
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }

	public static StringBuffer scoresText(ProjectStepper aProjectStepper) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Total Score:" + aProjectStepper.getScore());
		List<GradingFeature> gradingFeatures = aProjectStepper
				.getProjectDatabase().getGradingFeatures();

		for (GradingFeature aGradingFeature : gradingFeatures) {
			stringBuffer.append("\n");
			stringBuffer.append(aGradingFeature.toString());
		}
		return stringBuffer;

	}
	@Override
	public void setStoredOutput () {
		StringBuffer currentOutput = Common.toText(project.getOutputFileName());		
		 project.setCurrentOutput(currentOutput);
		 List<GradingFeature> gradingFeatures = getProjectDatabase().getGradingFeatures();
		 String allOutput = currentOutput.toString();

			for (GradingFeature aGradingFeature : gradingFeatures) {
				String output = ARunningProject.extractFeatureTranscript(aGradingFeature.getFeatureName(), allOutput);
				aGradingFeature.setOutput(output);			
			} 
			if (selectedGradingFeature != null) {
//		 internalSetOutput( project.getCurrentOutput().toString());
				internalSetOutput(selectedGradingFeature.getOutput());
			} else {
				internalSetOutput(allOutput);
			}
	}

	// Josh: We want to know when a project is set, so I'm adding the project
	// property change event here.
	@Visible(false)
	@Override
	public boolean setProject(SakaiProject newVal) {
		settingUpProject = true;
		propertyChangeSupport.firePropertyChange(OEFrame.SUPPRESS_NOTIFICATION_PROCESSING, false, true);
		setChanged(false);
		if (newVal == null) {
			// Josh: Added event
			propertyChangeSupport.firePropertyChange("Project", null, null);
			settingUpProject = false;
			// not sending anything to feature recorder
			return false;
		}
		writeScores(this);
		runExecuted = false;
		project = newVal;

		// not needed because of Josh's addition
		// if (project == null) {
		// System.out.println("No project submitted ");
		// return false;
		// }
		try {
			wrappedProject = new ProjectWrapper(project, BasicGradingEnvironment
					.get().getAssignmentName());
			studentFolder = ProjectWrapper.getStudentFolder(onyen);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// setName(project.getStudentAssignment().getStudentDescription());
//>>>>>>> working
		setName(project.getStudentAssignment().getStudentName());

		documents = project.getStudentAssignment().getDocuments();
		resetGradingFeatures();
//<<<<<<< HEAD
////		documents.remove(project.getOutputFileName());
////		documents.remove(project.getSourceFileName());
//		nextDocumentIndex = 0;
//		if (totalScoreRecorder != null)
//			setInternalScore(getGrade());
//
////			setInternalScore(gradeRecorder.getGrade(project.getStudentAssignment().getStudentName(), project.getStudentAssignment().getOnyen()));
//
//        // Josh: Added event
//        propertyChangeSupport.firePropertyChange("Project", null, project);
//
//		return true;
//	}
//	
//	public boolean preOutput() {
////		return project.canBeRun();
//=======
		// documents.remove(project.getOutputFileName());
		// documents.remove(project.getSourceFileName());
		nextDocumentIndex = 0;
//		if (totalScoreRecorder != null)
//			setInternalScore(getGrade());
		double savedScore = featureGradeRecorder.getGrade(getName(), onyen);
		
		internalSetScore(savedScore);
		if (!shouldVisit()) {
			return false;
		}

		// setInternalScore(gradeRecorder.getGrade(project.getStudentAssignment().getStudentName(),
		// project.getStudentAssignment().getOnyen()));

		// Josh: Added event
		propertyChangeSupport.firePropertyChange("Project", null, project);
		// this can be achived through property change listener also
		featureGradeRecorder.newSession(onyen, studentFolder);
		AllStudentsHistoryManagerFactory.getAllStudentsHistoryManager().newSession(onyen, studentFolder);
		// Josh's code from ProjectStepperDisplayerWrapper
		// Figure out the late penalty
		Option<DateTime> timestamp = studentFolder.getTimestamp();
		// double gradePercentage = timestamp.isDefined() ?
		// projectDatabase.getProjectRequirements().checkDueDate(timestamp.get())
		// : 0;

		if (isAutoRun() && !getGradingFeatures().isAllAutoGraded()) {
			projectDatabase.runProject(onyen, project);
		}
		
		
		if (isAutoAutoGrade() && !getGradingFeatures().isAllAutoGraded()) {
			autoGrade();
			// setComputedSummary();
			// gradePercentage = timestamp.isDefined() ?
			// projectDatabase.getProjectRequirements().checkDueDate(timestamp.get())
			// : 0;
		} else {
			multiplier = featureGradeRecorder.getEarlyLatePoints(name,
					onyen);
			if (multiplier == ASakaiCSVFeatureGradeManager.DEFAULT_VALUE)
				multiplier = 1;
			featureGradeRecorder.setEarlyLatePoints(name, onyen,
					multiplier);

			setStoredFeedback();

			setStoredOutput();
		}
		// featureGradeRecorder.setEarlyLatePoints(name, onyen,
		// gradePercentage);

		if (selectedGradingFeature != null) {
			internalSetManualNotes(getNotes(selectedGradingFeature));
//			internalSetResult(getSavedResult(selectedGradingFeature));  // could  use cached result in selected feature
			internalSetResult(selectedGradingFeature.getAutoNotes());  

		} else {
			internalSetManualNotes("");
			autoNotes = "";
		}

		internalSetOverallNotes(readComments(project));
		
		
		studentPhoto = projectDatabase.getStudentPhoto(onyen, project);
				
//				projectDatabase.getPhotoReader().getIcon(onyen);
//		photoLabelBeanModel.setIcon(studentPhoto);

		if (studentPhoto != null){
			photoLabelBeanModel.set("", studentPhoto);
		} else {
			photoLabelBeanModel.set(APhotoReader.NO_PHOTO_TITLE, studentPhoto);
		}
		settingUpProject = false;
//		setScore();
		setColors();
//		if (!shouldVisit()) {
//			return false;
//		}
		
		propertyChangeSupport.firePropertyChange(OEFrame.SUPPRESS_NOTIFICATION_PROCESSING, true, false);

//		boolean changed = setCurrentColors();
//		if (changed)
//			displayColors();

		return true;
	}
	@Override
	public boolean shouldVisit() {
		if (manualOnyen)
			return true;
		GraderSettingsModel graderSettingsModel = projectDatabase.getGraderSettings();
		if (graderSettingsModel == null) return true;
		if (graderSettingsModel.getNavigationSetter().getNavigationKind() != NavigationKind.MANUAL)
			return true;
		BasicNavigationFilter navigationFilter = projectDatabase.getNavigationFilter();
		return navigationFilter.includeProject(this, projectDatabase);
	}
	
//	void refreshColors() {
//		// no incremental updates as score and other properties change during auto grade
//		if (settingUpProject) 
//		    return;
//		boolean changed = setCurrentColors();
//		if (changed)
//			displayColors();
//		
//	}
	
	void setGradingFeatureColor(int index) {
		if (settingUpProject) return;

		nextColors.set(index, projectDatabase.getGradingFeatureColorer().color(projectDatabase.getGradingFeatures().get(index)) );

		if (currentColors.get(index) == nextColors.get(index)) return;
		setColor ( "GradingFeatures." + index, nextColors.get(index));
		currentColors.set(index, nextColors.get(index));		
	}
	void setColor(String aPath, Color aColor) {
		propertyChangeSupport.firePropertyChange(aPath, null, 
				new Attribute(AttributeNames.CONTAINER_BACKGROUND, aColor));
	}
//	public void computeNextColors() {
////		List<Color> colors = new ArrayList();
//		int i = 0;
//		for (GradingFeature aGradingFeature:projectDatabase.getGradingFeatures()) {
//			nextColors.set(i, projectDatabase.getGradingFeatureColorer().color(aGradingFeature) );
//			i++;
//		}
//		nextMultiplierColor = projectDatabase.getMultiplierColorer().color(multiplier);
//		nextScoreColor = projectDatabase.getScoreColorer().color(score);
//		nextOverallNotesColor = projectDatabase.getOverallNotesColorer().color(overallNotes);
//		
//	}
	@Override
	public void setMultiplierColor() {
		if (settingUpProject) return;
		nextMultiplierColor = projectDatabase.getMultiplierColorer().color(multiplier);
		if (currentMultiplierColor == nextMultiplierColor ) return;
		setColor("Multiplier",  nextMultiplierColor);
		currentMultiplierColor = nextMultiplierColor;

	}
	@Override
	public void setScoreColor() {
		if (settingUpProject) return;
		nextScoreColor = projectDatabase.getScoreColorer().color(score);
		if (currentScoreColor == nextScoreColor ) return;
		setColor("Score",  nextScoreColor);
		currentScoreColor = nextScoreColor;
	}
	@Override
	public void setOverallNotesColor() {
		if (settingUpProject) return;
		nextOverallNotesColor = projectDatabase.getOverallNotesColorer().color(overallNotes);
		if (currentOverallNotesColor == nextOverallNotesColor ) return;
		setColor("OverallNotes",  nextOverallNotesColor);
		currentOverallNotesColor = nextOverallNotesColor;
	}
	
	
	// setCurrentColors and refreshColors should probably be combined
//		boolean setCurrentColors() {
//			 computeNextColors();
//			boolean changed = !currentColors.equals(nextColors) ||
//					(currentMultiplierColor != nextMultiplierColor) ||
//					(currentOverallNotesColor != nextOverallNotesColor) ||
//					(currentScoreColor != nextScoreColor);
//			if (changed) {
//				for (int i=0; i < currentColors.size();i++) {
//					currentColors.set(i, nextColors.get(i));
//				}
//				currentScoreColor = nextScoreColor;
//				currentMultiplierColor = nextMultiplierColor;
//				currentOverallNotesColor = nextOverallNotesColor;
//			}
//			return changed;
//		}
	
	void setGradingFeatureColors() {
		if (settingUpProject) 
		    return;
//		for (int i = 0; i < gradingObjectAdapters.size(); i++) {
		for (int i = 0; i < projectDatabase.getGradingFeatures().size(); i++) {

//			if (currentColors.get(i) != nextColors.get(i)) {
				setGradingFeatureColor(i);
//				setGradingFeatureColor(i);
//				currentColors.set(i, nextColors.get(i));				
//			}			
		}
		
	}
	@Override
	public void setColors() {
		// no incremental updates as score and other properties change during auto grade
		if (settingUpProject) 
		    return;
//		computeNextColors();
		setGradingFeatureColors();
		setMultiplierColor();
		setOverallNotesColor();
		setScoreColor();
		
	}
	
	void displayColors() {
		for (int i = 0; i < gradingObjectAdapters.size(); i++) {
			ObjectAdapter gradingAdapter =  gradingObjectAdapters.get(i);
			gradingAdapter.setTempAttributeValue(AttributeNames.CONTAINER_BACKGROUND, currentColors.get(i));
		}
		
		
		stepperViewAdapter.get("score")
		.setTempAttributeValue(AttributeNames.COMPONENT_BACKGROUND, currentScoreColor);

		scoreAdapter.setTempAttributeValue(AttributeNames.COMPONENT_BACKGROUND, currentScoreColor);
		multiplierAdapter.setTempAttributeValue(AttributeNames.COMPONENT_BACKGROUND, currentMultiplierColor);
		overallNotesAdapter.setTempAttributeValue(AttributeNames.COMPONENT_BACKGROUND, currentOverallNotesColor);


		oeFrame.refresh();
//		stepperViewAdapter.refreshAttributes();
	}

	

	public boolean preOutput() {
		// return project.canBeRun();
//>>>>>>> working
		return runAttempted() && project.canBeRun();

	}

	public boolean preRun() {
		return project.canBeRun() && !autoRun
//<<<<<<< HEAD
////				&& !runExecuted
//				;
//	}
//	@Row(3)
//	@ComponentWidth(100)
//
//	public void run() {
//		runExecuted = true;
//		projectDatabase.runProject(onyen, project);
//		project.setHasBeenRun(true);
//		for (GradingFeature gradingFeature:projectDatabase.getGradingFeatures()) {
//=======
		// && !runExecuted
		;
	}
	
	@Row(3)
	@ComponentWidth(100)
	@Override
	public void run() {
		runExecuted = true;
		projectDatabase.runProject(getOnyen(), project);
		project.setHasBeenRun(true);
		for (GradingFeature gradingFeature : projectDatabase
				.getGradingFeatures()) {
//>>>>>>> working
			if (gradingFeature.isAutoGradable()) {
				gradingFeature.firePropertyChange("this", null, gradingFeature);
			}
		}

	}
//<<<<<<< HEAD
//	@Row(4)
//	@ComponentWidth(100)
//	
//=======

	void unSelectOtherGradingFeatures(GradingFeature currentGradingFeature) {
		for (GradingFeature gradingFeature : projectDatabase
				.getGradingFeatures()) {
			if (currentGradingFeature == gradingFeature)
				continue;
			gradingFeature.setSelected(false);
		}
	}

	@Row(4)
	@ComponentWidth(100)
//>>>>>>> working
	public void output() {
		project.setHasBeenRun(true);

		projectDatabase.displayOutput();

	}
//<<<<<<< HEAD
//	
//	public boolean preSources() {
////		return project.canBeRun();
//		return project.runChecked() || project.hasBeenRun();
////		return true;
//
//	}
//	@Row(5)
//	@ComponentWidth(100)
//	public void sources() {
//		project.setHasBeenRun(true);
//
//		project.displaySource(projectDatabase);
//		
//	}
//=======

	public boolean preSources() {
		// return project.canBeRun();
		return project.runChecked() || project.hasBeenRun();
		// return true;

	}

	@Row(5)
	@ComponentWidth(100)
	public void openSource() {
		project.setHasBeenRun(true);

		project.displaySource(projectDatabase);

	}

//>>>>>>> working
	@Row(6)
	@ComponentWidth(100)
	public void nextDocument() {
		if (nextDocumentIndex >= documents.size()) {
			System.out.println("No documents to display");
			return;
		}
		String nextDocument = documents.get(nextDocumentIndex);
		nextDocumentIndex++;
		if (nextDocumentIndex == documents.size())
			nextDocumentIndex = 0;
		DocumentDisplayerRegistry.display(nextDocument);
//<<<<<<< HEAD
//			
//		
//		
//	}
//	@Row(7)
//	@ComponentWidth(100)
//	public void comments() {
//		DocumentDisplayerRegistry.display(project.getStudentAssignment().getCommentsFileName());
//	}
//	
//	
//	void setInternalScore(double newVal) {
//		score = newVal;
//		propertyChangeSupport.firePropertyChange("Score", null, newVal);
//	}
//	void setGrade(double newVal) {
////		gradeRecorder.setGrade(project.getStudentAssignment().getStudentName(), project.getStudentAssignment().getOnyen(), newVal);
////		featureGradeRecorder.setGrade(project.getStudentAssignment().getStudentName(), project.getStudentAssignment().getOnyen(), newVal);
//		totalScoreRecorder.setGrade(project.getStudentAssignment().getStudentName(), project.getStudentAssignment().getOnyen(), newVal);
//	
//	}
//	
//	double getGrade() {
//		return totalScoreRecorder.getGrade(project.getStudentAssignment().getStudentName(), project.getStudentAssignment().getOnyen());
//
//	}
//	void setGrade(String aFeature, double newVal) {
//		featureGradeRecorder.setGrade(project.getStudentAssignment().getStudentName(), project.getStudentAssignment().getOnyen(), aFeature, newVal);
//
//	}
//	double getGrade(String aFeature) {
//		return featureGradeRecorder.getGrade(project.getStudentAssignment().getStudentName(), project.getStudentAssignment().getOnyen(), aFeature);
//
//	}
//	@Override
//	public void setScore(double newVal) {
//		setInternalScore(newVal);
//		if (totalScoreRecorder != null)
//
////		if (gradeRecorder != null)
//			setGrade(newVal);
////			gradeRecorder.setGrade(project.getStudentAssignment().getStudentName(), project.getStudentAssignment().getOnyen(), newVal);
////		score = newVal;
////		propertyChangeSupport.firePropertyChange("Score", null, newVal);
//	}
//	
//	@Override
//	public  boolean preGetGradingFeatures() {
//		return projectDatabase != null && projectDatabase.getGradingFeatures().size() > 0;
//	}
//	@Override
//	public boolean preAutoGrade() {
//		return project.runChecked() && project.canBeRun() && preGetGradingFeatures();
//	}
////	
//=======

	}

	@Row(7)
	@ComponentWidth(100)
	public void comments() {
		DocumentDisplayerRegistry.display(project.getStudentAssignment()
				.getCommentsFileName());
	}
@Override
	public void internalSetScore(double newVal) {
		score = newVal;
		if (!settingUpProject) {
			setScoreColor();
			propertyChangeSupport.firePropertyChange("Score", null, newVal);
		}
	}

	void setGrade(double newVal) {
		// This will be a spurious message to conglomerate as t serves as total
		// and feature recorder
		if (! (totalScoreRecorder instanceof ConglomerateRecorder))
		totalScoreRecorder.setGrade(project.getStudentAssignment()
				.getStudentName(), project.getStudentAssignment().getOnyen(),
				newVal);

	}

	double getGrade() {
		return totalScoreRecorder.getGrade(project.getStudentAssignment()
				.getStudentName(), project.getStudentAssignment().getOnyen());

	}

	void setGrade(String aFeature, double newVal) {
		if (!settingUpProject)
		featureGradeRecorder.setGrade(project.getStudentAssignment()
				.getStudentName(), project.getStudentAssignment().getOnyen(),
				aFeature, newVal);

	}

	double getGrade(String aFeature) {
		return featureGradeRecorder.getGrade(project.getStudentAssignment()
				.getStudentName(), project.getStudentAssignment().getOnyen(),
				aFeature);

	}

	@Override
	public void setScore(double newVal) {
		if (score == newVal) return;
		double oldVal = score;
		internalSetScore(newVal);
		if (totalScoreRecorder != null)

			// if (gradeRecorder != null)
			setGrade(newVal);
		featureGradeRecorder.setGrade(name, getOnyen(), newVal);
		NotesGenerator notesGenerator = projectDatabase.getNotesGenerator();
		setOverallNotes(notesGenerator.appendNotes(
				getOverallNotes(), 
				notesGenerator.totalScoreOverrideNotes(this, oldVal, newVal)));
		
//		
//		String aNotes = projectDatabase.getNotesGenerator().totalScoreOverrideNotes(this, oldVal, newVal);
//		String oldOverallNotes = getOverallNotes();
//		String newNotes = oldOverallNotes + " " + aNotes;
//		setOverallNotes(newNotes);
		
		
		// gradeRecorder.setGrade(project.getStudentAssignment().getStudentName(),
		// project.getStudentAssignment().getOnyen(), newVal);
		// score = newVal;
		// propertyChangeSupport.firePropertyChange("Score", null, newVal);
	}

	@Override
	public boolean preGetGradingFeatures() {
		return projectDatabase != null
				&& projectDatabase.getGradingFeatures().size() > 0;
	}

	@Override
	public boolean preAutoGrade() {
		// return project.runChecked() && project.canBeRun() &&
		// preGetGradingFeatures();
		return /* project.runChecked() && project.canBeRun() && */preGetGradingFeatures();

	}
	boolean settingUpProject;
	//
//>>>>>>> working
	@Row(8)
	@ComponentWidth(100)
	@Override
	public void autoGrade() {
		project.setHasBeenRun(true);
//<<<<<<< HEAD
//		for (GradingFeature gradingFeature:projectDatabase.getGradingFeatures()) {
//			if (gradingFeature.isAutoGradable()) {
//				gradingFeature.autoGrade();
//			}
//		}
//		
//	}
//=======
		setChanged (true);
		project.clearOutput();
		for (GradingFeature gradingFeature : projectDatabase
				.getGradingFeatures()) {
			if (gradingFeature.isAutoGradable()) {
				gradingFeature.pureSetGraded(true);
			}
		}
		featureResults = projectDatabase.getProjectRequirements()
				.checkFeatures(wrappedProject);
		restrictionResults = projectDatabase.getProjectRequirements()
				.checkRestrictions(wrappedProject);
		GradingFeatureList features = projectDatabase.getGradingFeatures();
//		setComputedScore(); // will trigger change occurred
		for (int i = 0; i < features.size(); i++) {
			// Figure out the score for the feature/restriction
			double score = (i < featureResults.size()) ? featureResults.get(i)
					.getScore() : restrictionResults.get(
					i - featureResults.size()).getScore();

			// Save the comments. We save them in the ConglomerateRecorder so
			// that, if it is being used as the
			// manual feedback, they will be pulled in.

			// correcting josh's code to separate feature comments and results
			// featureGradeRecorder.setFeatureComments(featureResults.get(i).getNotes());
			// featureGradeRecorder.setFeatureResults(featureResults.get(i).getResults());
			featureGradeRecorder
					.setFeatureComments((i < featureResults.size()) ? featureResults
							.get(i).getNotes() : restrictionResults.get(
							i - featureResults.size()).getNotes());
			featureGradeRecorder
					.setFeatureResults((i < featureResults.size()) ? featureResults
							.get(i).getResults() : restrictionResults.get(
							i - featureResults.size()).getResults());
			
			features.get(i).setManualNotes(
					(i < featureResults.size()) ? featureResults.get(i)
							.getNotes() : restrictionResults.get(
							i - featureResults.size()).getNotes());
			
			String result = (i < featureResults.size()) ? featureResults.get(i)
					.getTarget().getSummary() : restrictionResults
					.get(i - featureResults.size()).getTarget()
					.getSummary();
			// in memory save
			features.get(i).setAutoNotes(result);
			// save to the excel file so we can read it later
			featureGradeRecorder.setResultFormat(getName(), getOnyen(), features.get(i).getFeatureName(), 
					result);			
			features.get(i).setScore(score);

			// Save the score
			featureGradeRecorder.setGrade(getName(), getOnyen(), features.get(i)
					.getFeatureName(), score);
                        
		}
		setComputedScore(); // will trigger change occurred
		setComputedFeedback();
		setStoredOutput();
		// Josh's code from ProjectStepperDisplayerWrapper
				// Figure out the late penalty
		Option<DateTime> timestamp = studentFolder.getTimestamp();
				// double gradePercentage = timestamp.isDefined() ?
				// projectDatabase.getProjectRequirements().checkDueDate(timestamp.get())
				// : 0;
		
		// this should not be called
		double aMultiplier = timestamp.isDefined() ?
				 projectDatabase.getProjectRequirements().checkDueDate(null, timestamp.get())
				 : 0;
		internalSetMultiplier(aMultiplier);
//		featureGradeRecorder.setEarlyLatePoints(name, onyen, aMultiplier);
		// setSummary();
		

	}

//>>>>>>> working
	@Override
	@Row(9)
	public GradingFeatureList getGradingFeatures() {
		if (projectDatabase != null)
//<<<<<<< HEAD
//		return projectDatabase.getGradingFeatures();
//		else return null;
//		
//	}
//	public boolean preProceed() {
//		return (hasMoreSteps || manualOnyen) && isAllGraded();
//	}
//	public boolean preSkip() {
//		return !preProceed();
//	}
//=======
			return projectDatabase.getGradingFeatures();
		else
			return null;

	}
	boolean proceedWhenDone = true;
	@Visible(false)
	@Override
	public boolean isProceedWhenDone() {
		return proceedWhenDone;
		
	}
	@Override
	public void toggleProceedWhenDone() {
		proceedWhenDone = !proceedWhenDone;
	}

	public boolean preProceed() {
		// if manuelOnyen then we will go to next step
		// cannot proceed if it is not all graded
		// why check for manualOnyen?
		// removing first clause
		return //(hasMoreSteps || manualOnyen) && 
				isAllGraded() || !proceedWhenDone;
	}

	public boolean preSkip() {
		return !preProceed();
	}

//>>>>>>> working
	@Row(10)
	@Override
	public boolean isAllGraded() {
		return getGradingFeatures().isAllGraded();
	}
//<<<<<<< HEAD
//	
//	
//	
////	@Row(11)
////	@ComponentWidth(100)
//=======

	// @Row(11)
	// @ComponentWidth(100)
//>>>>>>> working
	@Visible(false)
	public synchronized void proceed() {
		super.proceed();
		if (manualOnyen)
			writeScores(this);
		manualOnyen = false;
		try {
//<<<<<<< HEAD
////			Common.appendText(logFile, onyen + " Skipped " + Common.currentTimeAsDate() + "\n\r");
//			Common.appendText(gradedFile, onyen + "\n");
//=======
			// Common.appendText(logFile, onyen + " Skipped " +
			// Common.currentTimeAsDate() + "\n\r");
			Common.appendText(gradedFile, getOnyen() + "\n");
//>>>>>>> working

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//<<<<<<< HEAD
//	
//		
//	}
//
////	@Row(12)
////	@ComponentWidth(100)
////	public synchronized void skip() {
////		proceed();
////		try {
////			Common.appendText(logFile, onyen + " Skipped " + Common.currentTimeAsDate() + "\n");
////			Common.appendText(skippedFile, onyen + "\n");
//=======

	}

	// @Row(12)
	// @ComponentWidth(100)
	// public synchronized void skip() {
	// proceed();
	// try {
	// Common.appendText(logFile, onyen + " Skipped " +
	// Common.currentTimeAsDate() + "\n");
	// Common.appendText(skippedFile, onyen + "\n");
	// List<String> list = FileProxyUtils.toList(new File(logFile));
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// // should put person in skipped list
	//
	// }
	
	boolean noNextFilteredRecords;

	@Override
	public boolean preNext() {
		return !noNextFilteredRecords && preProceed() && currentOnyenIndex < onyens.size() - 1;
		// this does not make sense, next is a stronger condition than next

		// return !preDone() && nextOnyenIndex < onyens.size() - 1 ;
	}

	@Row(12)
	@ComponentWidth(100)
	@Override
	public synchronized void next() {

//		try {
//			// Common.appendText(logFile, getOnyen() + " Skipped " +
//			// Common.currentTimeAsDate() + "\n");
//			Common.appendText(skippedFile, getOnyen() + "\n");
//>>>>>>> working
//			List<String> list = FileProxyUtils.toList(new File(logFile));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//<<<<<<< HEAD
////		// should put person in skipped list
////		
////	}
//	@Override
//	public boolean preNext() {
//		return !preDone() && nextOnyenIndex < onyens.size() - 1 ;
//	}
//	@Row(12)
//	@ComponentWidth(100)
//	@Override
//	public synchronized void next() {
//		
//		try {
////			Common.appendText(logFile, onyen + " Skipped " + Common.currentTimeAsDate() + "\n");
//			Common.appendText(skippedFile, onyen + "\n");
//			List<String> list = FileProxyUtils.toList(new File(logFile));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		move(true);
//		// should put person in skipped list
//		
//	}
//	@Override
//	public boolean prePrevious() {
//		return nextOnyenIndex > 0;
//	}
//	@Row(13)
//	@ComponentWidth(100)
//	@Override
//	public synchronized void previous() {
//		
////		try {
////			Common.appendText(logFile, onyen + " Skipped " + Common.currentTimeAsDate() + "\n");
////			Common.appendText(skippedFile, onyen + "\n");
////			List<String> list = FileProxyUtils.toList(new File(logFile));
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		move(false);
//		// should put person in skipped list
//		
//	}
//	
//	String filter = "";
//	@Row(14)
//	@ComponentWidth(150)
//	@Override
//	public String getNavigationFilter() {
//		return projectDatabase.getNavigationFilter();
//	}
//	@Override
//	public void setNavigationFilter(String newVal) {
//		 projectDatabase.setNavigationFilter(newVal);
//		 configureNavigationList();
//		 runProjectsInteractively();
//	}
//=======
		 move(true);
		// should put person in skipped list

	}
	boolean noPreviousFilteredRecords;

	@Override
	public boolean prePrevious() {
		return !noPreviousFilteredRecords && preProceed() && currentOnyenIndex > 0;
	}

	@Row(13)
	@ComponentWidth(100)
	@Override
	public synchronized void previous() {

		// try {
		// Common.appendText(logFile, onyen + " Skipped " +
		// Common.currentTimeAsDate() + "\n");
		// Common.appendText(skippedFile, onyen + "\n");
		// List<String> list = FileProxyUtils.toList(new File(logFile));
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		 move(false);
		// should put person in skipped list

	}
	
	void validate (GradingFeature aGradingFeature, boolean isValidate) {
		NotesGenerator notesGenerator = projectDatabase.getNotesGenerator();
		String newNotes = "";
		if (isValidate) {
			
		  newNotes =
				notesGenerator.appendNotes(
				aGradingFeature.getManualNotes(), 
				notesGenerator.validationNotes(this, aGradingFeature));
		}
		aGradingFeature.setManualNotes(newNotes);
		if (selectedGradingFeature == aGradingFeature) {
			setManualNotes(newNotes);
		}
//		setManualNotes(notesGenerator.appendNotes(
//				getManualNotes(), 
//				notesGenerator.validationNotes(this, aGradingFeature)));
		
//		
//		String aNotes = projectDatabase.getNotesGenerator().validationNotes(this, aGradingFeature);
//		setManualNotes(aNotes);
		
//		setNotes(aGradingFeature, aNotes);;
	}
	
	void validate (GradingFeature aGradingFeature) {
		NotesGenerator notesGenerator = projectDatabase.getNotesGenerator();
		String newNotes = notesGenerator.appendNotes(
				aGradingFeature.getManualNotes(), 
				notesGenerator.validationNotes(this, aGradingFeature));
		aGradingFeature.setManualNotes(newNotes);
		if (selectedGradingFeature == aGradingFeature) {
			setManualNotes(newNotes);
		}
//		setManualNotes(notesGenerator.appendNotes(
//				getManualNotes(), 
//				notesGenerator.validationNotes(this, aGradingFeature)));
		
//		
//		String aNotes = projectDatabase.getNotesGenerator().validationNotes(this, aGradingFeature);
//		setManualNotes(aNotes);
		
//		setNotes(aGradingFeature, aNotes);;
	}
	@Row(14)
	@Override
	@ComponentWidth(100)
	public void validate() {
		if (selectedGradingFeature != null) {
			validate(selectedGradingFeature);
		}
		
	}

//	String filter = "";

//	@Row(14)
//	@ComponentWidth(150)
//	@Override
//	public String getNavigationFilter() {
//		return projectDatabase.getNavigationFilter();
//	}
//
//	@Override
//	public void setNavigationFilter(String newVal) {
//		projectDatabase.setNavigationFilter(newVal);
//		configureNavigationList();
//		runProjectsInteractively();
//	}

	@Row(15)
	public double getMultiplier() {
		return multiplier;
	}
	@Override
	public void internalSetMultiplier(double newValue) {
		double oldValue = multiplier;
		multiplier = newValue;
		featureGradeRecorder.setEarlyLatePoints(getName(), getOnyen(),
				multiplier);
		setMultiplierColor();
		propertyChangeSupport.firePropertyChange("multiplier", oldValue, newValue);
	}
	
	public void setMultiplier(double newValue) {
		double oldVal = multiplier;
		if (oldVal == newValue) return;
		internalSetMultiplier(newValue);
		NotesGenerator notesGenerator = projectDatabase.getNotesGenerator();
		setOverallNotes(notesGenerator.appendNotes(
				getOverallNotes(), 
				notesGenerator.multiplierOverrideNotes(this, oldVal, newValue)));
//		String aNotes = projectDatabase.getNotesGenerator().multiplierOverrideNotes(this, oldVal, newValue);
//		String oldOverallNotes = getOverallNotes();
//		String newNotes = oldOverallNotes + " " + aNotes;
//		setOverallNotes(newNotes);
		
	}

	@Row(16)
	@ComponentWidth(400)
//	@Label("Auto Notes")
	@Override
	public String getAutoNotes() {
		return autoNotes;
	}
	@Override
	public void internalSetAutoNotes(String newVal) {
		autoNotes = newVal;
	}

	@Row(17)
	@ComponentWidth(400)
	@PreferredWidgetClass(JTextArea.class)
//	@Label("Manual Notes:")
	@Override
	public String getManualNotes() {
		return manualNotes;
	}

	public boolean preSetManualNotes() {
		return selectedGradingFeature != null;
	}
    @Override
	public void internalSetManualNotes(String newVal) {
		String oldVal = manualNotes;

		manualNotes = newVal;

		propertyChangeSupport.firePropertyChange("manualNotes", oldVal, newVal);
	}
	
	void internalSetOutput(String newVal) {
		String oldVal = output;

		output = newVal;

		propertyChangeSupport.firePropertyChange("output", oldVal, newVal);
	}
    @Override
	public void internalSetResult(String newVal) {
		String oldVal = autoNotes;

		autoNotes = newVal;

		propertyChangeSupport.firePropertyChange("AutoNotes", oldVal, newVal);
	}
    @Override
	public void setManualNotes(String newVal) {
		if (preSetManualNotes()) {
			setNotes(selectedGradingFeature, newVal);
			featureGradeRecorder.setFeatureComments(newVal);
		}

		setComputedFeedback();
		internalSetManualNotes(newVal);
		setGradingFeatureColors();
//		changed = true;
		// notes = newVal;

		// propertyChangeSupport.firePropertyChange("notes", oldVal, newVal);

	}

	@Row(18)
//	@Label("Overall Notes")
	@ComponentWidth(800)
	@Override
	public String getOverallNotes() {
		return overallNotes;

	}
@Override
	public void internalSetOverallNotes(String newVal) {
		String oldVal = overallNotes;

		overallNotes = newVal;

		propertyChangeSupport.firePropertyChange("comments", oldVal, newVal);
		
	}
    @Override
	public void setOverallNotes(String newVal) {
		internalSetOverallNotes(newVal);
		// String oldVal = newVal;
		// comments = newVal;
		// propertyChangeSupport.firePropertyChange("comments", oldVal, newVal);
		featureGradeRecorder.saveOverallNotes(overallNotes);
		writeComments(project, newVal);
		setComputedFeedback();
		

	}
    @Override
	public void setComputedFeedback() {
		String oldVal = feedback;
		feedback = featureGradeRecorder.computeSummary();
		propertyChangeSupport.firePropertyChange("feedback", oldVal, feedback);
	}
    @Override
	public void setStoredFeedback() {
		String oldVal = feedback;
		feedback = featureGradeRecorder.getStoredSummary();
		propertyChangeSupport.firePropertyChange("feedback", oldVal, feedback);
	}
	
	@Row(19)
//	@ComponentHeight(100)
//	@ComponentWidth(100)
	@Override
	public LabelBeanModel getPhoto() {
		return photoLabelBeanModel;
	}
	
	
	@Row(20)
	@Override
	@ComponentWidth(600)
	@ComponentHeight(100)
	@PreferredWidgetClass(JTextArea.class)
	public String getFeedback() {
		return feedback;

	}
	
	@Visible(true)
	@Row(21)
//	@Override
	@ComponentWidth(600)
	@ComponentHeight(100)
	@PreferredWidgetClass(JTextArea.class)
	public String getTranscript() {
		return output;

	}
	public boolean preGetNavigationSetter() {
		return projectDatabase.getGraderSettings() != null;
	}
	@Row(22)
	@Override
	public NavigationSetter getNavigationSetter() {
		return projectDatabase.getGraderSettings().getNavigationSetter();
	}
	

//>>>>>>> working
	@Override
	public boolean hasMoreSteps() {
		return hasMoreSteps;
	}
//<<<<<<< HEAD
//	@Override
//	public void setHasMoreSteps(boolean newVal) {
//		if (hasMoreSteps == newVal) return;
//		 hasMoreSteps = newVal;
//		 if (hasMoreSteps == false)
//				writeScores(this);
//
//			notifyPreconditionChanged();
//
//	}
//	
////	public void recordWindows() {
////		oldList = new ArrayList( uiFrameList.getList());
////		oldWindows =	Window.getWindows();
////	}
////	public void clearWindows() {
////		if (oldWindows != null && oldList != null) {// somebody went before me, get rid of their windows
//////			System.out.println("dispoing old windows");
////			List<OEFrame> newList = new ArrayList( uiFrameList.getList());
////			
////
////
////			for (OEFrame frame:newList) {
////				if (oldList.contains(frame))
////					continue;
////				frame.dispose(); // will this work
////			}
////			Window[] newWindows =	Window.getWindows();
////			
////			
////			for (Window frame:newWindows) {
////				if (Common.containsReference(oldWindows, frame)) {
////					continue;
////				}
////				frame.dispose();
////			}
////		}
////	}
//	
//	@Override
//	public synchronized void waitForClearance() {
//		
//		
//
//		
//		super.waitForClearance();
////		if (oldWindows != null && oldList != null) {// somebody went before me, get rid of their windows
//////			System.out.println("dispoing old windows");
////			List<OEFrame> newList = new ArrayList( uiFrameList.getList());
////			
////
////
////			for (OEFrame frame:newList) {
////				if (oldList.contains(frame))
////					continue;
////				frame.dispose(); // will this work
////			}
////			Window[] newWindows =	Window.getWindows();
////			
////			
////			for (Window frame:newWindows) {
////				if (Common.containsReference(oldWindows, frame)) {
////					continue;
////				}
////				frame.dispose();
////			}
////		}
////		clearWindows();
////		recordWindows();
//
////		oldList = new ArrayList( uiFrameList.getList());
////		oldWindows =	Window.getWindows();
//
//		
//		}
//		
//
//
//		
//=======

	@Override
	public void setHasMoreSteps(boolean newVal) {
		if (hasMoreSteps == newVal)
			return;
		hasMoreSteps = newVal;
		if (hasMoreSteps == false)
			writeScores(this);

		notifyPreconditionChanged();

	}

	

	@Override
	public synchronized boolean waitForClearance() {

		return super.waitForClearance();
		

	}

//>>>>>>> working
	@Override
	public void addPropertyChangeListener(PropertyChangeListener aListener) {
		propertyChangeSupport.addPropertyChangeListener(aListener);
	}
//<<<<<<< HEAD
//	
//	
//
//	@Override
//	public void propertyChange(PropertyChangeEvent evt) {
//		if (evt.getSource() instanceof GradingFeature && evt.getPropertyName().equalsIgnoreCase("score")) {
//			GradingFeature aGradingFeature = (GradingFeature) evt.getSource();
////			setInternalScore(aGradingFeature.getScore());
//			setScore();
//			setGrade(aGradingFeature.getFeature(), aGradingFeature.getScore());
//		}
//		propertyChangeSupport.firePropertyChange("this", null, this); // an event from grading features, perhaps our precondition chnaged such as auoGraded
//		
//	}
//
//
////	List<OEFrame> newList = new ArrayList( uiFrameList.getList());
//	
//
////
////	for (OEFrame frame:newList) {
////		if (oldList.contains(frame))
////			continue;
////		frame.dispose(); // will this work
////	}
////	Window[] newWindows =	Window.getWindows();
////	
////	
////	for (Window frame:newWindows) {
////		if (Common.containsReference(oldWindows, frame)) {
////			continue;
////		}
////		frame.dispose();
////	}
//=======

	CheckResult checkableToResult(Checkable aCheckable) {
		try {
			for (CheckResult checkResult : featureResults) {
				if (checkResult.getTarget() == aCheckable)
					return checkResult;
			}

			for (CheckResult checkResult : restrictionResults) {
				if (checkResult.getTarget() == aCheckable)
					return checkResult;
			}
		} catch (Exception e) {
			return null;
		}
		return null;

	}

	CheckResult gradingFeatureToCheckResult(GradingFeature aGradingFeature) {
		Checkable checkable = projectDatabase.getRequirement(aGradingFeature);
		if (checkable != null) {
			return checkableToResult(checkable);
		}
		return null;
	}
	
	String getSavedResult(GradingFeature aGradingFeature) {
		return featureGradeRecorder.getResult(getName(), getOnyen(), aGradingFeature.getFeatureName());
	}

	String getInMemoryResult(GradingFeature aGradingFeature) {
		return aGradingFeature.getAutoNotes();
//		CheckResult checkResult = gradingFeatureToCheckResult(aGradingFeature);
//		if (checkResult != null) {
//			return checkResult.getMessage();
//		}
//
//		return "";
	}

	void setNotes(GradingFeature aGradingFeature, String aNotes) {
		featureGradeRecorder.setFeatureComments(aNotes);
		featureGradeRecorder.comment(aGradingFeature);
		aGradingFeature.setManualNotes(aNotes);
		// CheckResult checkResult =
		// gradingFeatureToCheckResult(aGradingFeature);
		// if (checkResult != null) {
		// checkResult.setNotes(aNotes);
		// }

	}

	String getNotes(GradingFeature aGradingFeature) {
		String retVal = aGradingFeature.getManualNotes();
		// CheckResult checkResult =
		// gradingFeatureToCheckResult(aGradingFeature);
		// if (checkResult != null) {
		// return checkResult.getNotes(); }
		if (retVal == null)
			retVal = "";

		return retVal;
	}
	
	void refreshSelectedFeature() {
		if (selectedGradingFeature != null)
		manualNotes = getNotes(selectedGradingFeature);
	}
	@Override
	@Visible(false)
	public GradingFeature getSelectedGradingFeature() {
		return selectedGradingFeature;
	}
	public void setSelectedFeature (GradingFeature gradingFeature) {
		
			manualNotes = getNotes(gradingFeature);
			autoNotes = getInMemoryResult(gradingFeature);
			// log = gradingFeature.getFeature();
			selectedGradingFeature = gradingFeature;
			output = selectedGradingFeature.getOutput();
			unSelectOtherGradingFeatures(gradingFeature);
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() instanceof GradingFeature
				&& evt.getPropertyName().equalsIgnoreCase("score")) {
			GradingFeature aGradingFeature = (GradingFeature) evt.getSource();
			// setInternalScore(aGradingFeature.getScore());
			if (!settingUpProject)
			setComputedScore();
			setGrade(aGradingFeature.getFeatureName(), aGradingFeature.getScore());
//			setSelectedFeature(aGradingFeature);// auto select
			if (!settingUpProject) {
			setChanged(true);
			setComputedFeedback();
			setGradingFeatureColors();
			aGradingFeature.setSelected(true); 
			
			}

			
		} else if (evt.getSource() instanceof GradingFeature
				&& evt.getPropertyName().equalsIgnoreCase("selected") && !settingUpProject) {
			GradingFeature gradingFeature = (GradingFeature) evt.getSource();
			if ((Boolean) evt.getNewValue()) {
				setSelectedFeature(gradingFeature);
//				manualNotes = getNotes(gradingFeature);
//				autoNotes = getInMemoryResult(gradingFeature);
//				// log = gradingFeature.getFeature();
//				selectedGradingFeature = gradingFeature;
//				output = selectedGradingFeature.getOutput();
//				unSelectOtherGradingFeatures(gradingFeature);
			} else {
				// this may be a bounced feature
				// selectedGradingFeature = null;
				// unSelectOtherGradingFeatures(null);
				// setNotes("");
			}

		} else if (evt.getSource() instanceof GradingFeature
				&& evt.getPropertyName().equalsIgnoreCase("validate") && !settingUpProject) {
			GradingFeature gradingFeature = (GradingFeature) evt.getSource();
			if (evt.getNewValue() != evt.getOldValue()) {
//			validate(gradingFeature);
			validate(gradingFeature, (Boolean)evt.getNewValue() );

			setGradingFeatureColors();
			}
			gradingFeature.setSelected(true); 
		
		} else if (evt.getSource() == this) {
			// will get even name and onyen changes - let us focus on the changes that really need to be saved in the setters
			if (!settingUpProject)

			setChanged(true);
			return; // do not want to execute the statement below as it  will cause infinite recursion
			
		} else if (evt.getSource() == getNavigationSetter().getNavigationFilterSetter()) {
			noNextFilteredRecords = false;
			noPreviousFilteredRecords = false;
		}
		if (!settingUpProject) { 
		refreshSelectedFeature(); // we know a user action occurred
	
		propertyChangeSupport.firePropertyChange("this", null, this); // an
																		// event
																		// from
																		// grading
																		// features,
																		// perhaps
																		// our
																		// precondition
																		// chnaged
																		// such
																		// as
																		// auoGraded
		}

	}

	// List<OEFrame> newList = new ArrayList( uiFrameList.getList());

	//
	// for (OEFrame frame:newList) {
	// if (oldList.contains(frame))
	// continue;
	// frame.dispose(); // will this work
	// }
	// Window[] newWindows = Window.getWindows();
	//
	//
	// for (Window frame:newWindows) {
	// if (Common.containsReference(oldWindows, frame)) {
	// continue;
	// }
	// frame.dispose();
	// }
//>>>>>>> working
	@Override
	@Visible(false)
	public SakaiProjectDatabase getProjectDatabase() {
		// TODO Auto-generated method stub
		return projectDatabase;
	}
//<<<<<<< HEAD
//	List<String> onyens;
//	int nextOnyenIndex = 0;
//	String nextOnyen;
//	@Override
//	public void configureNavigationList() {
//		onyens = projectDatabase.getOnyenNavigationList();
//		nextOnyenIndex = 0;
//		hasMoreSteps = true;
//	}
//	@Override
//	public boolean preRunProjectsInteractively() {
//		return onyens !=null && nextOnyenIndex < onyens.size();
//	}
//	@Override
//	public void runProjectsInteractively() {
////		onyens = projectDatabase.getOnyenNavigationList();
//		
////		nextOnyenIndex = 0;
////		if (nextOnyenIndex >= onyens.size())
////			return;
//		if (!preRunProjectsInteractively()) {
//			Tracer.error("Projects not configured");
//			hasMoreSteps  = false;
//			return;
//		}
//		String anOnyen = onyens.get(nextOnyenIndex);
//		SakaiProject aProject = projectDatabase.getProject(anOnyen);
//		projectDatabase.initIO();
//
//		projectDatabase.recordWindows();		
//		setProject(anOnyen);
//		if (isAutoRun())
//			projectDatabase.runProject(anOnyen, aProject);		
//		
//		setProject(anOnyen);
//		
//		
//
//		
//		
//	}
//	@Override
//	public boolean preDone() {
//		return preProceed();
//	}
//	@Override
//	public synchronized void move(boolean forward) {
//		projectDatabase.resetIO();
//		projectDatabase.clearWindows();
//		if (forward) {
//		nextOnyenIndex++;
//	
//		if (nextOnyenIndex >= onyens.size()) {
//			hasMoreSteps  = false;
//			return;
//		}
//		} else {
//			nextOnyenIndex--;
//			if (nextOnyenIndex < 0) {
//				hasMoreSteps  = false;
//				return;
//			}
//			
//		}
//		String anOnyen = onyens.get(nextOnyenIndex);
//		SakaiProject aProject = projectDatabase.getProject(anOnyen);
//		projectDatabase.initIO();
//		projectDatabase.recordWindows();		
//		setProject(anOnyen);
//		if (isAutoRun())
//			projectDatabase.runProject(anOnyen, aProject);		
//		
////		setProject(anOnyen);
//	}
//	
//=======

	List<String> onyens;
	int currentOnyenIndex = 0;
	int filteredOnyenIndex = 0;
	String nextOnyen;

	@Override
	public void configureNavigationList() {
		onyens = projectDatabase.getOnyenNavigationList();
		currentOnyenIndex = 0;
		hasMoreSteps = true;
	}

	@Override
	public boolean preRunProjectsInteractively() {
		return onyens != null && currentOnyenIndex < onyens.size();
	}
	
	void setObjectAdapters() {
		if (oeFrame == null) return;
		for (GradingFeature gradingFeature:projectDatabase.getGradingFeatures()) {
			ObjectAdapter featureAdapter = oeFrame.getObjectAdapter(gradingFeature);	
//			featureAdapter.setTempAttributeValue(AttributeNames.CONTAINER_BACKGROUND, Color.PINK);
//			featureAdapter.refresh();
			gradingObjectAdapters.add(featureAdapter);
		}
		gradingFeaturesAdapter = oeFrame.getObjectAdapter(projectDatabase.getGradingFeatures());
		if (gradingFeaturesAdapter == null) return;
		stepperViewAdapter = (ClassAdapter) gradingFeaturesAdapter.getParentAdapter(); // this might be a delegator
		scoreAdapter = stepperViewAdapter.get("score");
		multiplierAdapter = stepperViewAdapter.get("multiplier");
		overallNotesAdapter = stepperViewAdapter.get("overallNotes");
		

	}
	
	public boolean runProjectsInteractively() {
	
			try {
				return runProjectsInteractively("");
			} catch (MissingOnyenException e) {
				// TODO Auto-generated catch block
				e.printStackTrace(); // this cannot happen
				return false;
			}
	}
	

	@Override
	public boolean runProjectsInteractively(String aGoToOnyen) throws MissingOnyenException {
		
		if (!preRunProjectsInteractively()) {
			Tracer.error("Projects not configured");
			hasMoreSteps = false;
			return false;
		}
		
		String anOnyen = aGoToOnyen;
		if (aGoToOnyen.isEmpty()) {
		
			anOnyen= onyens.get(currentOnyenIndex);
		} else {
			currentOnyenIndex = onyens.indexOf(anOnyen);
			if (currentOnyenIndex == -1) {
				throw new MissingOnyenException(anOnyen, this);
			}
		}
		SakaiProject aProject = projectDatabase.getOrCreateProject(anOnyen);
		projectDatabase.initIO();

		projectDatabase.recordWindows();
		featureGradeRecorder.setGradingFeatures(projectDatabase
				.getGradingFeatures());
		for (int i = 0; i < projectDatabase.getGradingFeatures().size(); i++) {
			currentColors.add(null);
			nextColors.add(null);
		}
		setObjectAdapters();
		boolean retVal = setProject(anOnyen);
		if (!retVal) {
			 next();
			 if (!hasMoreSteps) {
				 currentOnyenIndex = filteredOnyenIndex;
				 return false;
			 }
		}
		filteredOnyenIndex = currentOnyenIndex;
		return true;
		

	}
	

	@Override
	public boolean preDone() {
		// return preProceed();
		// we are done but do not have another step
		return preProceed() && !preNext();
	}
	void maybeSaveState() {
		// josh's code
				// no serialization otherwise
				if (isChanged() || 
						!featureGradeRecorder.logSaved()) 
				featureGradeRecorder.finish();

				// my original code
				projectDatabase.resetIO();
				projectDatabase.clearWindows();
	}
	void redirectProject() {
		projectDatabase.initIO();
		projectDatabase.recordWindows();
	}
	
	void setFailedMoveFlags(boolean forward) {
		if (forward)
			noNextFilteredRecords = true;
		else
			noPreviousFilteredRecords = true;
	}
	
	void setSuccessfulMoveFlags(boolean forward) {
		if (forward)
			noNextFilteredRecords = false;
		else
			noPreviousFilteredRecords = false;
	}
	
	

	@Override
	public synchronized boolean move(boolean forward) {
		maybeSaveState();
//		// josh's code
//		// no serialization otherwise
//		if (changed || 
//				!featureGradeRecorder.logSaved()) 
//		featureGradeRecorder.finish();
//
//		// my original code
//		projectDatabase.resetIO();
//		projectDatabase.clearWindows();
		if (forward) {
			currentOnyenIndex++;

			if (currentOnyenIndex >= onyens.size()) {
				hasMoreSteps = false;
				return false;
			}
		} else {
			currentOnyenIndex--;
			if (currentOnyenIndex < 0) {
				hasMoreSteps = false;
				return false;
			}

		}
		redirectProject();
		String anOnyen = onyens.get(currentOnyenIndex);
		SakaiProject aProject = projectDatabase.getOrCreateProject(anOnyen);
//		redirectProject();
//		projectDatabase.initIO();
//		projectDatabase.recordWindows();
		boolean projectSet = setProject(anOnyen);
		if (!projectSet) {
			boolean retVal = move(forward);
			if (!retVal && filteredOnyenIndex != currentOnyenIndex) {
				currentOnyenIndex = filteredOnyenIndex;
				try {
					internalSetOnyen(onyens.get(filteredOnyenIndex));
				} catch (MissingOnyenException e) {
					e.printStackTrace(); // this should never be executed
				}
				Tracer.error("Cannot move as no more records that satisfy selection condition");
				setFailedMoveFlags(forward);
			} else {
				filteredOnyenIndex = currentOnyenIndex;
				setSuccessfulMoveFlags(forward);
			}
			return retVal;
		}
		filteredOnyenIndex = currentOnyenIndex;
		return true;
			
		// these two steps should go into setProject unless there is something
		// subttle here, specially as the stepProject step below is commented
		// put
		// if (isAutoRun())
		// projectDatabase.runProject(anOnyen, aProject);
		// if (isAutoAutoGrade())
		// autoGrade();

		// setProject(anOnyen);
	}

//>>>>>>> working
	@Override
	@Row(11)
	@ComponentWidth(100)
	public synchronized void done() {
//<<<<<<< HEAD
//		if (manualOnyen)
//			writeScores(this);
//		try {
////			Common.appendText(logFile, onyen + " Skipped " + Common.currentTimeAsDate() + "\n\r");
//			Common.appendText(gradedFile, onyen + "\n");
//=======

		if (manualOnyen)
			writeScores(this);
		try {
			// Common.appendText(logFile, onyen + " Skipped " +
			// Common.currentTimeAsDate() + "\n\r");
			Common.appendText(gradedFile, getOnyen() + "\n");
//>>>>>>> working

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		move(true);
//<<<<<<< HEAD
////		projectDatabase.resetIO();
////		projectDatabase.clearWindows();
////		nextOnyenIndex++;
////		if (nextOnyenIndex >= onyens.size()) {
////			hasMoreSteps  = false;
////			return;
////		}
////		String anOnyen = onyens.get(nextOnyenIndex);
////		SakaiProject aProject = projectDatabase.getProject(anOnyen);
////		projectDatabase.initIO();
////		projectDatabase.recordWindows();		
////		setProject(anOnyen);
////		if (isAutoRun())
////			projectDatabase.runProject(anOnyen, aProject);		
////		
////		setProject(anOnyen);
//
//		
//		
//		
//	
//		
//	}
//		
//	
//	public static void main (String[] args) {
//		ObjectEditor.edit(new AProjectStepper());
//	}
//
//	
//
//	
//	
//	
//	
//=======
	

	}

	@Visible(false)
	@Override
	public void setFrame(Object aFrame) {
		frame = aFrame;
		if (aFrame instanceof uiFrame) {
			oeFrame = (uiFrame) aFrame;
			
//		oeFrame = aFrame;
//			setObjectAdapters();
		}
		if (project != null)
			setColors();
	}

	@Visible(false)
	@Override
	public Object getFrame() {
		return frame;
	}
	
	
	public void computeNextColors() {
//		List<Color> colors = new ArrayList();
		int i = 0;
		for (GradingFeature aGradingFeature:projectDatabase.getGradingFeatures()) {
			nextColors.set(i, projectDatabase.getGradingFeatureColorer().color(aGradingFeature) );
			i++;
		}
		nextMultiplierColor = projectDatabase.getMultiplierColorer().color(multiplier);
		nextScoreColor = projectDatabase.getScoreColorer().color(score);
		nextOverallNotesColor = projectDatabase.getOverallNotesColorer().color(overallNotes);
		
	}
	@Override
	public boolean isChanged() {
		return changed;
	}
	@Override
	public void setChanged(boolean changed) {
		this.changed = changed;
	}
	@Override
	public boolean isSettingUpProject() {
		return settingUpProject;
	}
	@Override
	public void setSettingUpProject(boolean settingUpProject) {
		this.settingUpProject = settingUpProject;
	}
	@Override
	public int getCurrentOnyenIndex() {
		return currentOnyenIndex;
	}
	@Override
	public void setCurrentOnyenIndex(int currentOnyenIndex) {
		this.currentOnyenIndex = currentOnyenIndex;
	}
	@Override
	public int getFilteredOnyenIndex() {
		return filteredOnyenIndex;
	}
	@Override
	public void setFilteredOnyenIndex(int filteredOnyenIndex) {
		this.filteredOnyenIndex = filteredOnyenIndex;
	}
	public static void main(String[] args) {
		ObjectEditor.edit(new AProjectStepper());
	}
	@Override
	public boolean isPlayMode() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void setPlayMode(boolean playMode) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void togglePlayPause() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void quit() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean isExitOnQuit() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void setExitOnQuit(boolean newVal) {
		// TODO Auto-generated method stub
		
	}

//>>>>>>> working
}
